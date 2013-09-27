package dtd.phs.lib.ui.images_loader;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.vsm.radio18.RadioConfiguration;

import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;

public class ImagePreviewer {

	private ArrayList<String> urls;
	private ImageLoaderThread imageLoaderThread;

	protected List<Bitmap> listImages;

	private PreviewThread previewThread;

	private WeakReference<ImageView> weakImageView;

	IImageLoaderListener loaderListener = new IImageLoaderListener() {

		@Override
		public void onNewBitmapLoaded(Bitmap bm) {
			synchronized (listImages) {
				Logger.logInfo("New bitmap is loaded !");
				listImages.add(bm);
				listImages.notifyAll();
			}
		}
	};
	private Handler handler;
	protected Context context;

	public ImagePreviewer(ArrayList<String> urls, ImageView imageView,
			Handler handler) {
		this.urls = urls;
		this.listImages = new ArrayList<Bitmap>();
		this.weakImageView = new WeakReference<ImageView>(imageView);
		this.handler = handler;
		this.context = imageView.getContext();
	}

	private void load() {
		imageLoaderThread = new ImageLoaderThread(urls, loaderListener);
		imageLoaderThread.start();
	}

	private void cancelLoading() {
		if (imageLoaderThread != null) {
			imageLoaderThread.stopLoading();
			listImages.clear();
			imageLoaderThread = null;
		}
	}

	public interface IImageLoaderListener {
		void onNewBitmapLoaded(Bitmap bm);
	}

	public class ImageLoaderThread extends Thread {

		private ArrayList<String> urls;
		private boolean stopped;
		private IImageLoaderListener listener;

		public ImageLoaderThread(ArrayList<String> urls,
				IImageLoaderListener listener) {
			super("ImageLoaderThread");
			this.urls = urls;
			this.stopped = false;
			this.listener = listener;
		}

		private Bitmap getBitmap(String urlString)
				throws MalformedURLException, IOException {
			// Logger.logInfo("Start loading: " + loadingItem.URL);

			if (urlString == null)
				return null;
			Bitmap bm = null;
			String hashName = Helpers.MD5(urlString);
			bm = ImageCache.get(hashName);

			if (bm != null) {
				return bm;
			}

			// if no mem cache -> try to find it in the local storage
			String fullFileName = ImageCache.getFileName(hashName);
			bm = ImageCache.loadFromStorage(fullFileName);
			if (bm != null) {
				ImageCache.put(hashName, bm);
				return bm;
			}

			// This file had never been downloaded before -> load from internet
			bm = Helpers.downloadBitmap(urlString);

			if (bm != null) {
				ImageCache.saveImage2Storage(hashName, bm);
				ImageCache.put(hashName, bm);
				return bm;
			}

			return null;
		}

		@Override
		public void run() {
			synchronized (this) {
				for (int i = 0; i < urls.size(); i++) {
					if (stopped)
						return;
					String urlString = urls.get(i);
					try {
						Bitmap bm = getBitmap(urlString);
						if (listener != null)
							listener.onNewBitmapLoaded(bm);
					} catch (MalformedURLException e) {
						Logger.logError(e);
					} catch (IOException e) {
						Logger.logError(e);
					}
				}

			}
		}

		public synchronized void stopLoading() {
			stopped = true;
		}

	}

	public void onPause() {
		cancelLoading();
		stopPreviewThread();
		setBitmap(null);
	}

	public void onResume() {
		load();
		setBitmap(null);
		startPreviewThread();

	}

	private boolean setBitmap(final Bitmap bm) {
		final ImageView imageView = weakImageView.get();
		if (imageView != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Animation startAnim = AnimationUtils.loadAnimation(context, R.anim.fade_out);
					startAnim.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							imageView.setImageBitmap(bm);
							Animation occAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
							imageView.setAnimation(occAnimation);
						}
					});
					imageView.startAnimation(startAnim);
					
				}
			});
			return true;
		}
		return false;
	}

	private void startPreviewThread() {
		previewThread = new PreviewThread();
		previewThread.start();
	}

	private void stopPreviewThread() {
		previewThread.stopPreview();
	}

	public class PreviewThread extends Thread {

		private static final int UNINIT = -1;
		private volatile boolean stopped;
		private int currentIndex;

		public PreviewThread() {
			super("PreviewThread");
			stopped = false;
			currentIndex = UNINIT;
		}

		@Override
		public void run() {
			while ( ! stopped ) {
				try {
					Bitmap nextBitmap = getNextBitmap();
					if ( nextBitmap == null ) {
						Thread.sleep(1000);
						continue;
					}
					if ( !nextBitmap.isRecycled() )
						setBitmap(nextBitmap);
					Thread.sleep(RadioConfiguration.PREVIEW_DURATION);
				} catch (InterruptedException e) {
					Logger.logInfo("PreviewThread is interrupted ");
					return;
				}

			}
		}

		private Bitmap getNextBitmap() {
			if ( listImages.size() == 0 ) return null;

			if (currentIndex == UNINIT)
				currentIndex = 0;
			else
				currentIndex++;
			if (currentIndex >= listImages.size())
				currentIndex = 0;
			return listImages.get(currentIndex);
		}

		public void stopPreview() {
			stopped = true;
			interrupt();
		}
	}

}

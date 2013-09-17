package dtd.phs.lib.ui.images_loader;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;

import com.vsm.radio18.RadioConfiguration;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;
import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;

public class LoaderThread extends Thread {
	private static final String THREAD_NAME = "ImageLoader";
	private static int instanceCount = 0;
	private LoadingImages imagesToLoad;
	private Handler handler;
	private Animation occurrence;
	private Context context;

	public LoaderThread(Context context, Handler handler, Animation occurence) {
		super(THREAD_NAME + (instanceCount++));
		imagesToLoad = new LoadingImages();
		ImageCache.createCacheDir();
		this.handler = handler;
		this.occurrence = occurence;
		this.context = context;
	}

	@Override
	public void run() {
		while (true) {
			if (Thread.interrupted()) {
				break;
			}
			LoadingImageItem loadingItem = imagesToLoad.poll();
			if (loadingItem != null) {
				loadImageItem(loadingItem);
			} else {
				// Logger.logInfo("Loading item is NULL!");
				break;
			}
		}
		// Logger.logInfo("Loader thread is finished ");
	}

	private void loadImageItem(LoadingImageItem loadingItem) {
		final WeakReference<ImageView> iview = loadingItem.weakIview;
		try {
			Bitmap bm = getBitmap(loadingItem);
			if ( iview != null ) postImageOnUI(iview, bm);
		} catch (MalformedURLException e) {
			// Logger.logError(e);
		} catch (IOException e) {
			Logger.logError(e);
		} catch (Exception e) {
			Logger.logError(e);
		}
		// do nothing if the image cannot be downloaded from internet
	}

	private Bitmap getBitmap(LoadingImageItem loadingItem)
			throws MalformedURLException, IOException {
		//Logger.logInfo("Start loading: " + loadingItem.URL);

		if (loadingItem.URL == null)
			return null;
		Bitmap bm = null;
		String hashName = loadingItem.getHashName();
		bm = ImageCache.get(hashName);
		if (bm != null) {
			// Logger.logInfo("Getting from cache ");
			return bm;
		}

		// if no mem cache -> try to find it in the local storage
		String fullFileName = ImageCache.getFileName(hashName);
		bm = ImageCache.loadFromStorage(fullFileName);
		if (bm != null) {
			// Logger.logInfo("Getting from sdcard" );
			bm = processCutCircle(loadingItem, bm);
			ImageCache.put(hashName, bm);
			return bm;
		}

		// This file had never been downloaded before -> load from internet
		bm = Helpers.downloadBitmap(loadingItem.URL); // TODO: could be faster
														// with HttpClient
		if (bm != null) {
			// Logger.logInfo("Getting from internet " );
			bm = processCutCircle(loadingItem, bm);
			ImageCache.saveImage2Storage(hashName, bm);
			ImageCache.put(hashName, bm);
			return bm;
		}

		// Logger.logInfo("Cannot get image of URL: " + loadingItem.URL );

		return null;
	}

	private Bitmap processCutCircle(LoadingImageItem loadingItem, Bitmap bm) {
		if ( loadingItem.isCircular() ) {
			return getCircularBitmap(bm);
		}
		else return bm;
	}

	private Bitmap getCircularBitmap(Bitmap bitmap) {
		return Helpers.getCircularBitmap(bitmap);
	}

	/**
	 * 
	 * @param weakIview
	 * @param bm
	 *            bitmap to be posted, if it is null, it won't be posted
	 */
	private void postImageOnUI(final WeakReference<ImageView> weakIview,
			final Bitmap bm) {
		if (bm != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					ImageView iv = weakIview.get();
					if (iv != null) {
						iv.setImageBitmap(bm);
						if (occurrence != null)
							iv.startAnimation(occurrence);
					}
				}
			});
		} else {
			Logger.logInfo("Bitmap is NULL!");
		}
	}

	public void load(String url, WeakReference<ImageView> weakIv,boolean isCircular) {
		imagesToLoad.add(new LoadingImageItem(url, weakIv, isCircular));
	}

	public void clearPendingImages() {
		synchronized (imagesToLoad) {
			imagesToLoad.clear();
		}
	}

}

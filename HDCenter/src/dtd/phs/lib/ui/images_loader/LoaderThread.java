package dtd.phs.lib.ui.images_loader;

import hdcenter.vn.utils.Helpers;
import hdcenter.vn.utils.Logger;

import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

public class LoaderThread extends Thread {
	private LoadingImages imagesToLoad;
//	protected Animation occAnim;
	//	private Bitmap stubBimap;
	private Handler handler;

	public LoaderThread(Context context, int stubId, Handler handler) {
		imagesToLoad = new LoadingImages();
		ImageCache.createCacheDir();
		this.handler = handler;
		//		createStubBitmap(context,stubId);
//		this.occAnim = AnimationUtils.loadAnimation(context, R.anim.anim_alpha_occ);
	}
	//
	//	private void createStubBitmap(Context context, int stubId) {
	//		stubBimap = BitmapFactory.decodeResource(context.getResources(),stubId);
	//	}

	@Override
	public void run() {
		while (true) {
			if ( Thread.interrupted() ) {
				Logger.logInfo("Loader thread is finished ");
				return;
			}
			LoadingImageItem loadingItem = imagesToLoad.pop();
			if ( loadingItem != null ) {
				loadImageItem(loadingItem);
			} else {
				Logger.logInfo("Loading item is NULL!");
				Logger.logInfo("Loader thread is finished ");
				return;
			}
		}
	}

	private void loadImageItem(LoadingImageItem loadingItem) {
		final ImageView iview = loadingItem.iview;
		try {
			//			postImageOnUI(iview, stubBimap);
			Bitmap bm = getBitmap(loadingItem);
			postImageOnUI(iview, bm);
		} catch (MalformedURLException e) {
			Logger.logError(e);
		} catch (IOException e) {
			Logger.logError(e);
		} catch (Exception e) {
			Logger.logError(e);
		}
		//do nothing if the image cannot be downloaded from internet 
	}

	private Bitmap getBitmap(LoadingImageItem loadingItem)
			throws MalformedURLException, IOException {
		if ( loadingItem.URL == null ) return null;
		Bitmap bm = null;
		String hashName = loadingItem.getHashName();
		bm = ImageCache.get(hashName);

		//if no mem cache -> try to find it in the local storage
		if ( bm == null ) {

			String fullFileName = ImageCache.getFileName(hashName);
			bm = ImageCache.loadFromStorage(fullFileName);
			if ( bm == null ) {
				//This file had never been downloaded before -> load from internet
				bm = Helpers.downloadBitmap(loadingItem.URL);
				if ( bm != null ) 
					ImageCache.saveImage2Storage(fullFileName,bm);
			} 
		} 
		if ( bm != null )
			ImageCache.put(hashName, bm);
		return bm;
	}

	private void postImageOnUI(final ImageView iview, final Bitmap bm) {
		if ( bm != null ) {
			handler.post(new Runnable() {
				@Override
				public void run() {
//					iview.startAnimation(occAnim);
					iview.setImageBitmap(bm);
				} 
			});
		} else {
			Logger.logInfo("Bitmap is NULL!");
		}
	}

	public void load(String url, ImageView iv) {
		imagesToLoad.add(new LoadingImageItem(url, iv));
	}


	public void clearPendingImages() {
		synchronized (imagesToLoad) {
			imagesToLoad.clear();
		}
	}

}

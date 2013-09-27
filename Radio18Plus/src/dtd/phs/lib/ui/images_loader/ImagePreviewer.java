package dtd.phs.lib.ui.images_loader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Bitmap;

import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;

public class ImagePreviewer {


	private ArrayList<String> urls;
	private ImageLoaderThread imageLoaderThread;

	public ImagePreviewer(ArrayList<String> urls) {
		this.urls = urls;
	}

	public void load() {
		imageLoaderThread = new ImageLoaderThread(urls);
		imageLoaderThread.start();
	}

	
	public interface IImageLoaderListener {
		void onNewBitmapLoaded(Bitmap bm);
	}
	
	public class ImageLoaderThread extends Thread {

		private ArrayList<String> urls;
		private boolean stopped;
		private IImageLoaderListener listener;

		public ImageLoaderThread(ArrayList<String> urls) {
			super("ImageLoaderThread");
			this.urls = urls;
			this.stopped = false;
		}

		@Override
		public void run() {
			for (int i = 0; i < urls.size(); i++) {
				if (stopped) return;
				String urlString = urls.get(i);
				try {
					Bitmap bm = getBitmap(urlString);
					if ( listener != null )
						listener.onNewBitmapLoaded(bm);
				} catch (MalformedURLException e) {
					Logger.logError(e);
				} catch (IOException e) {
					Logger.logError(e);
				}
			}
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

	}

}

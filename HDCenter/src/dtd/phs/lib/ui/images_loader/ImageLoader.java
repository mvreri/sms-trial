package dtd.phs.lib.ui.images_loader;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

/**
 * 
 * @author Pham Hung Son
 *	This module load and cache images for android app, main features:
 *	- Load image from URL - the loading thread has lower priority than the UI thread
 *	- Cache on mem: when to clean ?
 *	- Cache on sdcard - destination dir can be set
 *	- (# cached images) can be set
 *	- Cache can be cleared
 *	- Singleton
 *	- Can be stopped anytime (the thread must be stopped immediatly & clear all the jobs on the queue when needed - including cancel the currently being downloaded image)
 *	- Can be restarted anytime ( Already stopped instance must able to be restart anytime getInstance() gets called )
 *	- Images are saved
 *
 * The loader contains: one stack of (url,imageview)
 * 
 */
public class ImageLoader {
	private static volatile ImageLoader instance = null;
	private LoaderThread loaderThread;

	public static ImageLoader getInstance(Context context, int movieStub, Handler handler) {
		if ( instance == null ) {
			instance = new ImageLoader(context,movieStub, handler);
		}
		return instance;
	}

	public ImageLoader(Context context, int movieStub, Handler handler) {
//		Logger.logInfo("ImageLoader is initilized");
		loaderThread = new LoaderThread(context,movieStub,handler);
		loaderThread.setPriority(Thread.NORM_PRIORITY-1); //lower than normal thread
		loaderThread.start();
		ImageCache.checkNcleanSDCardCache(context);
	}


	public void loadImage(String url, ImageView iv) {
		loaderThread.load(url,iv);
	}
	
	public void stop() {
		loaderThread.clearPendingImages();
		loaderThread.interrupt();
		instance = null;
	}

	public void clear() {
		loaderThread.clearPendingImages();
	}
	

}

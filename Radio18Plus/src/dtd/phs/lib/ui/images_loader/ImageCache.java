package dtd.phs.lib.ui.images_loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.vsm.radio18.RadioConfiguration;

import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;
import dtd.phs.lib.utils.PreferenceHelpers;
/**
 * 
 * @author hungson175
 * TODO: 
 * - clean memcache
 * - Clean memory cards
 *
 */
public class ImageCache {
	private static final int MAX_CACHED_IMAGES_COUNT = RadioConfiguration.MAX_IMAGES_MEM_CACHE;
	private static HashMap<String,Bitmap> imagesCache = new HashMap<String, Bitmap>();

	public static Bitmap get(String hashName) {
		return imagesCache.get(hashName);
	}

	public static void put(String hashName, Bitmap bm) {
		synchronized (imagesCache) {
			if ( imagesCache.size() > MAX_CACHED_IMAGES_COUNT ) {
				clear();
			}
			imagesCache.put(hashName, bm);
		}
	}
	
	public static Bitmap getCacheImage(String URL) {
		if ( URL == null ) {
			Logger.logInfo("URL is NULL");
			return null;
		}
		return get(Helpers.MD5(URL));
	}

	public static void clear() {
		imagesCache.clear();
	}

	static public void clearCachedDir() {
		try {
			File dir = new File(getCachedDir());
			File[] files = dir.listFiles();
			for(File file : files) {
				if ( !file.delete()) {
					Logger.logInfo("File: ---"+file.getName()+"--- cannot be deleted !");
				}
			}
		} catch (Exception e) {
			Logger.logError(e);
		}
	}

	static public void createCacheDir() {
		try {
			File file = new File(getCachedDir());
			if ( ! file.exists() ) {
				file.mkdir();
			}
		} catch (Exception e) {
			Logger.logError(e);
		}
	}

	public static Bitmap loadFromStorage(String fullFileName) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(fullFileName);
			return BitmapFactory.decodeStream(is);
		} catch (FileNotFoundException e) {
//			Logger.logInfo("Image cache file not found");
			return null;
		} finally {
			try { is.close(); } catch (Exception e) {}
		}

	}

	static final String CACHED_DIR = "/Android/data/Radio18Plus/cached_images";
	static final String IMAGE_EXTENSION = ".imd";


	public static String getCachedDir() {
		return Environment.getExternalStorageDirectory().toString()+CACHED_DIR;
	}

	public static String getFileName(String hashName) {
		return getCachedDir()+"/"+hashName+IMAGE_EXTENSION;
	}

	public static void saveImage2Storage(String fileName, Bitmap bm) {
		
		FileOutputStream out = null;
		try {
			String fullDirPath = getCachedDir();
			File filePath = new File(fullDirPath);
			filePath.mkdirs();
			out = new FileOutputStream(getFileName(fileName));
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
		} catch (Exception e) {
			Logger.logError(e);
		} 
		finally {
			try { out.close(); } catch (Exception e) {} 
		}
	} 


	private static final long MAX_SDCARD_CACHE_TIME = ((1L)*(RadioConfiguration.IMAGES_ON_SDCARD_CACHED_DAYS))*24*60*60*1000;
	private static final int MAX_SDCARD_CACHE_FILES_COUNT = 200;
	public static void checkNcleanSDCardCache(final Context context) {
		Thread cleanUpThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try { 
					long lastCleanupTime = PreferenceHelpers.getLastCleanupTime(context);
					long current = System.currentTimeMillis();
					if (  current - lastCleanupTime > MAX_SDCARD_CACHE_TIME ) {
						Logger.logInfo("Start cleaning up ....");
						long startTime = System.currentTimeMillis();
						String dirStr = getCachedDir();
						File dir = new File(dirStr);
						int cnt = 0;
						if ( dir.exists() ) {
							File[] files = dir.listFiles();
							Arrays.sort(files, new Comparator<File>() {

								@Override
								public int compare(File st, File nd) {
									if ( st.lastModified() < nd.lastModified() ) return -1;
									if ( st.lastModified() > nd.lastModified() ) return 1;
									return 0;
								}
							});

							for(int i = 0 ; i < files.length - (MAX_SDCARD_CACHE_FILES_COUNT/2) ; i++ ) {
								if ( files[i].delete() ) cnt++;
							}
						}
						if ( cnt != 0 ) {
							long endTime = System.currentTimeMillis();
							Logger.logInfo("Cleanup time: " + (endTime-startTime));
							PreferenceHelpers.setLastCleanupTime(context, current);
						}
					}
				} catch (Exception e) {
					Logger.logError(e);
				}
				
			}
		});
		cleanUpThread.setPriority(Thread.NORM_PRIORITY-2);
		cleanUpThread.start();
	}


}

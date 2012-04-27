package dtd.phs.lib.ui.images_loader;

import hdcenter.vn.utils.Logger;
import hdcenter.vn.utils.PreferenceHelpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
/**
 * 
 * @author hungson175
 * TODO: 
 * - clean memcache
 * - Clean memory cards
 *
 */
public class ImageCache {
	private static final int MAX_CACHED_IMAGES_COUNT = 100;
	private static HashMap<String,Bitmap> imagesCache = new HashMap<String, Bitmap>();

	public static Bitmap get(String hashName) {
		return imagesCache.get(hashName);
	}

	public static void put(String hashName, Bitmap bm) {
		synchronized (imagesCache) {
			if ( imagesCache.size() > MAX_CACHED_IMAGES_COUNT ) {
//				Random rand = new Random(System.currentTimeMillis());
//				for(String key : imagesCache.keySet()) {
//					int kill = rand.nextInt(2);
//					if ( kill == 1) imagesCache.remove(key);
//				}
				clear();
			}
			imagesCache.put(hashName, bm);
		}
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
		FileInputStream is;
		try {
			is = new FileInputStream(fullFileName);
			return BitmapFactory.decodeStream(is);
		} catch (FileNotFoundException e) {
			Logger.logError(e);
			return null;
		}

	}

	static final String CACHED_DIR = "/data/hdcenter";
	static final String IMAGE_EXTENSION = ".imd";


	public static String getCachedDir() {
		return Environment.getExternalStorageDirectory().toString()+CACHED_DIR;
	}

	public static String getFileName(String hashName) {
		return getCachedDir()+"/"+hashName+IMAGE_EXTENSION;
	}

	public static void saveImage2Storage(String fullFileName, Bitmap bm) {
		try {
//			Logger.logInfo("Trying to save to file: "+fullFileName);
			FileOutputStream out = new FileOutputStream(fullFileName);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.close();
		} catch (Exception e) {
			Logger.logError(e);
		}		
	}


	private static final long MAX_SDCARD_CACHE_TIME = (7L)*24*60*60*1000;
	private static final int MAX_SDCARD_CACHE_FILES_COUNT = 200;
	public static void checkNcleanSDCardCache(Context context) {
		try { 
			long lastCleanupTime = PreferenceHelpers.getLastCleanupTime(context);
			long current = System.currentTimeMillis();
			if (  current - lastCleanupTime > MAX_SDCARD_CACHE_TIME ) {
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
					PreferenceHelpers.setLastCleanupTime(context, current);
				}
			}
		} catch (Exception e) {
			Logger.logError(e);
		}
	}


}

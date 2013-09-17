package com.vsm.radio18.data;

import java.util.HashMap;

public class Cache {

	private static final long CACHE_PERIOD = 10 * 60 * 1000; //cache for 10 minutes for maximum
	private static HashMap<String, CacheObject> cache = new HashMap<String, CacheObject>();
	static class CacheObject {
		public CacheObject(Object result, long currentTimeMillis) {
			this.data = result;
			this.startCacheTime = currentTimeMillis;
		}
		
		protected long startCacheTime;
		protected Object data;
	}

	public static void cacheData(String url, Object result) {
		cache.put(url,new CacheObject(result,System.currentTimeMillis()));
	}

	public static Object getCachedData(String url) {
		return cache.get(url).data;
	}

	public static boolean isCache(String url) {
		CacheObject cobj = cache.get(url);
		if ( cobj == null ) return false;
		if ( System.currentTimeMillis() - cobj.startCacheTime > CACHE_PERIOD ) {
			cache.remove(url);
			return false;
		} else return true;
	}

}

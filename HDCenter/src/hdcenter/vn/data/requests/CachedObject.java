package hdcenter.vn.data.requests;

public class CachedObject {
	private static final long MAX_CACHED_TIME = 15*60*1000;
	long savedTime = 0;
	Object data = null;
	private long maxCachedTime = MAX_CACHED_TIME;
	
	public CachedObject(Object data) {
		this.savedTime = System.currentTimeMillis();
		this.data = data;
	}
	
	public boolean isTimeOut() {
		long current = System.currentTimeMillis();
		if ( current - savedTime > getMaxCachedTime() ) {
			return true;
		}
		return false;
	}

	private long getMaxCachedTime() {
		return this.maxCachedTime;
	}
	
	public void setMaxCachedTime(long maxCachedTime) {
		this.maxCachedTime = maxCachedTime;
	}
}

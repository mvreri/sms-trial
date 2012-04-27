package hdcenter.vn.data.requests;

import hdcenter.vn.utils.Logger;

import java.util.HashMap;

public class ReqRecommendMovies extends RequestMoviesList {

	
	private static final String API_RECOMMENDED = "recommend";
	private static final String PAGE = "page";
	static HashMap<Integer, CachedObject> cache = new HashMap<Integer, CachedObject>();  

	public ReqRecommendMovies(int page) {
		super(page);
	}

	@Override
	protected String provideAPIName() {
		return API_RECOMMENDED;
	}

	@Override
	protected void provideParameters(HashMap<String, String> parameters) {
		parameters.put(PAGE, String.valueOf(getPage()));
	}

	/**
	 * cacheData: page -> (data,time)
	 * if ( current - time > MAX_CACHED_TIME ) -> refresh data !
	 */
	@Override
	protected Object cachedData() {
		return cache.get(getPage()).data;
	}

	@Override
	protected boolean isCached() {
		CachedObject obj = cache.get(getPage());
		if ( obj == null ) return false;
		if ( obj.isTimeOut() ) return false;
		Logger.logInfo("Recommend page: " + getPage() + " is cached !");
		return true;
	}

	@Override
	protected void saveCacheData(Object data) {
		cache.put(getPage(), new CachedObject(data));
	}


}

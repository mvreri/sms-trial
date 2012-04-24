package hdcenter.vn.data.requests;

import java.util.HashMap;

public class ReqNewMovies extends RequestMoviesList {
	
	private static final String PAGE = "page";
	private static final String API_NEW = "new";
	static HashMap<Integer, CachedObject> cache = new HashMap<Integer, CachedObject>();
	//TODO: to be refactored
	private int page;

	public ReqNewMovies(int page) {
		this.page = page;
	}


	protected String provideAPIName() {
		return API_NEW;
	}

	protected void provideParameters(HashMap<String, String> parameters) {		
		parameters.put(new String(PAGE), String.valueOf(page));
	}
	
	@Override
	protected Object cachedData() {
		return cache.get(page).data;
	}

	@Override
	protected boolean isCached() {
		CachedObject obj = cache.get(page);
		if ( obj == null ) return false;
		if ( obj.isTimeOut() ) return false;
		return true;
	}

	@Override
	protected void saveCacheData(Object data) {
		cache.put(page, new CachedObject(data));
	}
}

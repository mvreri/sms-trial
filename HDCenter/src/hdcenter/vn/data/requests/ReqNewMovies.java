package hdcenter.vn.data.requests;

import java.util.HashMap;

public class ReqNewMovies extends RequestMoviesList {
	
	private static final String PAGE = "page";
	private static final String API_NEW = "new";
	static HashMap<Integer, CachedObject> cache = new HashMap<Integer, CachedObject>();

	public ReqNewMovies(int page) {
		super(page);
	}


	protected String provideAPIName() {
		return API_NEW;
	}

	protected void provideParameters(HashMap<String, String> parameters) {		
		parameters.put(new String(PAGE), String.valueOf(getPage()));
	}
	
	@Override
	protected Object cachedData() {
		return cache.get(this.getPage()).data;
	}

	@Override
	protected boolean isCached() {
		CachedObject obj = cache.get(this.getPage());
		if ( obj == null ) return false;
		if ( obj.isTimeOut() ) return false;
		return true;
	}

	@Override
	protected void saveCacheData(Object data) {
		cache.put(getPage(), new CachedObject(data));
	}

}

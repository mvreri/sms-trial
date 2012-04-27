package hdcenter.vn.data.requests;

import java.util.HashMap;

public class ReqSearch extends RequestMoviesList {


	private static final String API_SEARCH = "search";
	private static final String KEYWORD = "keyword";
	private static final String PAGE = "page";
	private String keyword;
	private static final HashMap<Integer, CachedObject> cache = new HashMap<Integer, CachedObject>();

	static private Object cachedKeyword = null;

	public ReqSearch(String keyword, int page) {
		super(page);
		this.keyword = keyword;
	}

	@Override
	protected String provideAPIName() {
		return API_SEARCH;
	}

	@Override
	protected void provideParameters(HashMap<String, String> parameters) {
		parameters.put(KEYWORD,this.keyword);
		parameters.put(PAGE,String.valueOf(getPage()));

	}
	/**
	 * Only cache the last keyword
	 * Reset the cache when the keyword is changed
	 * cache: keyword x (page -> CachedObject = (time,data));
	 */

	@Override
	protected boolean isCached() {
		if ( cachedKeyword == null ) return false;
		if ( ! cachedKeyword.equals(keyword) ) {

			return false;
		}
		CachedObject obj = cache.get(getPage());
		if ( obj == null ) return false;
		if ( obj.isTimeOut() ) return false;
		return true;
	}

	@Override
	protected Object cachedData() {
		return cache.get(getPage()).data;
	}

	@Override
	protected void saveCacheData(Object data) {
		if ( cachedKeyword != null && !cachedKeyword.equals(keyword)) {
			cache.clear();
			ReqSearch.cachedKeyword = this.keyword;
		}
		cache.put(getPage(),new CachedObject(data));
	}


}

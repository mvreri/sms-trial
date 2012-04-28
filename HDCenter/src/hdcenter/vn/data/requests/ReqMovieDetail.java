package hdcenter.vn.data.requests;

import hdcenter.vn.entities.MovieDetailsItem;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqMovieDetail extends Request{
	
	private static final String API_MOVIE_DETAILS = "detail";
	private static final String ID = "id";
	private static final HashMap<String, CachedObject> cache = new HashMap<String, CachedObject>();
	private static final long CACHE_TIME = 30*60*1000;
	
	private String id;
	public ReqMovieDetail(String id) {
		this.id = id;
	}

	@Override
	protected String provideAPIName() {
		return API_MOVIE_DETAILS;
	}

	@Override
	protected MovieDetailsItem parseData(String resultString) throws JSONException {
		JSONObject jobject = new JSONObject(resultString);
		return MovieDetailsItem.createInstance(jobject);
	}


	@Override
	protected void provideParameters(HashMap<String, String> parameters) {
		parameters.put(ID, id);
	}
	
	@Override
	protected void saveCacheData(Object data) {
		CachedObject obj = new CachedObject(data);
		obj.setMaxCachedTime(CACHE_TIME);
		cache.put(id, obj);
	}
	
	@Override
	protected boolean isCached() {
//		return false;
		CachedObject obj = cache.get(id);
		if ( obj == null ) return false;
		if ( obj.isTimeOut() ) return false;
		return true;
	}
	
	@Override
	protected Object cachedData() {
		return cache.get(id).data;
	}
	
	

}

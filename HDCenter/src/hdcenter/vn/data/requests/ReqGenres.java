package hdcenter.vn.data.requests;

import hdcenter.vn.utils.Logger;
import hdcenter.vn.utils.PreferenceHelpers;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class ReqGenres extends Request {

	private static final String API_ALL_GENRES = "genre";
	private static final String ALL = "all";
	private static final String PAGE = "page";
	private static final String GENRE = "genre";

	private int page;
	private Context context;

	public ReqGenres(Context context, int page) {
		super();
		this.page = page;
		this.context = context;
	}

	@Override
	protected String provideAPIName() {
		return API_ALL_GENRES;
	}

	@Override
	protected void provideParameters(HashMap<String, String> parameters) {
		parameters.put(GENRE, ALL);
		parameters.put(PAGE, String.valueOf(this.page));
	}

	/**
	 * return the hashmap: english(genre) -> vietnamese(genre)
	 * e.g: {"action":"Hanh dong","drama":"Tam ly" .... }
	 */
	@Override
	protected HashMap<String, String> parseData(String resultString) throws JSONException {
		JSONObject jobject = new JSONObject(resultString);
		HashMap<String, String> mapE2V = new HashMap<String, String>();
		JSONArray names = jobject.names();
		for(int i  = 0 ; i < names.length() ; i++) {
			String key  = names.getString(i);
			mapE2V.put(key, jobject.getString(key));
		}
		return mapE2V;
	}


	@Override
	protected boolean isCached() {
		return PreferenceHelpers.isGenresCached(context);
	}

	/**
	 * Programming notes:
	 * All these cache methods hurt encapsulation: they all know about implementation details of class Request !
	 * One can say that is the general problem of inheritance - but this one hurts more than normal inheritance
	 */
	@Override
	protected Object cachedData() {
		String resultString = PreferenceHelpers.getCachedGenres(context);
		try {
			return parseData(resultString);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected void saveCacheData(Object data) {
		if ( data == null || ! (data instanceof HashMap)) {
			Logger.logError("Invalid return data");
			return;
		}
		@SuppressWarnings("unchecked")
		HashMap<String, String> mapE2V = (HashMap<String, String>) data;
		PreferenceHelpers.cacheGenres(context,mapE2V);
	}


}

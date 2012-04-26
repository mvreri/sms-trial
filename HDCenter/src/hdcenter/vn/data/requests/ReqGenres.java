package hdcenter.vn.data.requests;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReqGenres extends Request {

	private static final String API_ALL_GENRES = "genre";
	private static final String ALL = "all";
	private static final String PAGE = "page";
	private static final String GENRE = "genre";
	
	private int page;

	public ReqGenres(int page) {
		super();
		this.page = page;
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

}

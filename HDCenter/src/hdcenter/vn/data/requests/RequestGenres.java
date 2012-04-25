package hdcenter.vn.data.requests;

import hdcenter.vn.data.AutoCleanThread.JobsList;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestGenres extends Request {

	private static final String API_ALL_GENRES = "genre";
	private static final String ALL = "all";
	private static final String PAGE = "page";
	private static final String GENRE = "genre";
	
	private String page;

	public RequestGenres(String page) {
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
		parameters.put(PAGE, this.page);
		
	}

	/**
	 * return the hashmap: english(genre) -> vietnamese(genre)
	 * e.g: {"action":"Hanh dong","drama":"Tam ly" .... }
	 */
	@Override
	protected HashMap<String, String> parseData(String resultString) throws JSONException {
		assert false;
		JSONArray jarray = new JSONArray(resultString);
		HashMap<String, String> mapE2V = new HashMap<String, String>();
		for(int i = 0 ; i < jarray.length() ; i++) {
			
			
		}
		return mapE2V;
	}

}

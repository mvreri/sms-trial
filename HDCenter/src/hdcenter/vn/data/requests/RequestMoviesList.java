package hdcenter.vn.data.requests;

import hdcenter.vn.entities.MovieItem;
import hdcenter.vn.entities.MoviesList;
import hdcenter.vn.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Pair;

public abstract class RequestMoviesList extends PaggableRequest {

	private static final String TOTAL = "total";
	private static final String RESULT = "result";

	public RequestMoviesList(int page) {
		super(page);
	}

	@Override
	protected Pair<Integer, MoviesList> parseData(String resultString) throws JSONException {
		JSONObject jobject = new JSONObject(resultString);
		MoviesList list = new MoviesList();
		int total = jobject.getInt(TOTAL);
		if ( total != 0) {
			JSONArray jarray = jobject.getJSONArray(RESULT);
			for(int i = 0; i < jarray.length() ; i++) {
				try {
					MovieItem item  = MovieItem.createFromJSONObject(jarray.getJSONObject(i));
					list.add(item);
				} catch (JSONException e) {
					//TODO: remove this code - this is the server error
					if ( i == jarray.length()-1) {
						Logger.logError("Server error: _,_ after last array element");
						break;
					} else throw e;
				}
			}
		}
		return new Pair<Integer,MoviesList>(total,list);
	}

}

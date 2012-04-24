package hdcenter.vn.data.requests;

import hdcenter.vn.entities.MovieItem;
import hdcenter.vn.entities.MoviesList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class RequestMoviesList extends Request {

	private static final String TOTAL = "total";
	private static final String RESULT = "result";

	@Override
	protected MoviesList parseData(String resultString) throws JSONException {
		JSONObject jobject = new JSONObject(resultString);
		MoviesList list = new MoviesList();
		int total = jobject.getInt(TOTAL);
		if ( total != 0) {
			JSONArray jarray = jobject.getJSONArray(RESULT);
			for(int i = 0; i < jarray.length() ; i++) {
				MovieItem item  = MovieItem.createFromJSONObject(jarray.getJSONObject(i));
				list.add(item);
			}
		}
		return list;
	}

}

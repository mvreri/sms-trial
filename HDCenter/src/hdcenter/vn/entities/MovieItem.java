package hdcenter.vn.entities;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieItem {

	private static final String[] FIELDS = {
		"id",
		"name",
		"vn_name",
		"year",
		"short_desc",
		"image",
		"imdb_rating"
	};

	public MovieItem() {
		super();
		this.data = new HashMap<String, String>();
	}

	public static MovieItem createFromJSONObject(JSONObject jsonObject) throws JSONException {
		MovieItem item = new MovieItem();
		for(String key : FIELDS) {
			item.setData(key,jsonObject.getString(key));
		}
		return item;
	}
	
	private HashMap<String, String> data;
	
	private void setData(String key, String string) {
		data.put(key,string);
	}
	
	@Override
	public String toString() {
		String s = "Film:{";
		for(String key: FIELDS) {
			String value = data.get(key);
			s += key +": "+value+", ";
		}
		return s;
	}

	public String getName() {
		return data.get("name");
	}

	public String getVnName() {
		return data.get("vn_name");
	}

	public String getRating() {
		return data.get("imdb_rating");
	}

	public String getImageURL() {
		return data.get("image");
	}

	public String getId() {
		return data.get("id");
	}

}

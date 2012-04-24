package hdcenter.vn.entities;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailsItem {
	private static final String[] FIELDS = 
	{
		"id","name","vn_name","year","desc","image","imdb_rating","genre","director","cast","youtube"
	};
	
	private HashMap<String, String> data;

	public MovieDetailsItem() {
		this.data = new HashMap<String, String>();
	}
	
	public static MovieDetailsItem createInstance(JSONObject jobject) throws JSONException {
		MovieDetailsItem item = new MovieDetailsItem();
		for(String key : FIELDS) {
			String value = jobject.getString(key);
			item.setDate(key,value);
		}
		return item;
		
	}
	
	private void setDate(String key, String value) {
		data.put(key,value);
	}
	
	@Override
	public String toString() {
		String s = "Film:{";
		for(String key : FIELDS) {
			String value = data.get(key);
			s+=key+": "+value+",";
		}
		return s;
	}

	public String getStarrings() {
		return data.get("cast");
	}

	public String getDescription() {
		return data.get("desc");
	}

	public String getImageURL() {
		return data.get("image");
	}

	public String getYoutubeId() {
		return data.get("youtube");
	}

}

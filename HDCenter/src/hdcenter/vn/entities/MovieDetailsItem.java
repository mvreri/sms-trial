package hdcenter.vn.entities;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailsItem {
	private static final String VN_NAME = "vn_name";
	private static final String YEAR = "year";
	private static final String DIRECTOR = "director";
	private static final String CAST = "cast";
	private static final String DESC = "desc";
	private static final String IMAGE = "image";
	private static final String YOUTUBE = "youtube";
	
	private static final String[] FIELDS = 
	{
		"id","name",VN_NAME,YEAR,DESC,IMAGE,"imdb_rating","genre",DIRECTOR,CAST,YOUTUBE
	};
	

	private HashMap<String, String> data;

	public MovieDetailsItem() {
		this.data = new HashMap<String, String>();
	}
	
	public static MovieDetailsItem createInstance(JSONObject jobject) throws JSONException {
		MovieDetailsItem item = new MovieDetailsItem();
		for(String key : FIELDS) {
			String value = jobject.getString(key);
			item.setData(key,value);
		}
		return item;
		
	}
	
	private void setData(String key, String value) {
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
		return getData(CAST);
	}

	public String getDescription() {
		return getData(DESC);
	}

	public String getImageURL() {
		return getData(IMAGE);
	}

	public String getYoutubeId() {
		return getData(YOUTUBE);
	}

	public String getYear() {
		return getData(YEAR);
	}

	public CharSequence getDirector() {
		return getData(DIRECTOR);
	}

	private String getData(String field) {
		String ret = data.get(field);
		if ( ret == null) ret = "";
		return ret;
	}

	public String getVnName() {
		return getData(VN_NAME);
	}

	//TODO: later
	public String getMovieCalendarId() {
		return "Danh+Cho+Thang+Sau";
	}

}

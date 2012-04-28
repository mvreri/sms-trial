package hdcenter.vn.entities;

import hdcenter.vn.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class CollectionItem {

	String id;
	String ename;
	String vname;
	int size;
	private String image;

	public String getId() {
		return id;
	}

	public String getEname() {
		return ename;
	}

	public String getVname() {
		return vname;
	}

	public int getSize() {
		return size;
	}

	public String getImageURL() {
		return this.image;
	}
	
	public CollectionItem(String id, String ename, String vname, int size, String image) {
		super();
		this.id = id;
		this.ename = ename;
		this.vname = vname;
		this.size = size;
		this.image = image;
	}

	private static final String ID = "id";
	private static final String ENAME = "ename";
	private static final String VNAME = "vname";
	private static final String MOVIES = "movies";
	private static final String IMG = "img";
	
	public static CollectionItem create(JSONObject jsonObject) {
		try {
			return new CollectionItem(
					jsonObject.getString(ID),
					jsonObject.getString(ENAME),
					jsonObject.getString(VNAME),
					jsonObject.getInt(MOVIES),
					jsonObject.getString(IMG));
		} catch (JSONException e) {
			Logger.logError(e);
			return null;
		}
	}

}

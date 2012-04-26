package hdcenter.vn.entities;

import hdcenter.vn.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class CollectionItem {

	String id;
	String ename;
	String vname;
	int size;

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

	public CollectionItem(String id, String ename, String vname, int size) {
		super();
		this.id = id;
		this.ename = ename;
		this.vname = vname;
		this.size = size;
	}

	private static final String ID = "id";
	private static final String ENAME = "ename";
	private static final String VNAME = "vname";
	private static final String MOVIES = "movies";
	public static CollectionItem create(JSONObject jsonObject) {
		try {
			return new CollectionItem(
					jsonObject.getString(ID),
					jsonObject.getString(ENAME),
					jsonObject.getString(VNAME),
					jsonObject.getInt(MOVIES));
		} catch (JSONException e) {
			Logger.logError(e);
			return null;
		}
	}

}

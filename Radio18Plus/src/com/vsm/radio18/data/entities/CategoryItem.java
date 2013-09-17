package com.vsm.radio18.data.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class CategoryItem {

	private static final String NAME_TAG = "name";
	private static final String ID_TAG = "id";
	private long id;
	private String name;

	public CategoryItem(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static CategoryItem parse(JSONObject jitem) throws JSONException {
		long id = jitem.getLong(ID_TAG);
		String name = jitem.getString(NAME_TAG);
		return new CategoryItem(id,name);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}

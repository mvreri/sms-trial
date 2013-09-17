package com.vsm.radio18.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vsm.radio18.data.entities.ArticleItem;

import dtd.phs.lib.utils.Logger;

public class ReqListArticles extends RequestData {
	
	private static final String ID_TAG = "$id";
	static final String BASE_URL = "http://api.radio18plus.radito.com/1.0/category/"+ID_TAG+"/tracks";
	private static final String DATA_TAG = "data";
	
	private long catId;

	public ReqListArticles(long id) {
		super();
		this.catId = id;
	}
	@Override
	protected String getURL() {
		return BASE_URL.replace(ID_TAG, ""+catId);
	}

	@Override
	protected Object parseSuccessResult(JSONObject jso) throws JSONException {
		ArrayList<ArticleItem> list = new ArrayList<ArticleItem>();
		JSONArray array = jso.getJSONArray(DATA_TAG);
		for(int i = 0 ; i < array.length() ; i++ ) {
			try {
				list.add(ArticleItem.parse(array.getJSONObject(i)));
			} catch (JSONException e) {
				Logger.logError(e);
			}
		}
		return list;
	}
}

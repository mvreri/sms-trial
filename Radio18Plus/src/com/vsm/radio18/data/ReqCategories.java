package com.vsm.radio18.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vsm.radio18.data.entities.CategoryItem;

import dtd.phs.lib.utils.Logger;

public class ReqCategories extends RequestData {

	private static final String STATUS_TAG = "status";
	private static final int DEFAULT_SO_TIME_OUT = 5000;
	private static final int DEFAULT_CONN_TIME_OUT = 5000;
	private static final int STATUS_SUCCESS = 0;
	
	private static final String DATA_TAG = "data";	
	private static final String CATEGORIES_URL = "http://api.radio18plus.radito.com/1.0/categories"; //TODO: to be gener here !

	public ReqCategories() {
		super();
	}

	@Override
	protected String getURL() {
		return CATEGORIES_URL;
	}

	@Override
	protected Object parseSuccessResult(JSONObject jso) throws JSONException {
		ArrayList<CategoryItem> cats = new ArrayList<CategoryItem>();
		JSONArray array = jso.getJSONArray(DATA_TAG);
		for(int i = 0 ; i < array.length() ; i++ ) {
			try {
				cats.add(CategoryItem.parse(array.getJSONObject(i)));
			} catch (JSONException e) {
				Logger.logError(e);
			}
		}
		return cats;
	}


//	private ArrayList<CategoryItem> parseJSON(String message) 
//			throws JSONException //TODO: ParsingException 
//	{ 
//		JSONObject jso = new JSONObject(message);
//		int status = jso.getInt(STATUS_TAG);
//		 
//		if ( status == STATUS_SUCCESS ) {
//			ArrayList<CategoryItem> cats = new ArrayList<CategoryItem>();
//			JSONArray array = jso.getJSONArray(DATA_TAG);
//			for(int i = 0 ; i < array.length() ; i++ ) {
//				try {
//					cats.add(CategoryItem.parse(array.getJSONObject(i)));
//				} catch (JSONException e) {
//					Logger.logError(e);
//				}
//			}
//			return cats;
//		} else {
//			//TODO: throw exception here ! OR WAHT ?
//			return null;
//		}
//		
//	}

	
}

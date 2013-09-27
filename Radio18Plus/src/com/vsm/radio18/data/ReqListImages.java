package com.vsm.radio18.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dtd.phs.lib.utils.Logger;

public class ReqListImages extends RequestData {

	private static final String IMAGE = "image";
	private static final String BASE_URL = "http://api.appngon.com/index.php/image/images";
	private long articleId;//TODO: to be implemented when the "real" API is ready 

	public ReqListImages(long articleId) {
		this.articleId = articleId;
	}

	@Override
	protected String getURL() {
		return BASE_URL; //TODO: the URL should also contains articleId
	}

	@Override
	protected Object parseSuccessResult(JSONObject jso) throws JSONException {
		return null;
	}

	protected ArrayList<String> parseURLs(JSONArray array) throws JSONException {
		int len = array.length();
		ArrayList<String> results = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			JSONObject jo = array.getJSONObject(i);
			try {
				String url = jo.getString(IMAGE);
				results.add(url);
			} catch (Exception e) {
				Logger.logError(e);
			}

		}
		return results;
	}

}

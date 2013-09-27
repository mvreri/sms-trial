package com.vsm.radio18.data;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dtd.phs.lib.data_framework.IRequest;
import dtd.phs.lib.utils.Logger;

public abstract class RequestData implements IRequest {

	private static final String STATUS_TAG = "status";
	private static final int DEFAULT_SO_TIME_OUT = 5000;
	private static final int DEFAULT_CONN_TIME_OUT = 5000;
	private static final int STATUS_SUCCESS = 0;

	protected abstract String getURL();

	protected abstract Object parseSuccessResult(JSONObject jso)
			throws JSONException;

	/**
	 * @return the result if success, null if server return any status code !=
	 *         SUCCESS_CODE
	 */
	@Override
	public Object requestData() throws Exception {
		String url = getURL();
		if (isCached(url)) {
			return cachedData(url);
		} else {
			DefaultHttpClient httpClient = RadioHttpClient.getDataInstance();
			HttpGet httpGet = new HttpGet(url);
			HttpParams params = setTimeOut();
			httpClient.setParams(params);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String jsonString = httpClient.execute(httpGet, responseHandler);
			Object result = parseJSON(jsonString);
			if (result != null)
				cacheData(url, result);
			return result;
		}
	}

	private void cacheData(String url, Object result) {
		Cache.cacheData(url, result);
	}

	private Object cachedData(String url) {
		return Cache.getCachedData(url);
	}

	private boolean isCached(String url) {
		return Cache.isCache(url);
	}

	/**
	 * 
	 * @param message
	 * @return result if success OR null if server return status code != success
	 * @throws JSONException
	 */
	private Object parseJSON(String message) throws JSONException {
		JSONObject jso = new JSONObject(message);
		int status = -1;
		try {
			status = jso.getInt(STATUS_TAG);
		} catch (JSONException e) {
			//TODO: remove this try catch, 
			// the codes exist only because of the stub images API doesn't have Status code !
			//http://api.appngon.com/index.php/image/images
			JSONArray array = new JSONArray(message);
			if ( this instanceof ReqListImages ) {
				return ((ReqListImages) this).parseURLs(array);
			}
		}
		
		if (status == STATUS_SUCCESS) {
			return parseSuccessResult(jso);
		} else {
			return null;
		}

	}
			

	private HttpParams setTimeOut() {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				getConnectionTimeOut());
		HttpConnectionParams.setSoTimeout(httpParameters, getSoTimeOut());
		return httpParameters;
	}

	protected int getSoTimeOut() {
		return DEFAULT_SO_TIME_OUT;
	}

	protected int getConnectionTimeOut() {
		return DEFAULT_CONN_TIME_OUT;
	}

}

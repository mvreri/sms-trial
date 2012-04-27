package hdcenter.vn.data.requests;

import hdcenter.vn.utils.Helpers;
import hdcenter.vn.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONException;

public abstract class Request {
	private static final String BASE_URL = "phim.hdcenter.vn/api/";

	abstract protected String provideAPIName();
	abstract protected void provideParameters(HashMap<String, String> parameters);
	abstract protected Object parseData(String resultString) throws JSONException;
	

	protected String getFullURL(HashMap<String, String> parameters) {
		StringBuilder builder = new StringBuilder(BASE_URL + provideAPIName() + ".php?");
		String paraString = parameters2String(parameters);
		builder.append(paraString);
		builder.append("&code=" + genValidateCode(paraString));
		String url = "http://"+builder.toString();
		Logger.logInfo("Full URL: " + url);
		return url;
	}

	private static final String ADDITIONAL_KEY = "hdc";
	private String genValidateCode(String paraString) {
		return Helpers.MD5(ADDITIONAL_KEY+paraString);
	}

	//	protected String genValidateCode(HashMap<String, String> parameters) {
	//		ArrayList<String> keySet = new ArrayList<String>(parameters.keySet());
	//		Collections.sort(keySet);
	//		StringBuilder builder = new StringBuilder(ADDITIONAL_KEY);
	//		for(String key : keySet) {
	//			builder.append(key+"="+parameters.get(key));
	//		}
	//		String s = builder.toString();
	//		Logger.logInfo("Raw validate code: " + s);
	//		return Helpers.MD5(s);
	//	}

	protected String getResultString() throws IOException {
		HashMap<String, String> parameters = createParameter();
		URL url = new URL(getFullURL(parameters));
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		StringBuilder result = new StringBuilder();
		String line = "";
		while ( (line = in.readLine()) != null ) {
			result.append(line);
		}
		in.close();
		return result.toString();
	}


	public Object requestData() throws IOException, JSONException {
		if ( isCached() ) {
			return cachedData();
		} else {
			String resultString = getResultString();
			Object data = parseData(resultString);
			saveCacheData(data);
			return data;
		}
	}
	
	//To be ovewritten if cache is required
	protected Object cachedData() {
		return null;
	}
	
	//To be ovewritten if cache is required	
	protected boolean isCached() {
		return false;
	}
	
	protected void saveCacheData(Object data) {
		//do nothing - to be overwritten 
	}
	
	private HashMap<String, String> createParameter() {
		HashMap<String, String> para = new HashMap<String, String>();
		provideParameters(para);
		return para;
	}

	private String parameters2String(HashMap<String, String> parameters) {
		StringBuilder builder = new StringBuilder();
		for(String key : parameters.keySet()) {
			builder.append(key+"="+URLEncoder.encode(parameters.get(key))+"&");
		}
		if (builder.length()> 0) builder.deleteCharAt(builder.length()-1);
		String result = builder.toString();
		return result;
	}


}

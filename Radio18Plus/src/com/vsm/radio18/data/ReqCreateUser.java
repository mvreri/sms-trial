package com.vsm.radio18.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqCreateUser extends RequestData {
	private static final String MAC_ADD = "$mac_add";
	static final String BASE_URL = "http://sms.appngon.com/index.php/onepay_service/";
	static final String API_NAME = "create_user?mac_add="+MAC_ADD;
	private static final String USER_ID = "user_id";
	private static final int USER_ALREADY_EXISTs_STATUS = 1;
	private String macAdd;

	public ReqCreateUser(String macAdd) {
		this.macAdd = macAdd;
	}
	@Override
	protected String getURL() {
		return (BASE_URL+ API_NAME).replace(MAC_ADD, macAdd);
	}

	@Override
	protected Object parseSuccessResult(JSONObject jso) throws JSONException {
		String userId = jso.getString(USER_ID);
		return userId;
	}
	
	@Override
	protected Object parseUnsuccessResult(JSONObject jso) throws JSONException {
		int status = jso.getInt(STATUS_TAG);
		if ( status == USER_ALREADY_EXISTs_STATUS ) {
			return jso.getString(USER_ID);
		} else {
			return null;
		}
	}

}

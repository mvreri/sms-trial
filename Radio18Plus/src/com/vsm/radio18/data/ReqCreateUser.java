package com.vsm.radio18.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqCreateUser extends RequestData {
	private static final String SUCCESS = "success";
	private static final String USER_ID = "$user_id";
	static final String BASE_URL = "http://sms.appngon.com/index.php/onepay_service/";
	static final String API_NAME = "create_user?user_code="+USER_ID;
	private String userId;

	/** TODO:
	 * It should be: status =
	 * 	0: success
	 * 	1: user already exists
	 * 	2: failed - unknown reason
	 */

	public ReqCreateUser(String userId) {
		this.userId = userId;
	}
	@Override
	protected String getURL() {
		return (BASE_URL+ API_NAME).replace(USER_ID, userId);
	}

	@Override
	protected Object parseSuccessResult(JSONObject jso) throws JSONException {
		int succ = jso.getInt(SUCCESS);
		if ( succ == 1 ) return Boolean.valueOf(true);
		return Boolean.valueOf(false);
	}

}

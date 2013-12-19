package com.vsm.radio18.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqBalance extends PaymentRequest {
	private static final String USER_ID = "$user_id";
	static final String BASE_URL = "http://sms.appngon.com/index.php/onepay_service/";
	static final String API_NAME = "get_user_amount?user_code="+USER_ID;
	private static final String AMOUNT = "amount";
	
	private String userId;
	public ReqBalance(String userId) {
		this.userId = userId;
	}
	protected String getURL() {
		return (BASE_URL + API_NAME).replace(USER_ID,userId);
	}

	@Override
	protected Object parseSuccessResult(JSONObject jso) throws JSONException {
		int amount = jso.getInt(AMOUNT);
		return Integer.valueOf(amount);
	}


}

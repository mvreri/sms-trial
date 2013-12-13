package com.vsm.radio18.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqBuyItem extends RequestData {

	private static final String USER_ID = "$user_id";
	private static final String ITEM_PRICE = "$item_price";
	static final String BASE_URL = "http://sms.appngon.com/index.php/onepay_service/";
	static final String API_NAME = "buy_item?user_code="+USER_ID+"&item_price="+ ITEM_PRICE;
	private String userId;
	private int price;
	
	
	public ReqBuyItem(String userId, int price) {
		super();
		this.userId = userId;
		this.price = price;
	}

	@Override
	protected String getURL() {
		return (BASE_URL + API_NAME).replace(USER_ID, userId).replace(ITEM_PRICE,""+price);
	}

	@Override
	protected Object parseSuccessResult(JSONObject jso) throws JSONException {
		return Boolean.valueOf(true);
	}
	
	@Override
	protected Object parseUnsuccessResult(JSONObject jso) throws JSONException {
		return Boolean.valueOf(false);
	}

}

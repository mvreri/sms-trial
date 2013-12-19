package com.vsm.radio18.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqBuyItem extends PaymentRequest {

	private static final String USER_ID = "$user_id";
	private static final String ITEM_PRICE = "$item_price";
	static final String BASE_URL = "http://sms.appngon.com/index.php/onepay_service/";
	static final String API_NAME = "buy_item?user_code="+USER_ID+"&item_price="+ ITEM_PRICE;
	
	//Status codes:
	public static final int SUCCESS = 0;
	public static final int USER_NOT_EXISTs = 1;
	public static final int NOT_ENOUGH_MONEY = 2;
	public static final int UNKNOWN_ERROR = 3;
	
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
	protected Integer parseSuccessResult(JSONObject jso) throws JSONException {
		return Integer.valueOf(jso.getInt(STATUS_TAG));
	}
	
	@Override
	protected Integer parseUnsuccessResult(JSONObject jso) throws JSONException {
		int status = jso.getInt(STATUS_TAG);
		return Integer.valueOf(status);
	}

}

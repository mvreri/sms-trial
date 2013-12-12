package com.vsm.radio18.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqBuyItem extends RequestData {

	/** TODO:
	 * It should be: status =
	 * 	0: success
	 * 	1: not enough money
	 * 	2: user not exists
	 * 	3: unknown reason
	 */
	
	private static final String USER_ID = "$user_id";
	private static final String ITEM_PRICE = "$item_price";
	static final String BASE_URL = "http://sms.appngon.com/index.php/onepay_service/";
	static final String API_NAME = "buy_item?user_code="+USER_ID+"&item_price="+ ITEM_PRICE;
	private static final String SUCCESS = "success";
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
		int succ = jso.getInt(SUCCESS);
		if ( succ == 1 ) {
			return Boolean.valueOf(true);
		} else return Boolean.valueOf(false);
	}

}

package com.vsm.radio18.data;


public abstract class PaymentRequest extends RequestData {

	@Override
	protected void cacheData(String url, Object result) {
		//no-op
	}
	
	@Override
	protected boolean isCached(String url) {
		return false;
	}
	
	@Override
	protected Object cachedData(String url) {
		return null;
	}
}

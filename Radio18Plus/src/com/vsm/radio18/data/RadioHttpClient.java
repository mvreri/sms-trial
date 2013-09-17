package com.vsm.radio18.data;

import org.apache.http.impl.client.DefaultHttpClient;

import dtd.phs.lib.utils.Logger;

public class RadioHttpClient {

	static private volatile DefaultHttpClient dataClient = null;
	static private volatile DefaultHttpClient userDataClient = null;

	public static DefaultHttpClient getDataInstance() {
		if ( dataClient == null ) {
			dataClient = new DefaultHttpClient();
		}
		return dataClient;
	}

//	public static DefaultHttpClient getUserDataInstance() {
//		if ( userDataClient == null) {
//			userDataClient = new DefaultHttpClient();
//		}
//		return userDataClient;
//
//	}

	public static void destroyUserClient() {
		try {
			userDataClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			Logger.logError(e);
		}
		userDataClient = null;
	}

}

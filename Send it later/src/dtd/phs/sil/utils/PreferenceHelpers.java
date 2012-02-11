package dtd.phs.sil.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import dtd.phs.sil.R;

public class PreferenceHelpers {
	private static final int DEFAULT_MAX_SENT_SIZE = 5;

	public static void setPreference(Context context,String prefName, String data) {
		Logger.logInfo("Pref: " + prefName + " is set to: " + (data == null ? "NULL":data) );
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(prefName, data);
		editor.commit();
	}

	public static String getPreference(Context context,String prefName) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String prefData = settings.getString(prefName, null); 
		return prefData;        
	}

	public static int getMaxSentSize(Context context) {
		String str = getPreference(context, context.getResources().getString(R.string.PREF_MAX_SENT_ITEM));
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			Logger.logError(e);
			return DEFAULT_MAX_SENT_SIZE;
		}
	}
	
	
	private static final Object FALSE_VALUE = "false";
	private static final String PREF_ON_RATE_LINK_CLICKED = "PREF_ON_RATE_LINK_CLICKED";
	
	public static boolean clickedOnRateLink(Context context) {
		String str = getPreference(context, PREF_ON_RATE_LINK_CLICKED);
		if ( str == null || str.equals(FALSE_VALUE)) {
			return false;
		} else return true;
	}
	
	public static void markOnRateLinkClicked(Context context) {
		setPreference(context, PREF_ON_RATE_LINK_CLICKED, String.valueOf(true));
	}

	private static final String PREF_COUNT_SUCC_SENT = "PREF_COUNT_SUCC_SENT";
	public static int getSuccessSentMessagesCount(Context context) {
		String str = getPreference(context, PREF_COUNT_SUCC_SENT);
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			Logger.logInfo("PREF_COUNT_SUCC_SENT is null !");
			return 0;
		}
	}
	
	public static void increaseSuccMessagesCount(Context context) {
		int count = getSuccessSentMessagesCount(context);
		count++;
		setSuccessSentMessageCount(context,count);
	}

	private static void setSuccessSentMessageCount(Context context, int count) {
		setPreference(context, PREF_COUNT_SUCC_SENT, String.valueOf(count));
	}
	
//	public static void saveMaxSentSize(Context context, int maxSentSize) {
//		setPreference(context, context.getResources().getString(R.string.PREF_MAX_SENT_ITEM), String.valueOf(maxSentSize));
//	}
}

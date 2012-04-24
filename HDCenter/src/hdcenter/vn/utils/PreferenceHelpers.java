package hdcenter.vn.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelpers {

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
	private static final String PREF_FIRST_TIME = "PREF_FIRST_TIME";
	private static final String PREF_LAST_CLEAN_UP_SD = "PREF_LAST_CLEAN_UP_SD_CARD";
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

	public static boolean firstTimeRunning(Context context) {
		String firstTime = getPreference(context, PREF_FIRST_TIME );
		if ( firstTime == null ) {
			return true;
		} else return false;
		
	}

	public static void disableFirstTimeRunning(Context context) {
		setPreference(context, PREF_FIRST_TIME, "false");
	}

	public static long getLastCleanupTime(Context context) {
		String lastTime = getPreference(context, PREF_LAST_CLEAN_UP_SD);
		if (lastTime == null) {
			long current = System.currentTimeMillis();
			setLastCleanupTime(context,current);
			return current;
		} else {
			return Long.parseLong(lastTime);
		}
		
	}

	public static void setLastCleanupTime(Context context, long time) {
		setPreference(context, PREF_LAST_CLEAN_UP_SD, String.valueOf(time));
	}
	
}

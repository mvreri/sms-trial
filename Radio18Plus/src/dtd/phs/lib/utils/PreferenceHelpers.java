package dtd.phs.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

public class PreferenceHelpers {



	private static void setPreference(Context context,String prefName, String data) {
		//Logger.logInfo("Pref: " + prefName + " is set to: " + (data == null ? "NULL":data) );

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(prefName, data);
		editor.commit();
	}

	private static String getPreference(Context context,String prefName) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String prefData = settings.getString(prefName, null); 
		return prefData;        
	}
	
	private static void removePreference(Context context, String prefName) {
		Logger.logInfo("Pref: " + prefName + " is removed !");
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.remove(prefName);
		editor.commit();
	}



	private static final String PREF_FIRST_TIME = "PREF_FIRST_TIME";
	private static final String PREF_LAST_CLEAN_UP_SD = "PREF_LAST_CLEAN_UP_SD_CARD";
	private static final String PREF_USER_ID = "PREF_ACCOUNT_CREATED";
	private static final String TRUE = "true";
	private static final String FALSE = "false";

	public static boolean firstTimeRunning(Context context) {
		String firstTime = getPreference(context, PREF_FIRST_TIME );
		if ( firstTime == null ) {
			return true;
		} else return false;

	}

	public static void disableFirstTimeRunning(Context context) {
		setPreference(context, PREF_FIRST_TIME, FALSE);
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

	public static String getUserId(Context context) {
		String result = getPreference(context, PREF_USER_ID);
		return result;
	}

	public static void setUserId(Context context, String userId) {
		setPreference(context, PREF_USER_ID, userId);
	}

}

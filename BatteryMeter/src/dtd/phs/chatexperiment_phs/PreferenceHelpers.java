package dtd.phs.chatexperiment_phs;

import java.util.concurrent.ExecutionException;

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


	private static final String PREF_LAST_CLEAN_UP_SD = "PREF_LAST_CLEAN_UP_SD_CARD";
	private static final String PREF_FIRST_TIME = "PREF_FIRST_TIME_RUNNING";

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


	private static final String TRUE = "true";
	private static final String FALSE = "false";
	private static final String PREF_IS_IN_PROGRESS = "pref_in_progress";
	private static final String PREF_START_TIME = "pref_start_time";
	private static final String PREF_BATTERY_LEVEL = "pref_battery_level";

	public static void setInProgress(Context applicationContext, boolean b) {
		String value = b ? TRUE : FALSE;
		setPreference(applicationContext, PREF_IS_IN_PROGRESS, value);
	}

	public static boolean isInProgress(Context applicationContext, boolean defaultValue) {
		String value = getPreference(applicationContext, PREF_IS_IN_PROGRESS);
		if ( value == null ) return defaultValue;
		if ( value.equals(TRUE)) return true;
		if ( value.equals(FALSE)) return false;
		return defaultValue;
	}

	public static void setStartTime(Context applicationContext,long currentTimeMillis) {
		setPreference(applicationContext, PREF_START_TIME, String.valueOf(currentTimeMillis));
	}
	public static long getStartTime(Context applicationContext, long defaultValue) {
		String str = getPreference(applicationContext, PREF_START_TIME);	
		
		try { 
			return Long.parseLong(str);
		} catch (Exception e) {
			//null / numeric format exception
			Logger.logError(e);
			return defaultValue;
		}
	}

	public static void setStartBattery(Context applicationContext, int currentBatteryLevel) {
		setPreference(applicationContext, PREF_BATTERY_LEVEL, String.valueOf(currentBatteryLevel));
	}
	
	public static int getStartBattery(Context applicationContext, int defaultValue) {
		String str = getPreference(applicationContext, PREF_BATTERY_LEVEL);
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			Logger.logError(e);
			return defaultValue;
		}
	}

	private static final String PREF_END_TIME = "PREF_END_TIME";
	public static void setEndTime(Context applicationContext,long currentTimeMillis) {
		setPreference(applicationContext, PREF_END_TIME, ""+currentTimeMillis);
	}

	public static long getEndTime(Context applicationContext,long defaultValue) {
		String str = getPreference(applicationContext, PREF_END_TIME);
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	private static final String PREF_END_BATTERY = "PREF_END_BATTERY";
	public static void setEndBattery(Context applicationContext,
			int currentBatteryLevel) {
		setPreference(applicationContext, PREF_END_BATTERY, ""+currentBatteryLevel);
	}

	public static int getEndBattery(Context applicationContext, int defaultValue) {
		String str = getPreference(applicationContext, PREF_END_BATTERY);
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

}

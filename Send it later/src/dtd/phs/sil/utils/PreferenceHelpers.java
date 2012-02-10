package dtd.phs.sil.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelpers {
	private static final String PREF_FILE_NAME = "pref_send_it_later";
	private static final int DEFAULT_MAX_SENT_SIZE = 5;

	public static void setPreference(Context context,String prefName, String data) {
		SharedPreferences settings = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
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
		String str = getPreference(context, "PREF_MAX_SENT_ITEM");//context.getResources().getString(R.string.PREF_MAX_SENT_ITEM));
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			Logger.logError(e);
			return DEFAULT_MAX_SENT_SIZE;
		}
	}
	
//	public static void saveMaxSentSize(Context context, int maxSentSize) {
//		setPreference(context, context.getResources().getString(R.string.PREF_MAX_SENT_ITEM), String.valueOf(maxSentSize));
//	}
}

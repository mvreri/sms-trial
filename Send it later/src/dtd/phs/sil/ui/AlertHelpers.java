package dtd.phs.sil.ui;

import java.util.HashMap;

import dtd.phs.sil.R;

import android.content.Context;

public class AlertHelpers {
	static public enum AlertTypes { SILENT,VIBRANT,SMS_TONE,VIBRANT_N_TONE}
	public static final int DEFAULT_ALERT_INDEX = 0;;
//	static public String[] ALERT_STRINGS = {"Silent","Vibrant","Play sound","Both vibrant & sound"};
	static public String[] ALERT_STRINGS = null;
	static public AlertTypes[] ALERT_TYPE = { AlertTypes.SILENT,AlertTypes.VIBRANT,AlertTypes.SMS_TONE,AlertTypes.VIBRANT_N_TONE};
	public static HashMap<AlertTypes, String> mapAlertType2Str = new HashMap<AlertTypes, String>();
//	static {		
//		for(int i = 0 ; i < ALERT_STRINGS.length ; i++) {
//			mapAlertType2Str.put(ALERT_TYPE[i], ALERT_STRINGS[i]);
//		}
//	}
	public static int indexOf(AlertTypes alertType) {
		for(int i = 0 ; i < ALERT_TYPE.length ; i++)
			if ( ALERT_TYPE[i] == alertType ) return i;
		return -1;
	}
	
	public static void initAlert(Context context) {
		ALERT_STRINGS = context.getResources().getStringArray(R.array.alerts);
		mapAlertType2Str.clear();
		for(int i = 0 ; i < ALERT_STRINGS.length ; i++) {
			mapAlertType2Str.put(ALERT_TYPE[i], ALERT_STRINGS[i]);
		}
	}
}

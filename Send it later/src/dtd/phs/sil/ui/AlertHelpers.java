package dtd.phs.sil.ui;

import java.util.HashMap;

public class AlertHelpers {
	static public enum AlertTypes { SILENT,VIBRANT,PHONE_SETTING,SMS_TONE,VIBRANT_N_TONE};
	static public String[] ALERT_STRINGS = {"Silent","Vibrant","Phone setting","SMS received tone","Both vibrant & tone"};
	static public AlertTypes[] ALERT_TYPE = { AlertTypes.SILENT,AlertTypes.VIBRANT,AlertTypes.PHONE_SETTING,AlertTypes.SMS_TONE,AlertTypes.VIBRANT_N_TONE};
	public static HashMap<AlertTypes, String> mapAlertType2Str = new HashMap<AlertTypes, String>();
	static {
		for(int i = 0 ; i < ALERT_STRINGS.length ; i++) {
			mapAlertType2Str.put(ALERT_TYPE[i], ALERT_STRINGS[i]);
		}
	}
	public static int indexOf(AlertTypes alertType) {
		for(int i = 0 ; i < ALERT_TYPE.length ; i++)
			if ( ALERT_TYPE[i] == alertType ) return i;
		return -1;
	}
}

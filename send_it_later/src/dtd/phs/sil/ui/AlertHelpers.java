package dtd.phs.sil.ui;

public class AlertHelpers {
	static public enum AlertTypes { NONE,VIBRANT,PHONE_SETTING,SMS_TONE,VIBRANT_N_TONE};
	static public String[] ALERT_STRINGS = {"None","Vibrant","Phone setting (depends on phone profile)","SMS received tone","Both vibrant & tone"};
	static public AlertTypes[] ALERT_TYPE = { AlertTypes.NONE,AlertTypes.VIBRANT,AlertTypes.PHONE_SETTING,AlertTypes.SMS_TONE,AlertTypes.VIBRANT_N_TONE};
}

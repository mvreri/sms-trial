package dtd.phs.sil.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.PowerManager;
import android.telephony.SmsManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import dtd.phs.sil.R;
import dtd.phs.sil.SendSMSService;

public class Helpers {

	public static final boolean DEBUG_MODE = true;

	public static View inflate(Context context, int layout, ViewGroup parent) {
		LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inf.inflate(layout, parent);
	}

	public static void showOnlyView(FrameLayout mainFrames, int id) {
		for(int i = 0 ; i < mainFrames.getChildCount() ; i++) {
			int disp = View.INVISIBLE;
			if ( i == id) disp = View.VISIBLE;
			mainFrames.getChildAt(i).setVisibility(disp);
		}
	}

	public static float dp2px(Context context, int i) {		
		Resources r = context.getResources();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics());
	}

	public static void startAfter(final int waitingTime,final Runnable runner) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						this.wait(waitingTime);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runner.run();
			}
		}).start();
	}

	public static String formatTime(Context context, long sentTime) {
		Date date = new Date(sentTime);

		Calendar current = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Resources resources = context.getResources();
		String today = resources.getString(R.string.Today_at);
		String tommorw = resources.getString(R.string.Tomorrow_at);
		String yesterday = resources.getString(R.string.Yesterday_at);
		if ( equalsDate(cal,current)) {
			return today+ " " + (new SimpleDateFormat("HH:mm").format(date));
		}
		cal.add(Calendar.DATE, 1);
		if ( equalsDate(cal,current)) {
			return yesterday+ " " + (new SimpleDateFormat("HH:mm").format(date));
		}
		cal.add(Calendar.DATE, -2);
		if ( equalsDate(cal,current)) {
			return tommorw +" "+ (new SimpleDateFormat("HH:mm").format(date));
		}


//		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm EE - MMMM.dd, yyyy");
//		return formatter.format(date);
		return formatDate(date);
	}

	private static boolean equalsDate(Calendar cal, Calendar current) {
		if ( cal.get(Calendar.MONTH) != current.get(Calendar.MONTH)) return false;
		if ( cal.get(Calendar.YEAR) != current.get(Calendar.YEAR)) return false;
		if ( cal.get(Calendar.DATE) != current.get(Calendar.DATE)) return false;
		return true;
	}

	public static void hideSoftKeyboard(Activity act, View view) {
		InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}

	public static void hideSoftKeyboard(Activity activity) {
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);		
	}

	public static String parsePhoneNumber(String str) {
		return str.replaceAll(" ", "");
	}

	public static boolean isPhoneNumber(String str) {
		if ( str == null || str.length() == 0 ) return false;
		str = parsePhoneNumber(str);
		char[] s = str.toCharArray();
		if (! Character.isDigit(s[0]) && s[0] != '+' ) return false;
		for(int i = 1; i < s.length ; i++) {
			if ( ! Character.isDigit(s[i])) return false;
		}
		return true;
	}

	private static final String SENT = "dtd.phs.sil.send_message.sent";
	public static void sendMessage(
			Context context,
			String receiverNumber, 
			String content,
			final I_SMSListener listener) {
		SmsManager sms = SmsManager.getDefault();
		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);

		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					listener.onSentSuccess();
					return;
				default:
					listener.onSentFailed(getResultCode());
				}

			}
		},new IntentFilter(SENT));


		try {
			ArrayList<String> parts = sms.divideMessage(content);
			ArrayList<PendingIntent> sendPIs = new ArrayList<PendingIntent>();	
			for(int i = 0 ; i < parts.size() ; i++) {
				sendPIs.add(sentPI);
			}
			sms.sendMultipartTextMessage(receiverNumber, null, parts,sendPIs,null);
		} catch (Exception e) {
			Logger.logError(e);
		}
	}

	public static void broadcastDatabaseChanged(Context context) {
		Intent i = new Intent();
		i.setAction(SendSMSService.ACTION_MESSAGE_SENT);
		context.sendBroadcast(i);
	}

	public static final String APP_SIL_MARKET = "market://details?id=dtd.phs.sil";

	public static void gotoMarket(Activity activity) {
		Intent goToMarket = null;
		goToMarket = new Intent(Intent.ACTION_VIEW,Uri.parse(APP_SIL_MARKET));
		activity.startActivity(goToMarket);
	}

	public static final String APP_AUTHOR_MARKET = "market://search?q=pham+hung+son";
	public static void gotoMarketSameAuthor(Activity activity) {
		Intent goToMarket = null;
		goToMarket = new Intent(Intent.ACTION_VIEW,Uri.parse(APP_AUTHOR_MARKET));
		activity.startActivity(goToMarket);
	}

	public static void launchHomeScreen(Activity activity) {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(startMain);		
	}

	public static void enterTransition(Activity hostedActivity) {
		hostedActivity.overridePendingTransition(R.anim.zoom_out_haft_one,R.anim.zoom_out_one_2);		
	}

	public static void exitTransition(Activity hostedActivity) {
		hostedActivity.overridePendingTransition(R.anim.zoom_in_2o,R.anim.zoom_in_oh);		
	}

	public static void enterActivity(Activity hostedActivity, Intent i) {
		hostedActivity.startActivity(i);
		Helpers.enterTransition(hostedActivity);
	}

	public static void exitActivity(Activity hostedActivity, Intent i) {
		hostedActivity.startActivity(i);
		Helpers.exitTransition(hostedActivity);
	}

	private static final String POWER_TAG = "dtd.phs.wakelock";
	public static PowerManager.WakeLock acquireWakelock(Context context) {
		PowerManager pm  = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock lock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, POWER_TAG);
		lock.acquire();
		return lock;
	}

	public static void share(Activity activity,int subjectId, int contentId) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		Resources res = activity.getResources();
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, res.getString(subjectId));
		intent.putExtra(Intent.EXTRA_TEXT, res.getString(contentId));

		activity.startActivity(Intent.createChooser(intent, res.getString(R.string.Share)));
	}

	public static void launchIntentCall(Activity activity, String number) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));
		activity.startActivity(intent);
	}

	public static void launchIntentSMS(Activity activity, String number) {
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.setType("vnd.android-dir/mms-sms");
		smsIntent.putExtra("address", number);
		activity.startActivity(smsIntent);
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void copyArrayList(ArrayList dest, ArrayList src) {
		if ( src == null ) {
			Logger.logInfo("Source array list is NULL ! Cannot be copied ");
		} else if ( src == dest ) {
			Logger.logInfo("Same array list, need not to be copied !");
		} else {
			dest.clear();
			for(int i = 0 ; i < src.size() ; i++)
				dest.add(src.get(i));
		}
	}

	public static String formatDate(int y, int m, int d) {
		return formatDate(new Date(y-1900,m,d));
	}
	
	public static String formatDate(Date date) {
		String pattern = createDatePattern(); 
		String displayDate = new SimpleDateFormat(pattern).format(date);
		return wordsCapitalize(displayDate);
	}

	private static String createDatePattern() {
		String lang = Locale.getDefault().getISO3Language();
		Logger.logInfo(lang);
		//default date pattern: English style
		String pattern = "EEEE - MMMM.dd, yyyy"; 
		if ( lang.toLowerCase().equals("fra")) {
			pattern = "EEEE - dd MMMM, yyyy";
		} else if ( lang.toLowerCase().equals("vie")) {
			pattern = "EEEE - dd MMMM, yyyy";
		}
		return pattern;
	}

	private static String wordsCapitalize(String sentence) {
		StringBuilder builder = new StringBuilder();
		String[] words = sentence.split(" ");
		for(int i = 0 ; i< words.length ; i++) {
			words[i] = capitalizeFirstChar(words[i]);
			builder.append(words[i]);
			if ( i < words.length - 1) builder.append(" ");
		}
		return builder.toString();
	}

	private static String capitalizeFirstChar(String word) {
		if ( word == null) return null;
		if ( word.length() == 1 ) return word.toLowerCase();
		return ""+(Character.toUpperCase(word.charAt(0))) + word.substring(1);
	}

	
	
}

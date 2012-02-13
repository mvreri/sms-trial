package dtd.phs.sil.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.telephony.SmsManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import dtd.phs.sil.R;
import dtd.phs.sil.SendSMSService;

public class Helpers {

	public static final boolean DEBUG_MODE = true;

	public static View inflate(Context context, int layout) {
		LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inf.inflate(layout, null);
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

		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm EE - MMMM.dd, yyyy");
		return formatter.format(date);
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
		str = parsePhoneNumber(str);
		char[] s = str.toCharArray();
		if (! Character.isDigit(s[0]) && s[0] != '+' ) return false;
		for(int i = 1; i < s.length ; i++) {
			if ( ! Character.isDigit(s[i])) return false;
		}
		return true;
	}
	
	private static final String DELIVERED = "dtd.phs.sil.send_message.delivered";
	private static final String SENT = "dtd.phs.sil.send_message.sent";
	public static void sendMessage(
			Context context,
			String receiverNumber, 
			String content,
			final I_SMSListener listener) {
		SmsManager sms = SmsManager.getDefault();
		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);

		//Note: Be careful : listener.onNormalMessageSendSuccess() called 2 times (1 sent, 1 delivered)
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

		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					listener.onMessageDelivered();
					break;
				case Activity.RESULT_CANCELED:
					listener.onMessageDeliveryFailed();
					break;
				}
			}
		},new IntentFilter(DELIVERED));

		try {
			ArrayList<String> parts = sms.divideMessage(content);
			ArrayList<PendingIntent> sendPIs = new ArrayList<PendingIntent>();	
			ArrayList<PendingIntent> deliveryPIs = new ArrayList<PendingIntent>();
			for(int i = 0 ; i < parts.size() ; i++) {
				sendPIs.add(sentPI);
				deliveryPIs.add(deliveredPI);
			}
			sms.sendMultipartTextMessage(receiverNumber, null, parts,sendPIs,deliveryPIs);
		} catch (Exception e) {
			Logger.logError(e);
		}
	}

	public static void broadcastDatabaseChanged(Context context) {
		Intent i = new Intent();
		i.setAction(SendSMSService.ACTION_MESSAGE_SENT);
		context.sendBroadcast(i);
	}

}

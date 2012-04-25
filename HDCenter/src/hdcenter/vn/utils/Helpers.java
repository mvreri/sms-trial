package hdcenter.vn.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

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

	public static final String APP_SIL_MARKET = "market://details?id=hdcenter.vn";
	public static void gotoMarket(Activity activity) {
		Intent goToMarket = null;
		goToMarket = new Intent(Intent.ACTION_VIEW,Uri.parse(APP_SIL_MARKET));
		activity.startActivity(goToMarket);
	}

	public static String MD5(String inputString) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(inputString.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

	static public Bitmap downloadBitmap(String url) throws MalformedURLException, IOException {
		Logger.logInfo("URL: " + url +" -- is being loaded !");
		InputStream is = (InputStream) new URL(url).getContent();
		Bitmap bm = BitmapFactory.decodeStream(is); 
		return bm;
	}

	public static void showToast(
			Context applicationContext,
			int stringResId) {
		Toast.makeText(applicationContext, stringResId, Toast.LENGTH_LONG).show();
	}

	public static void cancelDialog(Activity activity, int dialogWaitId) {
		try {
			activity.dismissDialog(dialogWaitId);
		} catch (Exception e) {
			Logger.logError(e);
		}
		
	}

	public static void launchHomeScreen(Activity activity) {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(startMain);		
	}

	public static void enterActivity(Activity fromActivity, Intent i) {
		fromActivity.startActivity(i);
		//TODO: enter activity animation
	}

	public static void exitActivityFrom(Activity fromActivity, Intent i) {
		fromActivity.startActivity(i);
		//TODO: exit activity animation		
	}

	public static void verify(boolean b, String message) {
		if ( DEBUG_MODE )
			assert b : message;
	}

}

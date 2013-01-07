package dtd.phs.lib.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import dtd.phs.noad_uninstaller.PHS_AppInfo;

public class Helpers {

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

	public static void hideSoftKeyboard(Activity act, View view) {
		InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}

	public static void hideSoftKeyboard(Activity activity) {
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);		
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

	public static void showToast(
			final Activity act,
			final int stringResId) {
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(act, stringResId, Toast.LENGTH_LONG).show();
			}
		});
		
	}

	public static void showToast(
			final Activity act,
			final String message) {
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(act, message, Toast.LENGTH_LONG).show();
			}
		});
		
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

	static public Bitmap downloadBitmap(String url) throws MalformedURLException, IOException {
		//Logger.logInfo("URL: " + url +" -- is being loaded !");
		InputStream is = (InputStream) new URL(url).getContent();
		Bitmap bm = BitmapFactory.decodeStream(is); 
		return bm;
	}


	static public String getLocalIP() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()){
				NetworkInterface current = interfaces.nextElement();
				
				Enumeration<InetAddress> addresses = current.getInetAddresses();
				while (addresses.hasMoreElements()){
					InetAddress current_addr = addresses.nextElement();
					if (current_addr.isLoopbackAddress()) continue;
					if ( current_addr instanceof Inet4Address) {
						String addr = current_addr.toString();
						if (addr.startsWith("/") ) return addr.substring(1);
						return addr;
					}
				}
			}
			return null;
		} catch (SocketException e) {			
			e.printStackTrace();
			return null;
		}
	}


	public static ArrayList<PHS_AppInfo> getAppsInfo(Context context) {
		PackageManager packageManager = context.getPackageManager();
		List<ApplicationInfo> installedApplications = 
		   packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
		ArrayList<PHS_AppInfo> apps = new ArrayList<PHS_AppInfo>();
		for (ApplicationInfo appInfo : installedApplications)
		{
			int flags = appInfo.flags;
			if ( ( flags & ApplicationInfo.FLAG_SYSTEM ) == 0)
				apps.add(new PHS_AppInfo(appInfo.packageName, appInfo.loadLabel(packageManager).toString(), appInfo.loadIcon(packageManager)));
		} 

		Collections.sort(apps,new PHS_AppInfo.AppComparator());
		return apps;

	}
	
	public static void uninstall(Activity act, PHS_AppInfo app) {
		String packageName = app.getPackageName();
		Uri packageURI = Uri.parse("package:"+packageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		act.startActivity(uninstallIntent);
	}
}

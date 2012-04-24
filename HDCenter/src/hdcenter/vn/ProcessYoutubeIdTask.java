package hdcenter.vn;

import hdcenter.vn.utils.Helpers;
import hdcenter.vn.utils.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;

import com.keyes.youtube.YouTubeUtility;

public class ProcessYoutubeIdTask extends AsyncTask<String, Void, Uri> {

	private Activity activity;
	private int dialogWaitId;

	public ProcessYoutubeIdTask(Activity activity, int dialogWaitId) {
		super();
		this.activity = activity;
		this.dialogWaitId = dialogWaitId;
	}

	@Override
	protected Uri doInBackground(String... params) {
		String id = params[0];

		String lYouTubeFmtQuality = getVideoQuality();

		String lUriStr = null;
		try {
			lUriStr = YouTubeUtility.calculateYouTubeUrl(lYouTubeFmtQuality, true, id);
		} catch (ClientProtocolException e) {
			Logger.logError(e);
			return null;
		} catch (UnsupportedEncodingException e) {
			Logger.logError(e);
			return null;
		} catch (IOException e) {
			Logger.logError(e);
			return null;
		} catch (NullPointerException e) {
			Logger.logError(e);
			return null;
		}

		if ( lUriStr != null) 
			return Uri.parse(lUriStr);
		return null;

	}

	@Override
	protected void onPostExecute(Uri uri) {
		super.onPostExecute(uri);
		Helpers.cancelDialog(activity,dialogWaitId);
		if ( isCancelled() ) return;
		if ( uri == null ) {
			Helpers.showToast(activity, R.string.error_please_retry);
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "video/*");
		activity.startActivity(intent);
	}

	private String getVideoQuality() {
		String lYouTubeFmtQuality = "17"; 
		WifiManager lWifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
		TelephonyManager lTelephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		if( (lWifiManager.isWifiEnabled() && lWifiManager.getConnectionInfo() != null && lWifiManager.getConnectionInfo().getIpAddress() != 0) ||
				( (lTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS ||

						/* icky... using literals to make backwards compatible with 1.5 and 1.6 */		
						lTelephonyManager.getNetworkType() == 9 /*HSUPA*/  ||
						lTelephonyManager.getNetworkType() == 10 /*HSPA*/  ||
						lTelephonyManager.getNetworkType() == 8 /*HSDPA*/  ||
						lTelephonyManager.getNetworkType() == 5 /*EVDO_0*/  ||
						lTelephonyManager.getNetworkType() == 6 /*EVDO A*/) 

						&& lTelephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED) 
		){
			lYouTubeFmtQuality = "18";
		}
		return lYouTubeFmtQuality;
	}

}

package dtd.phs.sms.message_center;

import dtd.phs.sms.util.Helpers;
import dtd.phs.sms.util.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class ConnectivityChangedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	    // TODO Auto-generated method stub
		Logger.logInfo("Network state changed !");
		boolean connected = Helpers.isConnectedToInternet(context);
		Intent i = new Intent(context,GoogleXMPPService.class);
		GoogleXMPPService.messageToSend = null;
		if ( connected ) {
			Logger.logInfo("Network state : Connected !");
			context.startService(i);
		} else {
			context.stopService(i);
			Logger.logInfo("Network state : Disconnected !");			
		}
		

	}

}

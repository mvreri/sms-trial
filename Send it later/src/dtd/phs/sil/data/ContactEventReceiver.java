package dtd.phs.sil.data;

import dtd.phs.sil.UpdateLTCService;
import dtd.phs.sil.utils.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class ContactEventReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		String number = null;
		if(action.equalsIgnoreCase("android.intent.action.PHONE_STATE")){
			if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
				Logger.logInfo("Incoming call: " + number);
			}         
		}
		else if ( action.equalsIgnoreCase("android.intent.action.NEW_OUTGOING_CALL")) {
			number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			Logger.logInfo("Outgoing call: " + number);
		} else {
			Bundle bundle = intent.getExtras();
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage message = SmsMessage.createFromPdu((byte[])pdus[0]);
			if(!message.isEmail()) {				 
				number = message.getOriginatingAddress();
				//TODO: test with multiple message received at the same time !
				Logger.logInfo("Message: " + number);
			}
		}	
		
		if ( number != null) {
			UpdateLTCService.acquireWakelock(context);
			Intent service = new Intent(context,UpdateLTCService.class);
			service.putExtra(UpdateLTCService.EXTRA_NUMBER, number);
			context.startService(service);
		}
	}

}

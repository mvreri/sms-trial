package dtd.phs.sil.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import dtd.phs.sil.SendSMSService;

public class AlarmReceiver extends BroadcastReceiver {

	public static final String PENDING_MESSAGE_ID = "row_id";

	@Override
	public void onReceive(Context context, Intent intent) {
	    SendSMSService.acquireWakelock(context);
	    Intent i = new Intent(context,SendSMSService.class);
	    i.putExtra(PENDING_MESSAGE_ID, intent.getLongExtra(PENDING_MESSAGE_ID, -1));
	    context.startService(i);
	}


}

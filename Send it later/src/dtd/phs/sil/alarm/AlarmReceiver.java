package dtd.phs.sil.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import dtd.phs.sil.SendSMSService;

public class AlarmReceiver extends BroadcastReceiver {

	private static final String POWER_TAG = "phs.sms";
	public static final String PENDING_MESSAGE_ID = "row_id";

	@Override
	public void onReceive(Context context, Intent intent) {
	    PowerManager pm  = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	    PowerManager.WakeLock lock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, POWER_TAG);
	    lock.acquire();
	    SendSMSService.setWakeLock(lock);
	    Intent i = new Intent(context,SendSMSService.class);
	    i.putExtra(PENDING_MESSAGE_ID, intent.getLongExtra(PENDING_MESSAGE_ID, -1));
	    context.startService(i);
	}

}

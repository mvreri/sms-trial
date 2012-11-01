package dtd.phs.chatexperiment_phs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimerAlarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SaveDataService.startWakeLock(context);
		Intent service = new Intent(context, SaveDataService.class);
		context.startService(service);
	}

}

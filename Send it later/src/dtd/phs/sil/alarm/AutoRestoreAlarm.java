package dtd.phs.sil.alarm;

import dtd.phs.sil.data.DataCenter;
import dtd.phs.sil.entities.PendingMessageItem;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoRestoreAlarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if ( intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			PendingMessageItem message = DataCenter.getNextPendingMessage(context);
			if ( message != null )
				AlarmHelpers.refreshAlarm(context);
		}
	}

}

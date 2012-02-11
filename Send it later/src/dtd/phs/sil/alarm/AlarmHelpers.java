package dtd.phs.sil.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import dtd.phs.sil.data.DataCenter;
import dtd.phs.sil.entities.PendingMessageItem;

public class AlarmHelpers {
	private static final long DELTA_TIME = 59 * 1000;

	public static void createNewAlarm(Context context,PendingMessageItem message) {
		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		long time = message.getNextTimeMillis() - DELTA_TIME;
		if ( time > System.currentTimeMillis() ) {
			time += DELTA_TIME;
		}
		Intent intent = new Intent(context,AlarmReceiver.class);
		intent.putExtra(AlarmReceiver.PENDING_MESSAGE_ID, message.getId());
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarm.set(AlarmManager.RTC_WAKEUP, time, pi);
	}
	
	public static void cancelAlarm(Context context) {
		Intent intent = new Intent(context,AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pi);
	}
	
	public static void refreshAlarm(Context context) {
		cancelAlarm(context);
		PendingMessageItem message = DataCenter.getNextPendingMessage(context);
		if ( message != null )
			createNewAlarm(context, message);
	}
}

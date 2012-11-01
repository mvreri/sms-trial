package dtd.phs.chatexperiment_phs;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class Helpers {

	public static String formatToTime(long time) {
		Date date = new Date(time);
		String timeString = new SimpleDateFormat("HH:mm").format(date);
		return timeString;
	}
	
	public static int getCurrentBatteryLevel(Context context) {
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = context.registerReceiver(null, ifilter);
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
		int percent = (level * 100 ) / scale;
		Logger.logInfo("Level: " + level + " -- scale: " + scale  + " -- Percent: " + percent);
		return percent;
	}


}

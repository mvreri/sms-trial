package dtd.phs.chatexperiment_phs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class SaveDataService extends Service {

	private static WakeLock lock = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public static void startWakeLock(Context context) {
		if ( lock == null) {
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			lock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PHS_BATTERY_METER_LOCK");
			lock.acquire();
		}
	}
	private static void stopWakeLock() {
		if ( lock != null ) 
			lock.release();
		lock = null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {		
		super.onStart(intent, startId);
		Logger.logInfo("Timer fired: Wake up & write");
		BatteryStatusTable.addRow(getApplicationContext(), System.currentTimeMillis(), Helpers.getCurrentBatteryLevel(getApplicationContext()));
		stopSelf();
	}
	
	@Override
	public void onDestroy() {
		SaveDataService.stopWakeLock();
		super.onDestroy();
	}

}

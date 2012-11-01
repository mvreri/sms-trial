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
//			Logger.logInfo("Aquiring lock !");
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			lock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PHS_BATTERY_METER_LOCK");
			lock.acquire();
		} else {
			Logger.logError("Lock is aquired already!");
		}
	}
	private static void stopWakeLock() {
		if ( lock != null ) {
//			Logger.logInfo("Releasing lock !");
			lock.release();	
		} else Logger.logError("Lock is realeased already!");
			
		lock = null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {		
		super.onStart(intent, startId);
//		Logger.logInfo("Service started: Wake up & write");
		BatteryStatusTable.addRow(getApplicationContext(), System.currentTimeMillis(), Helpers.getCurrentBatteryLevel(getApplicationContext()));
		stopSelf();
	}
	
	@Override
	public void onDestroy() {
		SaveDataService.stopWakeLock();
		super.onDestroy();
	}

}

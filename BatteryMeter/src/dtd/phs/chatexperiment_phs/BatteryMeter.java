package dtd.phs.chatexperiment_phs;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BatteryMeter extends Activity implements OnClickListener {

	private static final long REPEAT_INTERVAL = 15*1000;
	private TextView tvRate;
	private TextView tvStart;
	private TextView tvEnd;
	private TextView tvDuration;
	private Button btShowLog;
	private Button btStart;
	private Button btEnd;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_poll_info);
        getViews();
        setActions();
    }
	
	@Override
	protected void onResume() {	
		super.onResume();
		refreshUI();
	}

	private void setActions() {
		btShowLog.setOnClickListener(this);
		btStart.setOnClickListener(this);
		btEnd.setOnClickListener(this);
	}

	private void getViews() {
		tvRate = (TextView) findViewById(R.id.tvRate);
        tvStart= (TextView) findViewById(R.id.tvStart);
        tvEnd = (TextView) findViewById(R.id.tvEnd);
        tvDuration = (TextView) findViewById(R.id.tvDuration) ;

        // read from database and print to screen {(time,batt_percentage) }
        btShowLog = (Button) findViewById(R.id.btShowLog);
        
        
        //btStart: confirm before start
        //Start: clear all old data
        //Set a preference: IsStarted = true
        //Start a timer: every 15 minutes: wake up & update database        
        btStart = (Button) findViewById(R.id.btStart);
        
        
        //Can only end if Pref.isStarted = true
        //Stop the timer
        btEnd = (Button) findViewById(R.id.btEnd);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btStart:
			if ( inProgress() ) {
				showToast(R.string.processing_pls_end_before_start);
			} else {
				startMonitor();
			}
			break;
		case R.id.btEnd:
			if ( inProgress() ) {
				endMonitor();
			} else {
				showToast(R.string.no_active_process_cannot_end);
			}
			
			break;
		case R.id.btShowLog:
			startActivity(new Intent(this,ShowLog.class));
			break;
		}
	}

	


	private void showToast(int processingPlsEndBeforeStart) {
		Toast.makeText(getApplicationContext(), processingPlsEndBeforeStart, Toast.LENGTH_LONG).show();
		
	}

	//Start: clear all old data
    //Set a preference: IsStarted = true
	//Set preference: start_time
	//Set preference: start_battery 
    //Start a timer: every 15 minutes: wake up & update database        
	private void startMonitor() {
		BatteryStatusTable.clearAllData(getApplicationContext());
		PreferenceHelpers.setInProgress(getApplicationContext(),true);
		PreferenceHelpers.setStartTime(getApplicationContext(),System.currentTimeMillis());
		PreferenceHelpers.setStartBattery(getApplicationContext(),Helpers.getCurrentBatteryLevel(this));
		startTimerLog();
	}

	private void startTimerLog() {
		PendingIntent pi = getPendingIntent();
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), REPEAT_INTERVAL, pi);
	}

	protected PendingIntent getPendingIntent() {
		Intent intent = new Intent(this,TimerAlarm.class);
		return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	}

	private void stopTimerLog() {
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		am.cancel(getPendingIntent());
	}
	

	/**
	 * Database : nothing 
	 * Preferences:
	 * - isStarted = false
	 * - end_time
	 * - end_battery
	 * Stop timer
	 * Refresh activity
	 */
    private void endMonitor() {
    	PreferenceHelpers.setInProgress(getApplicationContext(), false);
    	PreferenceHelpers.setEndTime(getApplicationContext(), System.currentTimeMillis());
    	PreferenceHelpers.setEndBattery(getApplicationContext(), Helpers.getCurrentBatteryLevel(this));
    	stopTimerLog();
    	refreshUI();
	}
    
	private void refreshUI() {
		tvRate.setText("Rates: xxx s /request" );
		tvStart.setText("Battery (START): " + PreferenceHelpers.getStartBattery(getApplicationContext(),0) + "%");
		tvEnd.setText("Battery (END): " + PreferenceHelpers.getEndBattery(getApplication(), 0) +"%");
		long startTime = PreferenceHelpers.getStartTime(getApplicationContext(), 0);
		long endTime = PreferenceHelpers.getEndTime(getApplication(), 0);
		tvDuration.setText("From " + Helpers.formatToTime(startTime) + " to " +  Helpers.formatToTime(endTime) + " - duration: "  + ((endTime-startTime)/(60*1000)) + " minutes");
	}



	private boolean inProgress() {
		return PreferenceHelpers.isInProgress(getApplicationContext(), false);
	}
	
	/**
	 * Preferences: 
	 * - start time
	 * - end time
	 * - start battery
	 * - end battery
	 * - isStarted
	 */
	
	/**
	 * Database: {(time,battery percentage)}
	 */
}
package dtd.phs.sil;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import dtd.phs.sil.alarm.AlarmHelpers;
import dtd.phs.sil.alarm.AlarmReceiver;
import dtd.phs.sil.data.DataCenter;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.ui.AlertHelpers.AlertTypes;
import dtd.phs.sil.utils.FrequencyHelpers.Frequencies;
import dtd.phs.sil.utils.Helpers;
import dtd.phs.sil.utils.I_SMSListener;
import dtd.phs.sil.utils.Logger;
import dtd.phs.sil.utils.PreferenceHelpers;

public class SendSMSService extends IntentService {

	private static final String SEND_SMS_SERVICE = "dtd.phs.SEND_SMS_SERVICE";

	public SendSMSService() {
		super(SEND_SMS_SERVICE);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.logInfo("Service onCreate() is called");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		Logger.logInfo("OnStartCommand is called");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		removeWakelock();
		Logger.logInfo("Sending service is destroyed !");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Logger.logInfo("Service intent: OnHandle is called !");
		long rowid = intent.getLongExtra(AlarmReceiver.PENDING_MESSAGE_ID, -1);
		if ( rowid != -1) {
			messageItem = DataCenter.getPendingMessageWithId(getApplicationContext(),rowid);
			if ( messageItem != null ) {
				sendMessages(messageItem.getPhoneNumbers(),messageItem.getContent());
				setThreadToSleep(WAITING_SENT_REPORT_TIME);
				runAfterSendingFinish();
			}
		}
	}


	private void setThreadToSleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			Logger.logError(e);
		}
	}

	public static final int WAITING_SENT_REPORT_TIME = 15*1000;
	private static final int NOTIFICATION_ICON = R.drawable.message_desat;
	public static final String ACTION_MESSAGE_SENT = "dtd.phs.sil.message_sent";
	private static final int START_REMIND_RATING_COUNT = 5;
	private static final int PERIOD_REMINDER_RATING = 3;
	private static WakeLock wakeLock = null;
	protected boolean errorOcc;
	protected PendingMessageItem messageItem = null;
	public boolean hasDeliveredMessage;


	private void removeWakelock() {
		if ( SendSMSService.wakeLock != null ) 
				wakeLock.release();
		wakeLock = null;
	}


	private void runAfterSendingFinish() {
		if ( errorOcc || ! hasDeliveredMessage ) {
			Logger.logInfo("Save failed message is progressing ... ");
			DataCenter.saveFailedMessage(getApplicationContext(), messageItem);
		} else {
			Logger.logInfo("Save successful message is progressing ... ");
			DataCenter.saveSentMessage(getApplicationContext(),messageItem);

		}
		if ( messageItem.getFreq() == Frequencies.ONCE ) {
			DataCenter.removePendingItem( getApplicationContext(),messageItem.getId() );
		}
		AlarmHelpers.refreshAlarm(getApplicationContext());
		fireNotification(errorOcc || !hasDeliveredMessage);
		Helpers.broadcastDatabaseChanged(getApplicationContext());
	}



	protected void sendMessages(String[] phoneNumbers, String smsContent) {
		errorOcc = false;
		hasDeliveredMessage = false;
		for(String number : phoneNumbers) {
			Helpers.sendMessage(getApplicationContext(), number,smsContent, new I_SMSListener() {
				@Override
				public void onSentSuccess() {
					hasDeliveredMessage = true;
				}

				@Override
				public void onSentFailed(int errorCode) {
					errorOcc = true;
				}

			});
		}
	}


	public void fireNotification(boolean errorOcc) {

		Resources res = getApplicationContext().getResources();
		String title = null;
		String text = null;
		if ( ! errorOcc ) {
			text = res.getString(R.string.Sent_to) + " " + messageItem.getContact();
			title = res.getString(R.string.successful_sent_notification_title);
			PreferenceHelpers.increaseSuccMessagesCount(getApplicationContext());
		} else {
			title = res.getString(R.string.failed_sent_notification_title);
			text = res.getString(R.string.Sent_failed) + messageItem.getContact();
		}
		Notification notification = createNotification(
				errorOcc,
				getApplicationContext(), 
				NOTIFICATION_ICON, 
				title, 
				text,
				messageItem.getAlert());
		NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(NOTIFICATION_ICON, notification);
	}

	public static Notification createNotification(
			boolean errorOcc, 
			Context context,
			int icon,
			String notificationTitle,
			String notificationText, 
			AlertTypes alertTypes) {
		Notification notification = new Notification(icon, notificationTitle, System.currentTimeMillis());		

		CharSequence contentTitle = notificationTitle;
		CharSequence contentText =  notificationText;

		Intent notificationIntent = new Intent(context, MainActivity.class);
		notificationIntent.putExtra(MainActivity.EXTRA_SELECTED_FRAME, MainActivity.FRAME_SENT);
		notificationIntent.putExtra(MainActivity.EXTRA_SHOW_DIALOG_RATE, false);
		if ( !errorOcc ) {
			Logger.logInfo("No error occurs by sending message");
			int succCount = PreferenceHelpers.getSuccessSentMessagesCount(context);
			boolean clicked = PreferenceHelpers.clickedOnRateLink(context);
			Logger.logInfo("Succ count: " + succCount + " -- clicked: " + clicked);
			if ( ! clicked && succCount >= START_REMIND_RATING_COUNT && (succCount-START_REMIND_RATING_COUNT)%PERIOD_REMINDER_RATING==0 ) {
				Logger.logInfo("Set extra to show rate dialog");
				notificationIntent.putExtra(MainActivity.EXTRA_SHOW_DIALOG_RATE, true);
			}
		} else {
			Logger.logInfo("Error occurs by sending message");
		}
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(
				context, 
				0, 
				notificationIntent, 
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		notification.defaults |= Notification.DEFAULT_LIGHTS;
		switch (alertTypes) {
		case SILENT:
			break;
		case SMS_TONE:
			notification.defaults |= Notification.DEFAULT_SOUND;
			break;
		case VIBRANT:
			notification.defaults |= Notification.DEFAULT_VIBRATE;	
			break;
		case VIBRANT_N_TONE:
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			break;
		}
		return notification;
	}

	public static void acquireWakelock(Context context) {
		if ( SendSMSService.wakeLock == null) {
			Logger.logInfo("Wakelock is acquired !");
			SendSMSService.wakeLock = Helpers.acquireWakelock(context);
		} else {
			Logger.logInfo("Lock already existed !");
		}
	}

}

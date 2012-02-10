package dtd.phs.sil;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import android.telephony.SmsManager;
import dtd.phs.sil.alarm.AlarmHelpers;
import dtd.phs.sil.alarm.AlarmReceiver;
import dtd.phs.sil.data.DataCenter;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.ui.AlertHelpers.AlertTypes;
import dtd.phs.sil.utils.FrequencyHelpers.Frequencies;
import dtd.phs.sil.utils.Helpers;
import dtd.phs.sil.utils.Logger;

public class SendSMSService extends Service {

	private static final String DELIVERED = "dtd.phs.sil.send_message.delivered";
	private static final String SENT = "dtd.phs.sil.send_message.sent";
	protected static final int WAITING_TIME = 5000;
	private static final int NOTIFICATION_ICON = R.drawable.message_desat;
	public static final String ACTION_MESSAGE_SENT = "dtd.phs.sil.message_sent";

	private static WakeLock wakeLock = null;
	protected boolean errorOcc;
	protected PendingMessageItem messageItem = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				long rowid = intent.getLongExtra(AlarmReceiver.PENDING_MESSAGE_ID, -1);
				if ( rowid != -1) {
					messageItem = DataCenter.getPendingMessageWithId(getApplicationContext(),rowid);
					sendMessages(messageItem.getPhoneNumbers(),messageItem.getContent());
					Helpers.startAfter(WAITING_TIME,new RunAfterSendingFinish());
				} else {
					wakeLock.release();
					setWakeLock(null);
				}
				stopSelf();
			}
		}).start();

		return Service.START_STICKY;
	}


	public class RunAfterSendingFinish implements Runnable {

		@Override
		public void run() {
			if ( errorOcc ) {
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
			
			fireNotification(errorOcc);
			broadcastAlarmIntent();
			
			if (wakeLock != null) {
				wakeLock.release();			
				setWakeLock(null);
			}
		}

	}


	protected void sendMessages(String[] phoneNumbers, String smsContent) {
		errorOcc = false;
		for(String number : phoneNumbers) {
			sendMessage(number,smsContent);
		}
	}


	public void broadcastAlarmIntent() {
		Intent i = new Intent();
		i.setAction(ACTION_MESSAGE_SENT);
		getApplicationContext().sendBroadcast(i);
	}

	public void fireNotification(boolean errorOcc) {
		
		Resources res = getApplicationContext().getResources();
		String title = null;
		String text = null;
		if ( ! errorOcc ) {
			text = res.getString(R.string.Delivered_to) + " " + messageItem.getContact();
			title = res.getString(R.string.successful_sent_notification_title);
		} else {
			title = res.getString(R.string.failed_sent_notification_title);
			text = res.getString(R.string.Sent_failed) + messageItem.getContact();
		}
		Notification notification = createNotification(
				getApplicationContext(), 
				NOTIFICATION_ICON, 
				title, 
				text,
				messageItem.getAlert());
		 NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		 manager.notify(NOTIFICATION_ICON, notification);
	}
	
	public static Notification createNotification(
			Context context,
			int icon,
			String notificationTitle,
			String notificationText, 
			AlertTypes alertTypes) {
		Notification notification = new Notification(icon, notificationTitle, System.currentTimeMillis());		
		
		CharSequence contentTitle = notificationTitle;
		CharSequence contentText =  notificationText;
		
		
//		Intent notificationIntent = new Intent(Intent.ACTION_MAIN);
//		notificationIntent.addCategory(Intent.CATEGORY_DEFAULT);
//		notificationIntent.setType("vnd.android-dir/mms-sms");
		
		Intent notificationIntent = new Intent(context, MainActivity.class);
		notificationIntent.putExtra(MainActivity.SELECTED_FRAME, MainActivity.FRAME_SENT);
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

	public void sendMessage(String receiverNumber, String content) {
		SmsManager sms = SmsManager.getDefault();
		Context context = getApplicationContext();
		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);

		//Note: Be careful : listener.onNormalMessageSendSuccess() called 2 times (1 sent, 1 delivered)
		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					toast("Sent");
					return;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					toast("Generic error");
					errorOcc = true;
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					toast("No service");
					errorOcc = true;
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					toast("Null pdu");
					errorOcc = true;
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					toast("Radio off");
					errorOcc = true;
					break;
				}

			}
		},new IntentFilter(SENT));

		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					toast("Delivered");
					break;
				case Activity.RESULT_CANCELED:
					toast("NOT delivered");
					errorOcc = true;
					break;
				}
			}
		},new IntentFilter(DELIVERED));

		try {
			ArrayList<String> parts = sms.divideMessage(content);
			ArrayList<PendingIntent> sendPIs = new ArrayList<PendingIntent>();	
			ArrayList<PendingIntent> deliveryPIs = new ArrayList<PendingIntent>();
			for(int i = 0 ; i < parts.size() ; i++) {
				sendPIs.add(sentPI);
				deliveryPIs.add(deliveredPI);
			}
			sms.sendMultipartTextMessage(receiverNumber, null, parts,sendPIs,deliveryPIs);
		} catch (Exception e) {
			Logger.logError(e);
		}
	}
	protected void toast(String string) {
		Logger.logInfo(string);
	}

	public static void setWakeLock(WakeLock lock) {
		SendSMSService.wakeLock = lock;
	}

}

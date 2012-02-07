package dtd.phs.sil.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import dtd.phs.sil.alarm.AlarmHelpers;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.PendingMessagesList;
import dtd.phs.sil.entities.SMSItem;
import dtd.phs.sil.entities.SentMessagesList;
import dtd.phs.sil.utils.Logger;


public class DataCenter {

	private static final String SMS_SENT_CONTENT_URI = "content://sms/sent";

	public static void loadPendingMessages(final IDataLoader loader,final Context context) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				//TODO: load the pending messages from private database
				try {
					PendingMessagesList list = Database.loadPendingMessages(context);
					loader.onGetDataSuccess(list);
				} catch (Exception e) {
					loader.onGetDataFailed(e);
				}

			}
		}).start();
	}

	public static void loadSentMessages(final Context context, final IDataLoader loader) {
		new Thread( new Runnable() {
			@Override
			public void run() {
				try {
					SentMessagesList list = Database.loadSentMessages(context);
					loader.onGetDataSuccess(list);
				} catch (Exception e) {
					loader.onGetDataFailed(e);
				}
			}
		}).start();
	}

	public static void removePendingItem(final Context context, final long rowId) {
		if ( Database.removePendingMessage(context, rowId) ) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					AlarmHelpers.refreshAlarm(context);
				}
			}).start();

		}

	}

	public static PendingMessageItem getNextPendingMessage(Context context) {
		return Database.getTheNextMessageToSend(context);
	}


	public static PendingMessageItem getPendingMessageWithId(Context context,long rowid) {
		return Database.getPendingMessage(context,rowid);
	}

	public static void saveSentMessage(final Context context,final PendingMessageItem messageItem) {
		Database.saveSentMessage(context, messageItem, true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Logger.logInfo("Start save message to content provider ...");
				try {
					saveToSMSProvider(context,messageItem);
				} catch (Exception e) {
					Logger.logError(e);
				}
			}
		}).start();
	}

	protected static void saveToSMSProvider(
			Context context,
			PendingMessageItem message) {

		String[] phoneNumbers = message.getPhoneNumbers();
		ContentValues values = new ContentValues();
		for(String num : phoneNumbers) {
			values.clear();
			values.put(SMSItem.ADDRESS, num);
			values.put(SMSItem.BODY, message.getContent());
			context.getContentResolver().insert(Uri.parse(SMS_SENT_CONTENT_URI), values);
		}
	}

	public static void saveFailedMessage(Context context,PendingMessageItem messageItem) {
		Database.saveSentMessage(context, messageItem, false);
	}

	public static void savePendingMessageItem(
			Context context,
			PendingMessageItem message) {
		Database.savePendingMessageItem(
				context,
				message);
		AlarmHelpers.refreshAlarm(context);

	}

	public static void modifyPendingMessage(
			Context context,
			long id, 
			PendingMessageItem message) {
		Database.modifyPendingMessage(context,id,message);
		AlarmHelpers.refreshAlarm(context);
	}

	public static void cleanUpSentMessages(final Context context, final int maxSentSize) {
		new Thread( new Runnable() {
			@Override
			public void run() {
				Database.cleanUpSentMessages(context,maxSentSize);
			}
		}).start();
	}


}

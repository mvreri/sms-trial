package dtd.phs.sil.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import dtd.phs.sil.UpdateLTCService;
import dtd.phs.sil.alarm.AlarmHelpers;
import dtd.phs.sil.entities.MessageItem;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.PendingMessagesList;
import dtd.phs.sil.entities.SMSItem;
import dtd.phs.sil.entities.SentMessageItem;
import dtd.phs.sil.entities.SentMessagesList;
import dtd.phs.sil.utils.Logger;


public class DataCenter {

	private static final String SMS_SENT_CONTENT_URI = "content://sms/sent";

	public static void loadPendingMessages(final IDataLoader loader,final Context context) {
		new Thread(new Runnable() {
			@Override
			public void run() {
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

	public static void saveSentMessage(final Context context,final PendingMessageItem message) {
		Database.saveSentMessage(context, message, true);
		asyncSaveToContentProvider(context, message);
	}

	public static void saveSentMessage(
			final Context context, 
			final SentMessageItem message, 
			boolean isDelivered) {
		Database.saveSentMessage(context, message, isDelivered);
		if ( isDelivered ) {
			asyncSaveToContentProvider(context, message);
		}
	}

	private static void asyncSaveToContentProvider(
			final Context context,
			final MessageItem message) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Logger.logInfo("Start save message to content provider ...");
				try {
					saveToSMSProvider(context,message);
				} catch (Exception e) {
					Logger.logError(e);
				}
			}
		}).start();
	}
	protected static void saveToSMSProvider(
			Context context,
			MessageItem message) {

		String[] phoneNumbers = message.getPhoneNumbers();
		ContentValues values = new ContentValues();
		for(String num : phoneNumbers) {
			values.clear();
			values.put(SMSItem.ADDRESS, num);
			values.put(SMSItem.BODY, message.getContent());
			context.getContentResolver().insert(Uri.parse(SMS_SENT_CONTENT_URI), values);
		}
		for(String num : phoneNumbers) {
			Intent service = new Intent(context,UpdateLTCService.class);
			service.putExtra(UpdateLTCService.EXTRA_NUMBER, num);
			context.startService(service);
		}
	}

	public static void saveFailedMessage(Context context,PendingMessageItem messageItem) {
		Database.saveSentMessage(context, messageItem, false);
	}

	public static void savePendingMessageItem(
			final Context context,
			PendingMessageItem message) {
		final long rowid = Database.savePendingMessageItem(
				context,
				message);
		new Thread( new Runnable() {
			@Override
			public void run() {
				Database.checkConflict(context,rowid);
				AlarmHelpers.refreshAlarm(context);
			}
		}).start();

	}

	public static void modifyPendingMessage(
			final Context context,
			final long id, 
			PendingMessageItem message) {
		Database.modifyPendingMessage(context,id,message);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Database.checkConflict(context,  id);
				AlarmHelpers.refreshAlarm(context);
			}
		}).start();
		
	}

	public static void cleanUpSentMessages(final Context context, final int maxSentSize) {
		new Thread( new Runnable() {
			@Override
			public void run() {
				Database.cleanUpSentMessages(context,maxSentSize);
			}
		}).start();
	}

	public static void removeSentItem(final Context context, final long id) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Database.removeSentMessage(context, id);
			}
		}).start();
	}



}

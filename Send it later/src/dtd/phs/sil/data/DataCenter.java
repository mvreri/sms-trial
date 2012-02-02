package dtd.phs.sil.data;

import android.content.Context;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.PendingMessagesList;
import dtd.phs.sil.entities.SentMessagesList;


public class DataCenter {

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
		Database.removePendingMessage(context, rowId);
	}

	public static PendingMessageItem getNextPendingMessage(Context context) {
		return Database.getTheNextMessageToSend(context);
	}


	public static PendingMessageItem getPendingMessageWithId(Context context,long rowid) {
		return Database.getPendingMessage(context,rowid);
	}

	public static void saveSentMessage(Context context,PendingMessageItem messageItem) {
		//TODO: save to content provider !
		Database.saveSentMessage(context, messageItem, true);
	}

	public static void saveFailedMessage(Context context,PendingMessageItem messageItem) {
		Database.saveSentMessage(context, messageItem, false);
	}
	

}

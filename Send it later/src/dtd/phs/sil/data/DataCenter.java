package dtd.phs.sil.data;

import android.content.Context;
import dtd.phs.sil.PendingMessageView;
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

	public static void loadSentMessages(final IDataLoader loader) {
		new Thread( new Runnable() {
			@Override
			public void run() {
				try {
					SentMessagesList list = Database.loadSentMessages();
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

}

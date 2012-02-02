package dtd.phs.sil.data;

import android.content.Context;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.PendingMessagesList;
import dtd.phs.sil.entities.SentMessageItem;
import dtd.phs.sil.entities.SentMessagesList;
import dtd.phs.sil.utils.Logger;

public class Database {

	public static PendingMessagesList loadPendingMessages(Context context) {
		PendingMessagesList pendingMessages = new PendingMessagesList();
		try {
			DatabaseHelpers helpers = new DatabaseHelpers(context);
			helpers.open();
			pendingMessages = helpers.getPendingMessages();
			helpers.close();
			return pendingMessages;
		} catch (Exception e) {
			return pendingMessages;
		}

		//TEST purpose:
		//		return StubPendingMessages();

	}

	private static PendingMessagesList StubPendingMessages() {
		PendingMessagesList list = new PendingMessagesList();
		PendingMessageItem m1 = new PendingMessageItem();
		//		m1.setContact("Cu Gung (0977686056)");
		m1.setContent("Thuc day di !");
		//		m1.setNextTime("08:00 Jan.05.2012");
		list.add(m1);
		m1 = new PendingMessageItem();
		//		m1.setContact("0986601094");
		m1.setContent("Long long long message, let's see what happens then. Okie, try too blah blah asdsdad adas a. adsd adas ad ad");
		//		m1.setNextTime("19:00 Jan.06.2012");
		list.add(m1);
		return list;
	}

	public static SentMessagesList loadSentMessages(Context context) {
		SentMessagesList list = new SentMessagesList();
		try {
			DatabaseHelpers helper = new DatabaseHelpers(context);
			helper.open();
			list = helper.getSentMessages();
			helper.close();
			return list;
		} catch (Exception e) {
			Logger.logError(e);
			return list;
		}
		//return StubSentMessages();
	}

	private static SentMessagesList StubSentMessages() {
		SentMessagesList list = new SentMessagesList();
		SentMessageItem item = new SentMessageItem();
//		item.setContact("Cu Gung (0916686056)");
		item.setContent("This must be failed !");
//		item.setStatus("Sent failed on 19:00 Jan.04.2012");
		item.setDelivered(false);
		list.add(item);

		item = new SentMessageItem();
//		item.setContact("Cu Gung (0977686056)");
		item.setContent("Hohoho, success , eh ?");
//		item.setStatus("Delivered on 21:00 Jan.04.2012");
		item.setDelivered(true);
		list.add(item);

		return list;
	}

	public static void savePendingMessageItem(Context context, PendingMessageItem item) {
		DatabaseHelpers dbHelper = new DatabaseHelpers(context);
		dbHelper.open();
		dbHelper.savePendingMessageItem(item);
		dbHelper.close();
	}

	public static PendingMessagesList getPendingMessages(Context context) {
		PendingMessagesList list = new PendingMessagesList();
		try {
			DatabaseHelpers dbHelper = new DatabaseHelpers(context);
			dbHelper.open();		
			list = dbHelper.getPendingMessages();
			dbHelper.close();			
		} catch (Exception e) {
			Logger.logError(e);
		}
		return list;

	}

	public static boolean removePendingMessage(Context context, long id) {
		try {
			DatabaseHelpers dbHelper = new DatabaseHelpers(context);
			dbHelper.open();
			boolean result = dbHelper.removePendingMessageItem(id);
			dbHelper.close();
			return result;
		} catch (Exception e) {
			Logger.logError(e);
			return false;
		}

	}

	public static boolean modifyPendingMessage(Context context, long id,PendingMessageItem item) {
		try {
			DatabaseHelpers helper = new DatabaseHelpers(context);
			helper.open();
			boolean successful = helper.modifyPendingItem( id, item);
			helper.close();
			return successful;
		} catch (Exception e) {
			Logger.logError(e);
			return false;
		} 
	}

	public static PendingMessageItem getTheNextMessageToSend(Context context) {
		try {
			DatabaseHelpers helper = new DatabaseHelpers(context);
			helper.open();
			PendingMessageItem message = helper.getNextPendingMessage(context);
			helper.close();
			return message;
		} catch (Exception e) {
			Logger.logError(e);
			return null;
		}
	}

	public static PendingMessageItem getPendingMessage(Context context,long rowid) {
		try {
			DatabaseHelpers helper = new DatabaseHelpers(context);
			helper.open();
			PendingMessageItem message = helper.getPendingMessage(rowid);
			helper.close();
			return message;
		} catch (Exception e) {
			Logger.logError(e);
			return null;
		}
	}

	public static void saveSentMessage(Context context,PendingMessageItem messageItem, boolean isDelivered) {
		try {
			Logger.logInfo("Sent message with status: " + isDelivered + " is saving ...");
			DatabaseHelpers helper = new DatabaseHelpers(context);
			helper.open();
			helper.saveSentMessage(messageItem,isDelivered);
			helper.close();
		} catch (Exception e) {
			Logger.logError(e);
		}
	}

}

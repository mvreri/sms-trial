package dtd.phs.sil.data;

import android.content.Context;
import android.content.IntentSender.SendIntentException;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.PendingMessagesList;
import dtd.phs.sil.entities.SentMessageItem;
import dtd.phs.sil.entities.SentMessagesList;
import dtd.phs.sil.utils.Logger;

public class Database {

	public static PendingMessagesList loadPendingMessages() {
		//TEST purpose:
		return StubPendingMessages();
	}

	private static PendingMessagesList StubPendingMessages() {
		PendingMessagesList list = new PendingMessagesList();
		PendingMessageItem m1 = new PendingMessageItem();
		m1.setContact("Cu Gung (0977686056)");
		m1.setContent("Thuc day di !");
		m1.setNextTime("08:00 Jan.05.2012");
		list.add(m1);
		m1 = new PendingMessageItem();
		m1.setContact("0986601094");
		m1.setContent("Long long long message, let's see what happens then. Okie, try too blah blah asdsdad adas a. adsd adas ad ad");
		m1.setNextTime("19:00 Jan.06.2012");
		list.add(m1);
		return list;
	}

	public static SentMessagesList loadSentMessages() {
		return StubSentMessages();
	}

	private static SentMessagesList StubSentMessages() {
		SentMessagesList list = new SentMessagesList();
		SentMessageItem item = new SentMessageItem();
		item.setContact("Cu Gung (0916686056)");
		item.setContent("This must be failed !");
		item.setStatus("Sent failed on 19:00 Jan.04.2012");
		item.setDelivered(false);
		list.add(item);

		item = new SentMessageItem();
		item.setContact("Cu Gung (0977686056)");
		item.setContent("Hohoho, success , eh ?");
		item.setStatus("Delivered on 21:00 Jan.04.2012");
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

}

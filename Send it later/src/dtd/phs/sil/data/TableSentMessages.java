package dtd.phs.sil.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.SentMessageItem;
import dtd.phs.sil.utils.StringHelpers;

public class TableSentMessages {

	//Also contains sent failed message
	static public final String SEPERATOR = "#@#";
	public static final String TABLE_NAME = "SENT_MESSAGES_TBL";
	
	private static final String ID = "_id";
	private static final String NAMEs = "names";
	private static final String PHONE_NUMBERs = "numbers";
	private static final String MESSAGE_CONTENT = "content";
	private static final String STATUS = "status";
	private static final String SENT_TIME = "sent_time";
	
	public static final String[] COLUMNS = {
		ID + " INTEGER PRIMARY KEY AUTOINCREMENT",
		NAMEs + " text",
		PHONE_NUMBERs + " text",
		MESSAGE_CONTENT + " text",
		STATUS + " integer",
		SENT_TIME + " integer"
	};
	private static final int DELIVERED = 1;
	private static final int FAILED = 0;

	public static void saveSentMessageFromPendingMessage(SQLiteDatabase database, PendingMessageItem messageItem) {
		SentMessageItem sentMessage = SentMessageItem.createFromPendingMessage(messageItem);
		saveSentMessage(database, sentMessage);
	}

	private static void saveSentMessage(
			SQLiteDatabase database,
			SentMessageItem sentMessage) {
		int status = FAILED;
		if ( sentMessage.isDelivered() ) {
			status = DELIVERED;
		}

		
		ContentValues values = new ContentValues();	
		values.put(NAMEs,StringHelpers.implode(sentMessage.getNames(),SEPERATOR));
		values.put(PHONE_NUMBERs, StringHelpers.implode(sentMessage.getPhonenumbers(), SEPERATOR));
		values.put(MESSAGE_CONTENT, sentMessage.getContent());
		values.put(STATUS, status);
		values.put(SENT_TIME, sentMessage.getSentTime());
		
		database.insert(TABLE_NAME, null, values);
	}

}

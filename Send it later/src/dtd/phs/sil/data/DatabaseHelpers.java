package dtd.phs.sil.data;

import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.PendingMessagesList;
import dtd.phs.sil.entities.SentMessagesList;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelpers extends PHS_DatabaseHelpers {

	private static final String DB_NAME = "SEND_IT_LATER_DB";
	private static final int DB_VERSION = 1;
	private static final String[] TABLES = {
		TablePendingMessages.TABLE_NAME, 
		TableSentMessages.TABLE_NAME };
	private SQLiteDatabase database;

	public DatabaseHelpers(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createTableString(TablePendingMessages.TABLE_NAME,TablePendingMessages.COLUMNS));
		db.execSQL(createTableString(TableSentMessages.TABLE_NAME, TableSentMessages.COLUMNS));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if ( newVersion > oldVersion) {
			for(String tname : TABLES) 
				db.execSQL("DROP TABLE IF EXISTS " + tname);
		}
	}

	public void savePendingMessageItem(PendingMessageItem item) {
		TablePendingMessages.saveItem(database, item);
	}

	public void open() {
		try {
			database = getWritableDatabase();
		} catch (Exception e) {
			database = getReadableDatabase();
		}
	}

	public PendingMessagesList getPendingMessages() {
		return TablePendingMessages.getAllMessages(database);
	}

	public boolean removePendingMessageItem(long id) {
		return TablePendingMessages.removeRow(database, id);
	}

	public boolean modifyPendingItem(long id, PendingMessageItem item) {
		return TablePendingMessages.modify(database,id,item);
	}

	public PendingMessageItem getNextPendingMessage(Context context) {
		return TablePendingMessages.getNextMessage(database);
	}

	public PendingMessageItem getPendingMessage(long rowid) {
		return TablePendingMessages.getMessage(database,rowid);
	}

	public void saveSentMessage(PendingMessageItem messageItem, boolean isDelivered) {
		TableSentMessages.saveSentMessageFromPendingMessage(database,messageItem,isDelivered);
	}

	public SentMessagesList getSentMessages() {
		return TableSentMessages.getAllSentMessages(database,null);
	}

	public int cleanUpSentMessages(int maxSentSize) {
		return TableSentMessages.cleanUp(database,maxSentSize);
	}

	public void removeSentItem(long id) {
		TableSentMessages.removeRow(database, id);
	}

}

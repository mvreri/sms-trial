package dtd.phs.sil.data;

import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.PendingMessagesList;
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

}

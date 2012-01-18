package dtd.phs.sil.data;

import java.util.Calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.PendingMessagesList;
import dtd.phs.sil.ui.AlertHelpers;
import dtd.phs.sil.utils.FrequencyHelpers;
import dtd.phs.sil.utils.StringHelpers;

public class TablePendingMessages {

	static public final String SEPERATOR = "#@#";
	public static final String TABLE_NAME = "PENDING_MESSAGES_TBL";
	
	
	private static final String NAMEs = "names";
	private static final String PHONE_NUMBERs = "phone_numbers";
	private static final String MESSAGE_CONTENT = "message_content";
	private static final String DATE_TIME = "date_time";
	private static final String FREQ_TYPE = "freq_type";
	private static final String ALERT_TYPE = "alert_type";
	
	public static final String[] COLUMNS = {
		NAMEs + " text",
		PHONE_NUMBERs + " text",
		MESSAGE_CONTENT + " text",
		DATE_TIME + " integer",
		FREQ_TYPE + " integer",
		ALERT_TYPE+ " integer"
	};
	
	
	static protected void saveItem(SQLiteDatabase db,PendingMessageItem item) {
		String names = StringHelpers.implode(item.getNames(),SEPERATOR);
		String numbers = StringHelpers.implode(item.getPhoneNumbers(),SEPERATOR);
		String content = item.getContent();
		Calendar dateTime = item.getStartDateTime();
		FrequencyHelpers.Frequencies freq = item.getFreq();
		AlertHelpers.AlertTypes alertType = item.getAlert();
		ContentValues values = new ContentValues();
		
		values.put(NAMEs, names );
		values.put(PHONE_NUMBERs, numbers);
		values.put(MESSAGE_CONTENT, content);
		values.put(DATE_TIME, dateTime.getTimeInMillis());
		values.put(FREQ_TYPE, FrequencyHelpers.indexOf(freq));
		values.put(ALERT_TYPE, AlertHelpers.indexOf(alertType));
		
		db.insert(TABLE_NAME, null, values);
	}
	
	/**
	 * Get all pending messages (not sorted in any particular order)
	 * @param db
	 * @return
	 */
	static protected PendingMessagesList getAllMessages(SQLiteDatabase db) {
		Cursor cursor = null;
		PendingMessagesList list = new PendingMessagesList();
		try {
			cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
			// sort by thread ?
			if ( cursor.moveToFirst()) {
				do {
					PendingMessageItem item = createFromCursor( cursor );
					list.add(item);
				} while ( cursor.moveToNext());
			}
			return list;
		} catch (Exception e) {
			return list;
		} finally {
			cursor.close();
		}
	}

	private static PendingMessageItem createFromCursor(Cursor cursor) {
		String names = cursor.getString(cursor.getColumnIndex(NAMEs));
		String numbers = cursor.getString(cursor.getColumnIndex(PHONE_NUMBERs));
		String content = cursor.getString(cursor.getColumnIndex(MESSAGE_CONTENT));
		long dateTime = cursor.getLong(cursor.getColumnIndex(DATE_TIME));
		int freqIndex = cursor.getInt(cursor.getColumnIndex(FREQ_TYPE));
		int alertIndex = cursor.getInt(cursor.getColumnIndex(ALERT_TYPE));
		
		return PendingMessageItem.createInstance(
				names.split(SEPERATOR), 
				numbers.split(SEPERATOR), 
				content, 
				dateTime, 
				freqIndex, 
				alertIndex);
	}
	
}

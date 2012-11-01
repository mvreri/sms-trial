package dtd.phs.chatexperiment_phs;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BatteryStatusTable {

	public static final String TBL_NAME = "BatteryStatusTable";
	private static final String SPACE = " ";

	private static String DB_ID = "_id";
	private static final String TIME = "time";
	private static final String PERCENTAGE = "percentage";

	private static final String[] FIELDS = {
		DB_ID + SPACE + "integer primary key autoincrement",
		TIME + SPACE + "integer not null",
		PERCENTAGE + SPACE + "integer not null"
	};
	

	protected DBHandler dbHelper;
	protected SQLiteDatabase database = null;
	protected Context context;

	public BatteryStatusTable(Context context) {
		dbHelper = new DBHandler(context);
		this.context = context;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public static String createTable(String tableName,String[] fields) {
		StringBuilder builder = new StringBuilder();

		builder.append("create table " + tableName + "(");
		for(int i = 0 ; i < fields.length ; i++) {
			builder.append(fields[i]);
			if ( i != fields.length - 1) 
				builder.append(", ");
			else builder.append(");"); 
		}

		return builder.toString();
	}
		
	public static String createTableQuery() {
		return createTable(TBL_NAME, FIELDS);
	}
	
	private long addRow(long time, int batteryPercent) {
		ContentValues values = new ContentValues();
		values.put(TIME, time );
		values.put(PERCENTAGE, batteryPercent);
		long rowId = database.insert(TBL_NAME, null, values);
		return rowId;
	}
	
	private ArrayList<MeterInfo> getMeterInfo() {
		ArrayList<MeterInfo> list = new ArrayList<MeterInfo>();
		Cursor cursor = null;
		try {
			cursor = database.query(TBL_NAME, null, null, null, null, null, TIME + " ASC");
			if ( cursor.moveToFirst() ) {
				do {
					list.add(createMeterInfo(cursor));
				} while (cursor.moveToNext());
			}
			return list;
		} catch (Exception e) {
			Logger.logError(e);
			return null;
		} finally {
			try { cursor.close(); } catch (Exception e) {}
		}
		
		
	}
	
	public static ArrayList<MeterInfo> getMeterInfo(Context context) {
		BatteryStatusTable tbl = new BatteryStatusTable(context);
		tbl.open();
		try {
			return tbl.getMeterInfo();
		} catch (Exception e) {
			Logger.logError(e);
			return null;			
		} finally {
			try { tbl.close() ; } catch (Exception e) {}
		}
		 
	}
	
	private MeterInfo createMeterInfo(Cursor cursor) {
		long time = cursor.getLong(cursor.getColumnIndex(TIME));
		int percentage = cursor.getInt(cursor.getColumnIndex(PERCENTAGE));
		return new MeterInfo(time,percentage);
	}

	public static long addRow(Context context, long time, int batteryPercent) {
		BatteryStatusTable tbl = new BatteryStatusTable(context);
		tbl.open();
		try {
			return tbl.addRow(time, batteryPercent);
		} catch (Exception e) {
			Logger.logError(e);
			return -1;
		} finally {
			try { tbl.close(); } catch (Exception e) {}
		}
	}

	public static int clearAllData(Context context) {
		BatteryStatusTable tbl = new BatteryStatusTable(context);
		tbl.open();
		try {
			return tbl.clearData();
		} catch (Exception e) {
			Logger.logError(e);
			return 0;
		} finally {
			try { tbl.close(); } catch (Exception e) {}
		}		
	}

	private int clearData() {
		return database.delete(TBL_NAME, null, null);
	}
	 
}

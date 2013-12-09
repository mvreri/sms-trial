package com.vsm.radio18.data.db;


import java.util.ArrayList;

import com.vsm.radio18.data.entities.Item;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import dtd.phs.lib.utils.Logger;
import dtd.phs.lib.utils.LongWrapper;

public abstract class DBTable {
	public static final String SPACE = " ";
	public static final String DB_ID = "_id";
	
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


	protected DBHandler dbHelper;
	protected SQLiteDatabase database = null;
	protected Context context;

	public DBTable(Context context) {
		dbHelper = new DBHandler(context);
		this.context = context;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}


	//CRUD
	//C: Create
	public boolean addItem(Item item, LongWrapper id) {
		ContentValues values = item.createContentValues();
		long rowId = database.insert(getTableName(), null, values);
		if ( rowId == -1 ) return false;
		id.setValue(rowId);
		return true;
	}
	
	//R: Read
	public Item getItem(long dbId) {
		Cursor cursor = null;
		try {
			cursor = database.query(
					getTableName(), null, 
					DB_ID + " = " + dbId, 
					null, null, null, null);
			if ( cursor.moveToFirst() ) {
				return createDBItem(cursor);
			} else return null;
		} catch (Exception e) {
			Logger.logError(e);
			return null;
		} finally {
			closeCursor(cursor);
		}
	}
	
	//R: Read all
	public ArrayList<Item> getAll() {
		ArrayList<Item> list = new ArrayList<Item>();
		Cursor cursor = null;
		try {
			cursor = database.query(
					getTableName(), null, null, null, null, null, itemOrderBy());
			if ( cursor.moveToFirst() ) {
				do {
					
				} while ( cursor.moveToNext());
			}
			return list;
		} catch (Exception e) {
			Logger.logError(e);
			return null;
		} finally {
			closeCursor(cursor);
		}
	}
	
	//U: Update
	public boolean update(long dbId, Item item) {
		ContentValues values = item.createContentValues();
		values.put(DB_ID, dbId);
		int cnt = database.update(
				getTableName(), 
				values, DB_ID + " = " + dbId, null);
		if ( cnt != 0 ) return true;
		return false;
	}
	
	//D: Delete
	public boolean removeItem(long dbId) {
		int cnt = database.delete(getTableName(), DB_ID + " = " + dbId, null);
		if  ( cnt != 0 ) return true;
		return false;
	}

	/*
	 * To be implemented in sub-class when ordering is needed
	 */
	protected String itemOrderBy() {
		return null;
	}

	protected abstract String getTableName();
	protected abstract Item createDBItem(Cursor cursor);

	protected static long getLong(Cursor cursor, String fieldName) {
		try {
			return cursor.getLong(cursor.getColumnIndex(fieldName));
		} catch (Exception e) {
			Logger.logError(e);
			return 0;
		}	
	}

	protected static String getString(Cursor cursor, String fieldName) {
		try {
			return cursor.getString(cursor.getColumnIndex(fieldName));
		} catch (Exception e) {
			return null;
		}
	}

	protected static int getInt(Cursor cursor, String fieldName) {
		try {
			return cursor.getInt(cursor.getColumnIndex(fieldName));
		} catch (Exception e) {
			Logger.logError(e);
			return 0;
		}
	}
	

	protected static void closeCursor(Cursor cursor) {
		try { cursor.close(); } catch (Exception e) {}
	}
}

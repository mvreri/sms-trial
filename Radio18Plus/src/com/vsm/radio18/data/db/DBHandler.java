package com.vsm.radio18.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

	private static final String DB_NAME = "RadioVnDB";
	private static final int DB_VERSION = 1;
	private static final String[] TABLE_NAMEs = {ArticlesTable.TBL_NAME};
	private static final String[][] FIELDs = {ArticlesTable.FIELDS};
	private SQLiteDatabase db = null;

	public DBHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION );
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		for(int i = 0 ; i < TABLE_NAMEs.length ; i++)
			database.execSQL(DBTable.createTable(TABLE_NAMEs[i], FIELDs[i]));
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		for(int i = 0 ; i < TABLE_NAMEs.length ; i++) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMEs[i]);
		}
	    onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (! db.isReadOnly()) {
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}
}

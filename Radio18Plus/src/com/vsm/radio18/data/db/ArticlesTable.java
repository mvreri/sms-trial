package com.vsm.radio18.data.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.vsm.radio18.data.entities.DB_ArticelItem;
import com.vsm.radio18.data.entities.Item;

public class ArticlesTable extends DBTable {
	public static final String TBL_NAME = "TBL_SONGS";

	// FIELDS
	
	public static final String ONL_ID = "online_id";
	public static final String ART_NAME = "article_name";
	public static final String DESC = "desc";
	public static final String ONL_COVER_URL = "online_cover_url";
	public static final String STREAMING_URL = "streaming_url";
	public static final String LOCAL_MUSIC_PATH = "local_mp3_path";
	public static final String CAT_ID = "category_id";
	public static final String EXTRA01 = "extra01";
	public static final String EXTRA02 = "extra02";

	static final String[] FIELDS = {
			DB_ID + SPACE + "integer primary key autoincrement",
			ONL_ID + SPACE + "integer unique not null",
			ART_NAME + SPACE + "text not null", DESC + SPACE + "text",
			ONL_COVER_URL + SPACE + "text",
			STREAMING_URL + SPACE + "text not null",
			LOCAL_MUSIC_PATH + SPACE + "text", CAT_ID + SPACE + "integer",
			EXTRA01 + SPACE + "text default NULL",
			EXTRA02 + SPACE + "text default NULL",

	};

	public ArticlesTable(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return ArticlesTable.TBL_NAME;
	}

	@Override
	protected Item createDBItem(Cursor cursor) {
		long dbId = getLong(cursor,DB_ID);
		long onlineId = getLong(cursor, ONL_ID);
		String name = getString(cursor, ART_NAME);
		String desc = getString(cursor, DESC);
		String onlineCoverURL = getString(cursor, ONL_COVER_URL);
		String streamingURL = getString(cursor, STREAMING_URL);
		String localMp3Path = getString(cursor, LOCAL_MUSIC_PATH);
		long categoryId = getLong(cursor, CAT_ID);
		String extr01 = getString(cursor, EXTRA01);
		String extr02 = getString(cursor, EXTRA02);
		
		return new DB_ArticelItem(
				dbId, 
				onlineId, 
				name, 
				desc, 
				onlineCoverURL, 
				streamingURL, 
				localMp3Path, 
				categoryId, 
				extr01, 
				extr02);
	}

}
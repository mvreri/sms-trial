package com.vsm.radio18.data.entities;

import android.content.ContentValues;

import com.vsm.radio18.data.db.ArticlesTable;

public class DB_ArticelItem extends ArticleItem  {

	private long dbId;
	private String extra02;
	private String extra01;
	private String localMusicPath;

	public DB_ArticelItem(
			long dbId,
			long onlineId, 
			String name,
			String desc,
			String onlineCoverURL,
			String streamingURL,
			String localMusicPath,
			long categoryId,
			String extr01,
			String extr02) {
		super(onlineId, categoryId, onlineCoverURL, name, desc, streamingURL);
		this.dbId = dbId;
		this.localMusicPath = localMusicPath;
		this.extra01 = extr01;
		this.extra02 = extr02;
	}
	
	@Override
	public ContentValues createContentValues() {
		ContentValues values = super.createContentValues();
		values.put(ArticlesTable.DB_ID, getDBId());
		if ( this.getLocalMusicPath() != null)
			values.put(ArticlesTable.LOCAL_MUSIC_PATH, getLocalMusicPath());
		if ( getExtra01() != null )
			values.put(ArticlesTable.EXTRA01, getExtra01());
		if ( getExtra02() != null)
			values.put(ArticlesTable.EXTRA02, getExtra02());
		return values;
	}

	private String getExtra02() {
		return this.extra02;
	}

	private String getExtra01() {
		return this.extra01;
	}

	private String getLocalMusicPath() {
		return this.localMusicPath;
	}

	public long getDBId() {
		return this.dbId;
	}
	
	
}

package com.vsm.radio18.data.db;

import java.util.ArrayList;

import com.vsm.radio18.data.entities.DB_ArticelItem;
import com.vsm.radio18.data.entities.Item;

import dtd.phs.lib.utils.Logger;

import android.content.Context;

public class DBCenter {

	static public ArrayList<DB_ArticelItem> getAllArticles(Context context) {
		ArticlesTable tbl = null;
		try {
			tbl = new ArticlesTable(context);
			tbl.open();
			ArrayList<Item> all = tbl.getAll();
			ArrayList<DB_ArticelItem> a = new ArrayList<DB_ArticelItem>();
			for(int i = 0; i < all.size(); i++) {
				a.add((DB_ArticelItem) all.get(i));
			}
			return a;
		} catch (Exception e) {
			Logger.logError(e);
			return null;
		} finally {
			try { tbl.close(); } catch (Exception e) {}
		}
	}
	
	public static boolean removeArticle(Context context, long dbId) {
		ArticlesTable tbl = null;
		try {
			tbl = new ArticlesTable(context);
			tbl.open();
			return tbl.removeItem(dbId);
		} catch (Exception e) {
			Logger.logError(e);
			return false;
		} finally {
			try { tbl.close(); } catch (Exception e) {}
		}
		
	}

	/**
	 	ArticlesTable tbl = null;
		try {
			tbl = new ArticlesTable(context);
			tbl.open();
		} catch (Exception e) {
			Logger.logError(e);
		} finally {
			try { tbl.close(); } catch (Exception e) {}
		}
	 */

	protected static void closeTable(DBTable tbl) {
		try {
			tbl.close();
		} catch (Exception e) {
		}
	}


}

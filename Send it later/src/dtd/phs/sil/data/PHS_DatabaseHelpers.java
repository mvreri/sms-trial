package dtd.phs.sil.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

abstract public class PHS_DatabaseHelpers extends SQLiteOpenHelper {

	public PHS_DatabaseHelpers(
			Context context, 
			String name,
			CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	protected String createTableString(String tableName, String[] fields) {
		StringBuilder builder = new StringBuilder("create table " + tableName + " (");
		for(int i = 0 ; i < fields.length ; i++) {
			builder.append( fields[i] );
			if ( i != fields.length - 1 ) {
				builder.append(" , ");
			}
		}
		builder.append(");");
		return builder.toString();	
	}
}

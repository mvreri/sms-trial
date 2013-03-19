package dtd.phs.sil;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.PowerManager.WakeLock;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import dtd.phs.sil.utils.Helpers;
import dtd.phs.sil.utils.Logger;

public class UpdateLTCService extends IntentService {

	private static final String UPDATE_LTC_SERVICE = "UpdateLTCService";
	public static final String EXTRA_NUMBER = "extra_number";
	private static WakeLock wakelock = null;

	public UpdateLTCService() {
		super(UPDATE_LTC_SERVICE);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String number = intent.getStringExtra(EXTRA_NUMBER);
		if ( number != null) {
			String contactId = getContactIdFromNumber(getApplicationContext(),number);
			if ( contactId != null ) {
				//				int countUpdated = 
				updateLTC(getApplicationContext(), contactId,System.currentTimeMillis());
				//				if (  countUpdated != 0 ) {
				//					Logger.logInfo("Update contact [id: "+contactId+",number:"+number+"] SUCCESSFUL - count: " + countUpdated);
				//				} else {
				//					Logger.logInfo("Update contact [id: "+contactId+",number:"+number+"] FAILED");
				//				}
				//			} else {
				//				Logger.logError("Contact ID is NULL");
			}
		} 
		//		else {
		//			Logger.logError("Number is NULL");
		//		}

	}

	private String getContactIdFromNumber(Context applicationContext,
			String number) {
		ContentResolver localContentResolver = applicationContext.getContentResolver();
		Cursor contactLookupCursor =  
				localContentResolver.query(
						Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, 
								Uri.encode(number)), 
								new String[] {PhoneLookup.DISPLAY_NAME,PhoneLookup._ID}, 
								null, 
								null, 
								null);
		try {
			if (contactLookupCursor.moveToFirst()){
//				String contactName = contactLookupCursor.getString(contactLookupCursor.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME));
				String contactId = contactLookupCursor.getString(contactLookupCursor.getColumnIndexOrThrow(PhoneLookup._ID));
//				Logger.logInfo("[ID: "+contactId+"--name:"+contactName+"--match with number:"+number);
				return contactId;
			} else {
//				Logger.logInfo("No match ID with number: "+number );
				return null;
			}
		}
		catch (Exception e) {
			return null;
		} finally {
			try { contactLookupCursor.close(); } catch (Exception e) {}
		} 
	}

	private int updateLTC(Context context, String contactId, long currentTimeMillis) {
		ContentValues values = new ContentValues();
		values.put(Contacts.LAST_TIME_CONTACTED, currentTimeMillis);
		return context.getContentResolver().update(Contacts.CONTENT_URI, values, Contacts._ID+"='"+contactId+"'", null);
	}

	public static void acquireWakelock(Context context) {
		if ( UpdateLTCService.wakelock == null) {
			UpdateLTCService.wakelock = Helpers.acquireWakelock(context);
		}
	}

	@Override
	public void onDestroy() {
		if ( UpdateLTCService.wakelock != null) {
			UpdateLTCService.wakelock.release();
			UpdateLTCService.wakelock = null;
		}
		super.onDestroy();
	}

}

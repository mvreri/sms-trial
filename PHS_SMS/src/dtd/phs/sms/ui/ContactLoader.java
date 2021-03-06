package dtd.phs.sms.ui;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Stack;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.widget.ImageView;
import android.widget.TextView;
import dtd.phs.sms.global.UIThreadHandler;
import dtd.phs.sms.util.Logger;

public class ContactLoader implements Runnable {

	ViewStack stack = new ViewStack();
	private HashMap<String, String> cache;
	private HashMap<String, Bitmap> cacheAvatar;
	private Context context;

	public ContactLoader(Context context) {
		this.context = context;
		loadContactThread = new Thread(this);
		cache = new HashMap<String, String>();
		cacheAvatar = new HashMap<String, Bitmap>();
		loadContactThread.setPriority(Thread.NORM_PRIORITY -1);
		loadContactThread.start();
		
		Logger.logInfo("Loader thread is started !");
		
	}

	public void loadContact(TextView tvContact , ImageView ivAvatar, String contactId) {
		Logger.logInfo("Loading contact: " );
		if ( contactId != null ) {
			Logger.logInfo("Loading contact: " + contactId);
		} else Logger.logInfo("Loading contact: NULL");
		ViewToLoad tvLoad = new ViewToLoad(tvContact, ivAvatar, contactId);
		stack.add(tvLoad);

	}

	public void stopThread() {
		cache.clear();
		cacheAvatar.clear(); 
		loadContactThread.interrupt();
		Logger.logInfo("Loader thread is stopped !");
	}

	@Override
	public void run() {
		while (true) {
			try {
				final ViewToLoad tvLoad = stack.pop();
				if ( tvLoad.contactId == null ) continue;
				final String contactName = getContactName(tvLoad.contactId);
				final Bitmap avatar = getContactPhoto(tvLoad.contactId);
				UIThreadHandler.getInstance().post(new Runnable() {
					@Override
					public void run() {
						if (tvLoad.tvToLoad != null && contactName != null )
							tvLoad.tvToLoad.setText(contactName);
						if ( tvLoad.ivToLoad != null && avatar != null )
							tvLoad.ivToLoad.setImageBitmap(avatar);
					}
				});

				if (Thread.interrupted())
					throw new InterruptedException();
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	private String getContactName(String contactId) {
		if ( contactId == null ) return null;

		String contactName = cache.get(contactId);
		if ( contactName != null ) return contactName;

		Cursor cursor = null;
		try {
			Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId);
			cursor = 
				context.getContentResolver().
				query(contactUri, new String[] {Contacts.DISPLAY_NAME}, null, null, null);
			if ( cursor.moveToFirst() ) {
				contactName = cursor.getString(0);
				cache.put(contactId, contactName);
				return contactName; //+ " ::ID = " + contactId;
			} else return null;
		} catch (Exception e) {
			Logger.logException(e);
			return null;
		} finally {
			cursor.close();
		}
	}


	public InputStream getContactPhotoStream(String contactId) {
		Uri contactURI = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,contactId);
		try {
			return ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), contactURI);
		} catch (Exception e) {
			Logger.logException(e);
			return null;
		}
	}
	public Bitmap getContactPhoto(String contactId) {
		if (SummariesAdapter.STUB_AVATAR_BITMAP == null) 
			SummariesAdapter.STUB_AVATAR_BITMAP = BitmapFactory.decodeResource(context.getResources(), SummariesAdapter.STUB_AVATAR);
		if ( contactId == null ) return  SummariesAdapter.STUB_AVATAR_BITMAP;

		Bitmap avatar = cacheAvatar.get(contactId);
		if ( avatar == null ) {
			avatar = getJustPhoto(contactId);
			cacheAvatar.put(contactId, avatar);
		}
		return avatar;
	}

	private Bitmap getJustPhoto(String contactId) {
		//		Bitmap avatar = null;
		//		InputStream is = getContactPhotoStream(contactId);
		//		if (is == null) {
		//			avatar = SummariesAdapter.STUB_AVATAR_BITMAP;
		//		} else {
		//			avatar = BitmapFactory.decodeStream(is);
		//		}
		//		return avatar;

		Bitmap avatar = SummariesAdapter.STUB_AVATAR_BITMAP;
		String[] projection = {ContactsContract.CommonDataKinds.Photo.PHOTO};
		Uri uri = ContactsContract.Data.CONTENT_URI;
		String where = ContactsContract.Data.MIMETYPE 
		+ "='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "' AND " 
		+ ContactsContract.Data.CONTACT_ID + " = " + contactId;
		Cursor cursor = context.getContentResolver().query(uri, projection, where, null, null);
		if( cursor != null && cursor.moveToFirst() ) {
			byte[] photoData = cursor.getBlob(0);
			if ( photoData != null )
				avatar = BitmapFactory.decodeByteArray(photoData, 0,photoData.length, null);
		}  
		cursor.close();
		return avatar;

	}

	private Bitmap getBitMapByBlahMethod(String contactId) {
		// TODO Auto-generated method stub
		return null;
	}

	public class ViewToLoad {
		public TextView tvToLoad;
		public ImageView ivToLoad;
		public String contactId;
		public ViewToLoad(TextView tvToLoad, ImageView ivAvatar, String contactId) {
			this.tvToLoad = tvToLoad;
			this.contactId = contactId;
			this.ivToLoad = ivAvatar;
		}

	}

	private Thread loadContactThread;
	class ViewStack {
		private Stack<ViewToLoad> stack = new Stack<ViewToLoad>();
		public void add(ViewToLoad tvLoad) {
			synchronized (this) {
				clean(tvLoad);
				stack.addElement(tvLoad);			
				this.notify();
			}
		}

		private void clean(ViewToLoad tvLoad) {
			for(int i = 0; i < stack.size() ;) {
				ViewToLoad tv = stack.get(i);
				if (tv.tvToLoad == tvLoad.tvToLoad) {
					stack.remove(i);
				} else i++;
			}
		}

		public ViewToLoad pop() throws InterruptedException {
			synchronized (this) {
				if ( stack.isEmpty() )
					this.wait();
			}
			return stack.pop(); 
		}

	}

}

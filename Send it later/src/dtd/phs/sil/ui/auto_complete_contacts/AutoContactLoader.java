package dtd.phs.sil.ui.auto_complete_contacts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import dtd.phs.sil.utils.Helpers;
import dtd.phs.sil.utils.Logger;

public class AutoContactLoader implements Runnable {
	protected static final String FILENAME = "/sdcard/temporary_contacts.txt";
	protected static final String SEPERATOR = " /split/ ";
	private static final long CACHED_TIME = 5*60*1000; //5 minutes
	
	//Cache
	static private ContactsList cachedContacts = null;
	static private long lastCachedTime = 0;

	private ContactsList allContacts;
	private Context context;
	private IContactLoader listener;
	public AutoContactLoader(Context context,ContactsList allContacts, IContactLoader listener) {
		this.context = context;
		this.allContacts = allContacts;
		this.listener = listener;
	}
	@Override
	public void run() {
		// WHAT TO DO ?
		// Need: all name & number : RawContacts.ACCOUNT_NAME - Data.DATA1 with Data.MIMETYPE = Phone.CONTENT_ITEM_TYPE
		//	- get all raw contacts
		//	- get all data
		//	- sort them by raw_contact_id (RawContacts._ID , Data.RAW_CONTACT_ID
		//	- join info by raw_contact_id
		
		//Get old result from cache if the data is not too old
		if ( cachedContacts != null && System.currentTimeMillis() - lastCachedTime < CACHED_TIME) {
			Helpers.copyArrayList(allContacts,cachedContacts);
			return;
		}
		
		//				Log.i(PHS_SMS,"Start loading contacts ..." );
		//First, load it from private storage to increase responsiveness
		try {
			Thread.currentThread().setPriority(Thread.NORM_PRIORITY+1);
		} catch (Exception e) {
			Logger.logInfo("Cannot set thread to this level");
		}
//		Logger.logInfo("Beginning loading contacts ");
//		long starting = System.currentTimeMillis();
		if (allContacts.size() == 0) {
			loadFromStorage();
		}
//		Logger.logInfo("Done loading from storage - spent: " + (System.currentTimeMillis()-starting)+"ms");


		//Load it from content provider
		//Lower the priority so the filter on other thread runs faster - which make better user responsiveness 
		try {
			Thread.currentThread().setPriority(Thread.NORM_PRIORITY-1);
		} catch (Exception e) {
			Logger.logInfo("Cannot set thread to this level");
		}
//		long startingCP = System.currentTimeMillis();
		MyRawContacts rawContacts = getAllRawContacts();
		MyContactData data = getAllContactsData();
		sort(rawContacts, data);
		joinAndFill(rawContacts, data);
//		Logger.logInfo("Done loading from content provider - spent: " + (System.currentTimeMillis()-startingCP)+"ms");


		//Save results to storage
//		long startingWrite = System.currentTimeMillis();
		writeToStorage();
		cachedContacts = allContacts;
		lastCachedTime = System.currentTimeMillis();
//		Logger.logInfo("Done write to storage- spent: " + (System.currentTimeMillis()-startingWrite)+"ms");
		//				Log.i(PHS_SMS,"Contacts are all loaded..." );
	}

	private void loadFromStorage() {
		try {
			FileInputStream fis = new FileInputStream(new File(FILENAME));
			BufferedReader buf = new BufferedReader(new InputStreamReader(new DataInputStream(fis)));
			String line;
			synchronized (allContacts) {
				allContacts.clear();
				while ((line = buf.readLine()) != null) {
					String[] words = line.split(SEPERATOR);
					allContacts.add(new ContactItem(words[0], words[1], Long.parseLong(words[2])));
				}
			}
			fis.close();
		} catch (FileNotFoundException e) {
			Logger.logError(e);
		} catch (IOException e) {
			Logger.logError(e);
		} catch (Exception e) {
			Logger.logError(e);
		}
	}

	private void writeToStorage() {
		File file = new File(FILENAME);
		try {
			if ( !file.exists()) file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, false ));
			for(ContactItem contact : allContacts) {
				writer.write(contact.getName()+SEPERATOR+contact.getNumber()+SEPERATOR+contact.getLastTimeContacted()+"\n");
			}
			writer.close();
			if ( listener != null ) {
				listener.onContactCacheSuccess(allContacts);
			}
			Logger.logInfo("Contacts are written on storage");
		} catch (IOException e) {
			if ( listener != null ) {
				listener.onContactLoadFailed(e);
			}
			e.printStackTrace();
		}

	}

	private void joinAndFill(MyRawContacts contacts, MyContactData data) {
		synchronized (allContacts) {
			int contactIndex = 0;
			int dataIndex = 0;
			allContacts.clear();
			while (dataIndex < data.size()) {
				while (	contactIndex < contacts.size()
						&& 	contacts.getId(contactIndex).
						compareTo(data.getId(dataIndex)) < 0) contactIndex++;
				if ( contactIndex >= contacts.size() ) break;

				while (dataIndex < data.size() 
						&& data.getId(dataIndex).compareTo(contacts.getId(contactIndex)) < 0) dataIndex++;
				if ( dataIndex >= data.size() ) break;					

				while ( dataIndex < data.size() 
						&& data.getId(dataIndex).equals(contacts.getId(contactIndex))) {
					String name = contacts.get(contactIndex).getName();
					String number = data.get(dataIndex).getNumber();
					allContacts.add(new ContactItem(name, number,contacts.get(contactIndex).getLastTimeContacted()));
					dataIndex++;
				}
				if ( dataIndex >= data.size() ) break;
			}
		}
	}

	private void sort(MyRawContacts rawContacts, MyContactData data) {
		Collections.sort(rawContacts, new Comparator<MyContactItem>() {
			@Override
			public int compare(MyContactItem lhs, MyContactItem rhs) {
				return lhs.getID().compareTo(rhs.getID());
			}
		});

		Collections.sort(data, new Comparator<MyDataItem>() {
			@Override
			public int compare(MyDataItem lhs, MyDataItem rhs) {
				return lhs.getRawContactID().compareTo(rhs.getRawContactID());
			}
		});
	}

	private MyContactData getAllContactsData() {
		Cursor cursor = null;
		try {
			MyContactData data = new MyContactData();
			cursor = getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, null, null, Data.CONTACT_ID + " asc");
			if ( cursor.moveToFirst()) {
				do {
					String type = cursor.getString(cursor.getColumnIndex(Data.MIMETYPE));
					if ( type.equals(Phone.CONTENT_ITEM_TYPE)) {
						String number = cursor.getString(cursor.getColumnIndex(Data.DATA1));
						String rawContactId = cursor.getString(cursor.getColumnIndex(Data.CONTACT_ID));
						MyDataItem item = new MyDataItem(rawContactId,number);
						data.add(item);
					}
				} while (cursor.moveToNext());
			}

			return data;
		} catch (Exception e) {
			Logger.logError(e);
			return null;
		} finally {
			try {
				cursor.close();
			} catch (Exception e) {
				Logger.logError(e);
			}
		}

	}

	private Context getContext() {
		return context;
	}
	private MyRawContacts getAllRawContacts() {
		Cursor cursor = null;
		try {
			MyRawContacts contacts = new MyRawContacts();
			cursor = getContext().getContentResolver().query(Contacts.CONTENT_URI, null, null, null, Contacts._ID+" asc");
			if ( cursor.moveToFirst()) {
				do {
					String id = cursor.getString(cursor.getColumnIndex(Contacts._ID));
					String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
					long lastTimeContact = cursor.getLong(cursor.getColumnIndex(Contacts.LAST_TIME_CONTACTED));						
					MyContactItem item = new MyContactItem(id,name,lastTimeContact);
					contacts.add(item);
				} while ( cursor.moveToNext() );
			}
			return contacts;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				cursor.close();
			} catch (Exception e) {

			}
		}
	}

	public class MyRawContacts extends ArrayList<MyContactItem> {
		private static final long serialVersionUID = -4273993522347750404L;

		public String getId(int contactIndex) {
			return this.get(contactIndex).getID();
		}

	}

	public class MyContactData extends ArrayList<MyDataItem> {
		private static final long serialVersionUID = -1144265732364365993L;

		public String getId(int dataIndex) {
			return this.get(dataIndex).getRawContactID();
		}
	}

	public class MyDataItem {
		private String rawContactID;
		private String number;

		public MyDataItem(String rawContactId, String number) {
			this.rawContactID = rawContactId;
			this.number = number;
		}

		public String getRawContactID() {
			return rawContactID;
		}

		public String getNumber() {
			return number;
		}
	}


	public class MyContactItem {
		private String id;
		private String name;
		private long lastTimeContacted;

		public MyContactItem(String id, String name, long lastTimeContact) {
			this.id = id;
			this.name = name;
			this.setLastTimeContacted(lastTimeContact);
		}
		public String getID() {
			return id;
		}
		public String getName() {
			return name;
		}

		public void setLastTimeContacted(long lastTimeContacted) {
			this.lastTimeContacted = lastTimeContacted;
		}
		public long getLastTimeContacted() {
			return lastTimeContacted;
		}


	}


}


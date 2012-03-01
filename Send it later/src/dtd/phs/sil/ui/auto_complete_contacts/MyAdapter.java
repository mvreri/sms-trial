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
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import dtd.phs.sil.R;
import dtd.phs.sil.utils.Logger;

public class MyAdapter extends ArrayAdapter<String> {


	protected static final String PHS_SMS = "PHS_SMS_TEST";
	protected static final String SEPERATOR = " /split/ ";
	protected static final String FILENAME = "/sdcard/temporary_contacts.txt";
	private final Object lock = new Object();
	protected final class ContactLoader implements Runnable {
		@Override
		public void run() {
			// Need: all name & number : RawContacts.ACCOUNT_NAME - Data.DATA1 with Data.MIMETYPE = Phone.CONTENT_ITEM_TYPE
			//get all raw contacts
			//get all data
			//sort them by raw_contact_id (RawContacts._ID , Data.RAW_CONTACT_ID
			//join info by raw_contact_id
			//				Log.i(PHS_SMS,"Start loading contacts ..." );

			//First, load it from private storage to increase responsiveness
			if (allContacts.size() == 0) {
				loadFromStorage();
			}

			//Load it from content provider
			MyRawContacts rawContacts = getAllRawContacts();
			MyContactData data = getAllContactsData();
			sort(rawContacts, data);
			joinAndFill(rawContacts, data);

			//Save results to storage
			writeToStorage();
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
			} catch (IOException e) {
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

		//Data.DATA1 with Data.MIMETYPE = Phone.CONTENT_ITEM_TYPE
		private MyContactData getAllContactsData() {
			Cursor cursor = null;
			try {
				MyContactData data = new MyContactData();
				cursor = getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, null, null, Data.CONTACT_ID + " asc");
				if ( cursor.moveToFirst()) {
					//						String[] columnNames = cursor.getColumnNames();
					//						String aaa = "";
					//						for(int i = 0; i < columnNames.length ; i++) {
					//							aaa += columnNames[i]+"//";
					//						}
					//						Log.i(PHS_SMS,aaa);
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
	}
	public class MyFilter extends Filter {

		private IFilterListener listener;

		public MyFilter(IFilterListener listener) {
			this.listener = listener;
		}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if ( constraint == null || constraint.length() == 0) {
				synchronized (lock) {
					ArrayList<String> list = new ArrayList<String>();
					results.values = list;
					results.count = 0;
				}
			} else {
				data = findMatchResults(constraint,allContacts);
				results.values = mergeInfo( data );
				results.count = data.size();
			}
			return results;
		}

		private ArrayList<String> mergeInfo(ContactsList data) {
			ArrayList<String> list = new ArrayList<String>();
			for (ContactItem item : data) {
				String s = MyAdapter.mergeInfo(item);
				list.add(s);
			}
			return list;
		}

		private ContactsList findMatchResults(
				CharSequence constraint,
				ContactsList allContacts) {
			ContactsList list = new ContactsList();
			synchronized (allContacts) {
				for (ContactItem item : allContacts) {
					if ( matchContraint(item,constraint)) {
						list.add( item );
					}
				}
				sortPriorityList(list);
				return list;

			}
		}

		private void sortPriorityList(ContactsList list) {
			Collections.sort(list, new Comparator<ContactItem>() {
				@Override
				public int compare(ContactItem lhs, ContactItem rhs) {
					long diff = lhs.getLastTimeContacted() - rhs.getLastTimeContacted();
					if ( diff < 0 ) return 1;
					if ( diff == 0) return 0;
					return -1;
				}
			});
		}

		private boolean matchContraint(ContactItem item, CharSequence constraint) {
			String fullContact = item.getName() + " " + item.getNumber();
			String str = constraint.toString();
			if ( isSubStr(str,fullContact)) return true;
			return false;
		}

		private boolean isSubStr(String str, String fullContact) {
			int j = 0;
			if ( str.equals("097768") ) {
				j++;
				j--;
			}
			String lowerCase = fullContact.toLowerCase();
			//			Log.i(PHS_SMS, "Input string: " + str + " -- Verifying full contact: " + lowerCase);
			for(int i = 0 ; i < str.length() ; i++) {
				if ( j >= lowerCase.length() ) 
					return false;
				while ( j < lowerCase.length() && lowerCase.charAt(j) != str.charAt(i)) {
					j++;
					if ( j >= lowerCase.length() ) 
						return false;
				}
				j++;
			}
			//			Log.i(PHS_SMS, "Passed full contact: " + lowerCase);
			return true;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			MyAdapter.this.notifyDataSetChanged();
			listener.onPublishResult(data);

		}
	}

	private ContactsList data;
	private ContactsList allContacts;
	private MyFilter filter;
	private IFilterListener listener;

	public MyAdapter(Context context,IFilterListener listener) {
		super(context, android.R.layout.simple_list_item_1);
		data = new ContactsList();
		allContacts = new ContactsList();
		this.listener = listener;
	}

	public void printAllContacts(ContactsList allContacts) {
		for(ContactItem contact : allContacts) {
			Logger.logInfo(contact.getName() + " " + contact.getNumber());
		}
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public String getItem(int arg0) {
		return null;
	}

	public ContactItem getContact(int position) {
		return data.get(position);
	}


	private static String mergeInfo(ContactItem contactItem) {
		return contactItem.getName() + " " + contactItem.getNumber();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.auto_contact_list_item, null);
		TextView tvContact = (TextView)v.findViewById(R.id.tvContact);
		TextView tvNumber= (TextView)v.findViewById(R.id.tvNumber);
		tvContact.setText(data.get(position).getName());
		tvNumber.setText(data.get(position).getNumber());
		return v;
	}

	@Override
	public Filter getFilter() {
		if ( filter == null ) {
			filter = new MyFilter(listener);
		} 
		return filter;
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
	public class MyRawContacts extends ArrayList<MyContactItem> {
		private static final long serialVersionUID = -4273993522347750404L;

		public String getId(int contactIndex) {
			return this.get(contactIndex).getID();
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
	public class MyContactData extends ArrayList<MyDataItem> {
		private static final long serialVersionUID = -1144265732364365993L;

		public String getId(int dataIndex) {
			return this.get(dataIndex).getRawContactID();
		}
	}

	public void loadAllContacts() {
		new Thread( new ContactLoader()).start();
	}

}

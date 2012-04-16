package dtd.phs.sil.ui.auto_complete_contacts;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import dtd.phs.sil.R;

public class MyAdapter extends ArrayAdapter<String> {


	protected static final String PHS_SMS = "PHS_SMS_TEST";
	
	private final Object lock = new Object();
	public class MyFilter extends Filter {

//		private IFilterListener listener;

//		public MyFilter(IFilterListener listener) {
//			this.listener = listener;
//		}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if ( constraint == null || constraint.length() == 0) {
				synchronized (lock) {
					results.values = new ContactsList();
					results.count = 0;
				}
			} else {
//				data = allContacts.findMatchResults(getContext(),constraint.toString());
				results.values = allContacts.findMatchResults(getContext(),constraint.toString());
				results.count = data.size();
			}
			return results;
		}

//		private ArrayList<String> mergeInfo(ContactsList data) {
//			ArrayList<String> list = new ArrayList<String>();
//			for (ContactItem item : data) {
//				String s = MyAdapter.mergeInfo(item);
//				list.add(s);
//			}
//			return list;
//		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			MyAdapter.this.data = (ContactsList) results.values;
			MyAdapter.this.notifyDataSetChanged();
//			listener.onPublishResult(data);
		}
	}

	private ContactsList data;
	private ContactsList allContacts;
	private MyFilter filter;

	public MyAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_1);
		data = new ContactsList();
		allContacts = new ContactsList();
	}

//	public void printAllContacts(ContactsList allContacts) {
//		for(ContactItem contact : allContacts) {
//			Logger.logInfo(contact.getName() + " " + contact.getNumber());
//		}
//	}

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


//	private static String mergeInfo(ContactItem contactItem) {
//		return contactItem.getName() + " " + contactItem.getNumber();
//	}

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
			filter = new MyFilter();
		} 
		return filter;
	}

	public void loadAllContacts() {
		new Thread( new AutoContactLoader(getContext(),allContacts,null)).start();
	}

}

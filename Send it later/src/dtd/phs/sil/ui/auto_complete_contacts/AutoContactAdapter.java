package dtd.phs.sil.ui.auto_complete_contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import dtd.phs.sil.R;

public class AutoContactAdapter 
	extends ArrayAdapter<String>
	implements	IFilterListener
{


//	protected static final String PHS_SMS = "PHS_SMS_TEST";
	private ContactsList data;
	private ContactsList allContacts;
	private ContactsFilter filter = null;

	public AutoContactAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_1);
		data = new ContactsList();
		allContacts = new ContactsList();
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
			filter = new ContactsFilter(getContext(),this,allContacts);
		} 
		return filter;
	}
	
	@Override
	public void onPublishResult(ContactsList contacts) {
		AutoContactAdapter.this.data = contacts;
		AutoContactAdapter.this.notifyDataSetChanged();
	}

	public void loadAllContacts() {
		new Thread( new AutoContactLoader(getContext(),allContacts,null)).start();
	}



}

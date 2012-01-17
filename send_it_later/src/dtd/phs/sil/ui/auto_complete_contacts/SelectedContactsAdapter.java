package dtd.phs.sil.ui.auto_complete_contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dtd.phs.sil.R;

public abstract class SelectedContactsAdapter extends BaseAdapter {

	private Context context;
	private ContactsList list;

	public Context getContext() {
		return context;
	}

	public SelectedContactsAdapter(Context applicationContext) {
		this.context = applicationContext;
		list = new ContactsList();
	}

	public void setContactsList(ContactsList list) {
		this.list = list;
	}
	
	public void addContact(ContactItem contact) {
		list.add(0, contact);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = (
				(LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
				inflate(R.layout.selected_contacts_item, null);
		TextView tv = (TextView) v.findViewById(R.id.tvContact);
		final String name = list.get(position).getName();
		tv.setText(name);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeItem(position);
				notifyDataSetChanged();
				onItemRemoved(position);
			}
		});
		return v;
	}

	abstract public void onItemRemoved(int position);

	protected void removeItem(int position) {
		list.remove(position);
	}

}

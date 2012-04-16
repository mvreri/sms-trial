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
	private ContactsList selectedList; 
	//The list may has no info about "last contacted time" 
	//(Operation: ContactsList.createContactsWithoutLastContactedTime()) 

	public ContactsList getSelectedList() {
		return selectedList;
	}

	public void setSelectedList(ContactsList selectedList) {
		this.selectedList = selectedList;
	}

	public Context getContext() {
		return context;
	}

	public SelectedContactsAdapter(Context applicationContext) {
		this.context = applicationContext;
		selectedList = new ContactsList();
	}


	
	public void addContact(ContactItem contact) {
		selectedList.add(0, contact);
	}
	
	@Override
	public int getCount() {
		return selectedList.size();
	}

	@Override
	public Object getItem(int position) {
		return selectedList.get(position);
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
		final String name = selectedList.get(position).getName();
		tv.setText(name);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onItemClick(position);
//				removeItem(position);
//				notifyDataSetChanged();
//				onItemRemoved(position);
			}
		});
		return v;
	}

	abstract public void onItemClick(int position);
	abstract public void onItemRemoved(int position);

	public void removeItem(int position) {
		selectedList.remove(position);
	}

	public ContactItem getSelectedContact(int position) {
		return selectedList.get(position);
	}
	
}

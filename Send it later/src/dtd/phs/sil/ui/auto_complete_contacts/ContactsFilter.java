package dtd.phs.sil.ui.auto_complete_contacts;

import android.content.Context;
import android.widget.Filter;

public class ContactsFilter extends Filter {
	private IFilterListener listener = null;
	private ContactsList allContacts;
	private Context context;

	public ContactsFilter(Context context, IFilterListener listener, ContactsList allContacts) {
		this.context = context;
		this.listener = listener;
		this.allContacts = allContacts;
	}
	@Override
	protected FilterResults performFiltering(CharSequence keyword) {
		FilterResults results = new FilterResults();
		if ( keyword == null || keyword.length() == 0) {
			synchronized (allContacts) {
				results.values = new ContactsList();
				results.count = 0;
			}
		} else {
			results.values = allContacts.findMatchResults(context,keyword.toString());
			results.count = ((ContactsList) (results.values)).size();
		}
		return results;
	}

	@Override
	protected void publishResults(CharSequence keyword,FilterResults results) {
		if ( listener != null) {
			listener.onPublishResult((ContactsList) results.values);
		}
	}
}

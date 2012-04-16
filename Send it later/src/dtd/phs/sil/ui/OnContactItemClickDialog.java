package dtd.phs.sil.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import dtd.phs.sil.R;
import dtd.phs.sil.ui.auto_complete_contacts.ContactItem;
import dtd.phs.sil.ui.auto_complete_contacts.SelectedContactsAdapter;
import dtd.phs.sil.utils.Helpers;

public class OnContactItemClickDialog extends Dialog {

	private SelectedContactsAdapter adapter;
	private int position;
	private TextView tvTitle;
	private TextView tvRemove;
	private TextView tvCallNow;
	private TextView tvSMSNow;
	private Activity activity;

	public OnContactItemClickDialog(Activity activity) {
		super(activity);
		this.activity = activity;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_contact_item_click);
		setCancelable(true);
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		
		tvRemove = (TextView)findViewById(R.id.tvRemove);
		tvRemove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.removeItem(position);
				adapter.notifyDataSetChanged();
				adapter.onItemRemoved(position);
			}
		});
		
		tvCallNow = (TextView)findViewById(R.id.tvCallNow);
		tvCallNow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ContactItem selectedContact = adapter.getSelectedContact(position);
				Helpers.launchIntentCall(activity,selectedContact.getNumber());
			}
		});
		
		tvSMSNow = (TextView)findViewById(R.id.tvSMSNow);
		tvSMSNow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ContactItem selectedContact = adapter.getSelectedContact(position);
				Helpers.launchIntentSMS(activity,selectedContact.getNumber());
			}
		});
		
	}

	public void prepare() {
		ContactItem selectedContact = adapter.getSelectedContact(position);
		tvTitle.setText(selectedContact.getName()+" "+selectedContact.getNumber());
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setAdapter(SelectedContactsAdapter selectedAdapter) {
		this.adapter = selectedAdapter;
	}
	
}

package dtd.phs.sil.ui;

import android.app.Activity;
import android.widget.TextView;
import dtd.phs.sil.R;
import dtd.phs.sil.ui.auto_complete_contacts.ContactItem;
import dtd.phs.sil.ui.auto_complete_contacts.SelectedContactsAdapter;
import dtd.phs.sil.utils.Helpers;
import dtd.phs.sil.utils.Logger;

public class OnContactItemClickDialog extends ChooseTextsDialog {

	private static final int REMOVE_POSITION = 0;
	private static final int CALL_POSITION = 1;
	private static final int SMS_POSITION = 2;
	private static final int[] RESOURCES_NAMES = {R.string.Remove,R.string.Call_now,R.string.sms_now};
	private SelectedContactsAdapter adapter;
	private int currentAdapdaterPosition;
//	private TextView tvTitle;
//	private TextView tvRemove;
//	private TextView tvCallNow;
//	private TextView tvSMSNow;
	private Activity activity;

	public OnContactItemClickDialog(Activity activity) {
		super(  activity,
				R.string.about_us, 
				RESOURCES_NAMES);
		this.activity = activity;
	}

	//	@Override
	//	protected void onCreate(Bundle savedInstanceState) {
	//		super.onCreate(savedInstanceState);
	//		requestWindowFeature(Window.FEATURE_NO_TITLE);
	//		setContentView(R.layout.dialog_contact_item_click);
	//		setCancelable(true);
	//		tvTitle = (TextView)findViewById(R.id.tvTitle);
	//		
	//		tvRemove = (TextView)findViewById(R.id.tvRemove);
	//		tvRemove.setOnClickListener(new View.OnClickListener() {
	//			@Override
	//			public void onClick(View v) {
	//				adapter.removeItem(position);
	//				adapter.notifyDataSetChanged();
	//				adapter.onItemRemoved(position);
	//			}
	//		});
	//		
	//		tvCallNow = (TextView)findViewById(R.id.tvCallNow);
	//		tvCallNow.setOnClickListener(new View.OnClickListener() {
	//			
	//			@Override
	//			public void onClick(View v) {
	//				ContactItem selectedContact = adapter.getSelectedContact(position);
	//				Helpers.launchIntentCall(activity,selectedContact.getNumber());
	//			}
	//		});
	//		
	//		tvSMSNow = (TextView)findViewById(R.id.tvSMSNow);
	//		tvSMSNow.setOnClickListener(new View.OnClickListener() {
	//			
	//			@Override
	//			public void onClick(View v) {
	//				ContactItem selectedContact = adapter.getSelectedContact(position);
	//				Helpers.launchIntentSMS(activity,selectedContact.getNumber());
	//			}
	//		});
	//		
	//	}

	public void prepare() {
		ContactItem selectedContact = adapter.getSelectedContact(currentAdapdaterPosition);
		tvTitle.setText(selectedContact.getName()+" "+selectedContact.getNumber());
	}

	public void setPosition(int position) {
		this.currentAdapdaterPosition = position;
	}

	public void setAdapter(SelectedContactsAdapter selectedAdapter) {
		this.adapter = selectedAdapter;
	}

	@Override
	public void onItemSelected(int textPosition) {
		ContactItem selectedContact = adapter.getSelectedContact(currentAdapdaterPosition);
		switch (textPosition) {
		case REMOVE_POSITION:
			adapter.removeItem(currentAdapdaterPosition);
			adapter.notifyDataSetChanged();
			adapter.onItemRemoved(textPosition);
			break;
		case CALL_POSITION:
			Helpers.launchIntentCall(activity,selectedContact.getNumber());
			break;
		case SMS_POSITION:
			Helpers.launchIntentSMS(activity,selectedContact.getNumber());			
			break;
		default: Logger.logInfo("Weird");
		}
	}

}

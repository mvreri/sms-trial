package dtd.phs.sil;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.devsmart.android.ui.HorizontalListView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import dtd.phs.sil.ui.ChooseDateDialog;
import dtd.phs.sil.ui.ChooseTimeDialog;
import dtd.phs.sil.ui.auto_complete_contacts.ContactItem;
import dtd.phs.sil.ui.auto_complete_contacts.MyAdapter;
import dtd.phs.sil.ui.auto_complete_contacts.SelectedContactsAdapter;
import dtd.phs.sil.utils.Helpers;

public class EditMessage extends Activity {

	protected static final String EDIT_TYPE = "edit_type";
	protected static final String TYPE_NEW = "type_new";

	protected static final int DIALOG_DATE = 0;
	protected static final int DIALOG_TIME = 1;

	protected static final int FRAME_FILL_INFO = 0;
	protected static final int FRAME_CONTACTS_LIST = 1;
	
	private TextView tvDate;
	private TextView tvTime;
	protected Calendar selectedCalendar = null;
	private FrameLayout mainFrames;
	private ListView contactsList;
	private EditText etContact;
	private MyAdapter adapter;
	private HorizontalListView lvSelectedContacts;
	private SelectedContactsAdapter selectedAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.edit_message);
	    init();
	    createViews();
	}

	private void init() {
		selectedCalendar = null;
	}

	private void createViews() {
		createDateTime();
		createAutoContactModules();
	}

	private void createAutoContactModules() {
		
		mainFrames = (FrameLayout) findViewById(R.id.mainFrames);
		Helpers.showOnlyView(mainFrames, FRAME_FILL_INFO);
		
		etContact = (EditText) findViewById(R.id.etTo);
		contactsList = (ListView) findViewById(R.id.listAutoComplete);
		adapter = new MyAdapter(getApplicationContext());
		
		adapter.loadAllContacts();
		contactsList.setAdapter(adapter);
		
		lvSelectedContacts = (HorizontalListView) findViewById(R.id.listSelected);
		selectedAdapter = new SelectedContactsAdapter(getApplicationContext());
		lvSelectedContacts.setAdapter(selectedAdapter);
		
		contactsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				ContactItem contact = adapter.getContact(position);
				selectedAdapter.addContact(contact);
				selectedAdapter.notifyDataSetChanged();
				lvSelectedContacts.setSelection(selectedAdapter.getCount()-1);
				etContact.setText("");
				onBackPressed();
			}
		});
		
		etContact.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				String text = s.toString();
				Helpers.showOnlyView(mainFrames, FRAME_CONTACTS_LIST);
				adapter.getFilter().filter(text);
			}
		});
		
		Helpers.showOnlyView(mainFrames, FRAME_FILL_INFO);
	}
	
	@Override
	public void onBackPressed() {
		if (mainFrames.getChildAt(FRAME_CONTACTS_LIST).getVisibility() == View.VISIBLE) {
			Helpers.showOnlyView(mainFrames, FRAME_FILL_INFO);
		} else super.onBackPressed();
	}

	private void createDateTime() {
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_DATE);
			}
		});
		tvTime = (TextView) findViewById(R.id.tvTime);
		tvTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_TIME);
			}
		});

	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DATE:
			return new ChooseDateDialog(this) {
				@Override
				public void onDateSelected(Calendar selectedCalendar) {
					EditMessage.this.selectedCalendar = selectedCalendar;
					updateTimeDateTexts(selectedCalendar);
				}
			};
		case DIALOG_TIME:
			return new ChooseTimeDialog(this) {
				
				@Override
				public void onCalendarSelected(Calendar selectedCalendar) {
					EditMessage.this.selectedCalendar.set(Calendar.HOUR_OF_DAY, selectedCalendar.get(Calendar.HOUR_OF_DAY));
					EditMessage.this.selectedCalendar.set(Calendar.MINUTE, selectedCalendar.get(Calendar.MINUTE));
					updateTimeDateTexts(selectedCalendar);
				}
			};
		}
		
		return null;
	}
	
	protected void updateTimeDateTexts(Calendar calendar) {
		int y = calendar.get(Calendar.YEAR);
		int m = calendar.get(Calendar.MONTH);
		int d = calendar.get(Calendar.DATE);
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		tvDate.setText("Date: " + new SimpleDateFormat("EEEE - MMMM.dd, yyyy").format(new Date(y-1900,m,d)));
		tvTime.setText("Time: " + new SimpleDateFormat("HH:mm").format(new Time(h, min, 0))); 
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DIALOG_DATE:
			((ChooseDateDialog)dialog).prepare(selectedCalendar);
			break;
		case DIALOG_TIME:
			((ChooseTimeDialog)dialog).prepare(selectedCalendar);
			break;
		}
	}

}

package dtd.phs.sil.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import dtd.phs.sil.EditMessage;
import dtd.phs.sil.R;
import dtd.phs.sil.data.Database;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.PendingMessagesList;
import dtd.phs.sil.utils.FrequencyHelpers;
import dtd.phs.sil.utils.Helpers;
import dtd.phs.sil.utils.StringHelpers;

public class TestAct extends Activity {

	public class MyAdapter extends ArrayAdapter<PendingMessageItem> {

		public MyAdapter(Context applicationContext,
				PendingMessagesList messages) {
			super(applicationContext,R.layout.simple_list_item,messages);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {		
			TextView v = (TextView) Helpers.inflate(getContext(), R.layout.simple_list_item);
			PendingMessageItem item = getItem(position);
			String s = ""+item.getId();
			String sep = "##";
			s += StringHelpers.implode(item.getNames(),sep) + "\n";
			s += StringHelpers.implode(item.getPhoneNumbers(), sep) + "\n";
			s += item.getContent()+"\n";
			Calendar startDateTime = item.getStartDateTime();
			s += "Start time: "+new SimpleDateFormat("EE - MMMM.dd, yyyy HH:mm").format(new Date(startDateTime.getTimeInMillis())) + "\n";
			Calendar nextCalendar = FrequencyHelpers.getNextCalendar(startDateTime, item.getFreq());
			if ( nextCalendar == null ) s+= "LATE ALREADY !\n"; else {
				s += "Next occurence: " + new SimpleDateFormat("EE - MMMM.dd, yyyy HH:mm").
					format(new Date(nextCalendar.getTimeInMillis())) + "\n";
			}
			s += "Frequency: " + item.getFreq().toString() + "\n"; 
			s += "Alert: " + item.getAlert().toString() + "\n";

			v.setText(s);

			return v;
		}

	}

	private ListView list;
	private Button btAdd;
	private MyAdapter adapter;
	private PendingMessagesList messages;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);
		list = (ListView) findViewById(R.id.list);
		btAdd = (Button) findViewById(R.id.btAdd);
		btAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),EditMessage.class);				
				startActivity(i);
			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				EditMessage.passedMessage = messages.get(position);
				Intent i = new Intent(getApplicationContext(),EditMessage.class);
				startActivity(i);
				//				Database.removePendingMessage(getApplicationContext(), messages.get(position).getId());
				//				updateUIFromDB();
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		updateUIFromDB();
	}
	private void updateUIFromDB() {
		messages = Database.getPendingMessages(getApplicationContext());
		adapter = new MyAdapter(
				getApplicationContext(),	    		
				messages);
		list.setAdapter(adapter);
	}

}

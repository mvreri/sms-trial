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
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import dtd.phs.sil.EditMessage;
import dtd.phs.sil.R;
import dtd.phs.sil.data.Database;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.PendingMessagesList;
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
			String s = "";
			String sep = "##";
			s += StringHelpers.implode(item.getNames(),sep) + "\n";
			s += StringHelpers.implode(item.getPhoneNumbers(), sep) + "\n";
			s += item.getContent();
			Calendar startDateTime = item.getStartDateTime();
			s += new SimpleDateFormat("EE - MMMM.dd, yyyy HH:mm").format(new Date(startDateTime.getTimeInMillis())) + "\n";
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
	}
	@Override
	protected void onResume() {
		super.onResume();
	    messages = Database.getPendingMessages(getApplicationContext());
	    adapter = new MyAdapter(
	    		getApplicationContext(),	    		
	    		messages);
	    list.setAdapter(adapter);
	}

}

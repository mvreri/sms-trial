package dtd.phs.chatexperiment_phs;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ShowLog extends Activity {
	
	private ListView listview;
	private Button btSummary;
	private ArrayList<String> meterInfo = new ArrayList<String>();
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_log);
		listview = (ListView) findViewById(R.id.listview);
		btSummary = (Button) findViewById(R.id.btSummary);
		
		adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, meterInfo );
		listview.setAdapter(adapter);
		btSummary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent act = new Intent(ShowLog.this, BatteryMeter.class);
				startActivity(act);
				finish();
			}
		});
	}
	
	@Override
	protected void onResume() {	
		super.onResume();
		ArrayList<MeterInfo> info = BatteryStatusTable.getMeterInfo(getApplicationContext());
		meterInfo.clear();
		for(MeterInfo item : info) {
			meterInfo.add("At: " + Helpers.formatToTime(item.time)  + " -- battery: " + item.percentage + "%"); 
		}
		adapter.notifyDataSetChanged();
	}
}

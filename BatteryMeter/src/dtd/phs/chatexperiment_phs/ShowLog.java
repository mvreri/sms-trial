package dtd.phs.chatexperiment_phs;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
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

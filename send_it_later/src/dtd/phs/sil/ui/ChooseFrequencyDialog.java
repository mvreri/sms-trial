package dtd.phs.sil.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import dtd.phs.sil.R;
import dtd.phs.sil.R.id;
import dtd.phs.sil.R.layout;
import dtd.phs.sil.utils.FrequencyHelpers;
import dtd.phs.sil.utils.FrequencyHelpers.Frequencies;

public abstract class ChooseFrequencyDialog extends Dialog {

	public static final int TEXT_COLOR = 0xff666666;


	public class MyAdapter extends ArrayAdapter<String> {
		public MyAdapter(Context context, int textViewResourceId,String[] objects) {
			super(context, textViewResourceId, objects);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = (TextView) super.getView(position, convertView, parent);
			view.setTextColor(TEXT_COLOR);
			return view;
		}

	}


	private ListView listview;
	private ArrayAdapter<String> adapter;


	public ChooseFrequencyDialog(Context context) {
		super(context);
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_frequencies);
		setCancelable(true);
		listview = (ListView)findViewById(R.id.list);
		adapter = new MyAdapter(getContext(), android.R.layout.simple_list_item_1, FrequencyHelpers.FREQ_NAMES);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				onFreqSelected(FrequencyHelpers.FREQUENCIES[position]);
				cancel();
			}
		});
	}


	abstract public void onFreqSelected(Frequencies frequencies);
}

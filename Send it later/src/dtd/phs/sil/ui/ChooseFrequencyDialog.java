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
	private TextView tvTitle;
	private int titleId;
	private String[] texts;


	public ChooseFrequencyDialog(Context context,int titleId, String[] texts) {
		super(context);
		this.titleId = titleId;
		this.texts = texts;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_text_select);
		setCancelable(true);
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		tvTitle.setText(getContext().getString(titleId));
		listview = (ListView)findViewById(R.id.list);
		adapter = new MyAdapter(getContext(), android.R.layout.simple_list_item_1, texts);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				onItemSelected(position);
				cancel();
			}
		});
	}


	abstract public void onItemSelected(int position);
}

package dtd.phs.sil.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import dtd.phs.sil.R;

public abstract class ChooseTextsDialog extends Dialog {



	public class MyAdapter extends ArrayAdapter<String> {
		public MyAdapter(Context context, int textViewResourceId,String[] objects) {
			super(context, textViewResourceId, objects);
		}

	}


	private ListView listview;
	private ArrayAdapter<String> adapter;
	protected TextView tvTitle;
	private int titleId;
	private String[] texts;


	public ChooseTextsDialog(Context context,int titleId, String[] texts) {
		super(context);
		init(titleId,texts);
	}

	public ChooseTextsDialog(Context context,int titleId, int[] textIds) {
		super(context);
		init(titleId,getTexts(context, textIds));
	}

	private String[] getTexts(Context context, int[] textIds) {
		String[] texts = new String[textIds.length];
		for(int i = 0 ; i < textIds.length ; i++) {
			texts[i] = context.getResources().getString(textIds[i]);
		}
		return texts;
	}

	
	private void init(int titleId, String[] texts) {
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
		adapter = new MyAdapter(getContext(), R.layout.simple_list_item, texts);
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

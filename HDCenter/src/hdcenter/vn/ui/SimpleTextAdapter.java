package hdcenter.vn.ui;

import hdcenter.vn.utils.Helpers;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SimpleTextAdapter extends BaseAdapter {

	
	private int itemLayout;
	private String[] itemTexts;
	private Context context;

	public SimpleTextAdapter(Context context, int itemLayout, int[] textResources) {
		super();
		init(context,itemLayout,getItemTextFromResources(context, textResources));
	}

	public SimpleTextAdapter(Context context, int itemLayout, String[] texts) {
		super();
		init(context,itemLayout,texts);
	}
	
	private void init(Context context, int itemLayout, String[] texts) {
		this.context = context;
		this.itemLayout = itemLayout;
		this.itemTexts = texts.clone();
	}
	
	private String[] getItemTextFromResources(Context context, int[] textResources) {
		String[] items = new String[textResources.length];
		for(int i = 0 ; i < items.length ; i++) 
			items[i] = context.getResources().getString(textResources[i]);
		return items;
	}

	@Override
	public int getCount() {
		return itemTexts.length;
	}

	@Override
	public Object getItem(int position) {
		return itemTexts[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if ( v == null ) {
			v = Helpers.inflate(this.context, this.itemLayout, null);
		}
		TextView tv = (TextView) v.findViewById(android.R.id.text1);
		tv.setText(itemTexts[position]);
		return v;
	}

}

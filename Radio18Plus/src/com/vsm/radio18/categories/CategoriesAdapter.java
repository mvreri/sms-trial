package com.vsm.radio18.categories;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vsm.radio18.R;
import com.vsm.radio18.data.entities.CategoryItem;

import dtd.phs.lib.utils.Helpers;

public class CategoriesAdapter extends BaseAdapter {



	private ArrayList<CategoryItem> cats;
	private Context context;

	public CategoriesAdapter(Context context) {
		super();
		this.context = context;
		cats = new ArrayList<CategoryItem>();
	}
	@Override
	public int getCount() {
		return cats.size();
	}

	@Override
	public CategoryItem getItem(int position) {
		return cats.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		TextView tvName = null;
		if ( v == null ) {
			v = Helpers.inflate(context, R.layout.category_item, null);
		} 
		tvName = (TextView) v.findViewById(R.id.tvName);
		CategoryItem cat = getItem(position);
		tvName.setText(cat.getName());
		return v;
	}

	public void refreshData(ArrayList<CategoryItem> list) {
		this.cats = list;
		notifyDataSetChanged();
	}

}

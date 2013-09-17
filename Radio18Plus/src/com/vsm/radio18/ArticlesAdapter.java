package com.vsm.radio18;

import java.util.ArrayList;

import javax.crypto.spec.IvParameterSpec;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vsm.radio18.data.entities.ArticleItem;

import dtd.phs.lib.ui.images_loader.ImageCache;
import dtd.phs.lib.ui.images_loader.ImageLoader;
import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;

public class ArticlesAdapter extends BaseAdapter {

	private ArrayList<ArticleItem> list;
	private ImageLoader imageLoader;
	private Activity act;
	private Handler handler = new Handler();

	public ArticlesAdapter(Activity activity) {
		this.act = activity;
		list = new ArrayList<ArticleItem>();
	}

	public void onResume() {
		imageLoader = new ImageLoader(act, handler, null);
	}

	public void onPause() {
		imageLoader.stop();
		imageLoader = null;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ArticleItem getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder = null;
		if (v == null) {
			v = Helpers.inflate(act, R.layout.article_item, null);
			holder = new ViewHolder();
			holder.ivCover = (ImageView) v.findViewById(R.id.ivCover);
			holder.tvName = (TextView) v.findViewById(R.id.tvName);
			holder.tvDesc = (TextView) v.findViewById(R.id.tvDesc);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		ArticleItem item = getItem(position);
		holder.tvName.setText(item.getName());
		holder.tvDesc.setText(item.getDesc());
		String coverURL = item.getCoverURL();
		Bitmap bm = ImageCache.getCacheImage(coverURL);
		if ( bm != null ) {
			holder.ivCover.setImageBitmap(bm);
		} else if (imageLoader != null) {
			holder.ivCover.setImageBitmap(null);
			imageLoader.loadImage(coverURL, holder.ivCover, true);
		}
		return v;
	}

	public class ViewHolder {
		ImageView ivCover;
		TextView tvName;
		TextView tvDesc;
	}

	public void refreshData(ArrayList<ArticleItem> list) {
		this.list = list;
		notifyDataSetChanged();
	}

}

package com.vsm.radio18;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vsm.radio18.data.db.DBCenter;
import com.vsm.radio18.data.db.QueryWorker;
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
	private ArrayList<OnClickListener> onItemBuyClick;

	public ArticlesAdapter(Activity activity) {
		this.act = activity;
		list = new ArrayList<ArticleItem>();
		onItemBuyClick = new ArrayList<OnClickListener>();
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
			holder.btBuy = (Button) v.findViewById(R.id.btBuy);
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
		holder.btBuy.setOnClickListener(onItemBuyClick.get(position));
		return v;
	}

	public class ViewHolder {
		ImageView ivCover;
		TextView tvName;
		TextView tvDesc;
		Button btBuy;
	}

	public void refreshData(ArrayList<ArticleItem> list) {
		this.list.clear();
		this.onItemBuyClick.clear();
		for(int i = 0 ; i < list.size(); i++) {
			this.list.add(list.get(i));
			final int  position = i;
			this.onItemBuyClick.add(new OnClickListener() {
				@Override
				public void onClick(View v) {
					buyItem(position);
				}
			});
		}
		notifyDataSetChanged();
	}

	protected void buyItem(int position) {
		final ArticleItem item = getItem(position);
		//TODO: later - send sms, check request ....
		QueryWorker.add(new Runnable() {
			
			@Override
			public void run() {
				boolean succ = DBCenter.addItem(act.getApplicationContext(), item);
				Logger.logInfo("Is item added to db ? " + succ);
			}
		});
	}

}

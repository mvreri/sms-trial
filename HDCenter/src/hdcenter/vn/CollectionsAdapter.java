package hdcenter.vn;

import javax.crypto.spec.IvParameterSpec;

import dtd.phs.lib.ui.images_loader.ImageLoader;
import hdcenter.vn.CollectionsAdapter.Holder;
import hdcenter.vn.entities.MovieCollectionsList;
import hdcenter.vn.utils.Helpers;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CollectionsAdapter extends BaseAdapter {



	private Context context;
	private MovieCollectionsList collections;
	private ImageLoader imageLoader;

	public CollectionsAdapter(
			Context applicationContext,
			MovieCollectionsList collections, 
			Handler handler) {
		super();
		this.context = applicationContext;
		this.collections = collections;
		imageLoader = new ImageLoader(applicationContext, R.drawable.movie_stub, handler);
	}

	@Override
	public int getCount() {
		return collections.size();
	}

	@Override
	public Object getItem(int position) {
		return collections.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Holder holder = null;
		if ( v == null) {
			v = Helpers.inflate(context, R.layout.collection_item, null);
			holder = new Holder();
			holder.tvTitle = (TextView) v.findViewById(R.id.tvTitle);
			holder.ivPicture = (ImageView) v.findViewById(R.id.ivPicture);
			v.setTag(holder);
		} else {
			holder = (Holder)v.getTag();
		}
		
		imageLoader.loadImage(collections.getImageURL(position), holder.ivPicture);
		holder.tvTitle.setText(collections.getName(position));
		
		return v;
	}

	public class Holder {
		ImageView ivPicture;
		TextView tvTitle;
	}
}

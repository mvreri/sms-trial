package hdcenter.vn.ui;

import hdcenter.vn.R;
import hdcenter.vn.entities.MovieItem;
import hdcenter.vn.entities.MoviesList;
import hdcenter.vn.utils.Helpers;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dtd.phs.lib.ui.images_loader.ImageLoader;

public class MovieAdapter extends BaseAdapter {



	private Context context;

	private MoviesList mList;

	private ImageLoader imageLoader;

	private Bitmap stubBitmap = null;

	
	public MovieAdapter(Context context, MoviesList list, Handler handler) {
		super();
		this.context = context;
		this.mList = list;
		this.imageLoader = ImageLoader.getInstance(context,R.drawable.movie_stub, handler);
	}

	public MoviesList getmList() {
		return mList;
	}
	public void setmList(MoviesList mList) {
		this.mList = mList;
	}


	@Override
	public int getCount() {
		return this.mList.size();
	}

	@Override
	public Object getItem(int index) {
		return this.mList.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	
	public class ViewHolder {

		public ImageView ivAvatar;
		public TextView tvName;
		public TextView tvVnName;
		public TextView tvRating;

	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder = null;
		if ( v == null ) {
			v = Helpers.inflate(context, R.layout.sum_movie_item, null);
			holder = new ViewHolder();

			holder.ivAvatar = (ImageView)v.findViewById(R.id.ivAvatar);
			holder.tvName = (TextView)v.findViewById(R.id.tvName);
			holder.tvVnName = (TextView)v.findViewById(R.id.tvVnName);
			holder.tvRating = (TextView)v.findViewById(R.id.tvIMDB);
			
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		
		MovieItem movie = mList.get(position);
		holder.ivAvatar.setImageBitmap(getStubImage());
		imageLoader.loadImage(movie.getImageURL(),holder.ivAvatar);
		holder.tvName.setText(movie.getName());
		holder.tvVnName.setText(movie.getVnName());
		holder.tvRating.setText(movie.getRating());
		return v;
		
	}

	private Bitmap getStubImage() {
		if ( stubBitmap == null) {
			stubBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.movie_stub);
		}
		return stubBitmap;
	}
}

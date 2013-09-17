package dtd.phs.lib.ui.images_loader;

import java.lang.ref.WeakReference;

import android.widget.ImageView;
import dtd.phs.lib.utils.Helpers;

public class LoadingImageItem {
	static final long DELAY_TIME = 500;
	public String URL;
	public WeakReference<ImageView> weakIview;
	private long addedTime;
	private boolean isCircular;
	
	public LoadingImageItem(String url, WeakReference<ImageView> weakIv, boolean isCircular) {
		super();
		URL = url;
		this.weakIview = weakIv;
		this.addedTime = System.currentTimeMillis();
		this.isCircular = isCircular;
	}

	public String getHashName() {
		return Helpers.MD5(URL);
	}
	public boolean isCircular() {
		return isCircular;
	}

	public boolean allowedLoad() {
		return System.currentTimeMillis() - this.addedTime >= DELAY_TIME;
	}

}

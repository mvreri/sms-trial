package dtd.phs.lib.ui.images_loader;

import hdcenter.vn.utils.Helpers;
import android.widget.ImageView;

public class LoadingImageItem {
	public String URL;
	public ImageView iview;
	public LoadingImageItem(String url, ImageView iview) {
		super();
		URL = url;
		this.iview = iview;
	}
	public String getHashName() {
		return Helpers.MD5(URL);
	}

}

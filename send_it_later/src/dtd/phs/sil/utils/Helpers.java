package dtd.phs.sil.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class Helpers {
	public static final boolean DEBUG_MODE = true;

	static public void showOnlyView(FrameLayout mainFrames, int id) {
		for(int i = 0 ; i < mainFrames.getChildCount() ; i++) {
			int displayed = View.INVISIBLE;
			if ( i == id ) displayed = View.VISIBLE;
			mainFrames.getChildAt(i).setVisibility(displayed);
		}
	}

	public static View inflate(Context context, int layout) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(layout, null);
	}


}

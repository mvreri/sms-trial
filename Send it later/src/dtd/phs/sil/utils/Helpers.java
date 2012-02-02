package dtd.phs.sil.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class Helpers {

	public static final boolean DEBUG_MODE = true;

	public static View inflate(Context context, int layout) {
		LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inf.inflate(layout, null);
	}

	public static void showOnlyView(FrameLayout mainFrames, int id) {
		for(int i = 0 ; i < mainFrames.getChildCount() ; i++) {
			int disp = View.INVISIBLE;
			if ( i == id) disp = View.VISIBLE;
			mainFrames.getChildAt(i).setVisibility(disp);
		}
	}

	public static float dp2px(Context context, int i) {		
		Resources r = context.getResources();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics());
	}

	public static void startAfter(final int waitingTime,final Runnable runner) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						this.wait(waitingTime);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runner.run();
			}
		}).start();
	}

	public static String formatTime(long sentTime) {
		Date date = new Date(sentTime);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm EE - MMMM.dd, yyyy");
		return formatter.format(date);
	}

}

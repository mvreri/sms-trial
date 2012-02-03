package dtd.phs.sil.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
		
		Calendar current = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if ( equalsDate(cal,current)) {
			return "Today, at " + (new SimpleDateFormat("HH:mm").format(date));
		}
		cal.add(Calendar.DATE, 1);
		if ( equalsDate(cal,current)) {
			return "Yesterday, at " + (new SimpleDateFormat("HH:mm").format(date));
		}
		cal.add(Calendar.DATE, -2);
		if ( equalsDate(cal,current)) {
			return "Tomorrow, at " + (new SimpleDateFormat("HH:mm").format(date));
		}

		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm EE - MMMM.dd, yyyy");
		return formatter.format(date);
	}

	private static boolean equalsDate(Calendar cal, Calendar current) {
		if ( cal.get(Calendar.MONTH) != current.get(Calendar.MONTH)) return false;
		if ( cal.get(Calendar.YEAR) != current.get(Calendar.YEAR)) return false;
		if ( cal.get(Calendar.DATE) != current.get(Calendar.DATE)) return false;
		return true;
	}

}

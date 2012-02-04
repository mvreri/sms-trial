package dtd.phs.sil.ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import dtd.phs.sil.utils.Logger;

public abstract class OnListItemTouchListener implements OnTouchListener {
	final private float MIN_X_SWIPE = 100;
	final private float MAX_Y_SWIPE = 30;
	final private float MIN_VELOCITY = 100;
	protected static final float MAX_CLICK = 20;
	protected static final long CLICK_TIME = 200;
	private static final long MIN_LONG_CLICK_TIME = 1000;
	private static final long VIBRATOR_PERIOD = 10;
	
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	private long startTime;
	private long endTime;
	
	private int position;
	
	public OnListItemTouchListener(int position) {
		super();
		this.position = position;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Logger.logInfo("ACtion down !");
			startX = event.getX();
			startY = event.getY();
			startTime = System.currentTimeMillis();
			return true;
		case MotionEvent.ACTION_UP:
			Logger.logInfo("ACtion Up!");
			endX = event.getX();
			endY = event.getY();
			endTime = System.currentTimeMillis();
			float dx = Math.abs(endX - startX);
			float dy = Math.abs(endY - startY);
			long dtime = endTime - startTime;
			float velo = (float) (Math.sqrt(dx*dy + dy*dy) / dtime );
			if ( dx > MIN_X_SWIPE && dy < MAX_Y_SWIPE && velo > MIN_VELOCITY) {
				onSwipe(position);
				return true;
			} else if ( dx < MAX_CLICK && dy < MAX_CLICK) {
				if ( dtime < CLICK_TIME ) {
					onClick(position);
					return true;
				} 
//				else {
//					onLongClick(position);
//					return true;
//				}
			}
			return false;
		case MotionEvent.ACTION_CANCEL:
			return false;
		case MotionEvent.ACTION_MOVE:
			float x = event.getX();
			float y = event.getY();
			dx = Math.abs(x - startX);
			dy = Math.abs(y - startY);
			dtime = System.currentTimeMillis() - startTime;
			if ( dx < MAX_CLICK && dy < MAX_CLICK && dtime >= MIN_LONG_CLICK_TIME ) {
//				vib = (Vibrator) (v.getContext()).getSystemService(Context.VIBRATOR_SERVICE);
//				vib.vibrate(VIBRATOR_PERIOD);
				onLongClick(position);
				return true;
			}
			return false;
		}
		
		return false;
		
	}

	abstract public void onLongClick(int position);
	abstract public void onClick(int position);
	abstract public void onSwipe(int position);
	
}
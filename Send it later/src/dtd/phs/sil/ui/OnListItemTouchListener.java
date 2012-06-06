package dtd.phs.sil.ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import dtd.phs.sil.utils.Logger;

public abstract class OnListItemTouchListener implements OnTouchListener {
	final private float MIN_X_SWIPE = 60;
	final private float MAX_Y_SWIPE = 120;
	final private float MIN_VELOCITY = 300;
	protected static final float MAX_CLICK = 10;
	protected static final long CLICK_TIME = 400;
	private static final long MIN_LONG_CLICK_TIME = 1000;
	
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	private long startTime;
	private long endTime;
	
	private int position;
	private View view;
	
	public OnListItemTouchListener(int position, View view) {
		super();
		this.position = position;
		this.view = view;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		long dtime = 0;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			startX = event.getX();
			startY = event.getY();
			startTime = System.currentTimeMillis();
			Logger.logInfo("ACtion down, time: " + startTime);
			return true;
		case MotionEvent.ACTION_CANCEL:
			Logger.logInfo("ACtion cancel, time: " + System.currentTimeMillis());
		case MotionEvent.ACTION_UP:
			endX = event.getX();
			endY = event.getY();
			endTime = System.currentTimeMillis();
			Logger.logInfo("ACtion up, time: " + endTime);
			float dx = Math.abs(endX - startX);
			float dy = Math.abs(endY - startY);
			dtime = endTime - startTime;
			float velo = (float) (Math.sqrt(dx*dx + dy*dy) / (0.001*dtime));
			Logger.logInfo("(DX,DY,Velo) at action up: (" + dx + "," + dy + "," + velo);
			if ( dx > MIN_X_SWIPE && dy < MAX_Y_SWIPE && velo > MIN_VELOCITY) {
				Logger.logInfo("OnSwipe is called !");
				onSwipe(this.view,position);
				return true;
			} else if ( dx < MAX_CLICK && dy < MAX_CLICK) {
				if ( dtime < CLICK_TIME ) {
					onClick(this.view,position);
					return true;
				} 
			}
			return false;
			
		case MotionEvent.ACTION_MOVE:
			float x = event.getX();
			float y = event.getY();
			dx = Math.abs(x - startX);
			dy = Math.abs(y - startY);
			dtime = System.currentTimeMillis() - startTime;
			if ( dx < MAX_CLICK && dy < MAX_CLICK && dtime >= MIN_LONG_CLICK_TIME ) {
				onLongClick(position);
				return true;
			}
			return false;
		}
		
		return false;
		
	}

	abstract public void onLongClick(int position);
	abstract public void onClick(View view,int position);
	abstract public void onSwipe(View view,int position);
	
}
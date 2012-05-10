package hdcenter.vn.ui;

import hdcenter.vn.R;
import hdcenter.vn.data.DataCenter;
import hdcenter.vn.data.IRequestListener;
import hdcenter.vn.movie_calendars.CalendarsList;
import hdcenter.vn.utils.Logger;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SecondaryFrame implements IRequestListener {

	protected static final int FRAME_DESCRIPTION = 0;
	protected static final int FRAME_CALENDAR = 1;
	private static final int FADE_BG = 0xff000000;
	private static final int HIGH_LIGHT_BG = 0xffffffff;
	private FrameLayout frames;
	private TextView tvDescription;
	private ViewGroup root;
	private View[] tabs;
	private LinearLayout calendarFrame;
	private Handler handler;
	private CalendarsList calendars;

	public SecondaryFrame(ViewGroup layout, Handler handler) {
		this.root = layout;
		this.handler = handler;
		bindViews();
		initTabs();
	}

	private void bindViews() {
		this.frames = (FrameLayout) findViewById(R.id.secondary_frames);
		this.tvDescription = (TextView) findViewById(R.id.tvDescription);
		this.calendarFrame = (LinearLayout) findViewById(R.id.calendar_frame);
		
	}

	private void initTabs() {
		this.tabs = new View[frames.getChildCount()];
		this.tabs[FRAME_DESCRIPTION] = findViewById(R.id.tab_desc);
		this.tabs[FRAME_CALENDAR] = findViewById(R.id.tab_calendar);
		
		for(int i = 0 ; i < frames.getChildCount() ; i++) {
			final int index = i;
			tabs[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Logger.logInfo("Index = " + index);
					showFrame(index);
				}
			});

		}
	}

	protected void showFrame(int index) {
		highlightTab(index);
		showOnlyView(index);
	}

	private void showOnlyView(int index) {
		for(int i = 0 ; i < frames.getChildCount() ; i++) {
			if ( i == index ) {
 				frames.getChildAt(i).setVisibility(View.VISIBLE);
			} else {
				frames.getChildAt(i).setVisibility(View.INVISIBLE);
			}
		}
	}

	//TODO: nicer !
	private void highlightTab(int index) {
		for(int i = 0 ; i < frames.getChildCount() ; i++) {
			if ( i != index ) {
				tabs[i].setBackgroundColor(FADE_BG);
			} else tabs[i].setBackgroundColor(HIGH_LIGHT_BG);
		}
	}

	private View findViewById(int id) {
		return root.findViewById(id);
	}

	public void setDescription(String description) {
		tvDescription.setText(description);
	}
	
	//TODO: better
	public void setCalendars(CalendarsList calendars) {
		for(int i = 0 ; i < calendars.size() ; i++) {
			TextView tv = new TextView(root.getContext());
			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			tv.setText(calendars.get(i).toString());
			calendarFrame.addView(tv, params);
			
		}
	}

	public void requestCalendar(String cinemaId, String movieId) {
		DataCenter.requestMovieCalendar(cinemaId, movieId, this, handler);
	}

	@Override
	public void onRequestSuccess(Object data) {
		if ( data == null || ! (data instanceof CalendarsList)) {
			Logger.logError("Invalid return data");
			return;
		}
		this.calendars = (CalendarsList) data;
		setCalendars(calendars);
	}

	@Override
	public void onRequestError(Exception e) {
		Logger.logError(e);
	}

}

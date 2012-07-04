package dtd.phs.sil.ui;

import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import dtd.phs.sil.R;
import dtd.phs.sil.utils.Helpers;

public abstract class ChooseDateDialog extends Dialog {

	public static final int FONT_SIZE = 22;
	private static final String PHS = "PHS";
	private WheelView monthWheel;
	private WheelView yearWheel;
	private WheelView dayWheel;
	private OnWheelChangedListener listener;
//	String MONTHS[] = new String[] {
//			"Jan.", "Feb.", "Mar.", "Apr.", 
//			"May","Jun.", "Jul.", "Aug.", 
//			"Sep.", "Oct.","Nov.", "Dec."};
	String MONTHS[] = null;

	private Calendar selectedCalendar;
	private Calendar currentTimeDate;
	private TextView tvSelectedDate;
	private DateNumericAdapter dayAdapter;
	private Calendar savedCalendar;



	public ChooseDateDialog(Context context) {
		super( context );
		MONTHS = context.getResources().getStringArray(R.array.months);
		currentTimeDate = Calendar.getInstance();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.date_dialog);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		createViews();
	};

	private void createViews() {
		createSubViews();
		createWheels();
		
	}

	private void createSubViews() {
		tvSelectedDate = (TextView) findViewById(R.id.tvSelectedTime);
		Button btOk = (Button) findViewById(R.id.btOk);
		btOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onDateSelected(selectedCalendar);
				cancel();
			}
		});
		Button btCancel = (Button) findViewById(R.id.btCancel);
		btCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onDateSelected(savedCalendar);
				cancel();
			}
		});
	}

	abstract public void onDateSelected(Calendar selectedCalendar); 

	private void createWheels() {
		listener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView arg0, int arg1, int arg2) {
				updateOnWheelChanged();
			}
		};
		createYearWheel();
		createMonthWheel();
		createDayWheel();
	}

	private void createDayWheel() {
		dayWheel = (WheelView) findViewById(R.id.day);
		dayWheel.addChangingListener(listener);
		dayWheel.setCyclic(true);
	}

	private void createMonthWheel() {
		monthWheel = (WheelView) findViewById(R.id.month);
		int currMonth = currentTimeDate.get(Calendar.MONTH);
		DateArrayAdapter adapter = new DateArrayAdapter(getContext(), MONTHS, currMonth);
		monthWheel.setViewAdapter(adapter);
		monthWheel.addChangingListener(listener);
		monthWheel.setCyclic(true);
	}

	private void createYearWheel() {
		yearWheel = (WheelView) findViewById(R.id.year);
		int yearNum = currentTimeDate.get(Calendar.YEAR);
		DateNumericAdapter adapter = new DateNumericAdapter(getContext(), yearNum, yearNum + 10, 0 , 0);
		yearWheel.setViewAdapter(adapter);
		yearWheel.addChangingListener(listener);
		yearWheel.setCyclic(true);
	}

	public void prepare(Calendar selectedCalendar) {
		Log.i(PHS,"preparing the dialog...");
		currentTimeDate = Calendar.getInstance();
		
		if ( selectedCalendar != null)
			this.selectedCalendar = (Calendar) selectedCalendar.clone();
		else this.selectedCalendar = Calendar.getInstance();
		this.savedCalendar = (Calendar)this.selectedCalendar.clone();
		updateViews( this.selectedCalendar );
	}


	private void updateViews(Calendar selectedCalendar) {
		int yearIndex = selectedCalendar.get(Calendar.YEAR) - Calendar.getInstance().get(Calendar.YEAR);
		yearWheel.setCurrentItem(yearIndex > 0 ? yearIndex : 0);
		monthWheel.setCurrentItem(selectedCalendar.get(Calendar.MONTH));
		updateDayView(true);
	}

	private void updateOnWheelChanged() {
		updateDayView(false);
		updateCalendarFromViews();
		updateTextView();
	}

	private void updateTextView() {
		int y = selectedCalendar.get(Calendar.YEAR);
		int m = selectedCalendar.get(Calendar.MONTH);
		int d = selectedCalendar.get(Calendar.DATE);
		tvSelectedDate.setText(Helpers.formatDate(y, m, d));
	}

	private void updateCalendarFromViews() {
		selectedCalendar.set(Calendar.YEAR, yearWheel.getCurrentItem() + currentTimeDate.get(Calendar.YEAR));
		selectedCalendar.set(Calendar.MONTH, monthWheel.getCurrentItem());
		selectedCalendar.set(Calendar.DATE, dayWheel.getCurrentItem() + 1);
	}

	private void updateDayView(boolean forceUpdate) {
		Calendar tmp = Calendar.getInstance();
		int currYear = tmp.get(Calendar.YEAR);
		tmp.set(Calendar.YEAR, yearWheel.getCurrentItem() + currYear);
		tmp.set(Calendar.MONTH, monthWheel.getCurrentItem());
		int maxDays = tmp.getActualMaximum(Calendar.DAY_OF_MONTH);
		int currDay = selectedCalendar.get(Calendar.DATE);
		if ( dayAdapter == null || dayAdapter.getMaximumValue() != maxDays) {
			dayAdapter = new DateNumericAdapter(getContext(),1,maxDays,maxDays-1,Calendar.getInstance().get(Calendar.DATE)-1);    	
			dayWheel.setViewAdapter(dayAdapter);
			dayWheel.setCurrentItem(Math.min(currDay,maxDays) - 1, false);
		} else {
			if ( forceUpdate ) dayWheel.setCurrentItem(currDay - 1, false);
		}
		
	}

//	private boolean isValidDate(WheelView year, WheelView month,WheelView day) {
//		Calendar tmp = Calendar.getInstance();
//		int currYear = tmp.get(Calendar.YEAR);
//		tmp.set(Calendar.YEAR, year.getCurrentItem() + currYear);
//		tmp.set(Calendar.MONTH, month.getCurrentItem());
//		Log.i(PHS,"Year: " + tmp.get(Calendar.YEAR) + " / " + (tmp.get(Calendar.MONTH) +1)); 
//		Log.i(PHS," Maximum days: " + tmp.getActualMaximum(Calendar.DAY_OF_MONTH) + "-- dayAdapter max: " + dayAdapter.getMaximumValue());
//		if ( dayAdapter.getMaximumValue() != tmp.getActualMaximum(Calendar.DAY_OF_MONTH)) return false;
//		else return true;
//
//	}

	/**
	 * Adapter for numeric wheels. Highlights the current value.
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {

		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;
		private int maxx;

		/**
		 * Constructor
		 * @param i 
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue, int selectedValue, int currentValue) {
			super(context, minValue, maxValue);
			this.maxx = maxValue;
			this.currentValue = currentValue;
			setTextSize(FONT_SIZE);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}

		public int getMaximumValue() {
			return maxx;
		}
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(FONT_SIZE);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

}

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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import dtd.phs.sil.R;

public abstract class ChooseTimeDialog extends Dialog {

	public static final int FONT_SIZE = 22;
	private Calendar beforeChoiceCalendar;
	private Calendar curCalendar;
//	private TextView tvSelectedTime;
	private Calendar selectedCalendar;
	private WheelView hour;
	private WheelView minute;
	private OnWheelChangedListener listener;
	private Button btOk;
	private Button btCancel;

	public ChooseTimeDialog(Context context) {
		super(context);
		curCalendar = Calendar.getInstance();		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.time_dialog);
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
		setCancelable(true);
		selectedCalendar = Calendar.getInstance();
		createViews();
		
	}
	
	public void prepare(Calendar calendar) {
		curCalendar = Calendar.getInstance();
		if ( calendar != null) {
			beforeChoiceCalendar = (Calendar) calendar.clone();
		} else {
			beforeChoiceCalendar = Calendar.getInstance();
		}
		selectedCalendar = (Calendar)beforeChoiceCalendar.clone();
		updateViews(selectedCalendar);
	}

	private void updateViews(Calendar selectedCalendar) {
		int h = selectedCalendar.get(Calendar.HOUR_OF_DAY);
		int m = selectedCalendar.get(Calendar.MINUTE);
		hour.setCurrentItem(h);
		minute.setCurrentItem(m);
		updateTitle();
		
	}

//	private String filZero(int h) {
//		String s = "" + h;
//		if ( h < 10) s = "0" + h;
//		return s;
//	}
	
	private void updateTitle() {
//		tvSelectedTime.setText("Selected: " + filZero(selectedCalendar.get(Calendar.HOUR_OF_DAY))  +":"+ filZero(selectedCalendar.get(Calendar.MINUTE)));
	}


	private void createViews() {
//		tvSelectedTime = (TextView) findViewById(R.id.tvSelectedTime);
		listener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				selectedCalendar.set(Calendar.HOUR_OF_DAY, hour.getCurrentItem() );
				selectedCalendar.set(Calendar.MINUTE, minute.getCurrentItem() );
//				tvSelectedTime.post(new Runnable() {
//					@Override
//					public void run() {
//						updateTitle();
//					}
//				});
			}
		};
		
		createWheels();
		createButtons();
	}

	private void createButtons() {
		btOk = (Button) findViewById(R.id.btOk);
		btOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCalendarSelected(selectedCalendar);
				cancel();
			}
		});
		
		btCancel = (Button) findViewById(R.id.btCancel);
		btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCalendarSelected(beforeChoiceCalendar);
				cancel();
			}
		});
	}

	abstract public void onCalendarSelected(Calendar selectedCalendar);

	private void createWheels() {
		hour = (WheelView) findViewById(R.id.hourWheel);
		hour.setCyclic(true);
		hour.setViewAdapter(new DateNumericAdapter(getContext(), 0, 23, selectedCalendar.get(Calendar.HOUR_OF_DAY), curCalendar.get(Calendar.HOUR_OF_DAY)));
		hour.setCurrentItem(selectedCalendar.get(Calendar.HOUR_OF_DAY));
		hour.addChangingListener(listener);
		
		minute = (WheelView) findViewById(R.id.minuteWheel);
		minute.setCyclic(true);
		minute.setViewAdapter(new DateNumericAdapter(getContext(), 0, 59, selectedCalendar.get(Calendar.MINUTE), curCalendar.get(Calendar.MINUTE)));
		minute.setCurrentItem(selectedCalendar.get(Calendar.MINUTE));
		minute.addChangingListener(listener);
		
	}

    private class DateNumericAdapter extends NumericWheelAdapter {

		// Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        /**
         * Constructor
         * @param i 
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int selectedValue, int currentValue) {
            super(context, minValue, maxValue);
            this.currentValue = currentValue;
            setTextSize(FONT_SIZE);            
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            String s = view.getText().toString();
            if ( s.length() < 2 ) { 
            	s = "0" + s;
            	view.setText(s);
            }
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
    
    /**
     * Adapter for string based wheel. Highlights the current value.
     */
//    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
//        // Index of current item
//        int currentItem;
//        // Index of item to be highlighted
//        int currentValue;
//        
//        /**
//         * Constructor
//         */
//        public DateArrayAdapter(Context context, String[] items, int current) {
//            super(context, items);
//            this.currentValue = current;
//            setTextSize(FONT_SIZE);
//        }
//        
//        @Override
//        protected void configureTextView(TextView view) {
//            super.configureTextView(view);
//            if (currentItem == currentValue) {
//                view.setTextColor(0xFF0000F0);
//            }
//            view.setTypeface(Typeface.SANS_SERIF);
//        }
//        
//        @Override
//        public View getItem(int index, View cachedView, ViewGroup parent) {
//            currentItem = index;
//            return super.getItem(index, cachedView, parent);
//        }
//    }
	
}

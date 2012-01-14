package dtd.phs.sil.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;
import dtd.phs.sil.utils.Helpers;

public class AlarmTextView extends TextView {

	private static final int ARROW_COLOR = 0xff828282;
	private Paint arrowPaint;


	public AlarmTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		arrowPaint.setColor(ARROW_COLOR);
		arrowPaint.setStrokeWidth(Helpers.dp2px(getContext(), 2));
	}

	public AlarmTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public AlarmTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	
	
	@Override
	protected void onDraw(Canvas canvas) {	
		super.onDraw(canvas);
		int mWidth = getMeasuredWidth();
		int mHeight = getMeasuredHeight();
		int py = mHeight / 2;
		int px = mWidth - 5;
		
		int length = (int) Helpers.dp2px(getContext(),10);
		int height = (int) Helpers.dp2px(getContext(), 8);
		
		int paddingBottom = getPaddingBottom();
		int paddingTop = getPaddingTop();
		int availHeight = mHeight - (paddingBottom+paddingTop);
		if ( 2*height > availHeight ) 
			height = availHeight / 2;

		canvas.drawLine(px, py, px-length, py+height, arrowPaint);
		canvas.drawLine(px, py, px-length, py-height, arrowPaint);
	}
	
	
	

}

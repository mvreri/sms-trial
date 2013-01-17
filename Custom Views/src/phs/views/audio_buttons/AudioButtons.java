package phs.views.audio_buttons;

import com.example.android.customviews.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AudioButtons extends ViewGroup {


	public class MyButton extends ImageView {
		public MyButton(Context context) {
			super(context);
			setScaleType(ScaleType.FIT_CENTER);
		}

	}
	
	private static final float BASE_UNIT_WIDTH = 155.0f;
	private static final float BASE_UNIT_HEIGHT = 65.0f;
	
	private static final float SMALL_BG_DIAMETER = 53.0f;
	private static final float OVERLAP_WIDTH = 8.0f;
	
	private static final float DROP_SHADOW_HEIGHT = 1.0f;
	private static final int DROP_SHADOW_COLOR = 0xffFFffFF;
	
	private static final int BG_START_GRADIENT = 0xffe2e2e2;
	private static final int BG_END_GRADIENT = 0xfff9f9f9;
	
	//Buttons shadow
	private static final int BLUR_SHADOW_RADIUS = 4;
	private static final int BT_DROP_SHADOW_COLOR = 0xffCDcdCD;
	
	private static final int OFF_HAPTIC = 0;
	private static final int ON_HAPTIC = 1;

	private RectF mPrevBgBounds;
	private RectF mPlayBgBounds;
	private RectF mNextBgBounds;
	private Paint mPrevBgPaint;
	private Paint mPlayBgPaint;
	private Paint mNextBgPaint;
	private LinearGradient linearGradient;
	private ImageView mPrevButton;
	private ImageView mPlayButton;
	private ImageView mNextButton;
	private RectF mPrevBgShadow;
	private float mDropShadowHeight;
	private RectF mPlayBgShadow;
	private RectF mNextBgShadow;
	private Paint mBgDropShadowPaint;
	private RectF mPrevButtonDropShadow;
	private RectF mPlayButtonDropShadow;
	private RectF mNextButtonDropShadow;
	private Paint mShadowPaint;
	private boolean mHapticFeedback;

	public AudioButtons(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false); 
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AudioButtons);
		Drawable nextImg = null;
		Drawable prevImg = null;
		Drawable playImg = null;
		try {
			nextImg = a.getDrawable(R.styleable.AudioButtons_nextImg);
			prevImg = a.getDrawable(R.styleable.AudioButtons_previousImg);
			playImg = a.getDrawable(R.styleable.AudioButtons_playImg);
			
			int hf = a.getInteger(R.styleable.AudioButtons_hapticFeedback, OFF_HAPTIC);
			mHapticFeedback = true;
			if ( hf == OFF_HAPTIC ) mHapticFeedback = false;
			
		} finally {
			a.recycle();
		}
		mPrevButton = new MyButton(getContext());
		mPrevButton.setImageDrawable(prevImg);

		mPlayButton = new MyButton(getContext());
		mPlayButton.setImageDrawable(playImg);
		
		mNextButton = new MyButton(getContext());
		mNextButton.setImageDrawable(nextImg);
		
		addView(mPrevButton);
		addView(mNextButton);
		addView(mPlayButton);
		
	}
	
	public void setOnPreviousListener(View.OnClickListener lst) {
		//TODO: haptic feedback
		this.mPrevButton.setOnClickListener(lst);
	}
	
	public void setOnNextListener(View.OnClickListener lst) {
		//TODO: haptic feedback		
		this.mNextButton.setOnClickListener(lst);
	}
	
	public void setOnPlayListener(View.OnClickListener lst) {
		//TODO: haptic feedback
		this.mPlayButton.setOnClickListener(lst);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//do nothing, the children's layout() will be called in onSizeChanged

	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {	
		super.onSizeChanged(w, h, oldw, oldh);
		
		float hPad = getPaddingBottom() + getPaddingTop();
		float wPad = getPaddingLeft() + getPaddingRight();
		
		float maxW = w - wPad;
		float maxH = h - hPad;
		
		//The w/h ratio = 155/65 - compute the "real" width/height
		float realW = maxW;
		float realH = BASE_UNIT_HEIGHT * realW / BASE_UNIT_WIDTH;
		if ( realH > maxH ) { 
			realH = maxH;
			realW = BASE_UNIT_WIDTH * realH / BASE_UNIT_HEIGHT;
		}
		
		float metricUnit = realW / BASE_UNIT_WIDTH;
		
		//Then add more padding - ha
		float moreWPad = (maxW - realW)/2;
		float moreHPad = (maxH - realH)/2;
		
		mDropShadowHeight = convertDp2Pixel(getContext(), DROP_SHADOW_HEIGHT);
		mBgDropShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBgDropShadowPaint.setColor(DROP_SHADOW_COLOR);
		
        // Set up the paint for the button shadow
        mShadowPaint = new Paint(0);
        mShadowPaint.setColor(BT_DROP_SHADOW_COLOR);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(convertDp2Pixel(getContext(), BLUR_SHADOW_RADIUS), BlurMaskFilter.Blur.NORMAL));		
		
		float smallBgDiameter = metricUnit * SMALL_BG_DIAMETER - mDropShadowHeight;
		float bigBgDiameter = realH - mDropShadowHeight;
		linearGradient = new LinearGradient(0, 0, 0, bigBgDiameter, BG_START_GRADIENT,BG_END_GRADIENT, Shader.TileMode.CLAMP);
		float buttonPadding = OVERLAP_WIDTH / 2.0f * metricUnit;
		
		mPrevBgBounds = new RectF(0,0,smallBgDiameter,smallBgDiameter);
		mPrevBgBounds.offset(getPaddingLeft()+moreWPad, getPaddingTop() + moreHPad + (bigBgDiameter-smallBgDiameter)/2);
		mPrevBgShadow = createShadow(mPrevBgBounds);
		
		mPrevBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPrevBgPaint.setShader(linearGradient);
		mPrevButtonDropShadow = new RectF();
		callLayout(mPrevButton, mPrevBgBounds, smallBgDiameter, buttonPadding, mPrevButtonDropShadow);
		
		mPlayBgBounds = new RectF(0,0,bigBgDiameter,bigBgDiameter);
		mPlayBgBounds.offset(mPrevBgBounds.right - OVERLAP_WIDTH*metricUnit , getPaddingTop() + moreHPad);
		mPlayBgShadow = createShadow(mPlayBgBounds);
		
		mPlayBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPlayBgPaint.setShader(linearGradient);
		mPlayButtonDropShadow = new RectF();
		callLayout(mPlayButton,mPlayBgBounds,bigBgDiameter, buttonPadding, mPlayButtonDropShadow);
		
		mNextBgBounds = new RectF(0,0,smallBgDiameter,smallBgDiameter);
		mNextBgBounds.offset(mPlayBgBounds.right - OVERLAP_WIDTH*metricUnit , getPaddingTop() + moreHPad + (bigBgDiameter-smallBgDiameter)/2);
		mNextBgShadow = createShadow(mNextBgBounds);
		
		mNextBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mNextBgPaint.setShader(linearGradient);
		mNextButtonDropShadow = new RectF();
		callLayout(mNextButton, mNextBgBounds, smallBgDiameter, buttonPadding, mNextButtonDropShadow);
		
	}
	
	private RectF createShadow(RectF bounds) {
		return new RectF(bounds.left, bounds.top + mDropShadowHeight, bounds.right, bounds.bottom + mDropShadowHeight);
	}

	private void callLayout(ImageView view, RectF bgBounds, float bgDiameter, float buttonPadding, RectF dropShadow) {
		float centerX = (bgBounds.left + bgBounds.right) / 2.0f;
		float centerY = (bgBounds.top + bgBounds.bottom) / 2.0f;
		int left = (int) (centerX - (bgDiameter/2.0f) + buttonPadding);
		int right = (int) (centerX + (bgDiameter/2.0f) - buttonPadding);
		int top = (int) (centerY - (bgDiameter/2.0f) + buttonPadding);
		int bottom = (int) (centerY + (bgDiameter/2.0f) - buttonPadding);
		view.layout(left, top, right, bottom);
		dropShadow.set(left, top + mDropShadowHeight, right, bottom + mDropShadowHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawOval(mPrevBgShadow, mBgDropShadowPaint);
		canvas.drawOval(mNextBgShadow, mBgDropShadowPaint);
		canvas.drawOval(mPlayBgShadow, mBgDropShadowPaint);
		
		canvas.drawOval(mPrevBgBounds, mPrevBgPaint);
		canvas.drawOval(mNextBgBounds, mNextBgPaint);
		canvas.drawOval(mPlayBgBounds, mPlayBgPaint);
		
		canvas.drawOval(mPrevButtonDropShadow, mShadowPaint);
		canvas.drawOval(mNextButtonDropShadow, mShadowPaint);
		canvas.drawOval(mPlayButtonDropShadow, mShadowPaint);
		
		canvas.draw
	}
	
	static public float convertDp2Pixel(Context context, float dp) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
		return px;
	}


}

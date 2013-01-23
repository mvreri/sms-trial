package phs.views.vol_button;

import phs.views.ViewHelpers;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.customviews.R;

import dtd.phs.lib.utils.Logger;

public class ExperimentKnobController extends FrameLayout {




	


	private static final int COLOR_WHITE = 0xFFffFFff;
	

	//Background
	private static final int BG_GRADIENT_START = 0xffE4e4E4;
	private static final int BG_GRADIENT_END = 0xffF9f9F9;
	private static final float DEFAULT_BG_SHADOW_HEIGHT = 1.0f;	
	private static final int BG_TO_BASE_PADDING_RATIO = 12;

	//Base
	private static final float BASE_STROKE_WIDTH = 2.0f;
	private static final float BASE_DELTA_STROKE = 4.0f;
	private static final int BASE_STROKE_COLOR = 0xffCCccCC;
	private static final int BASE_GRADIENT_END = 0xffEFefEF;
	private static final int BASE_GRADIENT_START = 0xffD5d5D5;
	private static final int BASE_2_KNOB_PADDING_RATIO = 18;

	//Shadow
	private static final int SHADOW_COLOR = 0x80000000;
	//private static final float SHADOW_BLUR_RADIUS = 25.0f;
	//private static final float SHADOW_DELTA = 20.0f;

	
	private static final int MIN_ANGLE = -135;
	private static final int MAX_ANGLE = -45;

	private static final float KNOB_INDICATOR_FACTOR = 15;

	//Knob
	private static final float WHITE_SHADOW_HEIGHT = 1.0f;
	private static final int KNOB_START_COLOR = 0xffF9f9F9;
	private static final int KNOB_END_COLOR = 0xffBAbaBA;

	//Under shadow
	private static final int KNOB_UNDER_SHADOW = 0xffB6b6B6;
	private static final float UNDER_SHADOW_BLUR_RADIUS = 2.0f;
	
	private static float mPaddingBg2Base = 20.0f;
	private static float mPaddingBase2Knob = 6.0f;
	private Paint mShadowPaint;
	private GestureDetector mDetector;
	private Bitmap mVolumeIndicator;
	private RectF mBgBounds;
	private RectF mBaseBounds;
	private RectF mKnobBounds;
	private CurrentIndicator mCurrIndicator;
	private RectF mIndicatorBounds;
	private int mVolumeRotation;
	private RectF mShadowBounds;
	private int mMinAngle = MIN_ANGLE;
	private int mMaxAngle = MAX_ANGLE;
	private float mBGShadHeight;
	private RectF mBGShaBounds;
	private Paint mWhitePaint;
	private Paint mBGPaint;
	private LinearGradient mBGGradient;
	private LinearGradient mBaseGradient;
	private Paint mBasePaint;
	private float mDimUnit;
	private Paint mBaseStrokePaint;


	private RectF mKnobUpShadBounds;


	private LinearGradient mKnobRadient;
	private Paint mKnobPaint;


	private float mShadowBlurRadius;


	private float mShadowDelta;


	private Paint mKnobUnderShadow;


	private RectF mKnobUndShadBounds;

	public ExperimentKnobController(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		init();
	}

	private void init() {
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		
		mDimUnit = ViewHelpers.convertDp2Pixel(getContext(), 1.0f);

		mDetector = new GestureDetector(getContext(), new GestureListener());

		mShadowPaint = new Paint(0);
		mShadowPaint.setColor(SHADOW_COLOR);

		mVolumeIndicator = BitmapFactory.decodeResource(getResources(), R.drawable.current);
		
		//Base
		mBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBaseStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBaseStrokePaint.setStyle(Style.STROKE);
		mBaseStrokePaint.setColor(BASE_STROKE_COLOR);
		mBaseStrokePaint.setStrokeWidth(BASE_STROKE_WIDTH * mDimUnit + BASE_DELTA_STROKE );
		
		mBGShadHeight = ViewHelpers.convertDp2Pixel(getContext(), DEFAULT_BG_SHADOW_HEIGHT);
		mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mWhitePaint.setColor(COLOR_WHITE);
		mBGPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		mKnobPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mKnobUnderShadow = new Paint(0);
		mKnobUnderShadow.setColor(KNOB_UNDER_SHADOW);
		mKnobUnderShadow.setMaskFilter(new BlurMaskFilter(UNDER_SHADOW_BLUR_RADIUS, BlurMaskFilter.Blur.NORMAL));
		
		
		mCurrIndicator = new CurrentIndicator(getContext()); //TODO: layout in onSizeChanged
		
		addView(mCurrIndicator);

		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		float xpad = getPaddingLeft() + getPaddingRight();
		float ypad = getPaddingBottom() + getPaddingTop();
		float xmax = w - xpad;
		float ymax = h - ypad - mBGShadHeight;

		float bgDiameter = Math.min(xmax, ymax);
		float offsetX = getPaddingLeft() + ( xmax - bgDiameter) / 2;
		float offsetY = getPaddingTop() + ( ymax - bgDiameter) / 2;
		mBgBounds = new RectF(
				0.0f,
				0.0f,
				bgDiameter,
				bgDiameter
				);
		mBgBounds.offset(offsetX, offsetY);
		mBGGradient = new LinearGradient(0, 0, 0, mBgBounds.height(), BG_GRADIENT_START, BG_GRADIENT_END, TileMode.CLAMP);
		mBGPaint.setShader(mBGGradient);
		
		mBGShaBounds = ViewHelpers.cloneRect(mBgBounds);
		mBGShaBounds.offset(0, mBGShadHeight);

		mPaddingBg2Base = bgDiameter / BG_TO_BASE_PADDING_RATIO;
		float baseDiameter = Math.max(1.0f, bgDiameter - 2* mPaddingBg2Base);
		offsetX += mPaddingBg2Base;
		offsetY += mPaddingBg2Base;
		mBaseBounds = new RectF(
				0.0f,
				0.0f,
				baseDiameter,
				baseDiameter
				);
		mBaseBounds.offset(offsetX, offsetY);
		mBaseGradient = new LinearGradient(0, 0, 0, mBaseBounds.height(), BASE_GRADIENT_START, BASE_GRADIENT_END, TileMode.CLAMP);
		mBasePaint.setShader(mBaseGradient);

		mPaddingBase2Knob = bgDiameter / BASE_2_KNOB_PADDING_RATIO;
		float knobDiameter = Math.max(1.0f, baseDiameter - 2*mPaddingBase2Knob);
		offsetX += mPaddingBase2Knob;
		offsetY += mPaddingBase2Knob;
		mKnobBounds = new RectF(
				0.0f,
				0.0f,
				knobDiameter,
				knobDiameter
				);
		mKnobBounds.offset(offsetX, offsetY);
		mKnobRadient = new LinearGradient(0, 0, 0, mKnobBounds.height(), KNOB_START_COLOR, KNOB_END_COLOR, TileMode.CLAMP);
		mKnobPaint.setShader(mKnobRadient);
		
		mKnobUpShadBounds = ViewHelpers.cloneRect(mKnobBounds);
		mKnobUpShadBounds.offset(0, - mDimUnit * WHITE_SHADOW_HEIGHT);
		
		mKnobUndShadBounds = ViewHelpers.cloneRect(mKnobBounds);
		mKnobUndShadBounds.offset(0, mDimUnit * WHITE_SHADOW_HEIGHT);

		//Drop shadow for the Knob
		mShadowBlurRadius = mKnobBounds.height() / 15; 
		mShadowPaint.setMaskFilter(new BlurMaskFilter(mShadowBlurRadius, Blur.NORMAL));
		mShadowBounds = ViewHelpers.cloneRect(mKnobBounds);
		float shadowDelta = mDimUnit * mKnobBounds.height() / 18;
		mShadowBounds.offset(0, shadowDelta);
		
		computeIndicatorBounds(knobDiameter);
		
	}

	private void computeIndicatorBounds(float knobDiameter) {
		float mIndDiameter = knobDiameter / KNOB_INDICATOR_FACTOR;
		float indicatorRadius = mIndDiameter / 2;
		float topPadding = mIndDiameter;
		float centerX = mKnobBounds.right - topPadding - indicatorRadius;
		float centerY = mKnobBounds.top + knobDiameter/2;
		mIndicatorBounds = new RectF(
				centerX-indicatorRadius,
				centerY-indicatorRadius,
				centerX+indicatorRadius,
				centerY+indicatorRadius
				);
//		Logger.logInfo("Knob Button(l,t,r,b) = " + mKnobBounds.left + " # " + mKnobBounds.top + " # " + mKnobBounds.right + " # " + mKnobBounds.bottom);
//		Logger.logInfo("Indicator(l,t,r,b) = " + mIndicatorBounds.left + " # " + mIndicatorBounds.top + " # " + mIndicatorBounds.right + " # " + mIndicatorBounds.bottom);
		
		mCurrIndicator.layout(
				(int) mIndicatorBounds.left,
				(int) mIndicatorBounds.top,
				(int) mIndicatorBounds.right,
				(int) mIndicatorBounds.bottom);
		
		mCurrIndicator.setPivot(mIndDiameter + topPadding - knobDiameter/2, indicatorRadius); //TODO: experiment
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawOval(mBGShaBounds, mWhitePaint);
		canvas.drawOval(mBgBounds, mBGPaint);
		canvas.drawCircle((mBaseBounds.left + mBaseBounds.right)/2, (mBaseBounds.bottom + mBaseBounds.top)/2, mBaseBounds.width()/2 + BASE_STROKE_WIDTH * mDimUnit-BASE_DELTA_STROKE, mBaseStrokePaint);
		canvas.drawOval(mBaseBounds, mBasePaint);

		canvas.drawOval(mShadowBounds, mShadowPaint);
		canvas.drawOval(mKnobUpShadBounds, mWhitePaint);
		canvas.drawOval(mKnobUndShadBounds, mKnobUnderShadow);	
		canvas.drawOval(mKnobBounds, mKnobPaint);
		
	}
//
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int wspec = resolveSizeAndState(DEFAULT_LAYOUT_DIMEN, widthMeasureSpec, 1);
//		int hspec = resolveSizeAndState(DEFAULT_LAYOUT_DIMEN, heightMeasureSpec, 1);
//
//		setMeasuredDimension(wspec, hspec);
//	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = mDetector.onTouchEvent(event);

		if ( ! result ) {
			if ( event.getAction() == MotionEvent.ACTION_UP) {
				result = true;
				mCurrIndicator.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}
		}

		return result;
	}

	private class GestureListener extends GestureDetector.SimpleOnGestureListener {

		private static final float VELOCITY_DOWNSCALE = 2.5f; //TODO: set to 2.5->4.0

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float dX, float dY) {
			float scrollTheta = vectorToScalarScroll(
					dX,
					dY,
					e2.getX() - getRotationCenterX(), //TODO: to be set in onSizeChanged
					e2.getY() - getRotationCenterY());
			
			int volumeRotation = getVolumeRotation();
			int newRotation = (int) (volumeRotation + scrollTheta / VELOCITY_DOWNSCALE);
			setVolumeRotation(newRotation);
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			mCurrIndicator.setLayerType(View.LAYER_TYPE_HARDWARE, null);
			return true;
		}

		/**
		 * Helper method for translating (x,y) scroll vectors into scalar rotation of the pie.
		 *
		 * @param dx The x component of the current scroll vector.
		 * @param dy The y component of the current scroll vector.
		 * @param x  The x position of the current touch, relative to the pie center.
		 * @param y  The y position of the current touch, relative to the pie center.
		 * @return The scalar representing the change in angular position for this scroll.
		 */
		private float vectorToScalarScroll(float dx, float dy, float x, float y) {
			// get the length of the vector
			float l = (float) Math.sqrt(dx * dx + dy * dy);

			// decide if the scalar should be negative or positive by finding
			// the dot product of the vector perpendicular to (x,y). 
			float crossX = -y;
			float crossY = x;

			float dot = (crossX * dx + crossY * dy);
			float sign = Math.signum(dot);

			return l * sign;
		}
	}


	public class CurrentIndicator extends View {
		private RectF mBounds;
		private Paint mPaint;

		public CurrentIndicator(Context context) {
			super(context);
		}

		public void setPivot(float x, float y) {
			setPivotX(x);
			setPivotY(y);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			mBounds = new RectF(0,0,w,h);
			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawBitmap(mVolumeIndicator,null,mBounds,mPaint);

		}
		
		public void rotateTo(int rotation) {
			setRotation(360-rotation);
		}


	}

	public float getRotationCenterX() {
		//return mCurrIndicator.getPivotX();
		return (mKnobBounds.left + mKnobBounds.right)/2;
	}

	public float getRotationCenterY() {
		//return mCurrIndicator.getPivotY();
		return (mKnobBounds.top + mKnobBounds.bottom)/2;
	}
	public void setVolumeRotation(int rotation) {
		rotation = (rotation % 360 + 360 ) % 360;
		if ( validRotation(rotation)) {
			mVolumeRotation = rotation;
			mCurrIndicator.rotateTo(rotation);
		}
	}
	/**
	 * TODO: to be implemented
	 * This implementation only valid for min angle in III quarter & max angle in IV quarter 
	 * @param rotation
	 * @return
	 */
	private boolean validRotation(int rotation) {
		int min = (mMinAngle + 360 ) % 360;
		int max = (mMaxAngle + 360) %360;
		return !(rotation > min && rotation < max);
	}

	public int getVolumeRotation() {
		return mVolumeRotation;
	}



}

package phs.views.vol_button;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.customviews.R;

import dtd.phs.lib.utils.Logger;

public class NewKnobController extends FrameLayout {

	private static final int SHADOW_COLOR = 0x33666666;
	private static final float SHADOW_BLUR_RADIUS = 8;

	private static final int MIN_ANGLE = -135;
	private static final int MAX_ANGLE = -45;


	private static final int DEFAULT_LAYOUT_DIMEN = 512;
	private static final float KNOB_INDICATOR_FACTOR = 10;	
	private static float mPaddingBg2Base = 20.0f;
	private static float mPaddingBase2Knob = 6.0f;
	private Paint mShadowPaint;
	private GestureDetector mDetector;
	private Bitmap mKnob;
	private Bitmap mVolumeIndicator;
	private Bitmap mKnobBase;
	private Bitmap mKnobBg;
	private RectF mBgBounds;
	private RectF mBaseBounds;
	private RectF mKnobBounds;
	private CurrentIndicator mCurrIndicator;
	private RectF mIndicatorBounds;
	private int mVolumeRotation;
	private RectF mShadowBounds;
	private int mMinAngle = MIN_ANGLE;
	private int mMaxAngle = MAX_ANGLE;

	public NewKnobController(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		init();
	}

	private void init() {
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		mDetector = new GestureDetector(getContext(), new GestureListener());

		mShadowPaint = new Paint(0);
		mShadowPaint.setColor(SHADOW_COLOR);
		mShadowPaint.setMaskFilter(new BlurMaskFilter(SHADOW_BLUR_RADIUS, Blur.NORMAL));

		mKnob = BitmapFactory.decodeResource(getResources(), R.drawable.knob_button); //without current indicator
		mVolumeIndicator = BitmapFactory.decodeResource(getResources(), R.drawable.current);
		mKnobBase = BitmapFactory.decodeResource(getResources(), R.drawable.knob_base);
		mKnobBg = BitmapFactory.decodeResource(getResources(), R.drawable.knob_bg);

		mCurrIndicator = new CurrentIndicator(getContext()); //TODO: layout in onSizeChanged
		
		//		mCurrIndicator.setImageResource(R.drawable.current_indicator);
		addView(mCurrIndicator);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// do nothing, manually call layout for each children
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Logger.logInfo("onSizeChanged: w=" + w + " # h=" +h);
		float xpad = getPaddingLeft() + getPaddingRight();
		float ypad = getPaddingBottom() + getPaddingTop();

		float bgDiameter = Math.min(w-xpad, h-ypad);
		float offsetX = getPaddingLeft();
		float offsetY = getPaddingTop();
		mBgBounds = new RectF(
				0.0f,
				0.0f,
				bgDiameter,
				bgDiameter
				);
		mBgBounds.offset(offsetX, offsetY);

		mPaddingBg2Base = bgDiameter / 20;
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

		mPaddingBase2Knob = bgDiameter / 40;
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

		//		mShadowBounds = new RectF(
		//				mKnobBounds.left + 20.0f ,
		//				mKnobBounds.top + 20.0f,
		//				mKnobBounds.right - 20.0f,
		//				mKnobBounds.bottom + 40.0f
		//				);
		computeIndicatorBounds(knobDiameter);
		
	}

	private void computeIndicatorBounds(float knobDiameter) {
		float mIndDiameter = knobDiameter / KNOB_INDICATOR_FACTOR;
		float indicatorRadius = mIndDiameter / 2;
		float topPadding = mIndDiameter;
		//AT 90
//		float centerX = mKnobBounds.left + knobDiameter / 2;
//		float centerY = mKnobBounds.top + topPadding + indicatorRadius;
		//AT 180
		float centerX = mKnobBounds.right - topPadding - indicatorRadius;
		float centerY = mKnobBounds.top + knobDiameter/2;
		mIndicatorBounds = new RectF(
				centerX-indicatorRadius,
				centerY-indicatorRadius,
				centerX+indicatorRadius,
				centerY+indicatorRadius
				);
		Logger.logInfo("Knob Button(l,t,r,b) = " + mKnobBounds.left + " # " + mKnobBounds.top + " # " + mKnobBounds.right + " # " + mKnobBounds.bottom);
		Logger.logInfo("Indicator(l,t,r,b) = " + mIndicatorBounds.left + " # " + mIndicatorBounds.top + " # " + mIndicatorBounds.right + " # " + mIndicatorBounds.bottom);
		
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
		canvas.drawBitmap(mKnobBg,null,mBgBounds,null);
		canvas.drawBitmap(mKnobBase,null,mBaseBounds,null);
		//		canvas.drawOval(mShadowBounds, mShadowPaint);
		canvas.drawBitmap(mKnob,null,mKnobBounds,null);

	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int wspec = resolveSizeAndState(DEFAULT_LAYOUT_DIMEN, widthMeasureSpec, 1);
		int hspec = resolveSizeAndState(DEFAULT_LAYOUT_DIMEN, heightMeasureSpec, 1);

		setMeasuredDimension(wspec, hspec);
	}


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

	//	public class CurrentIndicator extends ImageView {
	//		public CurrentIndicator(Context context) {
	//			super(context);
	//		}
	//
	//		public void rotateTo(int rotation) {
	//			setRotation(360-rotation);
	//		}		
	//	}

	public class CurrentIndicator extends View {
		private RectF mBounds;
		private Paint mPaint;
		private LinearGradient linearGradient;
		private int mHeight;
		private int mWidth;

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
			mHeight = h;
			mWidth = w;
			
			
		}
		
		private int outerColor(double rad) {
			double measure = (rad + 1)/2;
			int aByte = (int) (0xff * measure);
			return aByte | (aByte << 8) | (aByte << 16) | (0xff << 24);
		}
		private int innerColor(double rad) {
			double measure = (rad + 1)/2;
			int aByte = (int) (0xda * measure);
			return aByte | (aByte << 8) | (aByte << 16) | (0xff << 24);

			//return 0xffdadada;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawBitmap(mVolumeIndicator,null,mBounds,mPaint);
//			double rad = (1.0* mVolumeRotation* Math.PI / 180.0);
//			double sinRotation = Math.sin(rad);
//			linearGradient = new LinearGradient(0, 0, mWidth, 0, innerColor(sinRotation), outerColor(sinRotation), Shader.TileMode.CLAMP);
//			mPaint.setShader(linearGradient);
//			canvas.drawOval(mBounds, mPaint);
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

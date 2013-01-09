package com.example.android.customviews.vol_button;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.customviews.R;

import dtd.phs.lib.utils.Logger;

/**
 * 
 * @author Pham Hung Son
 * Step 1: simple button (jpg->bmp) -> rotate the button, no limit
 * Step 2: simple button (jpg->bmp) + shadow -> rotate the button, no limit
 * Step 3: ... later
 *
 */
public class VolumeController extends ViewGroup {
	
	private static final int DEFAULT_LAYOUT_DIMEN = 768;
	private static final float SHADOW_BLUR_RADIUS = 8.0f;
	private static final int SHADOW_COLOR = 0xff363636;
	public static final int VELOCITY_DOWNSCALE = 2;
	private static final float SHADOW_DELTA = 5.0f;
	private static final int MIN_ANGLE = -135;
	private static final int MAX_ANGLE = -45;
//	private static final int DELTA = 5;
	private RectF mButtonBounds;
	private VolumeButtonView mButtonView;
	private Bitmap mButtonBmp;
	private IOnVolumeChangedListener onVolumeChangedListener;
	private GestureDetector mDetector;
	private int mButtonRotation;
	private Paint mShadowPaint;
	private RectF mShadowBounds;
	private int mMinAngle = MIN_ANGLE; //it should be parameter
	private int mMaxAngle = MAX_ANGLE; //it 
	

	public void setVolumeRotation(int rotation) {
		rotation = (rotation % 360 + 360 ) % 360;
		if ( validRotation(rotation)) {
			mButtonRotation = rotation;
			mButtonView.rotateTo(rotation);
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
		return mButtonRotation;
	}


	public VolumeController(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setWillNotDraw(false);
		Logger.logInfo("VolumeController.constructor(context,attrs) is called");
		init();
	}
	
	private void init() {
        // Force the background to software rendering because otherwise the Blur
        // filter won't work.
		setLayerToSW(this);

        // Set up the paint for the shadow
		mShadowPaint = new Paint(0);
		mShadowPaint.setColor(SHADOW_COLOR);
		mShadowPaint.setMaskFilter(new BlurMaskFilter(SHADOW_BLUR_RADIUS, Blur.NORMAL));
		
		mDetector = new GestureDetector(getContext(), new GestureListener());
		mButtonBmp = BitmapFactory.decodeResource(getResources(), R.drawable.volume_button);
		mButtonView = new VolumeButtonView(getContext());
		addView(mButtonView);
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//do nothing ?
	}
    
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Logger.logInfo("VolumeController.onSizeChanged() is called");
		float xpad = getPaddingLeft() + getPaddingRight();
		float ypad = getPaddingBottom() + getPaddingTop();
		
		float diameter = Math.min(w-xpad-3*SHADOW_DELTA, h-ypad-3*SHADOW_DELTA); 
		mButtonBounds = new RectF(
				0.0f,
				0.0f,
				diameter,
				diameter
				);
//		mButtonBounds.offsetTo(getPaddingLeft()+DELTA, getPaddingTop()+DELTA);
		mButtonBounds.offsetTo(getPaddingLeft(), getPaddingTop());
		
		// Layout the child view that actually draws the button
		mButtonView.layout(
				(int)mButtonBounds.left,
				(int)mButtonBounds.top,
				(int)mButtonBounds.right,
				(int)mButtonBounds.bottom );
//		mButtonView.setPivot(mButtonBounds.width() / 2 + DELTA, mButtonBounds.height() / 2 + DELTA);
		mButtonView.setPivot(mButtonBounds.width() / 2 , mButtonBounds.height() / 2 );
		
        mShadowBounds = new RectF(
                mButtonBounds.left + SHADOW_DELTA,
                mButtonBounds.top + SHADOW_DELTA,
                mButtonBounds.right + SHADOW_DELTA,
                mButtonBounds.bottom + SHADOW_DELTA);
        setVolumeRotation(MIN_ANGLE);        
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Logger.logInfo("VolumeController.onDraw()");
		canvas.drawOval(mShadowBounds, mShadowPaint);
	}	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Logger.logInfo("VolumeController.onMeasure() is called");
		
		int wspec = resolveSizeAndState(DEFAULT_LAYOUT_DIMEN, widthMeasureSpec, 1);
		int hspec = resolveSizeAndState(DEFAULT_LAYOUT_DIMEN, heightMeasureSpec, 1);
		
		setMeasuredDimension(wspec, hspec);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = mDetector.onTouchEvent(event);
		if (! result) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				result = true;
			}
		}
		return result;
	}
	
	public class VolumeButtonView extends View {
		private float mRotation = 0;
		public VolumeButtonView(Context context) {
			super(context);
			setLayerToHW(this);
		}
		
        public void rotateTo(float rotation) {
        	mRotation = rotation;
        	Logger.logInfo("Rotation: " + rotation);
        	setRotation(360-rotation);
		}

		public void setPivot(float x, float y) {
        	setPivotX(x);
        	setPivotY(y);
		}

		/**
         * Enable hardware acceleration (consumes memory)
         */
        public void accelerate() {
            setLayerToHW(this);
        }

        /**
         * Disable hardware acceleration (releases memory)
         */
        public void decelerate() {
            setLayerToSW(this);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
        	super.onDraw(canvas);
        	canvas.drawBitmap(mButtonBmp,null,mButtonBounds,null);
        }

	}
	
    private void setLayerToSW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void setLayerToHW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

	public void setOnChangedListener(IOnVolumeChangedListener onVolChangedList) {
		this.onVolumeChangedListener = onVolChangedList;
	}
	
	private class GestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float dX, float dY) {
            float scrollTheta = vectorToScalarScroll(
                    dX,
                    dY,
                    e2.getX() - mButtonBounds.centerX(),
                    e2.getY() - mButtonBounds.centerY());
            int volumeRotation = getVolumeRotation();
            int newRotation = volumeRotation + (int)scrollTheta / VELOCITY_DOWNSCALE;
            //Logger.logInfo("onScroll: scrollTheta = " + scrollTheta + " -- currRot: " + volumeRotation + "-- newRot: " + newRotation );
            setVolumeRotation(newRotation);
			return true;
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			//TODO: turnon the hardware acc
			return true;
		}
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
    private static float vectorToScalarScroll(float dx, float dy, float x, float y) {
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

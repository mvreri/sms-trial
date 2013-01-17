package phs.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

public class PetrButton extends ViewGroup {

	
	//Button shadow
	private static final int BLUR_SHADOW_RADIUS = 8;
	private static final float BTN_SHADOW_SHIFT_WIDTH = 0.0f;
	private static final float BTN_SHADOW_SHIFT_HEIGHT = 6.0f;
	
	
	//Base
	private static final int BASE_SHADOW_COLOR = 0xffFFffFF;
	private static final float DEFAULT_BASE_SHADOW_DIM = 1.0f; //in dpi
	private static final int BASE_START_GRADIENT = 0xffe4e4e4;
	private static final int BASE_END_GRADIENT = 0xfff9F9f9;
	private static final float DEFAULT_BASE_PADDING = 20.0f;
	
	
	private float mDropShadowHeight;
	private float mDropShadowWidth;
	
	private RectF mBaseBounds;
	private RectF mBaseShadowBounds;
	private Paint mBaseShadowPaint;
	private Paint mBasePaint;
	private LinearGradient linearGradient;
	private float mBasePadding;
	private RectF mButtonBounds;
	private View mButton;
	private RectF mBtnShadowBounds;
	private Paint mBtnShadowPaint;

	public PetrButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLayerType(LAYER_TYPE_SOFTWARE, null); //for the shadow blur
		setWillNotDraw(false);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		//baseBound
		//drop shadow of base
	
		
		//TODO: drop shadow dimension should be custom-able
		//button base drop shadow
		mDropShadowHeight = ViewHelpers.convertDp2Pixel(getContext(), DEFAULT_BASE_SHADOW_DIM);
		mDropShadowWidth = ViewHelpers.convertDp2Pixel(getContext(), DEFAULT_BASE_SHADOW_DIM);
		
		//TODO: base padding should be custom-able
		mBasePadding = ViewHelpers.convertDp2Pixel(getContext(), DEFAULT_BASE_PADDING);
	
		//add button view
		mButton = new InnerButton(getContext());
		addView(mButton);
		//button shadow
        mBtnShadowPaint = new Paint(0);
        mBtnShadowPaint.setColor(0xffCDcdCD);
        mBtnShadowPaint.setMaskFilter(new BlurMaskFilter(BLUR_SHADOW_RADIUS, BlurMaskFilter.Blur.NORMAL));
	}
	

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// nothing

	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		float hPad = getPaddingBottom() + getPaddingTop();
		float wPad = getPaddingLeft() + getPaddingRight();
		
		float maxW = w - wPad;
		float maxH = h - hPad;
		
		float baseH = maxH - mDropShadowHeight;
		float baseW = maxW - mDropShadowWidth;
		
		linearGradient = new LinearGradient(0, 0, 0, baseH, BASE_START_GRADIENT,BASE_END_GRADIENT, Shader.TileMode.CLAMP);
		
		//base
		mBaseBounds = new RectF(0,0,baseW,baseH);
		mBaseBounds.offset(getPaddingLeft(), getPaddingTop());
		mBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);		
		mBasePaint.setShader(linearGradient);
		
		//base shadow
		mBaseShadowBounds = new RectF(mBaseBounds.left,mBaseBounds.top,mBaseBounds.right,mBaseBounds.bottom);
		mBaseShadowBounds.offset(mDropShadowWidth, mDropShadowHeight);
		mBaseShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBaseShadowPaint.setColor(BASE_SHADOW_COLOR);
		
		processButton();
	}

	private void processButton() {
		//button bounds
		float btnHei = mBaseBounds.height() - 2 * mBasePadding;
		float btnWid = mBaseBounds.width() - 2 * mBasePadding;
		mButtonBounds = new RectF(0,0,btnWid,btnHei);
		mButtonBounds.offset(mBaseBounds.left + mBasePadding, mBaseBounds.top + mBasePadding);
		mButton.layout((int)mButtonBounds.left, (int)mButtonBounds.top, (int)mButtonBounds.right, (int)mButtonBounds.bottom);

		float offX = ViewHelpers.convertDp2Pixel(getContext(), BTN_SHADOW_SHIFT_WIDTH);
		float offY = ViewHelpers.convertDp2Pixel(getContext(), BTN_SHADOW_SHIFT_HEIGHT);
		mBtnShadowBounds = ViewHelpers.cloneRect(mButtonBounds);
		mBtnShadowBounds.offset(offX, offY);
	}



	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRoundRect(mBaseShadowBounds, 10.0f, 10.0f, mBaseShadowPaint);		
		canvas.drawRoundRect(mBaseBounds, 10.0f, 10.0f, mBasePaint);
		canvas.drawRoundRect(mBtnShadowBounds, 10.0f, 10.f, mBtnShadowPaint);
	}
	
	public class InnerButton extends View {
		private int mWidth;
		private int mHeight;
		private RectF mBounds;
		private Paint mBtnPaint;
		public InnerButton(Context context) {
			super(context);
			mBtnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mBtnPaint.setColor(0xFF0000FF);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			mWidth = w;
			mHeight = h;
			mBounds = new RectF(0,0,w,h);
			
		}
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawRect(mBounds, mBtnPaint);
		}
	}



}

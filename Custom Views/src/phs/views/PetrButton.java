package phs.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.customviews.R;

import dtd.phs.lib.utils.Logger;

public class PetrButton extends ViewGroup {

	//Button
	private static final int BLUR_SHADOW_RADIUS = 8;
	private static final float BTN_SHADOW_SHIFT_WIDTH = 0.0f;
	private static final float BTN_SHADOW_SHIFT_HEIGHT = 6.0f;
	private static final int SHADOW_COLOR = 0xffCDcdCD;
	private static final float DEFAULT_BTN_CORNER_RADIUS = 8.0f;

	private static final float UNDER_SHADOW_BLUR_RADIUS = 2.0f;
	private static final int COLOR_WHITE = 0xffFFffFF;
	private static final int UNDER_SHADOW_COLOR = 0xffB6B6B6;


	//Base
	private static final int BASE_SHADOW_COLOR = 0xffFFffFF;
	private static final float DEFAULT_BASE_SHADOW_DIM = 1.0f; //in dpi
	private static final int BASE_START_GRADIENT = 0xffe4e4e4;
	private static final int BASE_END_GRADIENT = 0xfff9F9f9;
	private static final float DEFAULT_BASE_PADDING = 5.0f;

	//Text
	private static final float DEFAULT_TEXT_SIZE = 16.0f;
	private static final int DEFAULT_COLOR = 0xff797979;


	private float mDropShadowHeight;
	private float mDropShadowWidth;

	private RectF mBaseBounds;
	private RectF mBaseShadowBounds;
	private Paint mBaseShadowPaint;
	private Paint mBasePaint;

	private float mBasePadding;
	private RectF mButtonBounds;
	private View mButton;
	private RectF mBtnShadowBounds;
	private Paint mBtnShadowPaint;
	private float mBtnCornerRadius;
	private Paint mBtnPaint;
	private float mShadowDim;
	private Paint mWhitePaint;
	private Paint mDropShadow;
	private RectF mOverShadowBounds;

	private LinearGradient linearGradient;
	private LinearGradient btnGradient;
	private RectF mUnderShadowBounds;
	private float mTextSize;	
	private String mText;
	private int mTextColor;


	public PetrButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLayerType(LAYER_TYPE_SOFTWARE, null); //for the shadow blur
		setWillNotDraw(false);
		parseAttributes(attrs);
		init();
	}

	private void parseAttributes(AttributeSet attrs) {
		TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PetrButton, 0, 0);
		try {
			//text
			mTextSize = a.getDimension(R.styleable.PetrButton_textSize, ViewHelpers.convertDp2Pixel(getContext(), DEFAULT_TEXT_SIZE));
			mText = a.getString(R.styleable.PetrButton_text);
			mTextColor = a.getColor(R.styleable.PetrButton_textColor, DEFAULT_COLOR);

			//base drop shadow
			mDropShadowHeight = a.getDimension(R.styleable.PetrButton_baseShadowDim, ViewHelpers.convertDp2Pixel(getContext(), DEFAULT_BASE_SHADOW_DIM));
			mDropShadowWidth = a.getDimension(R.styleable.PetrButton_baseShadowDim, ViewHelpers.convertDp2Pixel(getContext(), DEFAULT_BASE_SHADOW_DIM));

			//base padding
			mBasePadding = a.getDimension(R.styleable.PetrButton_basePadding, ViewHelpers.convertDp2Pixel(getContext(), DEFAULT_BASE_PADDING));

			//button corner radius
			mBtnCornerRadius = a.getDimension(R.styleable.PetrButton_buttonCornerRadius, ViewHelpers.convertDp2Pixel(getContext(), DEFAULT_BTN_CORNER_RADIUS));
			Logger.logInfo("mBtnCornerRadius: "  + mBtnCornerRadius);
		} finally {
			a.recycle();
		}
	}

	private void init() {

		//add button view
		mButton = new InnerButton(getContext());
		addView(mButton);

		//button shadow
		mBtnShadowPaint = new Paint(0);
		mBtnShadowPaint.setColor(SHADOW_COLOR);
		mBtnShadowPaint.setMaskFilter(new BlurMaskFilter(BLUR_SHADOW_RADIUS, BlurMaskFilter.Blur.NORMAL));

		mBtnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mShadowDim = ViewHelpers.convertDp2Pixel(getContext(), DEFAULT_BASE_SHADOW_DIM);

		mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mWhitePaint.setColor(COLOR_WHITE);

		//button mimi shadow
		mDropShadow = new Paint(0);
		mDropShadow.setColor(UNDER_SHADOW_COLOR);
		mDropShadow.setMaskFilter(new BlurMaskFilter(UNDER_SHADOW_BLUR_RADIUS, BlurMaskFilter.Blur.NORMAL));


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
		mButton.layout(
				(int)(mButtonBounds.left + mShadowDim), 
				(int)(mButtonBounds.top + mShadowDim), 
				(int)(mButtonBounds.right - mShadowDim), 
				(int)(mButtonBounds.bottom - mShadowDim));

		float offX = ViewHelpers.convertDp2Pixel(getContext(), BTN_SHADOW_SHIFT_WIDTH);
		float offY = ViewHelpers.convertDp2Pixel(getContext(), BTN_SHADOW_SHIFT_HEIGHT);
		mBtnShadowBounds = ViewHelpers.cloneRect(mButtonBounds);
		mBtnShadowBounds.offset(offX, offY);

		drawButtonShadows(btnWid, btnHei, mButtonBounds);
	}


	private void drawButtonShadows(float w, float h, RectF bounds) {
		float mHeight = h - 2 * mShadowDim;
		float mWidth = w - 2 * mShadowDim;
		RectF btnBounds = new RectF(0,0,mWidth,mHeight);
		btnBounds.offset(bounds.left, bounds.top);

		//inner drop-shadow
		mOverShadowBounds = ViewHelpers.cloneRect(btnBounds);			


		btnBounds.offset(mShadowDim, mShadowDim);
		btnGradient = new LinearGradient(0, 0, 0, btnBounds.height(), 0xffEFefEF, 0xffBAbaBA, TileMode.CLAMP);
		mBtnPaint.setShader(btnGradient);
		//			mBtnDrawable.setBounds((int)mBtnBounds.left, (int)mBtnBounds.top, (int)mBtnBounds.right, (int)mBtnBounds.bottom );


		//outer-drop-shadow
		mUnderShadowBounds = ViewHelpers.cloneRect(btnBounds);
		mUnderShadowBounds.offset(mShadowDim, mShadowDim);

	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		mButton.setOnClickListener(l);
	}



	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRoundRect(mBaseShadowBounds, mBtnCornerRadius, mBtnCornerRadius, mBaseShadowPaint);		
		canvas.drawRoundRect(mBaseBounds, mBtnCornerRadius,mBtnCornerRadius, mBasePaint);
		canvas.drawRoundRect(mBtnShadowBounds,mBtnCornerRadius,mBtnCornerRadius, mBtnShadowPaint);
		canvas.drawRoundRect(mOverShadowBounds, mBtnCornerRadius, mBtnCornerRadius, mWhitePaint);
		canvas.drawRoundRect(mUnderShadowBounds, mBtnCornerRadius, mBtnCornerRadius, mDropShadow);

	}

	public class InnerButton extends View {
		private static final int TEXT_SHADOW_COLOR = 0xfff9f9f9;
		private static final float TEXT_SHADOW_DELTA = 1.0f;
		private static final int BTN_START_COLOR = 0xffF9f9F9;
		private static final int BTN_END_COLOR = 0xffBAbaBA;

		private Paint mTextPaint;
		private int mHeight;
		private int mWidth;
		private float mTextHeight;
		private float mTextWidth;
		private float mTextX;
		private float mTextY;
		private Paint mShadowPaint;
		private float mShadowDelta;
		private RectF mBGBounds;
		private LinearGradient btnShader;
		private Paint mBGPaint;
		private boolean mBeingPressed;
		private View.OnClickListener mOnClick;
		private Paint mPressedPaint;

		public InnerButton(Context context) {
			super(context);
			mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mTextPaint.setColor(mTextColor);
			mTextPaint.setTextSize(mTextSize);


			mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mShadowPaint.setColor(TEXT_SHADOW_COLOR);
			mShadowPaint.setTextSize(mTextSize);

			mBGPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPressedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			
			mBeingPressed = false;
			mOnClick = null;
		}

		public void setOnClick(OnClickListener onClick) {
			this.mOnClick = onClick;
		}
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			mHeight = h;
			mWidth = w;

			//BG bounds
			mBGBounds = new RectF(0,0,w,h);
			btnShader = new LinearGradient(0, 0, 0, mBGBounds.height(), BTN_START_COLOR, BTN_END_COLOR, TileMode.CLAMP);
			mBGPaint.setShader(btnShader);
			int btPressStartColor = getResources().getColor(R.color.start_pressed);
			int btPressEndColor = getResources().getColor(R.color.end_pressed);
			LinearGradient btnPressedShader = new LinearGradient(0, 0, 0, mBGBounds.height(), btPressStartColor, btPressEndColor, TileMode.CLAMP);
			mPressedPaint.setShader(btnPressedShader);

			//calculate the text bounds
			Rect bounds = new Rect();
			mTextPaint.getTextBounds(mText, 0, mText.length(), bounds);
			mTextHeight = bounds.height();
			mTextWidth = mTextPaint.measureText(mText);
			mTextX = (mWidth - mTextWidth)/2;
			mTextY = (mHeight + mTextSize)/2 - 2*mShadowDim;
			mShadowDelta = TEXT_SHADOW_DELTA;
			//			Logger.logInfo("Button Height: " + mHeight + " ## Button width= " + mWidth + " ## Text width =" + mTextWidth);
			//			Logger.logInfo("TextHeight: " + mTextHeight + " text: " + mText);
			//			Logger.logInfo("mTextX = " + mTextX + " ## mTextY = " + mTextY);
		}

		@Override
		protected void onDraw(Canvas canvas) {		
			super.onDraw(canvas);
			if ( !mBeingPressed ) {
				canvas.drawRoundRect(mBGBounds, mBtnCornerRadius, mBtnCornerRadius, mBGPaint);
			} else {
				canvas.drawRoundRect(mBGBounds, mBtnCornerRadius, mBtnCornerRadius, mPressedPaint);
			}
			canvas.drawText(mText.toString(), mTextX + mShadowDelta, mTextY + mShadowDelta, mShadowPaint);
			canvas.drawText(mText.toString(), mTextX, mTextY, mTextPaint);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				mBeingPressed = true;
				invalidate();
				return true;
			case MotionEvent.ACTION_UP:
				mBeingPressed = false;
				invalidate();
				if ( mOnClick != null ) 
					this.mOnClick.onClick(this);
				return true;
			case MotionEvent.ACTION_CANCEL:
				mBeingPressed = false;
				invalidate();
				return true;
			}
			return false;
		}
	}


}

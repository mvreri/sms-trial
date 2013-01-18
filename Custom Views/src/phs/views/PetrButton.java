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
import android.view.ViewGroup;
import android.widget.ImageView;

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
	private static final float BLUR_BTN_SHADOW_RADIUS = 5.0f;
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
	private ImageView mButton;
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
		} finally {
			a.recycle();
		}
	}

	private void init() {

		//add button view
		mButton = new InnerButton(getContext());
		mButton.setImageDrawable(getContext().getResources().getDrawable(R.drawable.petr_button));
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

	//	public class InnerButton extends View {
	//		private static final float UNDER_SHADOW_BLUR_RADIUS = 2.0f;
	//		private static final int COLOR_WHITE = 0xffFFffFF;
	//		private static final float BLUR_BTN_SHADOW_RADIUS = 5.0f;
	//		private static final int UNDER_SHADOW_COLOR = 0xffB6B6B6;
	//		private float mWidth;
	//		private float mHeight;
	//		private RectF mBtnBounds;
	//		private Paint mBtnPaint;
	//		private float mShadowDim;
	//		private RectF mOverShadowBounds;
	//		private RectF mUnderShadowBounds;
	//		private Paint mWhitePaint;
	//		private Paint mDropShadow;
	//		private LinearGradient gradient;
	//		
	//		public InnerButton(Context context) {
	//			super(context);
	//			setLayerType(LAYER_TYPE_SOFTWARE, null);
	//			
	//			mBtnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	//			
	//			mShadowDim = ViewHelpers.convertDp2Pixel(context, DEFAULT_BASE_SHADOW_DIM);
	//
	//			mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	//			mWhitePaint.setColor(COLOR_WHITE);
	//			
	//	        mDropShadow = new Paint(0);
	//	        mDropShadow.setColor(UNDER_SHADOW_COLOR);
	//	        mDropShadow.setMaskFilter(new BlurMaskFilter(UNDER_SHADOW_BLUR_RADIUS, BlurMaskFilter.Blur.NORMAL));
	//	        
	//
	//		}
	//
	//		@Override
	//		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	//			super.onSizeChanged(w, h, oldw, oldh);
	//			mHeight = h - 2 * mShadowDim;
	//			mWidth = w - 2 * mShadowDim;
	//			mBtnBounds  = new RectF(0,0,mWidth,mHeight);
	//			
	//			//inner drop-shadow
	//			mOverShadowBounds = ViewHelpers.cloneRect(mBtnBounds);			
	//			
	//			
	//			mBtnBounds.offset(mShadowDim, mShadowDim);
	//			gradient = new LinearGradient(0, 0, 0, mBtnBounds.height(), 0xffEFefEF, 0xffBAbaBA, TileMode.CLAMP);
	//			mBtnPaint.setShader(gradient);
	////			mBtnDrawable.setBounds((int)mBtnBounds.left, (int)mBtnBounds.top, (int)mBtnBounds.right, (int)mBtnBounds.bottom );
	//			
	//			
	//			//outer-drop-shadow
	//			mUnderShadowBounds = ViewHelpers.cloneRect(mBtnBounds);
	//			mUnderShadowBounds.offset(mShadowDim, mShadowDim);
	//		}
	//		@Override
	//		protected void onDraw(Canvas canvas) {
	//			super.onDraw(canvas);
	//			canvas.drawRoundRect(mOverShadowBounds, mBtnCornerRadius, mBtnCornerRadius, mWhitePaint);
	//			canvas.drawRoundRect(mUnderShadowBounds, mBtnCornerRadius, mBtnCornerRadius, mDropShadow);
	//			canvas.drawRoundRect(mBtnBounds, mBtnCornerRadius, mBtnCornerRadius, mBtnPaint) ; //ef -> ba
	//			
	//		}
	//		
	//		@Override
	//		public void setOnClickListener(OnClickListener l) {
	//			super.setOnClickListener(l);
	//		}
	//	}

	public class InnerButton extends ImageView {
		private static final int TEXT_SHADOW_COLOR = 0xfff9f9f9;
		private static final float TEXT_SHADOW_DELTA = 1.0f;
		private Paint mTextPaint;
		private int mHeight;
		private int mWidth;
		private float mTextHeight;
		private float mTextWidth;
		private float mTextX;
		private float mTextY;
		private Paint mShadowPaint;
		private float mShadowDelta;

		public InnerButton(Context context) {
			super(context);
			mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mTextPaint.setColor(mTextColor);
			mTextPaint.setTextSize(mTextSize);
			
			mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mShadowPaint.setColor(TEXT_SHADOW_COLOR);
			mShadowPaint.setTextSize(mTextSize);
		}
		
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			// TODO Auto-generated method stub
			super.onSizeChanged(w, h, oldw, oldh);
			mHeight = h;
			mWidth = w;
			
			
			//calculate the text bounds
			Rect bounds = new Rect();
			mTextPaint.getTextBounds(mText, 0, mText.length(), bounds);
			mTextHeight = bounds.height();
			mTextWidth = mTextPaint.measureText(mText);
			mTextX = (mWidth - mTextWidth)/2;
			mTextY = (mHeight + mTextSize)/2;
			
//			mShadowDelta = ViewHelpers.convertDp2Pixel(getContext(), TEXT_SHADOW_X);
			mShadowDelta = TEXT_SHADOW_DELTA;
			Logger.logInfo("Button Height: " + mHeight + " ## Button width= " + mWidth + " ## Text width =" + mTextWidth);
			Logger.logInfo("TextHeight: " + mTextHeight + " text: " + mText);
			Logger.logInfo("mTextX = " + mTextX + " ## mTextY = " + mTextY);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {		
			super.onDraw(canvas);
			canvas.drawText(mText.toString(), mTextX + mShadowDelta, mTextY + mShadowDelta, mShadowPaint);
			canvas.drawText(mText.toString(), mTextX, mTextY, mTextPaint);
		}
	}


}

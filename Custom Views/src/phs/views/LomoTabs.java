package phs.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.customviews.R;

public class LomoTabs extends ViewGroup {
	private static final float DEFAULT_BTN_RADIUS = 5.0f;
	private static final float DEFAULT_XPAD = 55.0f;
	private static final float DEFAULT_YPAD = 25.0f;
	private static final float BORDER_SHAD_HEIGHT = 1.0f; //1px
	private static final int COLOR_WHITE = 0xFFffFFff;
	private static final int COLOR_GREY_SHAD = 0xFF979797;

	//Seperated line
	private static final int COLOR_SEP_LINE = COLOR_GREY_SHAD;//0xFF8F8f8F;

	private static final int START_COLOR_NOR_TAB = 0xFFefEfef;//0xFFefEFef;
	private static final int END_COLOR_NOR_TAB = 0xFFBfBfBf;//0xFF999999;
	private static final int START_COLOR_PRESSED_TAB = 0xFFd0d0d0;
	private static final int END_COLOR_PRESSED_TAB = 0xFFaFaFaF;
	private static final float DEFAULT_TOP_SPACING = 1.0f;

	//Tab drop shadow
	private static final int TAB_DROP_SHADOW_COLOR = 0x80000000;
	private static final float TAB_DROP_SHADOW_RADIUS = 5.0f;
	private static final float DROP_SHADOW_X_OFF = 0.0f; //
	private static final float DROP_SHADOW_Y_OFF = 0.0f; //
	private static final float DEFAULT_SEP_STROKE_WIDTH = 1.0f;
	private static final float EPSILON = (float) (1e-3);
	private static final long CLICK_DURATION = 1000;
	
	//Tab sides
	private static final int TAB_SIDE_LEFT = 0; 	
	private static final int TAB_SIDE_RIGHT = 1;
	
	private static final int START_COLOR_EMBOSS_TAB = 0x00cdcdcd;//0x00999999;
	private static final int END_COLOR_EMBOSS_TAB = 0x33cdcdcd;
	
	//Text
	private static final float DEFAULT_TEXT_SIZE = 14.0f;
	private static final int DEFAULT_TEXT_COLOR = 0xff949494;
	private static final int TEXT_SHADOW_COLOR = 0xfff9f9f9;
	private static final int DEFAULT_SHAD_DELTA = 1; 

	//private ShapeDrawable shapeDrawable;
	private float mXpad;
	private float mYpad;
	private ShapeDrawable mWhiteShadow;
	private Rect mWhiteBounds;
	private Rect mTabsBounds;
	private Rect mGreyShadBounds;
	private float mBorderShadHeight;
	private ShapeDrawable mGreyShadow;
	private Rect mDropShadBounds;
	private ShapeDrawable mDropShadDrawable;

	private int mSepLineX;
	private int mSepLineTop;
	private int mSepLineBottom;

	//Separated line
	private Paint mSepLinePaint;
	private float mSepStrokerWidth;
	
	//Left tab
	private Rect mLeftTabBounds;
	private Rect mLTabEmbBounds;
	private ShapeDrawable mLeftTabDrawable;
	
	private Rect mRightTabBounds;
	private ShapeDrawable mRightTabDrawable;
	private float mBtnRadius;
	
	private long mStartPressedTime;
	private long mEndPressedTime;
	private boolean mBeingPressed = false;
	private int mPressingSide;
	private LinearGradient mPressedTabShader;
	private LinearGradient mNorTabShader;
	private int mCurrentSelectedSide;
	
	private View.OnClickListener onClickLeft;
	private View.OnClickListener onClickRight;
	private ShapeDrawable mLeftEmbossDrawable;
	private LinearGradient mEmbossTabShader;
	private Rect mRTabEmbossBounds;
	private ShapeDrawable mRightEmbossDrawable;
	private String mLeftText;
	private String mRightText;
	private float mTextSize;
	private int mTextColor;
	private Paint mTextPaint;
	private Paint mTextShadPaint;
	private int mTextHeight;
	private int mLTextWidth;
	private int mRTextWidth;
	private int mLTextX;
	private int mLTextY;
	private int mRTextX;
	private int mRTextY;
	private int mTextShadDelta;
	
	public void setOnClickLeftTab(OnClickListener leftClick) {
		this.onClickLeft = leftClick;
	}
	public void setOnClickRightTab(OnClickListener rightClick) {
		this.onClickRight = rightClick;
	}
	

	public LomoTabs(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		setWillNotDraw(false);

		parseAttributes(attrs);
		init();

	}

	private void init() {
		mBeingPressed = false;
		onClickLeft = null;
		mPressingSide  = TAB_SIDE_LEFT;
		mSepLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mSepLinePaint.setColor(COLOR_SEP_LINE);
		mSepStrokerWidth = DEFAULT_SEP_STROKE_WIDTH;
		mSepLinePaint.setStrokeWidth(mSepStrokerWidth);
		
		//Text
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setColor(mTextColor);
		//mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		mTextShadPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextShadPaint.setColor(TEXT_SHADOW_COLOR);
		mTextShadDelta = DEFAULT_SHAD_DELTA;
	
	}

	private void parseAttributes(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LomoTabs);
		try {
			mXpad = a.getDimension(R.styleable.LomoTabs_xpad, DEFAULT_XPAD);
			mYpad = a.getDimension(R.styleable.LomoTabs_ypad, DEFAULT_YPAD);
			mLeftText = a.getString(R.styleable.LomoTabs_leftText);
			mRightText = a.getString(R.styleable.LomoTabs_rightText);
			mTextColor = DEFAULT_TEXT_COLOR;
		} finally {
			a.recycle();
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//nothing
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mBorderShadHeight = ViewHelpers.convertDp2Pixel(getContext(), BORDER_SHAD_HEIGHT);
		float maxTabsHeight = h - (mYpad + 2 * mBorderShadHeight);
		float maxTabsWidth = w - 2 * mXpad;

		//Corners radius
		mBtnRadius = ViewHelpers.convertDp2Pixel(getContext(), DEFAULT_BTN_RADIUS); //TODO: should pass from XML
		float[] radii = new float[] {0,0,0,0,mBtnRadius,mBtnRadius,mBtnRadius,mBtnRadius};

		//White shadow
		mWhiteBounds = new Rect(0, 0, (int)maxTabsWidth, (int)maxTabsHeight);
		mWhiteBounds.offset((int) mXpad, (int) ViewHelpers.convertDp2Pixel(getContext(), DEFAULT_TOP_SPACING));

		mWhiteShadow = new ShapeDrawable(new RoundRectShape(radii, null, radii));
		mWhiteShadow.setBounds(mWhiteBounds);

		mWhiteShadow.getPaint().setAntiAlias(true);
		mWhiteShadow.getPaint().setColor(COLOR_WHITE);

		//Tabs
		mTabsBounds = new Rect();
		mTabsBounds = ViewHelpers.cloneRect(mWhiteBounds);
		mTabsBounds.offset(0, (int) mBorderShadHeight);
		computeTabsBounds(mTabsBounds);
		
		//TextSize
		mTextSize = mTabsBounds.height() / 3;
		mTextPaint.setTextSize(mTextSize);
		mTextShadPaint.setTextSize(mTextSize);
		
		computeTextBounds();

		//		
		//		mTabsDrawable = new ShapeDrawable(new RoundRectShape(radii, null, radii));
		//		mTabsDrawable.setBounds(mTabsBounds);
		//		
		//		mTabsDrawable.getPaint().setAntiAlias(true);
		//		Shader tabShader = new LinearGradient(0, 0, 0, mTabsBounds.height(), START_COLOR_NOR_TAB, END_COLOR_NOR_TAB, TileMode.CLAMP);
		//		mTabsDrawable.getPaint().setShader(tabShader);

		//Grey shadow
		mGreyShadBounds = new Rect();
		mGreyShadBounds = ViewHelpers.cloneRect(mTabsBounds);
		mGreyShadBounds.offset(0, (int) mBorderShadHeight);
		mGreyShadow = new ShapeDrawable(new RoundRectShape(radii, null, radii));
		mGreyShadow.setBounds(mGreyShadBounds);
		mGreyShadow.getPaint().setAntiAlias(true);
		mGreyShadow.getPaint().setColor(COLOR_GREY_SHAD);

		//Drop shadow
		mDropShadBounds = ViewHelpers.cloneRect(mGreyShadBounds);
		mDropShadBounds.offset(
				(int)ViewHelpers.convertDp2Pixel(getContext(), DROP_SHADOW_X_OFF), 
				(int)ViewHelpers.convertDp2Pixel(getContext(), DROP_SHADOW_Y_OFF));
		mDropShadDrawable = new ShapeDrawable(new RoundRectShape(radii, null, radii));
		mDropShadDrawable.setBounds(mDropShadBounds);
		mDropShadDrawable.getPaint().setAntiAlias(true);
		mDropShadDrawable.getPaint().setColor(TAB_DROP_SHADOW_COLOR);
		mDropShadDrawable.getPaint().setMaskFilter(new BlurMaskFilter(TAB_DROP_SHADOW_RADIUS, BlurMaskFilter.Blur.NORMAL));

	}

	private void computeTextBounds() {
		Rect leftBounds = new Rect();
		Rect rightBounds = new Rect();
		
		
		mTextPaint.getTextBounds(mLeftText, 0, mLeftText.length() , leftBounds);
		
		mTextPaint.getTextBounds(mRightText, 0, mRightText.length() , rightBounds);

		mTextHeight = Math.max(leftBounds.height(),rightBounds.height());
		mLTextWidth = leftBounds.width();
		mLTextX = mLeftTabBounds.left + (mLeftTabBounds.width() - mLTextWidth) / 2;
		mLTextY = mLeftTabBounds.top + (mLeftTabBounds.height() + mTextHeight) / 2 - (int) (2 * mBorderShadHeight);
		mRTextWidth = rightBounds.width();
		mRTextX = mRightTabBounds.left + (mRightTabBounds.width() - mRTextWidth) /2;
		mRTextY = mRightTabBounds.top  + (mRightTabBounds.height() + mTextHeight) / 2 - (int) (2 * mBorderShadHeight);

		
	}
	
	private void computeTabsBounds(Rect mTabsBounds) {
		
		final int EMBOSS_RATIO = 7;
		
		//Seperated line:
		mSepLineX = (mTabsBounds.left + mTabsBounds.right) / 2;
		mSepLineTop = mTabsBounds.top;
		mSepLineBottom = mTabsBounds.bottom;

		//Left tab
		mLeftTabBounds = new Rect(
				mTabsBounds.left,
				mTabsBounds.top,
				mSepLineX,
				mTabsBounds.bottom);
		mLTabEmbBounds = new Rect(
				mLeftTabBounds.left,
				mLeftTabBounds.bottom - (mLeftTabBounds.height() / EMBOSS_RATIO),
				mLeftTabBounds.right,
				mLeftTabBounds.bottom
				);

		float[] leftRadii = new float[] {0,0,0,0,0,0,mBtnRadius,mBtnRadius};
		
		mLeftTabDrawable = new ShapeDrawable(new RoundRectShape(leftRadii, null, leftRadii));
		mLeftTabDrawable.getPaint().setAntiAlias(true);
		mPressedTabShader = new LinearGradient(0, 0, 0, mTabsBounds.height(), START_COLOR_PRESSED_TAB, END_COLOR_PRESSED_TAB, TileMode.CLAMP);
		mLeftTabDrawable.getPaint().setShader(mPressedTabShader);
		mLeftTabDrawable.setBounds(mLeftTabBounds);

		mLeftEmbossDrawable = new ShapeDrawable(new RoundRectShape(leftRadii, null, leftRadii));
		mLeftEmbossDrawable.getPaint().setAntiAlias(true);
		mEmbossTabShader = new LinearGradient(0, 0, 0, mLTabEmbBounds.height(), START_COLOR_EMBOSS_TAB, END_COLOR_EMBOSS_TAB, TileMode.CLAMP);
		mLeftEmbossDrawable.getPaint().setShader(mEmbossTabShader);
		mLeftEmbossDrawable.setBounds(mLTabEmbBounds);


		//Right bounds
		mRightTabBounds = new Rect(
				(int) (mSepLineX + mSepStrokerWidth),
				this.mTabsBounds.top,
				mTabsBounds.right,
				mTabsBounds.bottom
				);
		
		mRTabEmbossBounds = new Rect(
				mRightTabBounds.left,
				mRightTabBounds.bottom - (mRightTabBounds.height() / EMBOSS_RATIO),
				mRightTabBounds.right,
				mRightTabBounds.bottom
				);
		float[] rightRadii = new float[] {0,0,0,0,mBtnRadius,mBtnRadius,0,0};

		mRightTabDrawable = new ShapeDrawable(new RoundRectShape(rightRadii, null, rightRadii));
		mRightTabDrawable.getPaint().setAntiAlias(true);
		mNorTabShader = new LinearGradient(0, 0, 0, mTabsBounds.height(), START_COLOR_NOR_TAB, END_COLOR_NOR_TAB, TileMode.CLAMP);
		mRightTabDrawable.getPaint().setShader(mNorTabShader);
		mRightTabDrawable.setBounds(mRightTabBounds);
		
		mRightEmbossDrawable = new ShapeDrawable(new RoundRectShape(rightRadii, null, rightRadii));
		mRightEmbossDrawable.getPaint().setAntiAlias(true);
		mRightEmbossDrawable.getPaint().setShader(mEmbossTabShader);
		mRightEmbossDrawable.setBounds(mLTabEmbBounds);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mDropShadDrawable.draw(canvas);
		mGreyShadow.draw(canvas);
		mWhiteShadow.draw(canvas);

		
		if ( mPressingSide == TAB_SIDE_LEFT ) {
			mLeftTabDrawable.getPaint().setShader(mPressedTabShader);
			mRightTabDrawable.getPaint().setShader(mNorTabShader);
		} else {
			mRightTabDrawable.getPaint().setShader(mPressedTabShader);
			mLeftTabDrawable.getPaint().setShader(mNorTabShader);
		}
		mLeftTabDrawable.draw(canvas);
		mLeftEmbossDrawable.draw(canvas);
		mRightTabDrawable.draw(canvas);
		mRightEmbossDrawable.draw(canvas);
		canvas.drawLine(mSepLineX, mSepLineTop, mSepLineX, mSepLineBottom, mSepLinePaint);
		
		
		//Text
		canvas.drawText(mLeftText, mLTextX + mTextShadDelta, mLTextY + mTextShadDelta, mTextShadPaint);
		canvas.drawText(mLeftText, mLTextX, mLTextY, mTextPaint);
		canvas.drawText(mRightText, mRTextX + mTextShadDelta, mRTextY + mTextShadDelta, mTextShadPaint);
		canvas.drawText(mRightText, mRTextX, mRTextY, mTextPaint);
		
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if ( insideBounds(event,mTabsBounds) ) {
				return handleDownAction(event);
			}
			else return false; 
		case MotionEvent.ACTION_UP:
			return handleUpAction(event);
		case MotionEvent.ACTION_CANCEL:
			return handleCancelAction(event);
		default:
			return false;
		}

	}

	private boolean handleCancelAction(MotionEvent event) {
		return handleUpAction(event); //may be handled later
	}

	private boolean handleUpAction(MotionEvent event) {
		mEndPressedTime = System.currentTimeMillis();
		if ( mEndPressedTime - mStartPressedTime <= CLICK_DURATION ) {
			selectSide(mPressingSide);
		} else {
			mPressingSide = mCurrentSelectedSide; //back to previous selected 
		}
		mBeingPressed = false;
		invalidate();
		return true;
	}

	private void selectSide(int mPressedSide) {
		mCurrentSelectedSide = mPressedSide;
		if ( mPressedSide == TAB_SIDE_LEFT && onClickLeft != null ) {
			onClickLeft.onClick(this);
		}
		if ( mPressedSide == TAB_SIDE_RIGHT && onClickRight != null ) {
			onClickRight.onClick(this);
		}
	}

	private boolean handleDownAction(MotionEvent event) {
		mStartPressedTime = System.currentTimeMillis();
		mBeingPressed = true;
		mPressingSide = getTabSide(event);
		if ( mPressingSide == TAB_SIDE_LEFT ) pressOnLeft();
		else pressOnRight();

		return true;
	}

	private void pressOnRight() {
		invalidate();
	}

	private void pressOnLeft() {
		invalidate();
	}

	private int getTabSide(MotionEvent event) {
		if ( insideBounds(event,mLeftTabBounds) ) return TAB_SIDE_LEFT;
		else return TAB_SIDE_RIGHT;
	}

	private boolean insideBounds(MotionEvent event, Rect bounds) {
		float x = event.getX(0);
		float y = event.getY(0);
		if ( beetween (x,bounds.left, bounds.right ) && beetween(y,bounds.top,bounds.bottom) ) return true;
		return false;
	}

	private boolean beetween(float x, float left, float right) {
		if ( Math.abs(x-left) < EPSILON ) return true;
		if ( Math.abs(x-right) < EPSILON ) return true;
		return ((double) (x-left)) * ((double)(x-right)) <= 0;
	}


}

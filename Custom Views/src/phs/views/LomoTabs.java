package phs.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.customviews.R;

import dtd.phs.lib.utils.Logger;

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
	private static final float DEFAULT_TOP_SPACING = 3.0f;

	//Tab drop shadow
	private static final int TAB_DROP_SHADOW_COLOR = 0x80000000;
	private static final float TAB_DROP_SHADOW_RADIUS = 5.0f;
	private static final float DROP_SHADOW_X_OFF = 0.0f; //
	private static final float DROP_SHADOW_Y_OFF = 0.0f; //
	private static final float DEFAULT_SEP_STROKE_WIDTH = 1.0f;
	private static final float EPSILON = (float) (1e-3);
	private static final long CLICK_DURATION = 2000;
	
	//Tab sides
	private static final int TAB_SIDE_LEFT = 0;
	private static final int TAB_SIDE_RIGHT = 1;

	//private ShapeDrawable shapeDrawable;
	private float mXpad;
	private float mYpad;
	private ShapeDrawable mWhiteShadow;
	private Rect mWhiteBounds;
	private Rect mTabsBounds;
	private Rect mGreyShadBounds;
	private float mBorderShadHeight;
	private ShapeDrawable mGreyShadow;
	private ShapeDrawable mTabsDrawable;
	private Rect mDropShadBounds;
	private ShapeDrawable mDropShadDrawable;

	private int mSepLineX;
	private int mSepLineTop;
	private int mSepLineBottom;

	private Paint mSepLinePaint;
	private float mSepStrokerWidth;
	private Rect mLeftTabBounds;
	private Rect mRightTabBounds;
	private ShapeDrawable mLeftTabDrawable;
	private float mBtnRadius;
	private ShapeDrawable mRightTabDrawable;
	private long mStartPressedTime;
	private long mEndPressedTime;
	private boolean mBeingPressed = false;
	private int mPressingSide;
	private LinearGradient mPressedTabShader;
	private LinearGradient mNorTabShader;
	private int mCurrentSelectedSide;

	public LomoTabs(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		setWillNotDraw(false);

		parseAttributes(attrs);
		init();

	}

	private void init() {
		mBeingPressed = false;
		mPressingSide  = TAB_SIDE_LEFT;
		mSepLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mSepLinePaint.setColor(COLOR_SEP_LINE);
		mSepStrokerWidth = DEFAULT_SEP_STROKE_WIDTH;
		mSepLinePaint.setStrokeWidth(mSepStrokerWidth);
	
	}

	private void parseAttributes(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LomoTabs);
		try {
			mXpad = a.getDimension(R.styleable.LomoTabs_xpad, DEFAULT_XPAD);
			mYpad = a.getDimension(R.styleable.LomoTabs_ypad, DEFAULT_YPAD);
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
		//		float xpad = getPaddingLeft() + getPaddingBottom();
		//		float ypad = getPaddingBottom() + getPaddingTop();
		//		
		//		float radius = ViewHelpers.convertDp2Pixel(getContext(), 15.0f);
		//		float[] outerRadii = new float[] {radius,radius,0,0,0,0,0,0};
		//		Shape shape = new RoundRectShape(outerRadii, null, outerRadii);
		//		shapeDrawable = new ShapeDrawable(shape);
		//		
		//		shapeDrawable.setBounds(0,0,w,h);
		//		LinearGradient shader = new LinearGradient(0, 0, 0, h, 0x00000000, 0xFFffFFff, TileMode.CLAMP);
		//		shapeDrawable.getPaint().setShader(shader);
		//		shapeDrawable.getPaint().setAntiAlias(true);
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

	private void computeTabsBounds(Rect mTabsBounds) {
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

		float[] leftRadii = new float[] {0,0,0,0,0,0,mBtnRadius,mBtnRadius};
		mLeftTabDrawable = new ShapeDrawable(new RoundRectShape(leftRadii, null, leftRadii));
		mLeftTabDrawable.getPaint().setAntiAlias(true);
		mPressedTabShader = new LinearGradient(0, 0, 0, mTabsBounds.height(), START_COLOR_PRESSED_TAB, END_COLOR_PRESSED_TAB, TileMode.CLAMP);
		mLeftTabDrawable.getPaint().setShader(mPressedTabShader);
		mLeftTabDrawable.setBounds(mLeftTabBounds);


		//Right bounds
		mRightTabBounds = new Rect(
				(int) (mSepLineX + mSepStrokerWidth),
				this.mTabsBounds.top,
				mTabsBounds.right,
				mTabsBounds.bottom
				);
		float[] rightRadii = new float[] {0,0,0,0,mBtnRadius,mBtnRadius,0,0};
		mRightTabDrawable = new ShapeDrawable(new RoundRectShape(rightRadii, null, rightRadii));
		mRightTabDrawable.getPaint().setAntiAlias(true);

		mNorTabShader = new LinearGradient(0, 0, 0, mTabsBounds.height(), START_COLOR_NOR_TAB, END_COLOR_NOR_TAB, TileMode.CLAMP);
		mRightTabDrawable.getPaint().setShader(mNorTabShader);
		mRightTabDrawable.setBounds(mRightTabBounds);
		//mRightTabDrawable.getPaint().setAlpha(180);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		mDropShadDrawable.draw(canvas);
		mGreyShadow.draw(canvas);
		mWhiteShadow.draw(canvas);
		//mTabsDrawable.draw(canvas);
		
		if ( mPressingSide == TAB_SIDE_LEFT ) {
			mLeftTabDrawable.getPaint().setShader(mPressedTabShader);
			mRightTabDrawable.getPaint().setShader(mNorTabShader);
		} else {
			mRightTabDrawable.getPaint().setShader(mPressedTabShader);
			mLeftTabDrawable.getPaint().setShader(mNorTabShader);
		}
		mLeftTabDrawable.draw(canvas);
		mRightTabDrawable.draw(canvas);
		canvas.drawLine(mSepLineX, mSepLineTop, mSepLineX, mSepLineBottom, mSepLinePaint);
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
			if ( insideBounds(event,mTabsBounds) ) {
				return handleUpAction(event);
			} else return false;
		case MotionEvent.ACTION_CANCEL:
			if ( insideBounds(event,mTabsBounds) ) {
				return handleCancelAction(event);
			} else return false;
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
		float x = event.getX(0);
		float y = event.getY(0);
		if ( insideBounds(event,mLeftTabBounds) ) return TAB_SIDE_LEFT;
		else return TAB_SIDE_RIGHT;
	}

	private boolean insideBounds(MotionEvent event, Rect bounds) {
		float x = event.getX(0);
		float y = event.getY(0);
		Logger.logInfo("Touch: (x,y) = " + x + " ### " + y);
		if ( beetween (x,bounds.left, bounds.right ) && beetween(y,bounds.top,bounds.bottom) ) return true;
		return false;
	}

	private boolean beetween(float x, float left, float right) {
		if ( Math.abs(x-left) < EPSILON ) return true;
		if ( Math.abs(x-right) < EPSILON ) return true;
		return ((double) (x-left)) * ((double)(x-right)) <= 0;
	}


}

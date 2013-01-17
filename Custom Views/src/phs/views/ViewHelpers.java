package phs.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.util.TypedValue;

public class ViewHelpers {
	static public float convertDp2Pixel(Context context, float dp) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
		return px;
	}
	
	public static RectF cloneRect(RectF bounds) {
		return new RectF(bounds.left, bounds.top, bounds.right, bounds.bottom);
	}

}

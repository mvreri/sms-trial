package hdcenter.vn.ui;

import hdcenter.vn.R;
import hdcenter.vn.utils.Helpers;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 
 * @author hungson175
 *	This class provides only the methods to hide/show views - nothing more
 */
public class ListFooter
extends FrameLayout 
{

	private static final int LOAD_MORE_FRAME = 0;
	private static final int WAITING_FRAME = 1;
	private FrameLayout mainFrames;

	public ListFooter(Context context) {
		super(context);
		Helpers.inflate(getContext(), R.layout.footer, this);
		mainFrames = (FrameLayout) findViewById(R.id.main_frames);
	}

	private void showOnlyView(int id) {
		for(int i = 0 ; i < mainFrames.getChildCount() ; i++) {
			View child = mainFrames.getChildAt(i);
			if ( i == id)
				child.setVisibility(View.VISIBLE);
			else child.setVisibility(View.INVISIBLE);
		}
	}
	
	public void showLoadMore() {
		showOnlyView(LOAD_MORE_FRAME);
	}
	
	public void showWaiting() {
		showOnlyView(WAITING_FRAME);
	}


	public void disable() {
		this.setVisibility(View.GONE);
	}
	
	public void enable() {
		this.setVisibility(View.VISIBLE);
	}

}
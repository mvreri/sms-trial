package hdcenter.vn.ui;

import hdcenter.vn.R;
import hdcenter.vn.utils.Helpers;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

/**
 * 
 * @author hungson175
 *	This class provides only the methods to hide/show views - nothing more
 */
public abstract class MoviesListFooter
extends FrameLayout 
implements OnClickListener 
{

	private static final int LOAD_MORE_FRAME = 0;
	private static final int WAITING_FRAME = 1;
	private View loadmore;

	public MoviesListFooter(Context context) {
		super(context);
		Helpers.inflate(getContext(), R.layout.footer, this);
		bindViews();
	}

	private void bindViews() {
		loadmore = this.findViewById(R.id.loadmore);
		loadmore.setOnClickListener(this);
	}


	private void showOnlyView(int id) {
		for(int i = 0 ; i < this.getChildCount() ; i++) {
			if ( i == id)
				this.getChildAt(i).setVisibility(View.VISIBLE);
			else this.getChildAt(i).setVisibility(View.INVISIBLE);
		}
	}
	
	public void showLoadMore() {
		showOnlyView(LOAD_MORE_FRAME);
	}
	
	public void showWaiting() {
		showOnlyView(WAITING_FRAME);
	}

	@Override
	public void onClick(View v) {
		onLoadMoreClick();
	}

	abstract public void onLoadMoreClick();

	public void disable() {
		this.setVisibility(View.GONE);
	}

}
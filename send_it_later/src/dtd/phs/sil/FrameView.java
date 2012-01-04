package dtd.phs.sil;

import android.content.Context;
import android.os.Handler;
import android.widget.LinearLayout;

public abstract class FrameView extends LinearLayout {

	protected Handler handler;

	public FrameView(Context context, Handler handler) {
		super(context);
		this.handler = handler;
		onCreate(context);
	}

	abstract void onCreate(Context context);

}

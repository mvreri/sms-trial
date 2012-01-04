package dtd.phs.sil;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.LinearLayout;

public abstract class FrameView extends LinearLayout {

	protected Handler handler;
	protected Activity hostedActivity;
	
	public FrameView(Activity activity, Handler handler) {
		super(activity.getApplicationContext());
		this.handler = handler;
		this.hostedActivity = activity;
		onCreate(activity.getApplicationContext());
	}

	abstract void onCreate(Context context);

	abstract public void onDisplayed();

}

package dtd.phs.sil;

import android.app.Activity;
import android.app.Dialog;
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
	
	public void showDialog(int id) {
		hostedActivity.showDialog(id);
	}

	abstract void onCreate(Context context);

	abstract public void onDisplayed();

	abstract public void onResume();
	
	abstract public Dialog onCreateDialog(int id);

}

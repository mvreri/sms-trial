package dtd.phs.lib.ui.frames_host;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.LinearLayout;

public abstract class FrameView extends LinearLayout {

	private Activity hostedActivity;
	
	public FrameView(Activity activity) {
		super(activity.getApplicationContext());
		this.hostedActivity = activity;
		onCreate(activity.getApplicationContext());
	}
	
	public void showDialog(int id) {
		hostedActivity.showDialog(id);
	}
	
	public Activity getHostedActivity() {
		return hostedActivity;
	}

	abstract public void onCreate(Context context);
	abstract public void onResume();
	abstract public void onPause();
	
	abstract public Dialog onCreateDialog(int id);
	abstract public void onPrepareDialog(int id);

	abstract public void onRefresh();

	

}

package hdcenter.vn;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import dtd.phs.lib.ui.frames_host.FrameView;

public class CinemaFrame extends FrameView {

	public CinemaFrame(Activity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.cinema, this);
		createViews();
	}

	private void createViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrepareDialog(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

}

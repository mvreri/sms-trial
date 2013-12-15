package dtd.phs.lib.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import dtd.phs.lib.utils.Logger;

public class PHS_DialogFragment extends DialogFragment {

	protected View rootView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, 0);
		setCancelable(true);

	}	

	protected void forceCancel() {
		try { this.dismiss(); } catch (Exception e) {}
	}

	protected View findViewById(int id) {
		return rootView.findViewById(id);
	}

	@Override
	public void show(FragmentManager manager, String tag) {
		try {
			super.show(manager, tag);
		} catch (IllegalStateException e) {
			Logger.logError(e);
		}
	}

}

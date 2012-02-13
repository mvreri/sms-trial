package dtd.phs.sil;

import android.app.Application;
import dtd.phs.sil.ui.AlertHelpers;
import dtd.phs.sil.utils.FrequencyHelpers;

public class SIL_App extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		FrequencyHelpers.initFrequencies(getApplicationContext());
		AlertHelpers.initAlert(getApplicationContext());
	}
}

package dtd.phs.sil.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import dtd.phs.sil.R;

public class SIL_Preferences extends PreferenceActivity {

	protected static final int DIALOG_ABOUT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		Preference aboutPref = findPreference("ABOUT");
		aboutPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				showDialog(DIALOG_ABOUT);
				return true;
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ABOUT:
			return new AboutDialog(this);
		}
		return super.onCreateDialog(id);
	}
}

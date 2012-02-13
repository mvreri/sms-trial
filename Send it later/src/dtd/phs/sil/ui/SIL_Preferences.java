package dtd.phs.sil.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import dtd.phs.sil.R;
import dtd.phs.sil.utils.Helpers;
import dtd.phs.sil.utils.PreferenceHelpers;

public class SIL_Preferences extends PreferenceActivity {

	protected static final int DIALOG_ABOUT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		Preference ratePref = findPreference("RATE_APP");
		ratePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				PreferenceHelpers.markOnRateLinkClicked(getApplicationContext());
				Helpers.gotoMarket(SIL_Preferences.this);
				return true;
			}
		});
		
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

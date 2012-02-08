package dtd.phs.sil.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import dtd.phs.sil.R;

public class OptionsMenu {

	private Activity activity;

	public OptionsMenu(Activity activity) {
		this.activity = activity;
	}

	public void createOptionsMenu(Menu menu) {
		MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
	}

	public boolean onItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_preferences:
			Intent i = new Intent(activity, SIL_Preferences.class);
			activity.startActivity(i);
			return true;
		case R.id.menu_exit:
			activity.finish();
			return true;
		default: return false;
		}
	}

}

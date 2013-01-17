package phs.views.vol_button;

import android.app.Activity;
import android.os.Bundle;

import com.example.android.customviews.R;

import dtd.phs.lib.utils.Helpers;

public class TestVolume extends Activity {
	private VolumeController volumeController;
	private IOnVolumeChangedListener onVolChangedList = new IOnVolumeChangedListener() {
		
		@Override
		public void onVolumeChanged(int oldLevel, int newLevel) {
			Helpers.showToast(TestVolume.this, "Change volume from: " + oldLevel + " to " + newLevel);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_volume);
		volumeController = (VolumeController) findViewById(R.id.vol_contr);
		volumeController.setOnChangedListener(onVolChangedList);		
	}
}

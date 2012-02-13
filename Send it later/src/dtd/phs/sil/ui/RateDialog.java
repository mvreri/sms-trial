package dtd.phs.sil.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import dtd.phs.sil.R;
import dtd.phs.sil.utils.Helpers;
import dtd.phs.sil.utils.PreferenceHelpers;

public class RateDialog extends Dialog {

	
	protected Activity activity;

	public RateDialog(Activity mainActivity) {
		super(mainActivity);
		this.activity = mainActivity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rate_dialog);
		setCancelable(true);

		Button btClose = (Button) findViewById(R.id.btClose);
		btClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		
		Button btRate = (Button) findViewById(R.id.btRate);
		btRate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Helpers.gotoMarket(activity);		
				PreferenceHelpers.markOnRateLinkClicked(getContext());
				cancel();
			}

		});
	}

}

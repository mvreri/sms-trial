package dtd.phs.sil.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import dtd.phs.sil.R;

public class AboutDialog extends Dialog {

	private TextView tvAppVersion;
	private TextView tvEmail;
	private TextView tvCodeHome;

	public AboutDialog(Context context) {
		super(context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_dialog);
		setCancelable(true);
		
		Button btClose = (Button) findViewById(R.id.btClose);
		btClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		
		tvAppVersion = (TextView) findViewById(R.id.tvAppVersion);
		String versionName = "1.70";
		try {
			versionName = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		};
		String textVersion = tvAppVersion.getText().toString() +" "+ versionName;
		tvAppVersion.setText(textVersion);
		
		tvCodeHome = (TextView) findViewById(R.id.tvCodeHome);
		tvCodeHome.setText(Html.fromHtml(getContext().getString(R.string.google_code)));
		tvCodeHome.setMovementMethod(LinkMovementMethod.getInstance());
		
		tvEmail = (TextView) findViewById(R.id.tvEmail);
		tvEmail.setText(Html.fromHtml(getContext().getString(R.string.or_send_email)));
		tvEmail.setMovementMethod(LinkMovementMethod.getInstance());
		
	}
	
}

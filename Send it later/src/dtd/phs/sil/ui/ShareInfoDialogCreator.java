package dtd.phs.sil.ui;

import android.app.Dialog;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import dtd.phs.sil.R;

public class ShareInfoDialogCreator {

	private TextView tvTitle;
	private TextView tvCall;
	private TextView tvSMS;
	private TextView tvRemove;

//	public TextView getTvTitle() {
//		return tvTitle;
//	}
//
//	public TextView getTvShareSMS() {
//		return tvShareSMS;
//	}
//
//	public TextView getTvShareEmail() {
//		return tvShareEmail;
//	}

	public ShareInfoDialogCreator(Dialog dialog) {
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.options_on_click_dialog);
		tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
		tvCall = (TextView) dialog.findViewById(R.id.tvCall);
		tvSMS = (TextView) dialog.findViewById(R.id.tvSMS);
		tvRemove = (TextView) dialog.findViewById(R.id.tvRemove);
		tvShareEmail.setText(R.string.share_email);
		dialog.setCancelable(true);
	}

	public void setOnShareSMSClickListener(OnClickListener onClickListener) {
		this.tvShareSMS.setOnClickListener(onClickListener);
	}

	public void setOnShareEmailClickListener(OnClickListener onClickListener) {
		this.tvShareEmail.setOnClickListener(onClickListener);		
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

}

package com.vsm.radio18.ui;

import java.lang.ref.WeakReference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vsm.radio18.R;
import com.vsm.radio18.ui.DialogWarningSMS.IDialogSMSListener;

import dtd.phs.lib.ui.PHS_DialogFragment;

public class DialogWarningSMS extends PHS_DialogFragment {
	public interface IDialogSMSListener {
		void onClosed();
		void onAccepted();
	}

	private Button btNo;
	private Button btYes;
	private WeakReference<IDialogSMSListener> wlistener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.dialog_2_buttons, container, false);
		btNo = (Button) findViewById(R.id.btLeft);
		btYes = (Button) findViewById(R.id.btRight);
		((TextView) findViewById(R.id.tvTitle)).setText(R.string.SMS_fee);
		((TextView) findViewById(R.id.tvMessage)).setText(R.string.SMS_Warning);
		
		btYes.setText(R.string.Yes);
		btNo.setText(R.string.No);
		
		
		btNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( wlistener != null ) {
					IDialogSMSListener l = wlistener.get();
					if ( l != null )
						l.onClosed();
				}
				forceCancel();
			}
		});
		
		btYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( wlistener != null ) {
					IDialogSMSListener listener = wlistener.get();
					if ( listener != null ) 
						listener.onAccepted();
				}
				forceCancel();
			}
		});
		return rootView;
	}

	public static DialogWarningSMS getInstance(IDialogSMSListener dlistener) {
		DialogWarningSMS dialog = new DialogWarningSMS();
		dialog.wlistener = new WeakReference<DialogWarningSMS.IDialogSMSListener>(dlistener);
		return dialog;
	}
}

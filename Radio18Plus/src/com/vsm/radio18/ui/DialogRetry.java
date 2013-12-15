package com.vsm.radio18.ui;

import java.lang.ref.WeakReference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vsm.radio18.R;
import com.vsm.radio18.ui.DialogRetry.IDialogRetryListener;

import dtd.phs.lib.ui.PHS_DialogFragment;

public class DialogRetry extends PHS_DialogFragment {
	public interface IDialogRetryListener {
		public void onClosed();
		public void onRetryClick();
	}

	private Button btRetry;
	private Button btClose;
	protected WeakReference<IDialogRetryListener> wlist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.dialog_retry_buy_item, container, false);
		btRetry = (Button) findViewById(R.id.btRetry);
		btClose = (Button) findViewById(R.id.btClose);
		
		btClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				forceCancel();
				if ( wlist != null && wlist.get() != null ) {
					wlist.get().onClosed();
				}
			}
		});
		
		btRetry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				forceCancel();
				if ( wlist != null && wlist.get() != null ) {
					wlist.get().onRetryClick();
				}
			}
		});
		return rootView;
	}

	public static DialogRetry getInstance(IDialogRetryListener dlistener) {
		DialogRetry dialog = new DialogRetry();
		dialog.wlist = new WeakReference<DialogRetry.IDialogRetryListener>(dlistener);
		return dialog;
	}
}

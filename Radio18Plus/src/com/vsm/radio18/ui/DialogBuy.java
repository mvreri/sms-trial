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
import com.vsm.radio18.RadioConfiguration;
import com.vsm.radio18.ui.DialogBuy.IDialogBuyListener;

import dtd.phs.lib.ui.PHS_DialogFragment;

public class DialogBuy extends PHS_DialogFragment {
	public interface IDialogBuyListener {

		void onClosed();

		void onAccepted();

	}

	private Button btYes;
	private Button btNo;
	protected WeakReference<IDialogBuyListener> wlist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.dialog_2_buttons, container, false);
		
		((TextView) findViewById(R.id.tvTitle)).setText(R.string.Mua);
		String content = getString(R.string.Price) + ": " + RadioConfiguration.ITEM_PRICE + " - " + getString(R.string.Do_u_want_to_buy_it);
		((TextView) findViewById(R.id.tvMessage)).setText(content);
		
		btNo = (Button) findViewById(R.id.btLeft);
		btYes = (Button) findViewById(R.id.btRight);
		btNo.setText(R.string.No);
		btYes.setText(R.string.Yes);
		
		btNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				forceCancel();
				if ( wlist != null && wlist.get() != null ) {
					wlist.get().onClosed();
				}
			}
		});
		
		btYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				forceCancel();
				if ( wlist != null && wlist.get() != null ) {
					wlist.get().onAccepted();
				}
			}
		});
		
		return rootView;
	}

	public static DialogBuy getInstance(IDialogBuyListener dlistener) {
		DialogBuy dialog = new DialogBuy();
		dialog.wlist = new WeakReference<DialogBuy.IDialogBuyListener>(dlistener);
		return dialog;
	}
}	

package com.vsm.radio18.categories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vsm.radio18.R;
import com.vsm.radio18.RadioConfiguration;

public class FragHelp extends BaseFragment {
	private View layoutEmail;
	private View layoutPhone;

	public static Fragment getInstance() {
		return new FragHelp();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.help_layout, container, false);
		layoutEmail = findViewById(R.id.layoutEmail);
		layoutPhone = findViewById(R.id.layoutPhone);
		
		layoutEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",RadioConfiguration.HELP_EMAIL, null));
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
			}
		});
		
		layoutPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 String uri = "tel:" + RadioConfiguration.HELP_PHONE_NUMBER;
				 Intent intent = new Intent(Intent.ACTION_CALL);
				 intent.setData(Uri.parse(uri));
				 startActivity(intent);			
			}
		});
		
		return rootView;
	}

}

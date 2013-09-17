package com.vsm.radio18.categories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vsm.radio18.R;
import com.vsm.radio18.RadioConfiguration;

public class FragQNA extends BaseFragment {
	

	protected static final String QUESTION_ABOUT_APP_RADIO_18_PLUS = "Cau hoi ve app radio 18+";

	public static FragQNA getInstance() {
		return new FragQNA();
	}

	private EditText etEmail;
	private EditText etContent;
	private Button btSend;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.q_n_a_layout, container, false);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etContent = (EditText) findViewById(R.id.etContent);
		btSend = (Button) findViewById(R.id.btSend);
		btSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",RadioConfiguration.SUPPORT_EMAIL, null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, QUESTION_ABOUT_APP_RADIO_18_PLUS);
				emailIntent.putExtra(Intent.EXTRA_TEXT, "From " + etEmail.getText() + "\n\n" + etContent.getText());
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
			}
		});
		
		return rootView;
	}
}

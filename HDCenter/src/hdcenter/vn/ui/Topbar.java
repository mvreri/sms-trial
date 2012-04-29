package hdcenter.vn.ui;

import hdcenter.vn.R;
import hdcenter.vn.SearchMovies;
import hdcenter.vn.utils.Helpers;
import hdcenter.vn.utils.StringHelpers;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

public class Topbar {

	private Activity hostedActivity;
	private View rootView;
	private ImageView ivHome;
	private AutoCompleteTextView atSearch;
	private ImageView ivSearch;

	public Topbar(Activity activity, View topBar) {
		this.hostedActivity = activity;
		this.rootView = topBar;
		setActions();
	}

	private void setActions() {
		createHomeButton();
		createSearchModule();
	}

	private void createHomeButton() {
		ivHome = (ImageView) rootView.findViewById(R.id.ivHome);
		ivHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Helpers.gotoMainActivity(hostedActivity);
			}
		});
	}
	
	private void createSearchModule() {
		atSearch = (AutoCompleteTextView) rootView.findViewById(R.id.atSearch);
		atSearch.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_ENTER)) {
					searchCurrentKeyword();
					return true;
				}
				return false;
			}

		});
		
	    ivSearch = (ImageView) rootView.findViewById(R.id.ivSearch);
	    ivSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchCurrentKeyword();
			}
		});
	}
	
	private void searchCurrentKeyword() {
		String s = StringHelpers.replaceLowerSignCharacter(hostedActivity.getApplicationContext(), atSearch.getText().toString().trim());
		s = s.toLowerCase();
		if ( s.length() > 0 ) {
			Intent i = new Intent(hostedActivity.getApplicationContext(),SearchMovies.class);
			i.putExtra(SearchMovies.IEXTRA_KEYWORD, s);
			Helpers.enterActivity(hostedActivity, i);
		}
	}
	
	public void setOnClickButtonSearch(View.OnClickListener listener) {
		ivSearch.setOnClickListener(listener);
	}
}

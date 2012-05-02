package hdcenter.vn;

import hdcenter.vn.data.requests.ReqSearch;
import hdcenter.vn.ui.MoviesListControl;
import hdcenter.vn.utils.Helpers;
import hdcenter.vn.utils.StringHelpers;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

/**
 * This class differs from ListMoviesActivity only by the search modules (home-edittext-searchbutton)
 * More specific: only by the action performed by click on the search button
 * 
 *  
 * @author hungson175
 *
 */
public class SearchMovies extends Activity
{

	public static final String IEXTRA_KEYWORD = "extra_keyword";
	private static final int FIRST_PAGE = 1;
	protected Handler handler = new Handler();
	private String keyword;
	private MoviesListControl moviesList = null;
	private AutoCompleteTextView atSearch;
	private View btHome;
	private View btSearch;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sub_activity);
		bindViews();
	}

	private void bindViews() {
		bindTopbar();
		bindListview();
		processPassedSearchKeyword();
	}

	private void bindListview() {
		moviesList = new MoviesListControl(this, (ListView) findViewById(R.id.lvMovies), null, handler);
	}

	private void bindTopbar() {
		atSearch = (AutoCompleteTextView) findViewById(R.id.atSearch);		
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
		
		btHome = findViewById(R.id.ivHome);
		btHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Helpers.gotoMainActivity(SearchMovies.this);
			}
		});
		btSearch = findViewById(R.id.ivSearch);
		btSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						searchCurrentKeyword();
					}
				});

			}
		});
	}

	private void processPassedSearchKeyword() {
		//TODO: select keyword after search - so user can delete it easily
		Helpers.hideSoftKeyboard(this);
		keyword = getIntent().getStringExtra(IEXTRA_KEYWORD);
		if (keyword != null) {
			atSearch.setText(keyword.trim());
			searchCurrentKeyword();
		}
	}

	private void searchCurrentKeyword() {
		String s = StringHelpers.replaceLowerSignCharacter(getApplicationContext(), atSearch.getText().toString().trim());
		s = s.toLowerCase();
		if ( s.length() > 0 ) {
			moviesList.reset(); 
			moviesList.setRequest(new ReqSearch(s, FIRST_PAGE));
			moviesList.requestFirstPage();
			Helpers.hideSoftKeyboard(this,atSearch);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Helpers.exitTransition(this);
	}
}

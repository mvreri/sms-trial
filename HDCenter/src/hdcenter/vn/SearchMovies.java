package hdcenter.vn;

import hdcenter.vn.data.requests.ReqSearch;
import hdcenter.vn.ui.MoviesListControl;
import hdcenter.vn.utils.Helpers;
import hdcenter.vn.utils.StringHelpers;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
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
						onClickButtonSearch();
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
			onClickButtonSearch();
		}
	}
	//
	//	private void createViews() {
	//		lvMovies = (ListView) findViewById(R.id.lvMovies);
	//		lvMovies.setOnItemClickListener(new OnItemClickListener() {
	//			@Override
	//			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	//				MovieItem item = mList.get(position);
	//				ShowMovieDetails.passedSummaryItem = item;
	//				Intent i = new Intent(getApplicationContext(),ShowMovieDetails.class);
	//				startActivity(i);
	//			}
	//		});		
	//	    createHomeButton();
	//	    createSearchModule();
	//	}

	//	private void createHomeButton() {
	//		ivHome = (ImageView) findViewById(R.id.ivHome);
	//	    ivHome.setOnClickListener(new OnClickListener() {
	//			@Override
	//			public void onClick(View v) {
	//				Intent i = new Intent(getApplicationContext(),HDCenterActivity.class);
	//				startActivity(i);
	//			}
	//		});
	//	}

	//	private void createSearchModule() {
	//		atSearch = (AutoCompleteTextView) findViewById(R.id.atSearch);
	//	    ivSearch = (ImageView) findViewById(R.id.ivSearch);
	//	    ivSearch.setOnClickListener(new OnClickListener() {
	//			@Override
	//			public void onClick(View v) {
	//				onClickButtonSearch();
	//			}
	//		});
	//	}

	private void onClickButtonSearch() {
		String s = StringHelpers.replaceLowerSignCharacter(getApplicationContext(), atSearch.getText().toString().trim());
		s = s.toLowerCase();
		if ( s.length() > 0 ) {
			moviesList.reset(); 
			moviesList.setRequest(new ReqSearch(s, FIRST_PAGE));
			moviesList.requestFirstPage();
			Helpers.hideSoftKeyboard(this,atSearch);
		}
	}

	//	@Override
	//	public void onRequestError(Exception e) {
	//		Logger.logError(e);
	//	}
	//
	//	@Override
	//	public void onRequestSuccess(Object data) {
	//		mList = (MoviesList) data;
	//		adapter = new MovieAdapter(getApplicationContext(), mList, handler);
	//		lvMovies.setAdapter(adapter);
	//	}

}

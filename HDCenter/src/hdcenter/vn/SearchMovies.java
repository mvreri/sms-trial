package hdcenter.vn;

import hdcenter.vn.data.DataCenter;
import hdcenter.vn.data.IRequestListener;
import hdcenter.vn.entities.MovieItem;
import hdcenter.vn.entities.MoviesList;
import hdcenter.vn.ui.MovieAdapter;
import hdcenter.vn.utils.Helpers;
import hdcenter.vn.utils.Logger;
import hdcenter.vn.utils.StringHelpers;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchMovies 
	extends Activity
	implements IRequestListener
{

	public static final String IEXTRA_KEYWORD = "extra_keyword";
	private static final int FIRST_PAGE = 1;
	private MoviesList mList = new MoviesList();
	private MovieAdapter adapter;
	private ListView lvMovies;
	private ImageView ivHome;
	private AutoCompleteTextView atSearch;
	private ImageView ivSearch;
	protected Handler handler = new Handler();
	private String keyword;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.search_movies);
	    createViews();
	    processPassedSearchKeyword();
	}

	private void processPassedSearchKeyword() {
		Helpers.hideSoftKeyboard(this);
	    keyword = getIntent().getStringExtra(IEXTRA_KEYWORD);
	    if (keyword != null) {
	    	atSearch.setText(keyword.trim());
	    	onClickButtonSearch();
	    }
	}

	private void createViews() {
		lvMovies = (ListView) findViewById(R.id.lvMovies);
		lvMovies.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				MovieItem item = mList.get(position);
				ShowMovieDetails.passedSummaryItem = item;
				Intent i = new Intent(getApplicationContext(),ShowMovieDetails.class);
				startActivity(i);
			}
		});		
	    createHomeButton();
	    createSearchModule();
	}

	private void createHomeButton() {
		ivHome = (ImageView) findViewById(R.id.ivHome);
	    ivHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),HDCenterActivity.class);
				startActivity(i);
			}
		});
	}

	private void createSearchModule() {
		atSearch = (AutoCompleteTextView) findViewById(R.id.atSearch);
	    ivSearch = (ImageView) findViewById(R.id.ivSearch);
	    ivSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickButtonSearch();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	private void onClickButtonSearch() {
		String s = StringHelpers.replaceLowerSignCharacter(getApplicationContext(), atSearch.getText().toString().trim());
		s = s.toLowerCase();
		if ( s.length() > 0 ) {
			DataCenter.requestSearch(SearchMovies.this,s,FIRST_PAGE,handler);
			Helpers.hideSoftKeyboard(this,atSearch);
		}
	}

	@Override
	public void onRequestError(Exception e) {
		Logger.logError(e);
	}

	@Override
	public void onRequestSuccess(Object data) {
		mList = (MoviesList) data;
		adapter = new MovieAdapter(getApplicationContext(), mList, handler);
		lvMovies.setAdapter(adapter);
	}

}

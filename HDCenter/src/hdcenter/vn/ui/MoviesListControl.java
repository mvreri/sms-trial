package hdcenter.vn.ui;

import hdcenter.vn.R;
import hdcenter.vn.ShowMovieDetails;
import hdcenter.vn.data.DataCenter;
import hdcenter.vn.data.IRequestListener;
import hdcenter.vn.data.requests.RequestMoviesList;
import hdcenter.vn.entities.MovieItem;
import hdcenter.vn.entities.MoviesList;
import hdcenter.vn.utils.Helpers;
import hdcenter.vn.utils.Logger;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MoviesListControl 
implements IRequestListener
{

	private static final int DELAY_LOAD_MORE = 500;
	private Activity hostedActivity;
	private ListView listview;
	protected MoviesList moviesList = null;
	private MovieAdapter adapter = null;
	private Handler handler = null;
	private RequestMoviesList request;
	private MoviesListFooter footer;
	protected boolean loadingData;
	protected int currentPage;
	private int totalPage;
	protected boolean hasData;
	private String title;

	public MoviesListControl(Activity hostedActivity, ListView listview, String title, Handler handler) {
		this.hostedActivity = hostedActivity;
		Helpers.assertCondition(handler != null, "");
		this.handler = handler;
		//		this.title = title;
		this.title = null;
		initListview(listview);
		reset();
	}

	public void reset() {
		currentPage = 0;
		totalPage = 1;
		hasData = false;
		loadingData = false;
		if ( footer != null ) {
			footer.enable();
		} else {
			createFooter();
		}
		initAdapter();
	}

	private void initAdapter() {
		moviesList = new MoviesList();
		adapter = new MovieAdapter(hostedActivity, moviesList, handler);
		listview.setAdapter(adapter);
	}

	private void initListview(ListView listview) {
		this.listview = listview;
		setOnItemClickListener();
		createHeader();
		createFooter();
		this.hasData = false;
		this.listview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if ( hasData && !loadingData && firstVisibleItem + visibleItemCount >= totalItemCount) {
					onLoadMore();
				}
			}
		});
	}

	private void createHeader() {
		if ( this.title != null ) {
			TextView tv = (TextView) Helpers.inflate(hostedActivity, R.layout.movies_list_header, null);
			tv.setText(this.title);
			listview.addHeaderView(tv);
		}
	}

	private void setOnItemClickListener() {
		this.listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int rawPosition,
					long arg3) {
				int position = rawPosition - listview.getHeaderViewsCount();
				Helpers.assertCondition( moviesList != null, "Movie list is NULL");
				Helpers.assertCondition( moviesList.size() > position, "Position: " + position + " -- mvlist size: " + moviesList.size() );
				MovieItem item = moviesList.get(position);
				ShowMovieDetails.passedSummaryItem = item;
				Intent i = new Intent(
						MoviesListControl.this.hostedActivity.getApplicationContext(),
						ShowMovieDetails.class);
				Helpers.enterActivity(hostedActivity, i);
			}
		});
	}



	/**
	 * 
	 * @return the footer view
	 * Pseudo code:
	 * 	When the footer is clicked:
	 * 	If last page already
	 * 		Hide the footer
	 * 	ElseIf
	 * 		mark: loadingMore = true
	 * 		show waiting footer
	 * 		The request is called on the next page
	 * 		if request succeed
	 * 			update the UI
	 * 		endif
	 * 		mark: loadingMore = false
	 * 		show clickable footer
	 * 	EndIf
	 * 
	 * -> Coder's notes:
	 * 	- Total page should be returned, not only the moviesList
	 * 	- Don't create new adapter everytime the result returns, just add data
	 *  - Pay attention 3 cases: first page, (2,3,4....), last page
	 *  - Be careful with case: last page = first page
	 *  
	 */	
	private void createFooter() {
		this.footer = new MoviesListFooter(hostedActivity.getApplicationContext());
		this.listview.addFooterView(footer);
	}

	private void onLoadMore() {
		if ( isLastPage() && footer != null) {
			footer.disable();
			listview.removeFooterView(footer);
			footer = null;
		} else {
			if ( ! loadingData ) {
				markLoadingData();
				requestNextPage();
			} else {
				Logger.logError("This shouldn't be called now !");
			}
		}
	}


	private void markLoadingData() {
		loadingData = true;
		footer.showWaiting();
	}

	private void unmarkLoadingData() {
		loadingData = false;
		footer.showLoadMore();
	}


	protected boolean isLastPage() {
		/**
		 * CODER'S NOTES (just for me)
		 * The assert is failed when a searching with no results returned - 
		 * It's nobody fault, it's just the inconsistent between PHP & Java (indexing: 1 & indexing: 0 ) 
		 * 	-> different logic thinking between coders (about index & totalpage)
		 * This is the first time I've observed something like this
		 */
		//Helpers.assertCondition( currentPage <= totalPage , "Surpassed last page: currentPage: " + currentPage + " -- lastpage: " + totalPage);
		return currentPage >= totalPage;
	}

	//	The result is returned as pair(totalPage,moviesList)
	//		- adapter should be initialized & set to listview before (note: after footer)
	//		- the moviesList should always be added, not new !
	@Override
	public void onRequestSuccess(Object data) {
		this.hasData = true;

		@SuppressWarnings("unchecked")
		final Pair<Integer,MoviesList> pair = (Pair<Integer, MoviesList>) data;
		this.totalPage = pair.first;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				moviesList.append(pair.second);		
				adapter.notifyDataSetChanged();
				unmarkLoadingData();
			}
		},DELAY_LOAD_MORE);


	}

	@Override
	public void onRequestError(Exception e) {
		unmarkLoadingData();
		Logger.logError(e);
	}

	public void setRequest(RequestMoviesList request) {
		this.request = request;
	}

	public void requestFirstPage() {
		markLoadingData();
		currentPage = 1;
		requestCurrentPage();
	}

	protected void requestNextPage() {
		currentPage++;
		Helpers.assertCondition(currentPage <= totalPage, "Surpassed last page !");
		requestCurrentPage();
	}

	private void requestCurrentPage() {
		request.setPage(currentPage);
		DataCenter.addMoviesListRequest(request, this, handler);
	}



}

package hdcenter.vn.ui;

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
	protected boolean loadingData = false;
	protected int currentPage = 0;
	private int totalPage = -1;
	protected boolean hasData;

	public MoviesListControl(Activity hostedActivity, ListView listview, Handler handler) {
		this.hostedActivity = hostedActivity;
		this.handler = handler;
		initListview(listview);
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

	private void setOnItemClickListener() {
		this.listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Helpers.assertCondition( moviesList != null, "Movie list is NULL");
				Helpers.assertCondition( moviesList.size() > position, "Position: " + position + " -- mvlist size: " + moviesList.size() );
				MovieItem item = moviesList.get(position);
				ShowMovieDetails.passedSummaryItem = item;
				Intent i = new Intent(
						MoviesListControl.this.hostedActivity.getApplicationContext(),
						ShowMovieDetails.class);
				MoviesListControl.this.hostedActivity.startActivity(i);

			}
		});
	}



	/**
	 * 
	 * @return the footer view
	 * Pseudo code:
	 * 	When the footer is clicked:
	 * 	If last page already
	 * 		"Blur" the Footer
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
	 * -> Notes:
	 * 	- Total page should be returned, not only the moviesList
	 * 	- Don't create new adapter everytime the result returns, just add data
	 *  - Pay attention 3 cases: first page, (2,3,4....), last page
	 *  - Be careful with case: last page = first page
	 *  
	 */	
	private void createFooter() {
//		Logger.logInfo("Footer is created !");
		this.footer = new MoviesListFooter(hostedActivity.getApplicationContext()) {
			@Override
			public void onLoadMoreClick() {
				onLoadMore();
			}
		};
		this.listview.addFooterView(footer);
	}

	private void onLoadMore() {
//		Logger.logInfo("On load more is called ... ");
		if ( isLastPage() ) {
//			Logger.logInfo("Last page reach !");
			footer.disable();
		} else {
			if ( ! loadingData ) {
//				Logger.logInfo("Begin loading next page");
				markLoadingData();
				requestNextPage();
			} else {
//				Logger.logInfo("Begin loading next page");
				Helpers.assertCondition(false, "This button shouldn't be clickable now !");
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
		Helpers.assertCondition( currentPage <= totalPage , "Surpassed last page !");
		return currentPage >= totalPage;
	}

	//	The result is returned as pair(totalPage,moviesList)
	//		- adapter should be initialized & set to listview before (note: after footer)
	//		- the moviesList should always be added, not new !
	@Override
	public void onRequestSuccess(Object data) {
		this.hasData = true;
		Pair<Integer,MoviesList> pair = (Pair<Integer, MoviesList>) data;
		this.totalPage = pair.first;
		moviesList.append(pair.second);
		Helpers.startAfter(DELAY_LOAD_MORE, new Runnable() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
						unmarkLoadingData();
					}
				});
			}
		});
		//		moviesList = (MoviesList) data;
		//		adapter = new MovieAdapter(hostedActivity.getApplicationContext(), moviesList, handler);
		//		listview.setAdapter(adapter);
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

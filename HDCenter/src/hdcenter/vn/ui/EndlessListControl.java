package hdcenter.vn.ui;

import hdcenter.vn.R;
import hdcenter.vn.data.DataCenter;
import hdcenter.vn.data.IRequestListener;
import hdcenter.vn.data.requests.PaggableRequest;
import hdcenter.vn.data.requests.RequestMoviesList;
import hdcenter.vn.utils.Helpers;
import hdcenter.vn.utils.Logger;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public abstract class EndlessListControl 
implements IRequestListener
{


	abstract protected BaseAdapter createAdapter(Activity hostedActivity,ArrayList<?> itemsList, Handler handler);
	
	/*
	 * Provide the empty list of items - 
	 * this should be done because the endlesslistview need the empty data at first, 
	 * and load more & more data later on to add to the first empty set
	 */
	abstract protected ArrayList<?> provideEmptyData();

	abstract protected void appendItems(ArrayList<?> itemsList, ArrayList<?> additionalData);


	private static final int DELAY_LOAD_MORE = 500;
	private Activity hostedActivity;
	private ListView listview;
	protected ArrayList<?> itemsList = null;
	private BaseAdapter adapter = null; 
	private Handler handler = null;
	private PaggableRequest request;
	private ListFooter footer;
	protected boolean loadingData;
	protected int currentPage;
	private int totalPage;
	protected boolean hasData;
	private String title;

	public EndlessListControl(Activity hostedActivity, ListView listview, String title, Handler handler) {
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
		itemsList = provideEmptyData();
		adapter = createAdapter( hostedActivity, itemsList, handler );
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
				Helpers.assertCondition( itemsList != null, "Movie list is NULL");
				Helpers.assertCondition( itemsList.size() > position, "Position: " + position + " -- mvlist size: " + itemsList.size() );
				Object item = itemsList.get(position);
				onListItemClick(position,item);
			}


		});
	}

	abstract public void onListItemClick(int position, Object item);


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
		Logger.logInfo("Footer is created");
		this.footer = new ListFooter(hostedActivity.getApplicationContext());
		this.listview.addFooterView(footer);
	}

	private void onLoadMore() {
		if ( isLastPage() ) {
			if ( footer != null) {
				footer.disable();
				listview.removeFooterView(footer);
				footer = null;
			}
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

	@Override
	public void onRequestSuccess(Object data) {


		@SuppressWarnings("unchecked")
		final Pair<Integer,ArrayList<?>> pair = (Pair<Integer, ArrayList<?>>) data;
		this.totalPage = pair.first;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				EndlessListControl.this.hasData = true;
				appendItems(itemsList,pair.second);
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

	public void setRequest(PaggableRequest request) {
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
		DataCenter.addPaggableRequest(request,this, handler);
	}

	public Activity getHostedActivity() {
		return hostedActivity;
	}



}

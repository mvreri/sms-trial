package hdcenter.vn;

import hdcenter.vn.data.requests.ReqMoviesInCollection;
import hdcenter.vn.data.requests.RequestMoviesList;

public class ShowCollection extends ListMoviesActivity {

	protected static final String EXTRA_ID = "extra_id";
	protected static final String EXTRA_NAME = "extra_name";
	private static final int FIRST_PAGE = 1;
	
	private String id;
	private String title;

	protected void getInputData() {
		this.id = getIntent().getStringExtra(EXTRA_ID);
		this.title = getIntent().getStringExtra(EXTRA_NAME);
	}

	@Override
	protected RequestMoviesList provideRequest() {
		return new ReqMoviesInCollection(id,FIRST_PAGE);
	}

	@Override
	protected String getListTitle() {
		return this.title;
	}

}

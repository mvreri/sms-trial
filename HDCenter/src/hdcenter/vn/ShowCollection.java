package hdcenter.vn;

import hdcenter.vn.data.requests.ReqMoviesInCollection;
import hdcenter.vn.data.requests.Request;

public class ShowCollection extends ListMoviesActivity {

	protected static final String EXTRA_ID = "extra_id";
	private static final int FIRST_PAGE = 1;
	private String id;

	protected void getInputData() {
		this.id = getIntent().getStringExtra(EXTRA_ID);
	}

	@Override
	protected Request provideRequest() {
		return new ReqMoviesInCollection(id,FIRST_PAGE);
	}

}

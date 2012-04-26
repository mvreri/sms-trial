package hdcenter.vn;

import hdcenter.vn.data.requests.ReqNewMovies;
import hdcenter.vn.data.requests.Request;

public class NewMovies extends ListMoviesActivity {
	private static final int FIRST_PAGE = 1;
	@Override
	protected Request provideRequest() {
		return new ReqNewMovies(FIRST_PAGE);
	}
	@Override
	protected void getInputData() {
		// no input data
	}

}

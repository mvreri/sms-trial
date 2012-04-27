package hdcenter.vn;

import hdcenter.vn.data.requests.ReqMoviesInGenre;
import hdcenter.vn.data.requests.RequestMoviesList;

public class ShowGenre extends ListMoviesActivity {

	protected static final String EXTRA_GENRE = "extra_genre";
	private static final int FIRST_PAGE = 1;
	private String genre;
	
	protected void getInputData() {
		genre = getIntent().getStringExtra(EXTRA_GENRE);
	}

	@Override
	protected RequestMoviesList provideRequest() {
		return new ReqMoviesInGenre(genre,FIRST_PAGE);
	
	}

}

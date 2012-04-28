package hdcenter.vn;

import hdcenter.vn.data.requests.ReqMoviesInGenre;
import hdcenter.vn.data.requests.RequestMoviesList;

public class ShowGenre extends ListMoviesActivity {

	protected static final String EXTRA_GENRE = "extra_genre";
	protected static final String EXTRA_NAME = "extra_name";
	private static final int FIRST_PAGE = 1;
	private String genre;
	private String title;
	
	protected void getInputData() {
		genre = getIntent().getStringExtra(EXTRA_GENRE);
		this.title = getIntent().getStringExtra(EXTRA_NAME);
	}

	@Override
	protected RequestMoviesList provideRequest() {
		return new ReqMoviesInGenre(genre,FIRST_PAGE);
	
	}

	@Override
	protected String getListTitle() {
		return this.title;
	}

}

package hdcenter.vn.data.requests;

import java.util.HashMap;

public class ReqMoviesInGenre extends RequestMoviesList {
	private static final String API_GENRES = "genre";
	private static final String PAGE = "page";
	private static final String GENRE = "genre";

	private int page;
	private String genre;

	public ReqMoviesInGenre(String genre, int page) {
		super();
		this.page = page;
		this.genre = genre;
	}

	@Override
	protected String provideAPIName() {
		return API_GENRES;
	}

	@Override
	protected void provideParameters(HashMap<String, String> parameters) {
		parameters.put(GENRE, this.genre);
		parameters.put(PAGE, String.valueOf(this.page));
	}

}

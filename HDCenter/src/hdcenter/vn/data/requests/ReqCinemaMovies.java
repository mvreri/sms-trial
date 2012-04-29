package hdcenter.vn.data.requests;

import java.util.HashMap;

public class ReqCinemaMovies extends RequestMoviesList {

	private static final String CINEMA = "cinema";
	private static final String PAGE = "page";

	public ReqCinemaMovies(int page) {
		super(page);
		setPage(page);
	}
	@Override
	protected String provideAPIName() {
		return CINEMA;
	}
	@Override
	protected void provideParameters(HashMap<String, String> parameters) {
		parameters.put(PAGE, String.valueOf(getPage()));
	}

}

package hdcenter.vn.data.requests;

import hdcenter.vn.movie_calendars.CalendarParser;
import hdcenter.vn.movie_calendars.CalendarsList;
import hdcenter.vn.movie_calendars.MegastarParser;

public class ReqMovieCalendars implements IRequest {

	private String cinemaId;
	private String movieId;

	public ReqMovieCalendars(String cinemaId, String movieId) {
		this.cinemaId = cinemaId;
		this.movieId = movieId;
	}

	@Override
	public Object requestData() throws Exception {
		CalendarsList calendars = cachedCalendars();
		if ( calendars != null ) return calendars;
		CalendarParser parser = new MegastarParser(cinemaId,movieId);
		calendars = parser.parse();
		return calendars;
	}

	/**
	 * 
	 * @return calendars if cached, null otherwise
	 */
	private CalendarsList cachedCalendars() {
		// TODO Auto-generated method stub
		return null;
	}

}

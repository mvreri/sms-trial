package hdcenter.vn.data;

import hdcenter.vn.data.requests.IRequest;
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
		CalendarParser parser = new MegastarParser(cinemaId,movieId);
		CalendarsList calendars = parser.parse();
		return calendars;
	}

}

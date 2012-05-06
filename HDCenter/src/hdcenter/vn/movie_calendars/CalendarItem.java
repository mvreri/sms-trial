package hdcenter.vn.movie_calendars;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarItem {
	long time;
	String bookURL;
	public CalendarItem(long time, String bookURL) {
		super();
		this.time = time;
		this.bookURL = bookURL;
	}
	public long getTime() {
		return time;
	}
	public String getBookURL() {
		return bookURL;
	}
	
	@Override
	public String toString() {
		DateFormat df = new SimpleDateFormat("dd/MM hh:mm");
		return df.format(new Date(time));
	}
}

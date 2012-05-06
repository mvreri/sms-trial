package hdcenter.vn.movie_calendars;

import java.util.ArrayList;

public class CalendarsList extends ArrayList<CalendarItem> {
	private static final long serialVersionUID = 8589595443468585012L;

	public void append(CalendarsList additional) {
		if ( additional == null) return;
		for(int i = 0 ; i < additional.size() ; i++) {
			this.add(additional.get(i));
		}
	}
}
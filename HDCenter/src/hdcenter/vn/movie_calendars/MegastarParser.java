package hdcenter.vn.movie_calendars;

import hdcenter.vn.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MegastarParser implements CalendarParser {

	private static final String MOVIE_ID_TEMPLATE = "#movie_id";
	private static final String CINEMA_ID_TEMPLATE = "#cinema_id";
	private String movieId;
	private String cinemaId;

	public MegastarParser(String movieId, String cinemaId) {
		this.movieId = movieId;
		this.cinemaId = cinemaId;
	}

	static final String URL = "http://m.megastar.vn/internal_xml.aspx?RequestType=SessionTime&movie_name="+MOVIE_ID_TEMPLATE+"&cinema_id="+CINEMA_ID_TEMPLATE+"&&visLang=1";
	private static final String BEG_DATE = "<span class=\"orange-13\">";
	private static final char END_DATE_CHAR= '<';
	private static final String BEG_LINE_TIME = "<li class=\"box-shadow\">";
	private static final String BEG_BOOK_URL = "http://";
	private static final String END_BOOK_URL = "\"";
	private static final String END_LIST_TIME = "</ul><div class=\"clear\"></div>";
	private static final char BEG_TIME_CHAR= '>';
	private static final char END_TIME_CHAR= '<';

	@Override
	public CalendarsList parse() {
		BufferedReader in = null;
		CalendarsList calendars = new CalendarsList();
		try {
			URL url = getURL(movieId,cinemaId);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine="ABC";		
			while ((inputLine = in.readLine()) != null) {
				
				String date = containsBeginDate(in,inputLine);
				if ( date != null) {
					CalendarsList additional = parseForDate(in,date);
					calendars.append(additional);
				}
			}

			return calendars;
		} catch (IOException e) {
			Logger.logError(e);
			return calendars;
		}  finally {
			try {
				if ( in != null)
					in.close();
			} catch (Exception e) {
				Logger.logError(e);
			}
		}
	}

	private CalendarsList parseForDate(BufferedReader in, String date) throws IOException {
		String line = "ABC";
		CalendarsList calendars = new CalendarsList();
		while ( (line = in.readLine() ) != null ) {			
			if ( line.contains(END_LIST_TIME)) return calendars;
			if ( line.contains(BEG_LINE_TIME)) {
				CalendarItem item = parseCalendarItem(line,date);
				if ( item != null ) calendars.add(item);
				else Logger.logError("Item is null");
			}
		}
		return null;
	}

	private CalendarItem parseCalendarItem(String line, String date) {
		try {
			//parse bookingurl
			int startBook = line.indexOf(BEG_BOOK_URL);
			int endBook = line.indexOf(END_BOOK_URL,startBook );
			String book = line.substring(startBook,endBook);
			
			String timeStr = parseTime(line, endBook);
			
			CalendarItem item = new CalendarItem(createTimeInMillis(date,timeStr), book);
			return item;
		} catch (Exception e) {
			return null;
		}
	}

	private long createTimeInMillis(String dateStr, String timeStr) {
		String pattern = "dd/MM/yyyy hh:mma";
		Date date = convertStr2Date(dateStr+ " " + timeStr, pattern);
		Logger.logInfo("Date: " + date.toString());
		return date.getTime();
	}
	
	public static Date convertStr2Date(String s,String pattern) {
		try {
			DateFormat f = new SimpleDateFormat(pattern);
			return (Date) f.parse(s);
		} catch (ParseException e) {
			return null;
		}
	}

	private String parseTime(String line, int fromIndex) {
		int startTimeIndex = line.indexOf(BEG_TIME_CHAR, fromIndex) + 1;
		int endTimeIndex = line.indexOf(END_TIME_CHAR, startTimeIndex);
		String timeStr = line.substring(startTimeIndex,endTimeIndex);
		return timeStr;
	}

	private String containsBeginDate(BufferedReader in, String inputLine) throws IOException {
		if (inputLine.contains(BEG_DATE)) {
			String dateLine = in.readLine();
			int eindex = dateLine.indexOf(END_DATE_CHAR);
			return dateLine.substring(0, eindex);
		} else return null;
	}

	private java.net.URL getURL(String movieId, String cinemaId) throws MalformedURLException {
		String urlString = URL.replaceFirst(MOVIE_ID_TEMPLATE, movieId);
		urlString = urlString.replaceFirst(CINEMA_ID_TEMPLATE, cinemaId);
		return new URL(urlString);
	}

}

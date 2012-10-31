package dtd.phs.chatexperiment_phs;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helpers {

	public static String formatToTime(long time) {
		Date date = new Date(time);
		String timeString = new SimpleDateFormat("HH:mm").format(date);
		return timeString;
	}

}

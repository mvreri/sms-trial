package dtd.phs.sil.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SentMessagesList extends ArrayList<SentMessageItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1456804353830100424L;

	public static void sortByTime(SentMessagesList list) {
		Collections.sort(list, new Comparator<SentMessageItem>() {

			@Override
			public int compare(SentMessageItem object1, SentMessageItem object2) {
				long diff = object1.getSentTime() - object2.getSentTime();
				if ( diff < 0 ) return 1;
				if ( diff > 0 ) return -1;
				return 0;
			}
		});
	}

}

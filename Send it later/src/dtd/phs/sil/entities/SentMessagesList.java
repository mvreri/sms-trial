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

	public static void cutList(SentMessagesList list, int maxSentSize) {
		if ( maxSentSize < list.size() )
			list.removeRange(maxSentSize, list.size());
	}

	public long getId(int i) {
		return this.get(i).getId();
	}

	public boolean removeMessageWithId(long id) {
		int index = -1;
		for(int i = 0 ; i < this.size() ; i++) {
			if (this.getId(i) == id) {
				index = i;
				break;
			}
		}
		if ( index != -1) {
			this.remove(index);
			return true;
		}
		return false;
	}

}

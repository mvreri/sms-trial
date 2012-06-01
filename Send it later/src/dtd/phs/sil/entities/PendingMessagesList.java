package dtd.phs.sil.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PendingMessagesList 
	extends ArrayList<PendingMessageItem>
	implements IMessagesList
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4228781323997712912L;

	public static void sortByNextOccurence(PendingMessagesList allMessages) {
		if ( allMessages != null )
		Collections.sort(allMessages, new Comparator<PendingMessageItem>() {
			@Override
			public int compare(PendingMessageItem lhs, PendingMessageItem rhs) {
				long left = lhs.getNextTimeMillis();
				//TODO code review : mysterious number -1 !
				if ( left == -1) return 1;
				long right = rhs.getNextTimeMillis();
				if ( right == -1) return -1;
				if ( left < right ) return -1;
				if ( left > right ) return 1;
				return 0;
			}
		});
	}

	public int removeMessageWithId(long id) {
		int index = -1;
		for(int i = 0 ; i < this.size() ; i++) {
			if ( id == this.get(i).getId() ) {
				index = i;
				break;
			}
		}
		if ( index != -1) {
			this.remove(index);
		}
		return index;
	}

}

package hdcenter.vn.entities;

import hdcenter.vn.utils.Logger;

import java.util.ArrayList;

public class MoviesList extends ArrayList<MovieItem> {
	private static final long serialVersionUID = 3499413108964525364L;

	public void append(MoviesList second) {
		if ( second == null) {
			Logger.logInfo("List to be appended is NULL");
			return;
		}
		for(int i = 0; i < second.size() ; i++)
			this.add(second.get(i));
	}

}

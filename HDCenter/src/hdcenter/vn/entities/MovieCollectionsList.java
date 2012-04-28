package hdcenter.vn.entities;

import java.util.ArrayList;

public class MovieCollectionsList extends ArrayList<CollectionItem> {
	private static final long serialVersionUID = 7031504204376868813L;

	public String getId(int position) {
		return get(position).getId();
	}

	public String[] getNames() {
		String[] names = new String[this.size()];
		for(int i = 0 ; i < size() ; i++) {
			names[i] = this.get(i).getVname();
		}
		return names;
	}

	public String getImageURL(int position) {
		return this.get(position).getImageURL();
	}

	public String getName(int position) {
		return this.get(position).getVname();
	}
}

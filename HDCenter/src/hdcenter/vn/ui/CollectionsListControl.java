package hdcenter.vn.ui;

import hdcenter.vn.CollectionsAdapter;
import hdcenter.vn.ShowCollection;
import hdcenter.vn.entities.CollectionItem;
import hdcenter.vn.entities.MovieCollectionsList;
import hdcenter.vn.utils.Helpers;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class CollectionsListControl extends EndlessListControl {

	public CollectionsListControl(Activity hostedActivity, ListView listview, String title, Handler handler) {
		super(hostedActivity, listview, title, handler);
	}

	@Override
	protected BaseAdapter createAdapter(Activity hostedActivity,
			ArrayList<?> itemsList, Handler handler) {
		return new CollectionsAdapter(getHostedActivity(), (MovieCollectionsList) itemsList, handler);
	}

	@Override
	protected ArrayList<?> provideEmptyData() {
		return new MovieCollectionsList();
	}

	@Override
	protected void appendItems(ArrayList<?> itemsList,
			ArrayList<?> additionalData) {
		MovieCollectionsList collections = (MovieCollectionsList) itemsList;
		collections.addAll((MovieCollectionsList)additionalData);
	}

	@Override
	public void onListItemClick(int position, Object item) {
		CollectionItem collection = (CollectionItem) item;
		String id = collection.getId();
		Intent i = new Intent(getHostedActivity().getApplicationContext(),ShowCollection.class);
		i.putExtra(ShowCollection.EXTRA_ID, id);
		i.putExtra(ShowCollection.EXTRA_NAME, collection.getVname());
		Helpers.enterActivity(getHostedActivity(), i);
		
	}

}

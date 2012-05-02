package hdcenter.vn;

import hdcenter.vn.data.DataCenter;
import hdcenter.vn.data.IRequestListener;
import hdcenter.vn.ui.SimpleTextAdapter;
import hdcenter.vn.ui.Topbar;
import hdcenter.vn.utils.Helpers;
import hdcenter.vn.utils.Logger;
import hdcenter.vn.utils.StringHelpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MovieGenres 
extends Activity 
implements IRequestListener 
{

	private ListView lvGeneres;
	private Handler handler = new Handler();
	private ArrayList<Pair<String, String>> genres;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sub_activity);
		bindViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Helpers.hideSoftKeyboard(this);
	}

	private void bindViews() {
		bindTopbar();
		bindListView();
	}

	private void bindListView() {
		lvGeneres = (ListView) findViewById(R.id.lvMovies);
		lvGeneres.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				String genreCode = genres.get(position).first;
				Intent i = new Intent(getApplicationContext(),ShowGenre.class);
				i.putExtra(ShowGenre.EXTRA_GENRE,genreCode);
				i.putExtra(ShowGenre.EXTRA_NAME, genres.get(position).second);
				Helpers.enterActivity(MovieGenres.this, i);
			}
		});
		DataCenter.requestGenres(this,handler);
	}

	private void bindTopbar() {
		new Topbar(this, findViewById(R.id.topbar));
	}

	@Override
	public void onRequestSuccess(Object data) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> mapE2V = (HashMap<String, String>) data;
		
		genres = extractGenresList(mapE2V);
		SimpleTextAdapter adapter = new SimpleTextAdapter(getApplicationContext(), R.layout.more_item_layout, extractVNnames(genres));
		lvGeneres.setAdapter(adapter);
	}

	private ArrayList<Pair<String,String>> extractGenresList(HashMap<String, String> mapE2V) {
		ArrayList<Pair<String, String>> list = new ArrayList<Pair<String,String>>();
		for(String key : mapE2V.keySet()) {
			list.add(new Pair<String, String>(key, mapE2V.get(key)));
		}

		//Sort according to Vietnamese translation
		Collections.sort(list, new Comparator<Pair<String,String>>() {
			@Override
			public int compare(Pair<String, String> lhs, Pair<String, String> rhs) {
				if ( lhs.second == null) return -1;
				if ( rhs.second == null) return 1;
				String left = StringHelpers.replaceLowerSignCharacter(getApplicationContext(),lhs.second.toLowerCase());
				String right = StringHelpers.replaceLowerSignCharacter(getApplicationContext(),rhs.second.toLowerCase());
				return left.compareTo(right);
			}
		});

		return list;
	}

	private String[] extractVNnames(ArrayList<Pair<String, String>> genres) {
		String[] items = new String[ genres.size()];
		for(int i  = 0; i < genres.size() ; i++) {
			items[i] = genres.get(i).second;
		}
		return items;
	}

	@Override
	public void onRequestError(Exception e) {
		Logger.logError(e);
	}

}

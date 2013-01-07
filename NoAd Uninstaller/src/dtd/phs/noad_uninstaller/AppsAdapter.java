package dtd.phs.noad_uninstaller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dtd.phs.lib.utils.Helpers;
import dtd.phs.noad_uninstaller.R;

public class AppsAdapter extends BaseAdapter {

	private List<PHS_AppInfo> apps;
	private Context context;
//	private int evenBg;
//	private int oddBg;
	private boolean[] selected;

	public AppsAdapter(Context context) {
		this.apps = new ArrayList<PHS_AppInfo>();
		this.context = context;
//		this.evenBg = context.getResources().getColor(R.color.even_item_bg);
//		this.oddBg = context.getResources().getColor(R.color.odd_item_bg);
		selected = new boolean[apps.size()];
		Arrays.fill(selected, false);
	}
	
	public void setData(ArrayList<PHS_AppInfo> appsList) {
		this.apps = appsList;
		this.selected = new boolean[apps.size()];
		Arrays.fill(selected,false);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if ( apps == null ) return 0;
		return apps.size();
	}

	@Override
	public PHS_AppInfo getItem(int position) {
		return apps.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = Helpers.inflate(context, R.layout.app_item, null);
		TextView name = (TextView) v.findViewById(R.id.appName);
		name.setText(getItem(position).getAppName());
		ImageView icon = (ImageView) v.findViewById(R.id.icon);
		icon.setImageDrawable(getItem(position).getIcon());
		
		if ( position % 2 == 0 ) 
			v.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.even_item_bg));
		else v.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.odd_item_bg));
		
		ImageView checkBox = (ImageView) v.findViewById(R.id.checkBox);
		if ( selected[position]) 
			checkBox.setImageResource(R.drawable.check_box_on);
		else checkBox.setImageResource(R.drawable.check_box_off);
		return v;
	}

	public void changeSelectedState(int position) {
		selected[position] = ! selected[position];
		notifyDataSetChanged();
	}

	public ArrayList<PHS_AppInfo> getSelectedApps() {
		ArrayList<PHS_AppInfo> result = new ArrayList<PHS_AppInfo>();
		for(int i = 0 ; i < apps.size() ; i++)
			if ( selected[i]) result.add(apps.get(i));
		
		return result;
	}

	public PHS_AppInfo findApp(String name) {
		for(PHS_AppInfo app : apps) {
			if ( app.getAppName().equals(name)) return app; 
		}
		return null;
	}

}
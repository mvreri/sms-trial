package dtd.phs.sil;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import dtd.phs.sil.utils.Logger;

public class MainScreen 
	extends TabActivity
	implements OnTabChangeListener
{
    private static final String WAIT_TAG = "wait_tag";
	private static final String SENT_TAG = "sent_tag";
	private static final String FAILED_TAG = "failed_tag";
	private TabHost tabHost;
	private Resources resources;
	private LayoutInflater layoutInflater;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tabHost = getTabHost();
        tabHost.setOnTabChangedListener(this);
        resources = getResources();
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addTab(WAIT_TAG,resources.getString(R.string.wait_tab_title),R.drawable.tab_waiting_dr,
        		new Intent(getApplicationContext(),WaitingSMS.class));
        addTab(SENT_TAG,resources.getString(R.string.sent_tab_title),R.drawable.tab_sent_dr,
        		new Intent(getApplicationContext(),SentSMS.class));
        addTab(FAILED_TAG,resources.getString(R.string.failed_tab_title),R.drawable.tab_failed_dr,
        		new Intent(getApplicationContext(),FailedSMS.class));
        tabHost.setCurrentTab(getLastSelectedTab());
    }
    
    private int getLastSelectedTab() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addTab(String tag, String title, int iconId, Intent intent ) {
		
    	TabSpec spec = tabHost
    	.newTabSpec(tag)    	
    	.setContent(intent);
    	
    	View v = layoutInflater.inflate(R.layout.tab_indicator, null);
    	ImageView iconView = (ImageView) v.findViewById(R.id.icon);
//    	TextView titleView = (TextView) v.findViewById(R.id.title);
    	iconView.setImageResource(iconId);
//    	titleView.setText(title);
    	spec.setIndicator(v);
    	
    	tabHost.addTab(spec);
    }

	@Override
	public void onTabChanged(String tabId) {
		Logger.logInfo(tabId);
	}
}
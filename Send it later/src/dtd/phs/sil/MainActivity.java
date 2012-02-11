package dtd.phs.sil;

import java.util.ArrayList;

import dtd.phs.sil.ui.OptionsMenu;
import dtd.phs.sil.ui.RateDialog;
import dtd.phs.sil.utils.Logger;
import dtd.phs.sil.utils.PreferenceHelpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;

public class MainActivity extends Activity {
	private FrameLayout mainFrames;
	private Handler handler = new Handler();
	private ArrayList<FrameView> frames;
	private ArrayList<ImageView> tabButtons;
	private int displayingFrameId;
	private OptionsMenu optionsMenu;
	private BroadcastReceiver myReceiver;
	private Animation occAnimation;
	private boolean toShowRateDialog;


	static final int FRAME_PENDING = 0;
	static final int FRAME_SENT = 1;
	public static final String EXTRA_SELECTED_FRAME = "selected_frame";
	public static final String EXTRA_SHOW_DIALOG_RATE = "show_dialog_rate";

	protected static final int DIALOG_REMOVE_PENDING_ITEM = 0;
	private static final int[] HIGHLIGHT_TAB_RES = {R.drawable.clock , R.drawable.message};
	private static final int[] NORMAL_TAB_RES = {R.drawable.clock_desat , R.drawable.message_desat};
	private static final int DIALOG_RATE = R.id.btRate;



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		initAnimations();
		addFrames();
		processBottomTabs();
	}


	private void initAnimations() {
		occAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_occ);
	}


	private void processBottomTabs() {

		tabButtons = new ArrayList<ImageView>();
		tabButtons.add((ImageView) findViewById(R.id.bt_tab_pending));
		tabButtons.add((ImageView) findViewById(R.id.bt_tab_sent));

		for(int i = 0 ; i < tabButtons.size() ; i++) {
			final int id = i;
			tabButtons.get(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showOnlyView(id);
				}
			});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		myReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				frames.get(displayingFrameId).onRefresh();
			}
		};
		registerReceiver( myReceiver, new IntentFilter(SendSMSService.ACTION_MESSAGE_SENT));
		showOnlyView(displayingFrameId);
		frames.get(displayingFrameId).onResume();
		if (toShowRateDialog) {
			toShowRateDialog = false;
			showDialog(DIALOG_RATE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(myReceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		optionsMenu = new OptionsMenu(this);
		optionsMenu.createOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		return optionsMenu.onItemSelected(item);
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		displayingFrameId = savedFrameId(intent);
		toShowRateDialog = getExtraShowRateDialog(intent);
		Logger.logInfo("To show rate dialog: " + toShowRateDialog);
	}

	private boolean getExtraShowRateDialog(Intent intent) {
		return intent.getBooleanExtra(EXTRA_SHOW_DIALOG_RATE, false);
	}


	private void addFrames() {
		mainFrames = (FrameLayout) findViewById(R.id.main_frames);

		frames = new ArrayList<FrameView>();
		frames.add(new PendingMessageView(this,handler));
		frames.add(new SentMessageView( this,handler ));

		for(int i = 0 ; i < frames.size() ; i++) {
			mainFrames.addView( frames.get(i), new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}

		Logger.logInfo("Calling .addFrames()");
		displayingFrameId = FRAME_PENDING;
	}


	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_RATE:
			return new RateDialog(this);
		default: return frames.get(displayingFrameId).onCreateDialog(id);
		}
	}

	private int savedFrameId(Intent intent) {
		int selectedFrame = intent.getIntExtra(EXTRA_SELECTED_FRAME, -1);
		Logger.logInfo("Selected frame: " + selectedFrame);
		if ( selectedFrame != -1) {
			return selectedFrame;
		}
		return FRAME_PENDING;
	}

	private void showOnlyView(int id) {
		for(int i = 0 ; i < frames.size() ; i++) {
			if ( id != i ) {
				frames.get(i).setVisibility(View.INVISIBLE);
				frames.get(i).onPause();
			}
		}
		displayingFrameId = id;
		frames.get(id).startAnimation(occAnimation);
		frames.get(id).setVisibility(View.VISIBLE);
		frames.get(id).onResume();
		highlightTab(id);
	}


	private void highlightTab(int id) {
		for(int i =  0 ; i < frames.size() ; i++) {
			ImageView imageView = tabButtons.get(i);
			if ( i == id )
				imageView.setImageResource(HIGHLIGHT_TAB_RES[i]);
			else imageView.setImageResource(NORMAL_TAB_RES[i]);
		}
	}

}
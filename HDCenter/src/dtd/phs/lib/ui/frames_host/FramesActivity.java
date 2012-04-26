package dtd.phs.lib.ui.frames_host;

import hdcenter.vn.R;
import hdcenter.vn.utils.Logger;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
public abstract class FramesActivity extends Activity {

	abstract protected void addFrames(ArrayList<FrameView> frames);
	abstract protected int getDefaultFrame();	
	protected abstract int normalTabResource(int i);
	protected abstract int highlightTabResource(int i);
	abstract protected int[] provideTabIds();
	abstract protected int mainLayoutId();

	private FrameLayout mainFrames;
	private ArrayList<FrameView> frames;
	private ArrayList<ImageView> tabButtons;
	private int displayingFrameId;
	private Animation occAnimation;

	//	private static final int[] HIGHLIGHT_TAB_RES = {R.drawable.clock , R.drawable.message, R.drawable.message};
	//	private static final int[] NORMAL_TAB_RES = {R.drawable.clock_desat , R.drawable.message_desat, R.drawable.message_desat };



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(mainLayoutId());

		initAnimations();
		createFrames();
		processBottomTabs();
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		showOnlyView(displayingFrameId);
	}

	@Override
	protected void onPause() {
		super.onPause();
		frames.get(displayingFrameId).onPause();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		//global dialog comes here
		//		case DIALOG_RATE:
		//			return new RateDialog(this);
		default: return frames.get(displayingFrameId).onCreateDialog(id);
		}
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		//global dialog comes here		
		//		case DIALOG_RATE:
		//			break;
		default: frames.get(displayingFrameId).onPrepareDialog(id);
		}
		super.onPrepareDialog(id, dialog);
	}

	private void initAnimations() {
		occAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_occ);
	}

	private void createFrames() {
		mainFrames = (FrameLayout) findViewById(R.id.main_frames);
		frames = new ArrayList<FrameView>();
		addFrames(frames);
		//		frames.add(new PendingMessageView(this,handler));
		//		frames.add(new SentMessageView( this,handler ));
		for(int i = 0 ; i < frames.size() ; i++) {
			mainFrames.addView( frames.get(i), new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			frames.get(i).onCreate(getApplicationContext());
		}

		Logger.logInfo("Calling .addFrames()");
		displayingFrameId = getDefaultFrame();
		
	}


	private void processBottomTabs() {

		tabButtons = new ArrayList<ImageView>();
		int[] buttonIds = provideTabIds();
		for(int i = 0 ; i < buttonIds.length ; i++) {
			ImageView view = (ImageView) findViewById(buttonIds[i]);
			tabButtons.add(view);
		}
		//		tabButtons.add((ImageView) findViewById(R.id.bt_tab_pending));
		//		tabButtons.add((ImageView) findViewById(R.id.bt_tab_sent));

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





	private void showOnlyView(int id) {
		highlightTab(id);
		int previousDisplaying = displayingFrameId;
		for(int i = 0 ; i < frames.size() ; i++) {
			if ( id != i ) {
				frames.get(i).setVisibility(View.INVISIBLE);
			}
		}
		displayingFrameId = id;
		frames.get(id).startAnimation(occAnimation);
		frames.get(id).setVisibility(View.VISIBLE);
		if ( previousDisplaying != displayingFrameId ) {
			frames.get(previousDisplaying).onPause();
		}
		frames.get(id).onResume();
		
	}

	private void highlightTab(int id) {
		for(int i =  0 ; i < frames.size() ; i++) {
			ImageView imageView = tabButtons.get(i);
			if ( i == id )
				//				imageView.setImageResource(HIGHLIGHT_TAB_RES[i]);
				imageView.setImageResource(highlightTabResource(i));
			else 
				//				imageView.setImageResource(NORMAL_TAB_RES[i]);
				imageView.setImageResource(normalTabResource(i));
		}
	}


}
package dtd.phs.sil;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;

public class MainActivity extends Activity {
    private FrameLayout mainFrames;

	private Handler handler = new Handler();

	private ArrayList<FrameView> frames;

	private ImageView btPending;

	private ImageView btSent;

	private ArrayList<ImageView> tabButtons;

    static final int FRAME_PENDING = 0;
    static final int FRAME_SENT = 1;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mainFrames = (FrameLayout) findViewById(R.id.main_frames);
        addFrames();
        processBottomTabs();
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
    	showOnlyView(savedFrameId());
    }

	private void addFrames() {
		frames = new ArrayList<FrameView>();
		frames.add(new PendingMessageView( this, handler ));
		frames.add(new SentMessageView( this,handler ));

		for(int i = 0 ; i < frames.size() ; i++) {
			mainFrames.addView( frames.get(i), new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
	}
	
	private int savedFrameId() {
		return FRAME_PENDING;
	}

	private void showOnlyView(int id) {
		for(int i = 0 ; i < frames.size() ; i++) {
			int displayed = View.INVISIBLE;
			if ( id == i ) displayed = View.VISIBLE;
			frames.get(i).setVisibility(displayed);
			frames.get(i).onDisplayed();
		}
	}
	
}
package dtd.phs.sil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class MainActivity extends Activity {
    private FrameLayout mainFrames;

	private Handler handler = new Handler();

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

	private void addFrames() {
		mainFrames.addView( new PendingMessageView( this, handler ),new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
		mainFrames.addView( new SentMessageView( this,handler ),new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
        showOnlyView(savedFrameId());
	}
	
	private int savedFrameId() {
		return FRAME_PENDING;
	}

	private void showOnlyView(int id) {
		for(int i = 0 ; i < mainFrames.getChildCount() ; i++) {
			int displayed = View.INVISIBLE;
			if ( i == id ) displayed = View.VISIBLE;
			mainFrames.getChildAt(i).setVisibility(displayed);
		}
	}
	
}
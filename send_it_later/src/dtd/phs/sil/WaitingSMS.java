package dtd.phs.sil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class WaitingSMS extends Activity {

	private TextView textView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.stub_text_view);
	    textView = (TextView) findViewById(R.id.tvStub);
	    textView.setText("Waiting SMS...");
	}

}

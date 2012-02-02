package dtd.phs.sil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ListView;
import dtd.phs.sil.data.DataCenter;
import dtd.phs.sil.data.IDataLoader;
import dtd.phs.sil.entities.SentMessagesList;
import dtd.phs.sil.utils.Helpers;
import dtd.phs.sil.utils.Logger;

public class SentMessageView extends FrameView implements IDataLoader {

	protected static final int MESSAGES_FRAME = 1;
	private static final int WAIT_FRAME = 0;
	private FrameLayout mainFrames;
	private ListView list;
	private SentMessagesAdapter adapter;

	public SentMessageView(Activity hostedActivity, Handler handler) {
		super(hostedActivity, handler);
	}

	@Override
	void onCreate(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.sent_messages, this);
		createFrames();
	}
	

	private void createFrames() {
		mainFrames = (FrameLayout) findViewById(R.id.main_frames);
		list = (ListView) findViewById(R.id.listSent);
		adapter = new SentMessagesAdapter(hostedActivity.getApplicationContext(), new SentMessagesList());
		list.setAdapter(adapter);
		loadSentDataAsync();
	}

	private void loadSentDataAsync() {
		Helpers.showOnlyView(mainFrames, WAIT_FRAME );
		DataCenter.loadSentMessages( this.getContext(),this );
	}

	@Override
	public void onGetDataFailed(final Exception e) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Logger.logError(e);
				hostedActivity.finish();
			}
		});
	}

	@Override
	public void onGetDataSuccess(final Object data) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				SentMessagesList list = (SentMessagesList) data;
				adapter.setMessages( list );
				adapter.notifyDataSetChanged();
				Helpers.showOnlyView(mainFrames, MESSAGES_FRAME);
			}
		});

	}

	@Override
	public void onDisplayed() {
		loadSentDataAsync();
	}

	@Override
	public void onResume() {
		loadSentDataAsync();
	}

	@Override
	public Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}

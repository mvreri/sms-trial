package dtd.phs.sil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import dtd.phs.sil.data.DataCenter;
import dtd.phs.sil.data.IDataLoader;
import dtd.phs.sil.entities.PendingMessagesList;
import dtd.phs.sil.utils.Helpers;
import dtd.phs.sil.utils.Logger;

public class PendingMessageView 
	extends FrameView
	implements IDataLoader
{

	private static final int WAIT_FRAME = 0;
	private static final int MESSAGES_FRAME = 1;
	private View topFrame;
	private View btAdd;
	private FrameLayout mainFrames;
	private ListView list;
	private PendingMessageAdapter adapter;
	
	public PendingMessageView(Activity hostedActivity, Handler handler) {
		super(hostedActivity, handler);
	}

	@Override
	void onCreate(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.pending_messages, this);
		createViews();
	}
	
	private void createViews() {
		createTopBar();
		createMainFrames();
	}

	private void createMainFrames() {
		mainFrames = (FrameLayout) findViewById(R.id.pending_main_frames);
		list = (ListView) findViewById(R.id.listPending);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				EditMessage.passedMessage = adapter.getMessage(position);
				Intent i = new Intent(getContext(),EditMessage.class);
				hostedActivity.startActivity(i);
			}
		});
		adapter = new PendingMessageAdapter(hostedActivity.getApplicationContext(),new PendingMessagesList() );
		list.setAdapter(adapter);
		loadPendingMessageAsync();		
	}

	private void loadPendingMessageAsync() {
		Helpers.showOnlyView(mainFrames,WAIT_FRAME);
		DataCenter.loadPendingMessages( this, getContext());
	}

	private void createTopBar() {
		topFrame = findViewById(R.id.top_bar_pending);
		btAdd = topFrame.findViewById(R.id.btAdd);
		btAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(hostedActivity.getApplicationContext() , EditMessage.class);
				i.putExtra(EditMessage.EDIT_TYPE,EditMessage.TYPE_NEW);
				hostedActivity.startActivity(i);
			}
		});
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
				PendingMessagesList list = (PendingMessagesList) data;
				adapter.setMessages( list );
				adapter.notifyDataSetChanged();
				Helpers.showOnlyView(mainFrames, MESSAGES_FRAME);
				
			}
		});
	}

	@Override
	public void onDisplayed() {

	}

	@Override
	public void onResume() {
		loadPendingMessageAsync();
	}


}

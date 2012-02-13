package dtd.phs.sil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import dtd.phs.sil.data.DataCenter;
import dtd.phs.sil.data.IDataLoader;
import dtd.phs.sil.entities.SentMessageItem;
import dtd.phs.sil.entities.SentMessagesList;
import dtd.phs.sil.ui.SentMessageDialog;
import dtd.phs.sil.utils.Helpers;
import dtd.phs.sil.utils.Logger;
import dtd.phs.sil.utils.PreferenceHelpers;

public class SentMessageView extends FrameView implements IDataLoader {

	private static final int WAIT_FRAME = 0;
	protected static final int MESSAGES_FRAME = 1;
	protected static final int DIALOG_OPTIONS = 0;
	private FrameLayout mainFrames;
	private ListView list;
	private SentMessagesAdapter adapter;
	private SentMessageDialog dialogOptions;
	private TextView tvEdit;
	private boolean onEditMode;

	public SentMessageView(Activity hostedActivity, Handler handler) {
		super(hostedActivity, handler);
		dialogOptions = new SentMessageDialog(hostedActivity, this);
	}

	@Override
	void onCreate(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.sent_messages, this);
		createViews();
	}

	private void createViews() {
		createTopbar();
		createFrames();
	}
	

	private void createTopbar() {
		tvEdit = (TextView) findViewById(R.id.tvEdit);
		onEditMode = false;
		tvEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.post(new Runnable() {
					public void run() {
						if ( ! onEditMode ) {
							adapter.displayAllDeleteButton();
							onEditMode = true;
							tvEdit.setText(R.string.Done);
						} else {
							onEditMode = false;
							adapter.clearAllDeleteButton();
							tvEdit.setText(R.string.Edit);
						}
						
					}
				});
			}
		});
	}

	private void createFrames() {
		
		
		mainFrames = (FrameLayout) findViewById(R.id.main_frames);
		list = (ListView) findViewById(R.id.listSent);
		
		adapter = new SentMessagesAdapter(hostedActivity.getApplicationContext(), new SentMessagesList()) {
			@Override
			public void onItemLongClick(int position) {
			}
			
			@Override
			public void onItemClick(final View view, int position) {
				//TODO: prepare & show dialog
				final Drawable oldBackground = view.getBackground();
				view.setBackgroundColor(getResources().getColor(R.color.blur_blue));
				Helpers.startAfter(300, new Runnable() {
					@Override
					public void run() {
						view.post(new Runnable() {
							@Override
							public void run() {
								view.setBackgroundDrawable(oldBackground);
							}
						});
						
					}
				});				
				//TODO: next version
//				dialogOptions.setMessage((SentMessageItem) adapter.getMessage(position));
//				showDialog(DIALOG_OPTIONS);
				EditMessage.passedSentMessage = (SentMessageItem) adapter.getMessage(position);
				Intent i = new Intent(getContext(),EditMessage.class);
				hostedActivity.startActivity(i);
			}
		};
		
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
			}
		});
	}

	@Override
	public void onGetDataSuccess(final Object data) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				SentMessagesList list = (SentMessagesList) data;
				SentMessagesList.sortByTime(list);
				int maxSentSize = getMaxSentSize();
				if ( list.size() > maxSentSize) {
					DataCenter.cleanUpSentMessages(getContext(),maxSentSize);
				}
				SentMessagesList.cutList(list,maxSentSize);
				adapter.setMessages( list );
				adapter.notifyDataSetChanged();
				Helpers.showOnlyView(mainFrames, MESSAGES_FRAME);
			}
		});

	}

	protected int getMaxSentSize() {
		return PreferenceHelpers.getMaxSentSize(getContext());
	}

	@Override
	public void onResume() {
		updateEditMode();
		loadSentDataAsync();
	}

	private void updateEditMode() {
		onEditMode = false;
		tvEdit.setText(R.string.Edit);
	}

	@Override
	public Dialog onCreateDialog(int id) {
		return dialogOptions;
	}
	
	@Override
	public void onPrepareDialog(int id) {
		dialogOptions.prepare();
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onRefresh() {
		loadSentDataAsync();
	}



}

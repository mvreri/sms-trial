package dtd.phs.sil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import dtd.phs.sil.data.DataCenter;
import dtd.phs.sil.data.IDBLinked;
import dtd.phs.sil.data.IDataLoader;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.PendingMessagesList;
import dtd.phs.sil.ui.RemovePendingItemDialog;
import dtd.phs.sil.utils.Helpers;
import dtd.phs.sil.utils.Logger;

public class PendingMessageView 
extends FrameView
implements 
IDataLoader,
IDBLinked
{

	private static final int WAIT_FRAME = 0;
	private static final int MESSAGES_FRAME = 1;
	private FrameLayout mainFrames;
	private ListView list;
	private PendingMessageAdapter adapter;
	
	protected static final int DIALOG_REMOVE_PENDING_ITEM = 0;
	private RemovePendingItemDialog dialogRemovePendingItem;
	private boolean onEditMode;
	private TextView tvEdit;

	public PendingMessageView(Activity hostedActivity, Handler handler) {
		super(hostedActivity, handler);
	}

	@Override
	void onCreate(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.pending_messages, this);
		dialogRemovePendingItem = new RemovePendingItemDialog(hostedActivity);
		createViews();

	}

	private void createViews() {
		createTopBar();
		createMainFrames();
	}

	private void createMainFrames() {
		mainFrames = (FrameLayout) findViewById(R.id.pending_main_frames);
		list = (ListView) findViewById(R.id.listPending);

		adapter = new PendingMessageAdapter(hostedActivity.getApplicationContext(),new PendingMessagesList() ) {

			@Override
			public void onItemClick(View view,int position) {
				PendingMessageView.this.onItemClick(view,position);
			}

			@Override
			public void onItemLongClick(int position) {
//				PendingMessageView.this.onItemLongClick(position);				
			}

		};
		list.setAdapter(adapter);
		loadPendingMessageAsync();		
	}

	protected void onItemClick(final View view, int position) {
		EditMessage.passedMessage = (PendingMessageItem) adapter.getMessage(position);
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
		Intent i = new Intent(getContext(),EditMessage.class);
		Helpers.enterActivity(hostedActivity,i);
			
	}
//
//	private void onItemLongClick(int position) {
//		dialogRemovePendingItem.setRemovedRowId(adapter.getMessageRowId(position));
//		dialogRemovePendingItem.setLinkedDBObject(PendingMessageView.this);
//		showDialog(DIALOG_REMOVE_PENDING_ITEM);
//	}

	private void loadPendingMessageAsync() {
		Helpers.showOnlyView(mainFrames,WAIT_FRAME);
		DataCenter.loadPendingMessages( this, getContext());
	}

	private void createTopBar() {
		View topFrame = findViewById(R.id.top_bar_pending);
		tvEdit = (TextView) topFrame.findViewById(R.id.tvEdit);
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
		View btAdd = topFrame.findViewById(R.id.btAdd);
		btAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(hostedActivity.getApplicationContext() , EditMessage.class);
				i.putExtra(EditMessage.EDIT_TYPE,EditMessage.TYPE_NEW);
				Helpers.enterActivity(hostedActivity, i);
			}
		});
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
				PendingMessagesList list = (PendingMessagesList) data;
				PendingMessagesList.sortByNextOccurence(list);
				adapter.setMessages( list );
				adapter.notifyDataSetChanged();
				Helpers.showOnlyView(mainFrames, MESSAGES_FRAME);

			}
		});
	}

	@Override
	public void onDBUpdated() {
		loadPendingMessageAsync();
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onRefresh() {
		loadPendingMessageAsync();
	}
	
	@Override
	public void onResume() {
		updateEditMode();
		loadPendingMessageAsync();
	}

	private void updateEditMode() {
		onEditMode = false;
		tvEdit.setText(R.string.Edit);
	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_REMOVE_PENDING_ITEM:
			return dialogRemovePendingItem;
		default:
			break;
		}
		return null;
	}
	
	@Override
	public void onPrepareDialog(int id) {
	}






}

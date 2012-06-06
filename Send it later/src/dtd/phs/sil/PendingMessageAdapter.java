package dtd.phs.sil;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import dtd.phs.sil.data.DataCenter;
import dtd.phs.sil.entities.MessageItem;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.PendingMessagesList;
import dtd.phs.sil.ui.OnListItemTouchListener;

public abstract class PendingMessageAdapter extends MessageAdapter {


//	private static final int STUB_AVATAR = R.drawable.contact;

	public PendingMessageAdapter(Context context, PendingMessagesList pendingMessagesList) {
		super(context,pendingMessagesList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return super.getView(position, convertView, R.layout.pending_item);
	}


	protected void processOnItemTouch(int position, View view) {
		view.setOnTouchListener(new OnListItemTouchListener(position,view) {
			
			@Override
			public void onSwipe(View view,int position) {
				
				View delete = view.findViewById(R.id.btDelete);
				if ( delete.getVisibility() == View.VISIBLE) {	
					makeButtonDispear(delete,position);
				} else {
					makeButtonOccur(position, delete);
				}
			}


			
			@Override
			public void onLongClick(int position) {
				onItemLongClick(position);
			}
			
			@Override
			public void onClick(View view,int position) {
				View delete = view.findViewById(R.id.btDelete);
				if ( delete.getVisibility() == View.VISIBLE ) {
					makeButtonDispear(delete,position);
				} else {
					onItemClick(view,position);
				}
			}
		});
	}


	protected void updateView(ViewHolder holder, final MessageItem message, int position) {
//		holder.avatar.setImageResource(STUB_AVATAR);
		holder.contact.setText(message.getContact());
		holder.content.setText(message.getContent());
		updateDeleteButton(holder.delete, position, message);
		updateNext(holder, (PendingMessageItem)message);
	}
	
	protected void modifyDatabaseOnDeleteButtonClick(long id) {
		DataCenter.removePendingItem(context, id);
	}

	private void updateNext(ViewHolder holder, PendingMessageItem message) {
		String next = context.getResources().getString(R.string.next);
		Resources res = context.getResources();
		String nextTime = message.getNextTime(context);
		if ( nextTime == null) {
			nextTime = next + ": " + context.getResources().getString(R.string.Never);
			holder.status.setTextColor(res.getColor(R.color.red));
		}
		else {
			nextTime = next +": "+ nextTime;
			holder.status.setTextColor(res.getColor(R.color.mainTextColor));
		}
		holder.status.setText(nextTime);
	}



}

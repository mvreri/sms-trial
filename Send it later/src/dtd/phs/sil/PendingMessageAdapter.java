package dtd.phs.sil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
//		View view = convertView;
//		ViewHolder holder;
//		if ( view == null ) {
//			view = Helpers.inflate(context,R.layout.pending_item);
//			holder = new ViewHolder();
//			createHolder(view, holder);
//			view.setTag(holder);
//		} else {
//			holder = (ViewHolder) view.getTag();
//		}
//		updateView(view,holder,(PendingMessageItem)messages.get(position),position);
//		processOnItemTouch(position, view);
//		return view;
	}


	protected void processOnItemTouch(int position, View view) {
		view.setOnTouchListener(new OnListItemTouchListener(position,view) {
			
			@Override
			public void onSwipe(View view,int position) {
				
				View delete = view.findViewById(R.id.btDelete);
				if ( delete.getVisibility() == View.VISIBLE) {	
					makeButtonDispear(delete,position);
				} else {
					displayingDeleteButton.set(position, true);
					delete.startAnimation(occAnim);
					delete.setVisibility(View.VISIBLE);
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
		String nextTime = message.getNextTime();
		if ( nextTime == null) 
			nextTime = next + ": Never";
		else nextTime = next +": "+ nextTime;
		holder.status.setText(nextTime);
	}

}

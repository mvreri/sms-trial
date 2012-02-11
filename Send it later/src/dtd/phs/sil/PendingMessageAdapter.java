package dtd.phs.sil;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import dtd.phs.sil.data.DataCenter;
import dtd.phs.sil.entities.PendingMessageItem;
import dtd.phs.sil.entities.PendingMessagesList;
import dtd.phs.sil.ui.OnListItemTouchListener;
import dtd.phs.sil.utils.Helpers;

public abstract class PendingMessageAdapter extends MessageAdapter {


//	private static final int STUB_AVATAR = R.drawable.contact;

	public PendingMessageAdapter(Context context, PendingMessagesList pendingMessagesList) {
		super(context,pendingMessagesList);
	}


	public class ViewHolder {
		public ImageView avatar;
		public TextView contact;
		public TextView content;
		public TextView status;
		public Button delete;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if ( view == null ) {
			view = Helpers.inflate(context,R.layout.pending_item);
			holder = new ViewHolder();
			createHolder(view, holder);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		updateView(view,holder,(PendingMessageItem)messages.get(position),position);

		view.setOnTouchListener(new OnListItemTouchListener(position,view) {
			
			@Override
			public void onSwipe(View view,int position) {
				
				View delete = view.findViewById(R.id.btDelete);
				if ( delete.getVisibility() == View.VISIBLE) {	
					makeButtonDisapear(delete,position);
				} else {
					displayingDeleteButton.set(position, true);
					delete.startAnimation(occAnim);
					delete.setVisibility(View.VISIBLE);
				}
			}
			
			private void makeButtonDisapear(View delete, int position) {
				displayingDeleteButton.set(position, false);
				delete.startAnimation(disAnim);
				delete.setVisibility(View.GONE);
			}
			
			@Override
			public void onLongClick(int position) {
				onItemLongClick(position);
			}
			
			@Override
			public void onClick(View view,int position) {
				View delete = view.findViewById(R.id.btDelete);
				if ( delete.getVisibility() == View.VISIBLE ) {
					makeButtonDisapear(delete,position);
				} else {
					onItemClick(view,position);
				}
			}
		});
		return view;
	}


	abstract public void onItemClick(View view, int position);
	abstract public void onItemLongClick(int position);

	private void updateView(View view, ViewHolder holder, final PendingMessageItem message, int position) {

//		holder.avatar.setImageResource(STUB_AVATAR);
		holder.contact.setText(message.getContact());
		holder.content.setText(message.getContent());
		updateNext(holder, message);
		if (displayingDeleteButton.get(position)) {
			holder.delete.setVisibility(View.VISIBLE);
		} else holder.delete.setVisibility(View.GONE);
		holder.delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				long id = message.getId();
				DataCenter.removePendingItem(context, id);
				int index = messages.removeMessageWithId(id);
				if ( index !=- 1) {
					displayingDeleteButton.remove(index);
					v.post(new Runnable() {
						@Override
						public void run() {
							v.setVisibility(View.GONE);
						}
					});
					
					Toast.makeText(context, R.string.message_removed_success, Toast.LENGTH_SHORT).show();
					notifyDataSetChanged();
				} else {
					Toast.makeText(context, R.string.message_removed_failed, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void updateNext(ViewHolder holder, PendingMessageItem message) {
		String next = context.getResources().getString(R.string.next);
		String nextTime = message.getNextTime();
		if ( nextTime == null) 
			nextTime = next + ": Never";
		else nextTime = next +": "+ nextTime;
		holder.status.setText(nextTime);
	}

	private void createHolder(View view, ViewHolder holder) {
		holder.avatar = (ImageView)view.findViewById(R.id.ivAvatar);
		holder.contact = (TextView) view.findViewById(R.id.tvContact);
		holder.content = (TextView) view.findViewById(R.id.tvContent);
		holder.status = (TextView) view.findViewById(R.id.tvStatus);
		holder.delete = (Button) view.findViewById(R.id.btDelete);
	}

}

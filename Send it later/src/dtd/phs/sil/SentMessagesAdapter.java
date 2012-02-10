package dtd.phs.sil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import dtd.phs.sil.data.DataCenter;
import dtd.phs.sil.entities.SentMessageItem;
import dtd.phs.sil.entities.SentMessagesList;
import dtd.phs.sil.ui.OnListItemTouchListener;
import dtd.phs.sil.utils.Helpers;

public abstract class SentMessagesAdapter extends BaseAdapter {

	
//	private static final int STUB_AVATAR = R.drawable.contact;
	private Context context;
	private SentMessagesList messages;

	public SentMessagesAdapter(Context applicationContext,SentMessagesList sentMessagesList) {
		this.context = applicationContext;
		this.messages = sentMessagesList;
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class ViewHolder {

		public ImageView avatar;
		public TextView contact;
		public TextView content;
		public TextView status;
		public ImageView failedIcon;
		public Button delete;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		Logger.logInfo("Position: " + position);
		View v = convertView;
		ViewHolder holder;
		if ( v == null ) {
			v = Helpers.inflate(context, R.layout.sent_item);
			holder = new ViewHolder();
			createHolder(v, holder);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		updateView( holder, messages.get(position) );
		v.setOnTouchListener(new OnListItemTouchListener(position,v) {
			
			@Override
			public void onSwipe(View view, int position) {
				View delete = view.findViewById(R.id.btDelete);
				if ( delete.getVisibility() == View.VISIBLE) {
					delete.setVisibility(View.GONE);			
					updateMessageDeliveredIcon(view.findViewById(R.id.ivFailed), messages.get(position));
				} else {
					delete.setVisibility(View.VISIBLE);
					view.findViewById(R.id.ivFailed).setVisibility(View.GONE);
				}
			}
			
			@Override
			public void onLongClick(int position) {
				onItemLongClick(position);
			}
			
			@Override
			public void onClick(View view, int position) {
				View delete = view.findViewById(R.id.btDelete);
				if ( delete.getVisibility() == View.VISIBLE) {
					delete.setVisibility(View.GONE);
					updateMessageDeliveredIcon(view.findViewById(R.id.ivFailed), messages.get(position));
				} else {
					onItemClick(view,position);
				}
			}
		});
		return v;
	}

	abstract public void onItemClick(View view, int position);
	abstract public void onItemLongClick(int position);

	private void updateView(ViewHolder holder, final SentMessageItem message) {
//		holder.avatar.setImageResource(STUB_AVATAR);
		holder.contact.setText(message.getContact());
		holder.content.setText(message.getContent());
		holder.status.setText(message.getStatus());
		updateMessageDeliveredIcon(holder.failedIcon, message);
		holder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				long id = message.getId();
				DataCenter.removeSentItem(context,id);
				if ( messages.removeMessageWithId(id)) {
					v.setVisibility(View.GONE);
					Toast.makeText(context, R.string.message_removed_success, Toast.LENGTH_LONG).show();
					notifyDataSetChanged();
				} else {
					Toast.makeText(context, R.string.message_removed_failed, Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private void updateMessageDeliveredIcon(View failedIcon,
			SentMessageItem message) {
		if ( ! message.isDelivered() ) {
			failedIcon.setVisibility(View.VISIBLE );
		} else failedIcon.setVisibility(View.INVISIBLE );
	}

	private void createHolder(View v, ViewHolder holder) {
		holder.avatar = (ImageView) v.findViewById(R.id.ivAvatar);
		holder.contact = (TextView) v.findViewById(R.id.tvContact);
		holder.content = (TextView) v.findViewById(R.id.tvContent);
		holder.status = (TextView) v.findViewById(R.id.tvStatus);
		holder.failedIcon = (ImageView) v.findViewById(R.id.ivFailed);
		holder.delete = (Button) v.findViewById(R.id.btDelete);
	}

	public void setMessages(SentMessagesList list) {
		messages = list;
	}

	public SentMessageItem getMessage(int position) {
		return messages.get(position);
	}

}

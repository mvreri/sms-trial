package dtd.phs.sil;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
	private Animation occAnim;
	private Animation disAnim;
	private ArrayList<Boolean> displayingDeleteButton;

	public SentMessagesAdapter(Context applicationContext,SentMessagesList sentMessagesList) {
		this.context = applicationContext;
		this.messages = sentMessagesList;
		initDisplayingDeleteButton();
		this.occAnim = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
		this.disAnim = AnimationUtils.loadAnimation(context, R.anim.push_right_out);
	}

	private void initDisplayingDeleteButton() {
		displayingDeleteButton = new ArrayList<Boolean>();
		for(int i = 0 ; i < messages.size() ; i++) displayingDeleteButton.add(new Boolean(false));
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
		updateView( holder, messages.get(position), position );
		v.setOnTouchListener(new OnListItemTouchListener(position,v) {
			
			@Override
			public void onSwipe(View view, int position) {
				View delete = view.findViewById(R.id.btDelete);
				if ( delete.getVisibility() == View.VISIBLE) {
					makeButtonDispear(delete, position);			
					updateMessageDeliveredIcon(view.findViewById(R.id.ivFailed), messages.get(position));
				} else {
					displayingDeleteButton.set(position, true);
					delete.startAnimation(occAnim);
					delete.setVisibility(View.VISIBLE);
					view.findViewById(R.id.ivFailed).setVisibility(View.GONE);
				}
			}

			private void makeButtonDispear(View delete,int position) {
				displayingDeleteButton.set(position, false);
				delete.startAnimation(disAnim);
				delete.setVisibility(View.GONE);
			}
			
			@Override
			public void onLongClick(int position) {
				onItemLongClick(position);
			}
			
			@Override
			public void onClick(View view, int position) {
				View delete = view.findViewById(R.id.btDelete);
				if ( delete.getVisibility() == View.VISIBLE) {
					makeButtonDispear(delete,position);
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

	private void updateView(ViewHolder holder, final SentMessageItem message, int position) {
//		holder.avatar.setImageResource(STUB_AVATAR);
		holder.contact.setText(message.getContact());
		holder.content.setText(message.getContent());
		holder.status.setText(message.getStatus());
		updateMessageDeliveredIcon(holder.failedIcon, message);
		if ( displayingDeleteButton.get(position) ) {
			holder.delete.setVisibility(View.VISIBLE);
		} else holder.delete.setVisibility(View.GONE);
		holder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				long id = message.getId();
				DataCenter.removeSentItem(context,id);
				int index = messages.removeMessageWithId(id);
				if ( index != -1 ) {
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
		initDisplayingDeleteButton();
	}

	public SentMessageItem getMessage(int position) {
		return messages.get(position);
	}

}

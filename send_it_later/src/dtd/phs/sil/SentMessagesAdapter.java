package dtd.phs.sil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dtd.phs.sil.entities.SentMessageItem;
import dtd.phs.sil.entities.SentMessagesList;
import dtd.phs.sil.utils.Helpers;

public class SentMessagesAdapter extends BaseAdapter {

	
	private static final int STUB_AVATAR = R.drawable.ic_launcher;
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

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
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
		return v;
	}

	private void updateView(ViewHolder holder, SentMessageItem message) {
		holder.avatar.setImageResource(STUB_AVATAR);
		holder.contact.setText(message.getContact());
		holder.content.setText(message.getContent());
		holder.status.setText(message.getStatus());
	}

	private void createHolder(View v, ViewHolder holder) {
		holder.avatar = (ImageView) v.findViewById(R.id.ivAvatar);
		holder.contact = (TextView) v.findViewById(R.id.tvContact);
		holder.content = (TextView) v.findViewById(R.id.tvContent);
		holder.status = (TextView) v.findViewById(R.id.tvStatus);
	}

	public void setMessages(SentMessagesList list) {
		messages = list;
	}

}

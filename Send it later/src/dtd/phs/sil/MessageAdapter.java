package dtd.phs.sil;

import java.util.ArrayList;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import dtd.phs.sil.entities.IMessagesList;
import dtd.phs.sil.entities.MessageItem;

abstract public class MessageAdapter extends BaseAdapter {
	protected IMessagesList messages;
	protected Context context;
	
	protected Animation occAnim;
	protected Animation disAnim;
	protected ArrayList<Boolean> displayingDeleteButton;
	public MessageAdapter(
			Context context,
			IMessagesList messagesList) {
		this.context = context;
		this.messages = messagesList;
		initDisplayingDeleteButton();
		this.occAnim = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
		this.disAnim = AnimationUtils.loadAnimation(context,R.anim.push_right_out);
	}
	
	protected void initDisplayingDeleteButton() {
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
	
	public void setMessages(IMessagesList list) {
		this.messages = list;
		initDisplayingDeleteButton();
	}

	public MessageItem getMessage(int position) {
		return messages.get(position);

	}
	
	public long getMessageRowId(int position) {
		return messages.get(position).getId();
	}

}

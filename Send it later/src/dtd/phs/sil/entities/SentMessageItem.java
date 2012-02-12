package dtd.phs.sil.entities;

import dtd.phs.sil.utils.Helpers;

public class SentMessageItem extends MessageItem {

	
	boolean isDelivered = false;
	private long sentTime;
	private long pendingId;
	
	public boolean isDelivered() {
		return isDelivered;
	}
	public void setDelivered(boolean isDelivered) {
		this.isDelivered = isDelivered;
	}

	public String getContact() {
		String contact = "";
		String[] names = getNames();
		if ( names.length != 1 ) {
			for(int i =  0 ; i < names.length - 1 ; i++)
				contact += names[i] + " ; ";
			contact += names[names.length-1];
		} else {
			if ( names[0].equals(phoneNumbers[0])) return names[0];
			contact= names[0] + " (" + getPhoneNumbers()[0] + ")";
		}
		return contact;
	}

	public String getStatus() {
		String status = "";
		if ( isDelivered() ) {
			status += "Delivered: ";
		} else status += "Sent failed: ";
		status += Helpers.formatTime(getSentTime()); 
		return status;
	}

	public static SentMessageItem createFromPendingMessage(
			PendingMessageItem messageItem, boolean isDelivered) {
		return createInstance(
				messageItem.getId(),
				messageItem.getNames(), 
				messageItem.getPhoneNumbers(), 
				messageItem.getContent(), 
				isDelivered, 
				System.currentTimeMillis());
	}
	
	public static SentMessageItem createInstance(
			long pending_id, 
			String[] names,
			String[] numbers,
			String content,
			boolean isDelivered,
			long sentTime
			) {
		SentMessageItem sentMessage = new SentMessageItem();
		sentMessage.setNames(names);
		sentMessage.setPhoneNumbers(numbers);
		sentMessage.setContent(content);
		sentMessage.setDelivered(isDelivered);
		sentMessage.setSentTime(sentTime);
		sentMessage.setPendingId(pending_id);
	
		return sentMessage;
	}
	

	private void setPendingId(long pendingId) {
		this.pendingId = pendingId;
	}

	public long getPendingId() {
		return pendingId;
	}

	public void setSentTime(long currentTimeMillis) {
		sentTime = currentTimeMillis;
	}

	public long getSentTime() {
		return sentTime;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long _id) {
		id = _id;
	}


}

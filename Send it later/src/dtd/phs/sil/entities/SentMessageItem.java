package dtd.phs.sil.entities;

import dtd.phs.sil.utils.Helpers;

public class SentMessageItem {

	String content;
	boolean isDelivered = false;
	private String[] names;
	private String[] numbers;
	private long sentTime;
	
	public boolean isDelivered() {
		return isDelivered;
	}

	public void setDelivered(boolean isDelivered) {
		this.isDelivered = isDelivered;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContact() {
		String contact = "";
		String[] names = getNames();
		if ( names.length != 1 ) {
			for(int i =  0 ; i < names.length - 1 ; i++)
				contact += names[i] + " ; ";
			contact += names[names.length-1];
		} else {
			contact= names[0] + " (" + getPhonenumbers()[0] + ")";
		}
		return contact;
	}

	public String getContent() {
		return content;
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
				messageItem.getNames(), 
				messageItem.getPhoneNumbers(), 
				messageItem.getContent(), 
				isDelivered, 
				System.currentTimeMillis());
	}
	
	public static SentMessageItem createInstance(
			String[] names,
			String[] numbers,
			String content,
			boolean isDelivered,
			long sentTime
			) {
		SentMessageItem sentMessage = new SentMessageItem();
		sentMessage.setNames(names);
		sentMessage.setPhonenumbers(numbers);
		sentMessage.setContent(content);
		sentMessage.setDelivered(isDelivered);
		sentMessage.setSentTime(sentTime);
	
		return sentMessage;
	}
	

	private void setSentTime(long currentTimeMillis) {
		sentTime = currentTimeMillis;
	}

	private void setPhonenumbers(String[] phoneNumbers) {
		this.numbers = phoneNumbers;
	}

	private void setNames(String[] names) {
		this.names = names;
	}

	public String[] getNames() {
		return names;
	}

	public String[] getPhonenumbers() {
		return numbers;
	}

	public long getSentTime() {
		return sentTime;
	}


}

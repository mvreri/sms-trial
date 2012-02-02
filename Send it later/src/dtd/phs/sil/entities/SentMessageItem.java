package dtd.phs.sil.entities;

public class SentMessageItem {

	String contact;
	String content;
	String status;
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

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContact() {
		return contact;
	}

	public String getContent() {
		return content;
	}

	public String getStatus() {
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

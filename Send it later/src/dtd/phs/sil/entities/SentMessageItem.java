package dtd.phs.sil.entities;

public class SentMessageItem {

	String contact;
	String content;
	String status;
	boolean isDelivered = false;
	
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


}

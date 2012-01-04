package dtd.phs.sil.entities;

public class PendingMessageItem {

	public String getNextTime() {
		return nextTime;
	}
	public void setNextTime(String nextTime) {
		this.nextTime = nextTime;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	String contact;
	String content;
	String nextTime;


}

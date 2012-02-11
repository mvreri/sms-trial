package dtd.phs.sil.entities;

public abstract class MessageItem {
	protected long id; // auto id in database
	protected String[] names;
	protected String[] phoneNumbers; 
	protected String content;
	
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}
	public String[] getPhoneNumbers() {return phoneNumbers;}	
	public void setPhoneNumbers(String[] phoneNumbers) {this.phoneNumbers = phoneNumbers;}
	public String getContent() {return content;}
	public void setContent(String content) {this.content = content;}
	public String[] getNames() {return names;}
	public void setNames(String[] names) { this.names = names; }
	abstract public String getContact();

}

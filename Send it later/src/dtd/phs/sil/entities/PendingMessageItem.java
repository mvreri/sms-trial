package dtd.phs.sil.entities;

import java.util.Calendar;

import dtd.phs.sil.ui.AlertHelpers;
import dtd.phs.sil.utils.FrequencyHelpers;
import dtd.phs.sil.utils.FrequencyHelpers.Frequencies;

public class PendingMessageItem {


	long id; // auto id in database
	String[] phoneNumbers;  
	String[] names; 
	String content;
	String contact;
	String nextTime;
	Calendar startDateTime;	//saved in db as long
	
	public Calendar getStartDateTime() {return startDateTime;}
	public void setStartDateTime(Calendar startDateTime) {this.startDateTime = startDateTime;}

	
	FrequencyHelpers.Frequencies freq; //saved in db as index of this type
	AlertHelpers.AlertTypes alert; //saved in db as index of this type

	
	public static PendingMessageItem createInstance(
			long id, 
			String[] names,
			String[] phoneNumbers,
			String content,
			long dateTime,
			int freqIndex,
			int alertIndex)
	{
		PendingMessageItem item = new PendingMessageItem();
		item.setId(id);
		item.setNames( names );
		item.setPhoneNumbers(phoneNumbers);
		item.setContent(content);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateTime);
		item.setStartDateTime(calendar);
		
		item.setFreq(FrequencyHelpers.FREQUENCIES[freqIndex]);
		item.setAlert(AlertHelpers.ALERT_TYPE[alertIndex]);
		
		return item;
	}
	


	public PendingMessageItem() {
		super();
	}


	private void setNames(String[] names) { this.names = names; }
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}
	public FrequencyHelpers.Frequencies getFreq() {return freq;}
	public int getFreqIndex() {return FrequencyHelpers.indexOf(getFreq()) ;}
	public void setFreq(FrequencyHelpers.Frequencies freq) {this.freq = freq;}
	public AlertHelpers.AlertTypes getAlert() {return alert;}
	public int getAlertIndex() { return AlertHelpers.indexOf(alert) ;}
	public void setAlert(AlertHelpers.AlertTypes alert) {this.alert = alert;}
	public void setPhoneNumbers(String[] phoneNumbers) {this.phoneNumbers = phoneNumbers;}
	public String getNextTime() {return nextTime;}
	public void setNextTime(String nextTime) {this.nextTime = nextTime;}
	public String getContact() {return contact;}
	public void setContact(String contact) {this.contact = contact;}
	public String getContent() {return content;}
	public void setContent(String content) {this.content = content;}
	public String[] getPhoneNumbers() {return phoneNumbers;}
	public String[] getNames() {return names;}
	

	


}

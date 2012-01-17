package dtd.phs.sil.entities;

import java.util.Calendar;

import dtd.phs.sil.ui.AlertHelpers;
import dtd.phs.sil.utils.FrequencyHelpers;

public class PendingMessageItem {

	long id; // auto id in database
	String phoneNumber;
	String content;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Calendar getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Calendar startDateTime) {
		this.startDateTime = startDateTime;
	}
	public FrequencyHelpers.Frequencies getFreq() {
		return freq;
	}
	public void setFreq(FrequencyHelpers.Frequencies freq) {
		this.freq = freq;
	}
	public AlertHelpers.AlertTypes getAlert() {
		return alert;
	}
	public void setAlert(AlertHelpers.AlertTypes alert) {
		this.alert = alert;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	Calendar startDateTime;	//saved in db as long
	FrequencyHelpers.Frequencies freq; //saved in db as index of this type
	AlertHelpers.AlertTypes alert; //saved in db as index of this type
	
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
	String nextTime;
	public String getPhoneNumber() {
		return phoneNumber;
	}


}

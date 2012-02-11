package dtd.phs.sil.entities;

import java.util.Calendar;

import dtd.phs.sil.ui.AlertHelpers;
import dtd.phs.sil.utils.FrequencyHelpers;
import dtd.phs.sil.utils.Helpers;

public class PendingMessageItem extends MessageItem {



	
	Calendar startDateTime;	//saved in db as long
	FrequencyHelpers.Frequencies freq; //saved in db as index of this type
	AlertHelpers.AlertTypes alert; //saved in db as index of this type


	public Calendar getStartDateTime() {return startDateTime;}
	public void setStartDateTime(Calendar startDateTime) {this.startDateTime = startDateTime;}

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


	
	public FrequencyHelpers.Frequencies getFreq() {return freq;}
	public int getFreqIndex() {return FrequencyHelpers.indexOf(getFreq()) ;}
	public void setFreq(FrequencyHelpers.Frequencies freq) {this.freq = freq;}
	public AlertHelpers.AlertTypes getAlert() {return alert;}
	public int getAlertIndex() { return AlertHelpers.indexOf(alert) ;}
	public void setAlert(AlertHelpers.AlertTypes alert) {this.alert = alert;}
	
	public String getNextTime() {
		Calendar next = FrequencyHelpers.getNextCalendar(startDateTime, getFreq());
		if ( next != null)
			return Helpers.formatTime(next.getTimeInMillis());
//		new SimpleDateFormat("HH:mm EE - MMMM.dd, yyyy  ").format(new Date(next.getTimeInMillis()));
		else return null;
	}
	
	public Calendar getNextCalendar() {
		return FrequencyHelpers.getNextCalendar(startDateTime, getFreq());
	}
	public long getNextTimeMillis() {
		Calendar nextCalendar = getNextCalendar();
		if ( nextCalendar == null ) return -1;
		return nextCalendar.getTimeInMillis();
	}


	public String getContact() {
		if ( names.length == 0 ) return "";
		if ( names.length == 1 ) {
			if ( names[0].equals(phoneNumbers[0])) return names[0];
			return mergeInfo(0);
		}
		String contact = names[0];
		for(int i = 1 ; i < names.length ; i++) {
			contact += " ; " + names[i];
		}
		return contact;
	}
	private String mergeInfo(int i) {
		return names[i] + " (" + phoneNumbers[i] + ")";
	}
	
	public static PendingMessageItem createFromSentItem(
			SentMessageItem passedSentMessage) {
		return createInstance(
				passedSentMessage.getPendingId(), 
				passedSentMessage.getNames(), 
				passedSentMessage.getPhoneNumbers(), 
				passedSentMessage.getContent(), 
				passedSentMessage.getSentTime(), 
				FrequencyHelpers.DEFAULT_FREQ_INDEX, 
				AlertHelpers.DEFAULT_ALERT_INDEX);
	}





}

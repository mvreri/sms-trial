package dtd.phs.sil.ui.auto_complete_contacts;

import android.content.Context;
import dtd.phs.sil.utils.StringHelpers;

public class ContactItem {
	private String name;
	private String number;
	private long lastTimeContacted;
	
	public ContactItem(String name, String number,long lastTimeContacted) {
		super();
		this.name = name;
		this.number = number;
		this.setLastTimeContacted(lastTimeContacted);
	}
	public String getName() {
		return name;
	}
	public String getNumber() {
		return number;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public void setLastTimeContacted(long lastTimeContacted) {
		this.lastTimeContacted = lastTimeContacted;
	}
	public long getLastTimeContacted() {
		return lastTimeContacted;
	}
	
	public boolean matchContraint(Context context, String constraint) {
		String fullContact = (this.getName() + " " + this.getNumber()).toLowerCase();
		fullContact = StringHelpers.replaceLowerSignCharacter(context, fullContact);
		String str = constraint.toString();
		if ( StringHelpers.isSubStr(str,fullContact)) return true;
		return false;
	}



}

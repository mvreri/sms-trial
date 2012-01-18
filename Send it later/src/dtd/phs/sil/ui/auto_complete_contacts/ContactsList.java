package dtd.phs.sil.ui.auto_complete_contacts;

import java.util.ArrayList;

import dtd.phs.sil.entities.PendingMessageItem;

public class ContactsList extends ArrayList<ContactItem> {
	private static final long serialVersionUID = -2187763289630847239L;

	public String[] getNames() {
		String[] names = new String[this.size()];
		for(int i = 0 ; i < names.length ; i++) {
			names[i] = get(i).getName();
		}
		return names;
	}

	public String[] getNumbers() {
		String[] numbers = new String[this.size()];
		for(int i = 0 ; i < numbers.length ; i++) {
			numbers[i] = get(i).getNumber();
		}
		return numbers;
	}

	/**
	 * Create contact list from the pending message (without last contacted time!)
	 * and set all lastContactdTime = 0
	 * @param beingEditedMessage 
	 * @return
	 */
	public static ContactsList createContactsWithoutLastContactedTime(
			PendingMessageItem beingEditedMessage) {
		ContactsList list = new ContactsList();
		
		String[] names = beingEditedMessage.getNames();
		String[] phoneNumbers = beingEditedMessage.getPhoneNumbers();
		for(int i = 0 ; i < names.length ; i++) {
			ContactItem item = new ContactItem(names[i], phoneNumbers[i], 0);
			list.add(item);
		}
		
		return list;
	}

}

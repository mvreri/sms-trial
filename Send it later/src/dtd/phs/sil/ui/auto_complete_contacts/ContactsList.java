package dtd.phs.sil.ui.auto_complete_contacts;

import java.util.ArrayList;

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

}

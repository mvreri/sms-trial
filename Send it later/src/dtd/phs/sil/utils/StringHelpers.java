package dtd.phs.sil.utils;

public class StringHelpers {

	public static String implode(String[] phoneNumbers, String seperator) {
		StringBuilder b = new StringBuilder(phoneNumbers[0]);
		for(int i = 1 ; i < phoneNumbers.length ; i++) {
			b.append(seperator+phoneNumbers[i]);
		}
		return b.toString();
	}

}

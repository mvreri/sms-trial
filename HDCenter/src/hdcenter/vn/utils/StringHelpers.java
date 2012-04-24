package hdcenter.vn.utils;

import hdcenter.vn.R;
import android.content.Context;

public class StringHelpers {

	public static String implode(String[] phoneNumbers, String seperator) {
		StringBuilder b = new StringBuilder(phoneNumbers[0]);
		for(int i = 1 ; i < phoneNumbers.length ; i++) {
			b.append(seperator+phoneNumbers[i]);
		}
		return b.toString();
	}
	
	public static String replaceLowerSignCharacter (Context context, String str) {
		String strOld = context.getResources().getString(R.string.vn_special_characters);
		String strNew = "aaaaaaeeeeeeeeeeeiiiiioooooooooooooooooouuuuuuuuuuuuyyyyyaaaaaaaaaaaad";
		for (int i = 0; i < strNew.length(); i ++) {
			str = str.replace(strOld.charAt(i), strNew.charAt(i));
		}
		return str;
	}
}

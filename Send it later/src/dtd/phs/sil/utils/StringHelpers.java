package dtd.phs.sil.utils;

import android.content.Context;
import dtd.phs.sil.R;

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
//
//	public static boolean[][] QWERTY_Like_Arr = {
//		{true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,true,false,false,false,true,false,false,true},
//		{false,true,false,false,false,false,true,true,false,false,false,false,false,true,false,false,false,false,false,false,false,true,false,false,false,false},
//		{false,false,true,true,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,true,false,false},
//		{false,false,true,true,true,true,false,false,false,false,false,false,false,false,false,false,false,true,true,false,false,false,true,true,false,true},
//		{false,false,false,true,true,true,false,false,false,false,false,false,false,false,false,false,false,true,true,false,false,false,true,false,false,false},
//		{false,false,true,true,true,true,true,false,false,false,false,false,false,false,false,false,false,true,false,true,false,true,false,false,false,false},
//		{false,true,false,false,false,true,true,true,false,false,false,false,false,false,false,false,false,true,false,true,false,true,false,false,true,false},
//		{false,true,false,false,false,false,true,true,false,true,false,false,false,true,false,false,false,false,false,true,true,false,false,false,true,false},
//		{false,false,false,false,false,false,false,false,true,true,true,true,false,false,true,false,false,false,false,false,true,false,false,false,false,false},
//		{false,false,false,false,false,false,false,true,true,true,true,false,true,true,false,false,false,false,false,false,true,false,false,false,true,false},
//		{false,false,false,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,true,false,false,false,false,false},
//		{false,false,false,false,false,false,false,false,true,false,true,true,true,false,true,true,false,false,false,false,false,false,false,false,false,false},
//		{false,false,false,false,false,false,false,false,false,true,true,true,true,true,false,false,false,false,false,false,false,false,false,false,false,false},
//		{false,true,false,false,false,false,false,true,false,true,true,false,true,true,false,false,false,false,false,false,false,false,false,false,false,false},
//		{false,false,false,false,false,false,false,false,true,false,true,true,false,false,true,true,false,false,false,false,false,false,false,false,false,false},
//		{false,false,false,false,false,false,false,false,false,false,true,true,false,false,true,true,false,false,false,false,false,false,false,false,false,false},
//		{true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,true,false,false,false,true,false,false,false},
//		{false,false,false,true,true,true,true,false,false,false,false,false,false,false,false,false,false,true,false,true,false,false,false,false,false,false},
//		{true,false,false,true,true,false,false,false,false,false,false,false,false,false,false,false,true,false,true,false,false,false,true,true,false,true},
//		{false,false,false,false,false,true,true,true,false,false,false,false,false,false,false,false,false,true,false,true,false,false,false,false,true,false},
//		{false,false,false,false,false,false,false,true,true,true,true,false,false,false,false,false,false,false,false,false,true,false,false,false,true,false},
//		{false,true,true,false,false,true,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,false,false,false},
//		{true,false,false,true,true,false,false,false,false,false,false,false,false,false,false,false,true,false,true,false,false,false,true,false,false,false},
//		{false,false,true,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,false,false,false,true,false,true},
//		{false,false,false,false,false,false,true,true,false,true,false,false,false,false,false,false,false,false,false,true,true,false,false,false,true,false},
//		{true,false,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,false,false,false,true,false,true}};
//	public static boolean QWERT_Like(char c, char b) {
//		if ( c == b ) return true;
//		if ( c <= 'z' && b <= 'z' && c >='a' && b >= 'a') {
//			return QWERTY_Like_Arr[b-'a'][c-'a'];
//		} else {
//			return b==c;
//		}
//	}
}

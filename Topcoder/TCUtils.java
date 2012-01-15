import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class TCUtils {
	static public boolean isPrime(int num) {
		if ( num <= 1) return false;
		for(int d = 2 ; d*d <= num ; d++){
			if ( num % d == 0 ) return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param s
	 * @param beginIndex
	 * @param endIndex
	 * @return Cut [beginIndex,endIndex) from s, return rest
	 */
	static public String cutString(String s,int beginIndex,int endIndex) {
		return s.substring(0,beginIndex) + s.substring(endIndex);
	}
	
	/**
	 * 
	 * @param s original Strign
	 * @param added String to be inserted
	 * @param index - insert before character at index
	 * @return
	 */
	static public String insertString(String s,String added, int index) {
		return s.substring(0,index) + added + s.substring(index);
	}
	
	/**
	 * 
	 * @param s the original String
	 * @param x the string to be inserted to s
	 * @param bindex begin index
	 * @param eindex end index
	 * @return return the string s, but cut out [bindex,eindex) and replaced by x
	 */
	static public String replaceStringInterval(String s, String x, int bindex, int eindex) {
		return s.substring(0,bindex) + x + s.substring(eindex);
	}


	//------------------------------ BEGIN Num->Roman------------------
	static public String reverse(String s) {
		StringBuilder b = new StringBuilder();
		for(int i = s.length() - 1 ; i >=0 ; i--) {
			b.append(s.charAt(i));
		}
		return b.toString();
	}
	
	static final String RD = "IVXLCDM";
	static final String[] groups = {"IVX","XLC","CDM","MMM"};
	/**
	 * 
	 * @param contraint: num only <= 3999
	 * @return roman number of num
	 */
	public static String convertNum2Rom(int num) {
		if ( num >= 4000 ) return null;
		int groupIndex = 0;
		ArrayList<String> digits = new ArrayList<String>();
		while ( num > 0) {
			int digit = num % 10;
			if ( digit != 0 ) {
				String romDigit = "";
				if ( digit == 4) romDigit = ""+groups[groupIndex].charAt(0) + groups[groupIndex].charAt(1);
				else if ( digit == 9) romDigit = ""+groups[groupIndex].charAt(0) + groups[groupIndex].charAt(2);
				else {
					int d5 = digit % 5;
					int b5 = digit / 5;
					if ( b5 == 1) romDigit += groups[groupIndex].charAt(1);
					romDigit += multiCharString(groups[groupIndex].charAt(0),d5);
				}
				digits.add(romDigit);
			}
			num /= 10;
			groupIndex++;
		}
		
		Collections.reverse(digits);
		return concaternate(digits);
	}


	public static String concaternate(ArrayList<String> l) {
		StringBuilder b = new StringBuilder();
		for(String s : l) b.append(s);
		return b.toString();
	}


	public static String multiCharString(char c, int n) {
		char[] cc = new char[n];
		Arrays.fill(cc, c);
		return new String(cc);
	}
	
	//------------ End: Num -> roman ------------------
	
	
	static final String RCODE = "IVXLCDM";
	static final int[] NVAL = {1,5,10,50,100,500,1000};
	/**
	 * 
	 * @param rom only for number <= 3999
	 * @return 
	 */
	public static int convertRoman2Num(String rom) {
		char[] c = rom.toCharArray();
		int sum = valueOf(c[0]);
		for(int i = 1; i < c.length ; i++) {
			sum += valueOf(c[i]);
			if ( valueOf(c[i-1]) < valueOf(c[i]))
				sum -= 2*valueOf(c[i-1]);
		}
		
		return sum;
	}


	private static int valueOf(char c) {
		for(int i = 0 ; i < RCODE.length() ; i++)
			if ( RCODE.charAt(i) == c) return NVAL[i];
		return -10000;
	}
}

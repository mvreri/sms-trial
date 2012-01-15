
public class TestLib {

	/**
	 * @param args
	 */
	static final String RD = "IVXLCDM";
	static final int[] val = {1,5,10,50,100,500,1000};
	static int[] num = { 
			1,2,3,4,5,6,7,8,9,10,13,14,15,16,
			19,20,21,32,49,50,51,90,91,92,99,
			100,140,149,150,151,300,400,404,
			409,444,445,449,450,489,490,491
			,500,899,900,999,1000,3000,3999};
	static String[] rom = {"I","II","III","IV","V","VI","VII","VIII","IX","X","XIII","XIV","XV","XVI","XIX","XX",
			"XXI","XXXII","XLIX","L","LI","XC","XCI","XCII","XCIX","C","CXL","CXLIX","CL","CLI","CCC",
			"CD","CDIV","CDIX","CDXLIV","CDXLV","CDXLIX","CDL","CDLXXXIX","CDXC","CDXCI","D","DCCCXCIX","CM","CMXCIX","M","MMM","MMMCMXCIX"};
	
	public static void main(String[] args) {
//		for(int i = 0 ; i < num.length ; i++) {
//			String romNum = TCUtils.convertNum2Rom(num[i]);
//			if ( romNum.equals(rom[i])) {
//				System.out.println("Test "+i+" with num " + num[i] + ": passed");
//			} else {
//				System.err.println("Test "+i+" with num" + num[i]+":(expected:" + rom[i] + "- return: " + romNum);
//			}
//		}

		for(int i = 0 ; i < num.length ; i++) {
			int normalNum = TCUtils.convertRoman2Num(rom[i]);
			if ( normalNum == num[i]) {
				System.out.println("Test "+i+" with num " + num[i] + ": passed");
			} else {
				System.err.println("Test "+i+" with num" + num[i]+":(expected:" + num[i] + "- return: " + normalNum);
			}
		}
		
	}

}

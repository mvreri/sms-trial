package learn.design_patterns.command;

import junit.framework.Assert;

public class Helpers {
	enum CodeModes {DEV_CODE,PRODUCTION_CODE}
	private static final CodeModes CODE_MODE = CodeModes.DEV_CODE;

	public static void assertTrue(boolean b) {
		if ( CODE_MODE != CodeModes.PRODUCTION_CODE )
			Assert.assertTrue(b);
		
	}

}

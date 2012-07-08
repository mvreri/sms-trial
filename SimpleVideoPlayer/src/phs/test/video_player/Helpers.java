package phs.test.video_player;

import junit.framework.Assert;

public class Helpers {

	public static final boolean DEBUG_MODE = true;

	public static void verify(boolean b) {
		if ( DEBUG_MODE ) 
			Assert.assertTrue(b);
	}

}

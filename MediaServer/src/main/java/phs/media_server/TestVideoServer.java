package phs.media_server;

public class TestVideoServer {
	
	private static final int MIN_IN_MILLIS = 60*1000;
	protected static final long WAIT_BEFORE_DIE = 30* MIN_IN_MILLIS;
	private static WirelessVideoServer server;

	public static void main(String[] args) throws InterruptedException {
		server = new WirelessVideoServer();
		if ( server.startService() == WirelessVideoServer.START_SUCCESS ) {
			System.out.println("Video server started ....");
			Thread wait = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(WAIT_BEFORE_DIE);
						System.out.println("Video server stopped !");
						server.stopService();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			wait.start();
			wait.join();
			System.out.println("Test end !");
		} else {
			System.err.println("Cannot start the video server");
		}
	}
}

package phs.media_server;

/**
 * @author Pham Hung Son  </br>
 *	This interface "wraps" around the core streaming module,
 *	because the streaming library is not reliable, so it will be changed 
 *	in the future. This wrapper is used to anticipate that change 
 */
public interface IMediaServer {
	/**
	 * Setup the streaming engine
	 * @param fullFilePath path to video file on the server
	 * @return error code: </br>
	 * 	- 0 : successfully started </br>
	 * 	- 1: called on illegal state </br>
	 */
	public int setup(String fullFilePath);
	
	/**
	 * Start streaming
	 * @return error code: </br>
	 * 	- 0 : successfully started </br>
	 * 	- 1: called on illegal state </br>
	 */
	public int start();
	
	/**
	 * Stop streaming </br>
	 * The method which implements stop() should clean up the resource assign to the streaming engine
	 * @return error code: </br>
	 * 	- 0: successfully started </br>
	 * 	- 1: called on illegal state </br>
	 */
	public int stop();
	
	/**
	 * Pause the streaming video </br>
	 * This operation is only supported if the player is in "Streaming" state
	 * @return error code: </br>
	 * 	- 0: successfully started </br>
	 * 	- 1: called on illegal state </br>
	 */
	public int pause();
	
	/**
	 * Resume the streaming video
	 * This operation is only supported if the player is in "Paused" state 
	 * @return error code: </br>
	 * 	- 0: successfully started </br>
	 *  - 1: called on illegal state </br>
	 */
	public int resume();
	
	/**
	 * Set the video position accordingly
	 * This operation is only supported if the player is in "Streaming"/"Paused" state
	 * @param percent - takes the value in range [0..1] (inclusive)
	 * @return error code: </br>
	 * 	- 0: successfully started </br>
	 *  - 1: called on illegal state </br>
	 */
	public int seekTo(float percent);
	
	
	/**
	 * @return current port in used to streaming video
	 */
	public int getServerPort();
	
	/**
	 * @return current streaming id
	 */
	public String getStreamId();
}

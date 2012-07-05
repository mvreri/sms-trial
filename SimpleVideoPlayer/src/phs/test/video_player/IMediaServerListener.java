package phs.test.video_player;

public interface IMediaServerListener {
	public void onSetupRespone(int errorCode,String streamPort, String streamId);
	public void onStartRespone(int errorCode);
	public void onPauseRespone(int errorCode);
	public void onResumeRespone(int errorCode);
	public void onSeekResponse(int errorCode);
	public void onStopRespone(int errorCode);
	public void onGetDurationResponse(int errorCode,int duration);
	
	//Consider later
	public void onElseRespone(int errorCode);
	
}

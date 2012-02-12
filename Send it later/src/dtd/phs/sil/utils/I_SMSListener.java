package dtd.phs.sil.utils;

public interface I_SMSListener {

	void onSentFailed(int errorCode);
	void onSentSuccess();
	void onMessageDelivered();
	void onMessageDeliveryFailed();

}

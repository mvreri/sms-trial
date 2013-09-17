package dtd.phs.lib.data_framework;

public interface IDataListener {
	void onCompleted(Object data);
	void onError(Exception e);
}

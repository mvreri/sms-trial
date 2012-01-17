package dtd.phs.sil.data;

public interface IDataLoader {
	public void onGetDataSuccess(Object data);
	public void onGetDataFailed(Exception e);
}

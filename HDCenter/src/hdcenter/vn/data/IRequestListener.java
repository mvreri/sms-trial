package hdcenter.vn.data;


public interface IRequestListener {
	void onRequestSuccess(Object data);
	void onRequestError(Exception e);

}

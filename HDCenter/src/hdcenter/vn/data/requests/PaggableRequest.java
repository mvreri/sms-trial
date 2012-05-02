package hdcenter.vn.data.requests;

public abstract class PaggableRequest extends Request {
	protected int page;
	
	public PaggableRequest(int page) {
		this.page = page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return this.page;
	}
}

package dtd.phs.lib.data_framework;

public interface PaggableRequest extends IRequest {
	void setPage(int currentPage);
	int getPageSize();

}

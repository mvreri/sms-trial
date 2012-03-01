package dtd.phs.sil.ui.auto_complete_contacts;

public interface IContactLoader {
	public void onContactCacheSuccess(Object data);
	public void onContactLoadFailed(Exception e);
}

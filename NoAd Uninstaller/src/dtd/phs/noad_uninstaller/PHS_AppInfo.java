package dtd.phs.noad_uninstaller;

import java.util.Comparator;

import android.graphics.drawable.Drawable;

public class PHS_AppInfo {
	public static class AppComparator implements Comparator<PHS_AppInfo> {

		@Override
		public int compare(PHS_AppInfo lhs, PHS_AppInfo rhs) {
			String lname = lhs.appName.toLowerCase().trim();
			String rname = rhs.appName.toLowerCase().trim();
			return lname.compareTo(rname);
		}

	}
	
	public String packageName;
	public String appName;
	private Drawable icon;
	public PHS_AppInfo(String packageName, String appName, Drawable icon) {
		this.packageName = packageName;
		this.appName = appName;
		this.icon = icon;
	}
	public Drawable getIcon() {
		return icon;
	}
	public String getPackageName() {
		return packageName;
	}
	public String getAppName() {
		return appName;
	}
}

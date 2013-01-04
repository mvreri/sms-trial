package dtd.phs.noad_uninstaller.learn;

import android.graphics.drawable.Drawable;

public class PHS_AppInfo {
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

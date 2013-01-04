package dtd.phs.noad_uninstaller;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainUninstaller extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_uninstaller);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_uninstaller, menu);
        return true;
    }
}

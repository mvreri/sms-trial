package dtd.phs.sil.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Toast;
import dtd.phs.sil.IDBLinked;
import dtd.phs.sil.R;
import dtd.phs.sil.data.DataCenter;

public class RemovePendingItemDialog extends Dialog {

	private long rowId;
	private IDBLinked listener;
	private Button btOk;
	private Button btCancel;

	public RemovePendingItemDialog(Activity activity) {
		super(activity);
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.remove_pend_message_dialog);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		btOk = (Button) findViewById(R.id.btOk);
		btOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DataCenter.removePendingItem(getContext(),rowId);
				Toast.makeText(getContext(), R.string.message_removed_success, Toast.LENGTH_SHORT).show();
				listener.onDBUpdated();
				cancel();
			}
		});
		
		btCancel = (Button) findViewById(R.id.btCancel);
		btCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
	}
	
	public void setRemovedRowId(long rowId) {
		this.rowId = rowId;
	}

	public void setLinkedDBObject(IDBLinked dbLinkedObj) {
		this.listener = dbLinkedObj;
	}

}

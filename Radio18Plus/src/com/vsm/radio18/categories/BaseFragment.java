package com.vsm.radio18.categories;

import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment extends Fragment {
	protected View rootView = null;
	
	protected View findViewById(int id) {
		return rootView.findViewById(id);
	}
}

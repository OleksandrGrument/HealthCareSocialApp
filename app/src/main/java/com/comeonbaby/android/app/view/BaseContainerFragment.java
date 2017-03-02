package com.comeonbaby.android.app.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.ILog;


public class BaseContainerFragment extends BaseFragment {

	public void replaceFragment(Fragment fragment, boolean addToBackStack) {
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.replace(R.id.container_framelayout, fragment);
		transaction.commit();
		getChildFragmentManager().executePendingTransactions();
	}

	public boolean popFragment() {
		ILog.e("pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
		boolean isPop = false;
		if (getChildFragmentManager().getBackStackEntryCount() > 0) {
			isPop = true;
			getChildFragmentManager().popBackStack();
		}
		return isPop;
	}
}
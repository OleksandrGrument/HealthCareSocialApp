package com.comeonbabys.android.app.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.ILog;


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
package com.comeonbaby.android.app.view;

import android.os.Bundle;
import android.view.View;

import com.comeonbaby.android.R;

public class SettingActivity extends BaseActivity {

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.container_framelayout);
		SettingFragment settingFragment = new SettingFragment();
		settingFragment.setFromDayNote(true);
		getSupportFragmentManager().beginTransaction().replace(R.id.container_framelayout, settingFragment).commit();
		getFragmentManager().executePendingTransactions();
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}
}
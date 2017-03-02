package com.comeonbaby.android.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.DialogUtilities;
import com.comeonbaby.android.app.utils.AppSession;
import com.comeonbaby.android.app.utils.PrefsHelper;
//import com.comeonbaby.android.common.listener.AutoUpdateApk;
//import com.comeonbaby.android.common.listener.PlayServicesHelper;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;

public class MainActivity extends BaseActivity implements OnClickListener, Observer {

	public static MainActivity homeActivity;
	private long lastBackPressTime = 0;
	public static final int EXIT_ON_TWO_PRESS_TIMEOUT = 3000; // 3 seconds

	private Fragment mCurrentFragment = null;

	private static final String TAB_1_TAG = "tab_1";
	private static final String TAB_2_TAG = "tab_2";
	private static final String TAB_3_TAG = "tab_3";
	private static final String TAB_4_TAG = "tab_4";
	private static final String TAB_5_TAG = "tab_5";

	private FragmentTabHost mTabHost;
	//---private PlayServicesHelper playServicesHelper;
	// declare updater class member here (or in the Application)
	//---private AutoUpdateApk aua;
	private boolean isShowDialogNewVersion;

	@Override
	protected int getContentViewID() {
		return R.layout.activity_home;
	}

	@Override
	protected void onAfterCreate(Bundle savedInstanceState) {
		super.onAfterCreate(savedInstanceState);
	}

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		homeActivity = this;
		//---playServicesHelper = new PlayServicesHelper(homeActivity);
		//---playServicesHelper.checkPlayServices();
		AppSession.getSession().initPrefsHelper();
		if (prefsHelper.getPref(PrefsHelper.SHARED_PREF_RUN_FIRST, true)) {
			DialogUtilities.showHomeFirstDialog(homeActivity);
			prefsHelper.savePref(PrefsHelper.SHARED_PREF_RUN_FIRST, false);
		}
		//---aua = new AutoUpdateApk(getApplicationContext()); // <-- don't forget to
		// instantiate
		//---aua.addObserver(this); // see the remark below, next to update() method
		//---aua.checkUpdatesManually();
		InitView();
	}

	private void InitView() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		mTabHost.addTab(setIndicator(MainActivity.this, mTabHost.newTabSpec(TAB_1_TAG),
						R.drawable.tab_indicator_left, getString(R.string.text_home),
						R.drawable.tab_icon_home), Tab1Container.class, null);
		mTabHost.addTab(setIndicator(MainActivity.this, mTabHost.newTabSpec(TAB_2_TAG),
						R.drawable.tab_indicator_center, getString(R.string.text_comeon),
						R.drawable.tab_icon_comeon), Tab2Container.class, null);
		mTabHost.addTab(setIndicator(MainActivity.this, mTabHost.newTabSpec(TAB_3_TAG),
						R.drawable.tab_indicator_center, getString(R.string.text_community),
						R.drawable.tab_icon_community), Tab3Container.class, null);
		mTabHost.addTab(setIndicator(MainActivity.this, mTabHost.newTabSpec(TAB_4_TAG),
						R.drawable.tab_indicator_center, getString(R.string.text_report),
						R.drawable.tab_icon_report), Tab4Container.class, null);
		mTabHost.addTab(setIndicator(MainActivity.this, mTabHost.newTabSpec(TAB_5_TAG),
						R.drawable.tab_indicator_right, getString(R.string.text_setting),
						R.drawable.tab_icon_setting), Tab5Container.class, null);
	}

	@SuppressLint("InflateParams")
	private TabSpec setIndicator(Context ctx, TabSpec spec, int resid, String string, int genresIcon) {
		View v = LayoutInflater.from(ctx).inflate(R.layout.tab_item, null);
		v.setBackgroundResource(resid);
		TextView tv = (TextView) v.findViewById(R.id.txt_tabtxt);
		ImageView img = (ImageView) v.findViewById(R.id.img_tabtxt);

		tv.setText(string);
		img.setBackgroundResource(genresIcon);
		return spec.setIndicator(v);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {

	}

	@Override
	public void onBackPressed() {
		boolean isPopFragment = false;
		String currentTabTag = mTabHost.getCurrentTabTag();

		if (currentTabTag.equals(TAB_1_TAG)) {
			isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(TAB_1_TAG)).popFragment();
		} else if (currentTabTag.equals(TAB_2_TAG)) {
			isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(TAB_2_TAG)).popFragment();
		} else if (currentTabTag.equals(TAB_3_TAG)) {
			isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(TAB_3_TAG)).popFragment();
		} else if (currentTabTag.equals(TAB_4_TAG)) {
			isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(TAB_4_TAG)).popFragment();
		} else if (currentTabTag.equals(TAB_5_TAG)) {
			isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(TAB_5_TAG)).popFragment();
		}

		if (!isPopFragment) {
			long tempTime = SystemClock.elapsedRealtime();
			// Exit if time difference between two consecutive back
			// press event is not more than 2 sec
			if ((lastBackPressTime > 0) && ((tempTime - lastBackPressTime) < EXIT_ON_TWO_PRESS_TIMEOUT)) {
				super.onBackPressed();
			} else {
				Toast toast = Toast.makeText(MainActivity.this, getString(R.string.string_press_back_again_to_exit), Toast.LENGTH_SHORT);
				toast.show();
			}
			lastBackPressTime = tempTime;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		String currentTabTag = mTabHost.getCurrentTabTag();
		if (currentTabTag.equals(TAB_5_TAG)) {
			for (Fragment fragment : ((BaseContainerFragment) getSupportFragmentManager()
					.findFragmentByTag(TAB_5_TAG)).getChildFragmentManager().getFragments())
				if (fragment != null && fragment instanceof SettingFragment)
					fragment.onActivityResult(arg0, arg1, arg2);
		}
	}

	@Override
	public void onClick(View v) {
	}

	public Fragment getmCurrentFragment() {
		return mCurrentFragment;
	}

	public void setmCurrentFragment(Fragment mCurrentFragment) {
		this.mCurrentFragment = mCurrentFragment;
	}

	/**
	 * @author PvTai
	 * @description This method using to cancel timer
	 * @param mTimer
	 *            The timer that to cancel
	 */
	public void cancelTimerTask(Timer mTimer) {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer.purge();
			mTimer = null;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isShowDialogNewVersion = false;
	}

	@Override
	public void update(Observable observable, Object data) {
//		if (((String) data).equalsIgnoreCase(AutoUpdateApk.AUTOUPDATE_GOT_UPDATE)) {
//			android.util.Log.i("LoginActivity", "Have just received update!");
//			OnClickListener yesButtonListener = new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					isShowDialogNewVersion = false;
//					startActivity(new Intent(
//							Intent.ACTION_VIEW,
//							Uri.parse("market://details?id=" + getPackageName())));
//				}
//			};
//			OnClickListener noButtonListener = new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					isShowDialogNewVersion = false;
//				}
//			};
//			if (!isShowDialogNewVersion) {
//				DialogUtilities.showConfirmDialog(this,
//						R.string.title_new_version,
//						R.string.content_new_version, yesButtonListener,
//						noButtonListener);
//				isShowDialogNewVersion = true;
//			}
//		}
//		if (((String) data).equalsIgnoreCase(AutoUpdateApk.AUTOUPDATE_HAVE_UPDATE)) {
//			android.util.Log.i("LoginActivity", "There's an update available!");
//		}
	}
}
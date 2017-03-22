package com.comeonbabys.android.app.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comeonbabys.android.R;

public class Tab2Container extends BaseContainerFragment  {

	private boolean IsViewInited;


	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.container_framelayout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (!IsViewInited) {
			IsViewInited = true;
			initViewGuide();
		}
	}


	private void initViewGuide() {
		replaceFragment(new RecipeGuideFragment(), false);
	}



}
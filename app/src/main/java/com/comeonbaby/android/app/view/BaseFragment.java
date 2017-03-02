package com.comeonbaby.android.app.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.OnEventControlListener;

public class BaseFragment extends Fragment implements
		OnCancelListener, OnEventControlListener {
	public boolean isFinished = false;
	BaseActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_base, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Context activity) {
		this.activity = (BaseActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		isFinished = false;
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		isFinished = true;
		super.onDestroy();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}
}

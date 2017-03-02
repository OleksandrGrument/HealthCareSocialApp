package com.comeonbaby.android.app.view.customview;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class MultiTextWatcher implements TextWatcher {

	public interface OnTextChangedListener {
		void onEvent(int id, String text);
	}

	private View view;
	private OnTextChangedListener listener;

	public MultiTextWatcher(View view, OnTextChangedListener listener) {
		this.view = view;
		this.listener = listener;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		String text = s.toString();
		listener.onEvent(view.getId(), text);
	}
}

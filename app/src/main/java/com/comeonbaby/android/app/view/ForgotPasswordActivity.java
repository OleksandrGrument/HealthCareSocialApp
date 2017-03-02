package com.comeonbaby.android.app.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.Common;
import com.comeonbaby.android.app.common.DialogUtilities;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends BaseActivity implements OnClickListener {
	private EditText editEmail;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_forgot_password);
		initObjectUI();

		editEmail.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_GO) {
					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});

		setupHideKeyboard(findViewById(R.id.layoutRootLogin));
	}

	private void initObjectUI() {
		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.btnButton)).setText(R.string.title_request_password_forgot_screen);
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.title_forgot_password_screen);
		float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
		float sp = ((EditText) findViewById(R.id.edt_Email)).getTextSize() / scaledDensity;
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
		((ButtonCustom) findViewById(R.id.btnButton)).setOnClickListener(this);
		editEmail = (EditText) findViewById(R.id.edt_Email);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnButton:
			requestPasswordAction();
			break;
		case R.id.imgBack:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// if (btnSignin.isEnabled() == false) {
		// // Common.LogPrint(LoginActivity.TAG, Common.LOG_DEBUG,
		// // "On back pressed, login already started, ignoring");
		// }
		// else {
		super.onBackPressed();
		// }
	}

	/**
	 * @author PvTai
	 * @description This method used for check information and call asyncTask
	 *              login
	 */
	private void requestPasswordAction() {
		String selectEmail = editEmail.getText().toString().trim();
		String err_msg = "";
		if (TextUtils.isEmpty(selectEmail)) {
			err_msg = getString(R.string.string_enter_your_email);
			DialogUtilities.showAlertDialog(ForgotPasswordActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
			return;
		} else {
			if ((!TextUtils.isEmpty(selectEmail) && Common.isValidEmail(ForgotPasswordActivity.this, selectEmail) == false)) {
				err_msg = getString(R.string.string_invalid_email_address);
				DialogUtilities.showAlertDialog(ForgotPasswordActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
				return;
			}
		}

//		stateAllButtons(false);// Disable all button when login progress
//		showProgress();


		JSONObject jsObject = new JSONObject();
		try {
			jsObject.put("email", selectEmail);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//-----------ForgotPasswordCommand.start(this, jsObject.toString());
	}

	/**
	 * @author PvTai This method used for set enable/disable all button
	 */
	private void stateAllButtons(boolean value) {
		((ButtonCustom) findViewById(R.id.btnButton)).setEnabled(value);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}

//	private class ForgotSuccessAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			hideProgress();
//			stateAllButtons(true);
//			boolean success = bundle.getBoolean(ServiceConsts.EXTRA_SUCCESS, false);
//			if (success) {
//				OnClickListener onclick = new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						finish();
//					}
//				};
//				DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_success_forgot_password, onclick);
//			} else {
//				DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_forgot_password, null);
//			}
//		}
//	}
//
//	private class ForgotFailAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			stateAllButtons(true);
//			hideProgress();
//			DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_forgot_password, null);
//		}
//	}
}
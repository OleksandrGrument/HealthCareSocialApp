package com.comeonbaby.android.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.commands.Command;
import com.comeonbaby.android.app.common.Common;
import com.comeonbaby.android.app.common.DialogUtilities;
import com.comeonbaby.android.app.db.dto.CityDTO;
import com.comeonbaby.android.app.db.dto.NotesHolder;
import com.comeonbaby.android.app.db.dto.UserDTO;
import com.comeonbaby.android.app.requests.Constants;
import com.comeonbaby.android.app.requests.ExtraConstants;
import com.comeonbaby.android.app.requests.commands.Commands;
import com.comeonbaby.android.app.utils.AppSession;
import com.comeonbaby.android.app.utils.PrefsHelper;
import com.comeonbaby.android.app.view.customview.ButtonCustom;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private final String TAG = "LoginActivity";
	private EditText editUsername, editPassword;
	private Button btnSignin;
	private TextView btnForgot;
    UserDTO userDTO;
    Handler handler;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login);
		initObjectUI();
        userDTO = AppSession.getSession().getSystemUser();

		editUsername.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		editPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		editPassword.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_GO) {
					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
					loginAction();
					return true;
				}
				return false;
			}
		});

		editPassword.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int DRAWABLE_RIGHT = 2;

				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (event.getRawX() >= (editPassword.getRight() - editPassword
							.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds()
							.width())) {
						// your action here
						Toast.makeText(baseActivity, "help", Toast.LENGTH_SHORT).show();
						return true;
					}
				}
				return false;
			}
		});

        initHandler();
		setupHideKeyboard(findViewById(R.id.layoutRootLogin));
	}

	private void initObjectUI() {
		((ImageView) findViewById(R.id.imgBack)).setVisibility(View.GONE);
		((ButtonCustom) findViewById(R.id.btnButton)).setText(R.string.title_sign_in);
		btnSignin = (Button) findViewById(R.id.btnButton);
		btnForgot = (TextView) findViewById(R.id.btnForgot);

		btnSignin.setOnClickListener(this);
		btnForgot.setOnClickListener(this);

		editUsername = (EditText) findViewById(R.id.edt_Username);
		editPassword = (EditText) findViewById(R.id.edt_Password);
		editUsername.setText((String) prefsHelper.getPref(PrefsHelper.PREF_USER_EMAIL));
		editPassword.setText((String) prefsHelper.getPref(PrefsHelper.PREF_USER_PASSWORD));
	}

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Bundle data = msg.getData();
                String message = "";
                JSONObject jsuser = null, jsdata = null;
                if (data.containsKey(ExtraConstants.MESSAGE)) message = data.getString(ExtraConstants.MESSAGE);
                if (msg.what != Constants.MSG_ERROR) showSnackMessage(message);
                switch (msg.what) {
                    //Success registration
                    case Constants.MSG_LOGIN_EMAIL_SUCCESS: {
                        CityDTO.updateCityList();
                        try {
                            if (data.containsKey(ExtraConstants.USER) && data.containsKey(ExtraConstants.DATA)) {
                                jsuser = new JSONObject(data.getString(ExtraConstants.USER));
                                jsdata = new JSONObject(data.getString(ExtraConstants.DATA));
                            }
                        } catch (JSONException exc) {exc.printStackTrace();}

                        if (jsuser != null && jsdata != null) {
                            userDTO.setFromJSON(jsuser.toString());
                            userDTO.getProfileDTO().parseFromJson(jsdata.toString());
                            NotesHolder.updateNotes();
                            AppSession.isLogined = true;
                            AppSession.saveRememberMe(true);
                            AppSession.getSession().save();
                            startNextActivity();
                        } else {
                            Log.d(TAG, "Get no user or data");
                            showSnackMessage(Constants.ERROR_MESSAGE_UNKNOWN);
                        }
                        break;
                    }
                    //Unsuccessful login
                    case Constants.MSG_LOGIN_EMAIL_FAIL: {
                        break;
                    }
                    //No connection
                    case Constants.MSG_ERROR: {
                        showSnackMessage(Constants.ERROR_MESSAGE_UNKNOWN);
                        break;
                    }
                }
                hideProgress();
                stateAllButtons(true);
            }
        };
    }

    //Всплывающее сообщение
    private void showSnackMessage(String msg) {
        Snackbar.make(findViewById(R.id.layoutRootLogin), msg, Snackbar.LENGTH_LONG).show();
    }

    void startNextActivity(){
        hideProgress();
        Intent intent;
        if(userDTO.getProfileDTO().isIs_finish_question()) {
            intent = new Intent(LoginActivity.this, MainActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, BasicQuestionActivity01.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnButton:
			loginAction();
			break;
		case R.id.btnForgot:
			Intent intent1 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}

	/**
	 * @author PvTai
	 * @description This method used for check information and call asyncTask
	 *              login
	 */


	private void loginAction() {
		String selectEmail = editUsername.getText().toString().trim();
		String selectPassword = editPassword.getText().toString().trim();
		String err_msg = "";
		if (TextUtils.isEmpty(selectEmail)) {
			err_msg = getString(R.string.string_enter_your_email);
			DialogUtilities.showAlertDialog(LoginActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
			return;
		} else {
			if ((!TextUtils.isEmpty(selectEmail) && Common.isValidEmail(LoginActivity.this, selectEmail) == false)) {
				err_msg = getString(R.string.string_invalid_email_address);
				DialogUtilities.showAlertDialog(LoginActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
				return;
			}
		}
		if (TextUtils.isEmpty(selectPassword)) {
			err_msg = getString(R.string.string_enter_your_password);
			DialogUtilities.showAlertDialog(LoginActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
			return;
		}

		login(selectEmail, selectPassword);
	}

	/**
	 * @author PvTai This method used for set enable/disable all button
	 */
	private void stateAllButtons(boolean value) {
		if (value) {
			if (!TextUtils.isEmpty(editUsername.getText().toString().trim()) && !TextUtils.isEmpty(editPassword.getText().toString().trim())) {
				btnSignin.setEnabled(value);
			}
		} else {
			btnSignin.setEnabled(value);
		}
		btnForgot.setEnabled(value);
		editUsername.setEnabled(value);
		editPassword.setEnabled(value);
	}

	private void login(String email, String userPassword) {
        stateAllButtons(false);// Disable all button when login progress
        // going on
        showProgress();
        // call API login
        Commands.startLoginEmail(handler, email, userPassword);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}
}
package com.comeonbaby.android.app.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.Common;
import com.comeonbaby.android.app.common.DialogUtilities;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.db.dto.CityDTO;
import com.comeonbaby.android.app.db.dto.NoteDTO;
import com.comeonbaby.android.app.db.dto.NotesHolder;
import com.comeonbaby.android.app.db.dto.ProfileDTO;
import com.comeonbaby.android.app.db.dto.UserDTO;
import com.comeonbaby.android.app.requests.Constants;
import com.comeonbaby.android.app.requests.ExtraConstants;
import com.comeonbaby.android.app.requests.commands.Commands;
import com.comeonbaby.android.app.utils.AppSession;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.MultiTextWatcher;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateAccountActivity extends BaseActivity implements OnClickListener, MultiTextWatcher.OnTextChangedListener {
    private final String TAG = "CreateAccountActivity";
	private EditText edtEmail;
	private EditText edtPassword;
	private EditText edtConfirmPassword;
	private EditText edtNickname;
    private final int REQUEST_CODE = 10;
    Handler handler;
    UserDTO userDTO;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_create_account);
		setupHideKeyboard(findViewById(R.id.layoutRootCreate));
        initViews();
        initHandler();
        userDTO = AppSession.getSession().getSystemUser();
	}

    private void initViews() {
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        ((ButtonCustom) findViewById(R.id.btnButton)).setOnClickListener(this);
        ((TextViewCustom) findViewById(R.id.txtTitle)).setText(getString(R.string.text_register));
        ((ButtonCustom) findViewById(R.id.btnButton)).setText(getString(R.string.text_register));
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        float sp = ((EditText) findViewById(R.id.edt_Email)).getTextSize() / scaledDensity;
        ((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);

        edtEmail = (EditText) findViewById(R.id.edt_Email);
        edtPassword = (EditText) findViewById(R.id.edt_Password);
        edtConfirmPassword = (EditText) findViewById(R.id.edt_ConfirmPassword);
        edtNickname = (EditText) findViewById(R.id.edt_Nickname);
        edtEmail.setTypeface(Typeface.DEFAULT);
        edtPassword.setTypeface(Typeface.DEFAULT);
        edtNickname.setTypeface(Typeface.DEFAULT);
        edtPassword.addTextChangedListener(new MultiTextWatcher(edtPassword, this));
        edtConfirmPassword.addTextChangedListener(new MultiTextWatcher(edtConfirmPassword, this));
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Bundle data = msg.getData();
                String message = "";
                JSONObject user = null, jsdata = null;
                if (data.containsKey(ExtraConstants.MESSAGE)) message = data.getString(ExtraConstants.MESSAGE);
                if (msg.what != Constants.MSG_ERROR) showSnackMessage(message);
                switch (msg.what) {
                    //Success registration
                    case Constants.MSG_REGISTER_USER_SUCCESS: {
                        CityDTO.updateCityList();
                        try {
                            if (data.containsKey(ExtraConstants.USER) && data.containsKey(ExtraConstants.DATA)) {
                                user = new JSONObject(data.getString(ExtraConstants.USER));
                                jsdata = new JSONObject(data.getString(ExtraConstants.DATA));
                            }
                        } catch (JSONException exc) {exc.printStackTrace();}

                        if (user != null && jsdata != null) {
                            userDTO.setFromJSON(user.toString());
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
                    case Constants.MSG_REGISTER_USER_FAIL: {
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

    void startNextActivity(){
        hideProgress();
        Intent intent = new Intent(CreateAccountActivity.this, BasicQuestionActivity01.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //Всплывающее сообщение
    private void showSnackMessage(String msg) {
        Snackbar.make(findViewById(R.id.layoutRootCreate), msg, Snackbar.LENGTH_LONG).show();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.imgBack:
				finish();
				break;
			case R.id.btnButton:
                createAccount();
				break;

			default:
				break;
		}
	}

	/**
	 * @author PvTai
	 * @description This method used for set enable/disable all button
	 */
	private void stateAllButtons(boolean value) {
		edtEmail.setEnabled(value);
		edtPassword.setEnabled(value);
		edtNickname.setEnabled(value);
		((ImageView) findViewById(R.id.imgBack)).setEnabled(value);
		((ButtonCustom) findViewById(R.id.btnButton)).setEnabled(value);
	}

    private boolean checkTermPolicy() {
        if(userDTO.getProfileDTO() == null) userDTO.setProfileDTO(new ProfileDTO());
        if(userDTO.getProfileDTO().isIs_agreement()) {
            return true;
        } else {
            leaveActivityForResult(CreateAccountActivity.this, TermPolicyActivity.class.toString(), false, false, null, null, REQUEST_CODE);
            return false;
        }
    }

	/**
	 * @author PvTai
	 * @description This method used for check information and call asyncTask
	 *              create account
	 */
	private void createAccount() {
		String selectEmail = edtEmail.getText().toString().trim();
		String selectPassword = edtPassword.getText().toString().trim();
		String selectNickname = edtNickname.getText().toString().trim();
		String err_msg = "";
		if (TextUtils.isEmpty(selectEmail)) {
			err_msg = getString(R.string.string_enter_your_email);
			DialogUtilities.showAlertDialog(CreateAccountActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
			return;
		} else {
			if ((!TextUtils.isEmpty(selectEmail) && Common.isValidEmail(CreateAccountActivity.this, selectEmail) == false)) {
				err_msg = getString(R.string.string_invalid_email_address);
				DialogUtilities.showAlertDialog(CreateAccountActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
				return;
			}
		}
		if (TextUtils.isEmpty(selectPassword)) {
			err_msg = getString(R.string.string_enter_your_password);
			DialogUtilities.showAlertDialog(CreateAccountActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
			return;
		}
		if (selectPassword.length() < Globals.MIN_PASSWORD_LEN || selectPassword.length() > Globals.MAX_PASSWORD_LEN) {
			err_msg = getString(R.string.string_passwd_length_hint);
			DialogUtilities.showAlertDialog(CreateAccountActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
			return;
		}
		if (TextUtils.isEmpty(selectNickname)) {
			err_msg = getString(R.string.string_enter_your_nickname);
			DialogUtilities.showAlertDialog(CreateAccountActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
			return;
		}
		if (selectNickname.length() < Globals.MIN_NICKNAME_LEN || selectPassword.length() > Globals.MAX_NICKNAME_LEN) {
			err_msg = getString(R.string.string_nickname_length);
			DialogUtilities.showAlertDialog(CreateAccountActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
			return;
		}

		if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            err_msg = getString(R.string.string_passwords_different);
            DialogUtilities.showAlertDialog(CreateAccountActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
            return;
		}
        if(checkTermPolicy()) {
            createUser(selectEmail, selectNickname, selectPassword);
        }
	}

	private void createUser(String email, String nickname, String userPassword) {
        stateAllButtons(false);
        showProgress();
        Commands.startRegisterEmail(handler, email, userPassword, nickname);
	}

    //Set results after leaveActivityForResult method
    //arg0 - REQUEST_CODE, arg1 - RESULT
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        Log.d(TAG, "On result term policy");
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == REQUEST_CODE && arg1 == RESULT_OK) {
            userDTO.getProfileDTO().setIs_agreement(true);
            createAccount();
        }
    }

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}

	@Override
	public void onEvent(int id, String text) {
		if (edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
			if (TextUtils.isEmpty(edtPassword.getText().toString()) && TextUtils.isEmpty(edtConfirmPassword.getText().toString()))
				((ImageView) findViewById(R.id.imageCheck)).setVisibility(View.GONE);
			((ImageView) findViewById(R.id.imageCheck)).setImageResource(R.drawable.password_ok);
		} else {
			if (!TextUtils.isEmpty(edtConfirmPassword.getText().toString())) {
				((ImageView) findViewById(R.id.imageCheck)).setVisibility(View.VISIBLE);
				((ImageView) findViewById(R.id.imageCheck)).setImageResource(R.drawable.password_fail);
			}
		}
	}
}
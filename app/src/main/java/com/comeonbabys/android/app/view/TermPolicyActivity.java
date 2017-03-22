package com.comeonbabys.android.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Constants;
import com.comeonbabys.android.app.common.DialogUtilities;
import com.comeonbabys.android.app.view.customview.ButtonCustom;
import com.comeonbabys.android.app.view.customview.CheckBoxCustom;
import com.comeonbabys.android.app.view.customview.TextViewCustom;

public class TermPolicyActivity extends BaseActivity implements OnClickListener {

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_term_policy);
		initObjectUI();
		setupHideKeyboard(findViewById(R.id.layoutRootTerm));
	}

	private void initObjectUI() {
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.title_dieu_khoan);
		float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
		float sp = ((ButtonCustom) findViewById(R.id.btnAcceptAll)).getTextSize() / scaledDensity;
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
		((TextViewCustom) findViewById(R.id.txtDieuKhoan)).setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
		((TextViewCustom) findViewById(R.id.txtThuThapThongTin)).setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);

		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.btnButtonAccept)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.btnAcceptAll)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.btnButtonCancel)).setOnClickListener(this);
		((TextViewCustom) findViewById(R.id.txtDieuKhoan)).setOnClickListener(this);
		((TextViewCustom) findViewById(R.id.txtThuThapThongTin)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnButtonAccept:
			sendConfirmation();
			break;
		case R.id.btnAcceptAll:
			((CheckBoxCustom) findViewById(R.id.checkboxDieuKhoan)).setChecked(true);
			((CheckBoxCustom) findViewById(R.id.checkboxThongTin)).setChecked(true);
			sendConfirmation();
			break;
		case R.id.imgBack:
		case R.id.btnButtonCancel:
			finish();
			break;
		case R.id.txtDieuKhoan:
			String urlFile = "file:///android_asset/dieukhoan.html";
			String title = getString(R.string.hint_dieu_khoan);
			startActivityWebView(urlFile, title);
			break;
		case R.id.txtThuThapThongTin:
			urlFile = "file:///android_asset/thuthapthongtin.html";
			title = getString(R.string.hint_thuthap_thongtin);
			startActivityWebView(urlFile, title);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private void sendConfirmation() {
		if (!((CheckBoxCustom) findViewById(R.id.checkboxDieuKhoan)).isChecked() || !((CheckBoxCustom) findViewById(R.id.checkboxThongTin)).isChecked()) {
			DialogUtilities.showAlertDialog(TermPolicyActivity.this, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_term_policy, null);
			return;
		}
		setResult(RESULT_OK);
		finish();
	}
	
	private void startActivityWebView(String urlFile, String title) {
		Intent i = new Intent(getApplicationContext(), CustomWebviewActivity.class);
		i.putExtra(Constants.INTENT_URL, urlFile);
		i.putExtra(Constants.INTENT_STRING_TITLE, title);
		i.putExtra(Constants.EX_URL_FROM_FILE, true);
		startActivity(i);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}
}
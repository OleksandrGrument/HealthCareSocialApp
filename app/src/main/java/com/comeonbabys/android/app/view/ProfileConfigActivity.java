package com.comeonbabys.android.app.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.DialogUtilities;
import com.comeonbabys.android.app.common.Globals;
import com.comeonbabys.android.app.db.dto.CityDTO;
import com.comeonbabys.android.app.db.dto.ProfileDTO;
import com.comeonbabys.android.app.db.dto.UserDTO;
import com.comeonbabys.android.app.requests.Constants;
import com.comeonbabys.android.app.requests.ExtraConstants;
import com.comeonbabys.android.app.requests.commands.Commands;
import com.comeonbabys.android.app.utils.AppSession;
import com.comeonbabys.android.app.view.customview.ButtonCustom;
import com.comeonbabys.android.app.view.customview.EditTextCustom;
import com.comeonbabys.android.app.view.customview.RadioButtonCustom;
import com.comeonbabys.android.app.view.customview.TextViewCustom;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class ProfileConfigActivity extends BaseActivity implements View.OnClickListener {
	public final static int TYPE_DIALOG_CHOICE_NAM_SINH = 1;
	public final static int TYPE_DIALOG_CHOICE_CITY = 2;
	public final static int TYPE_DIALOG_DATE = 4;
	public final static int TYPE_DIALOG_DAY = 5;
	public final static int TYPE_DIALOG_RED_DAY = 6;

	Dialog dialogChoice;
	Dialog dialogDate;
	ProfileDTO profileDto;
	boolean isClick = false;
	List<CityDTO> listCity;
	Handler handler;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_profile_config);
		setupHideKeyboard(findViewById(R.id.layoutRootProfile));

		//Мои правки
		profileDto = AppSession.getSession().getSystemUser().getProfileDTO();
		initObjectUI();
        initHandler();
		CityDTO.updateCityList();
		//**************//
	}

	private void initObjectUI() {
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_profile_config);
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((ImageView) findViewById(R.id.imgBack)).setVisibility(View.GONE);
		((ButtonCustom) findViewById(R.id.btnButtonFinish)).setOnClickListener(this);
		((TextViewCustom) findViewById(R.id.textNamSinh)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutCity)).setOnClickListener(this);
		((TextViewCustom) findViewById(R.id.textChuKy)).setOnClickListener(this);
		((TextViewCustom) findViewById(R.id.textRedday)).setOnClickListener(this);
		((TextViewCustom) findViewById(R.id.textNgayCuoi)).setOnClickListener(this);
		((TextViewCustom) findViewById(R.id.textNamSinh)).setText("1970");
		((RadioButtonCustom) findViewById(R.id.radioMale)).setChecked(profileDto.isGender());
		((RadioButtonCustom) findViewById(R.id.radioFeMale)).setChecked(!profileDto.isGender());
	}

	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				Bundle data = msg.getData();
				String message = "";
				JSONObject jsdata = null;
				if(data.containsKey(ExtraConstants.MESSAGE)) message = data.getString(ExtraConstants.MESSAGE);
                stateAllButtons(true);
                hideProgress();
				try {
					if (data.containsKey(ExtraConstants.DATA)) {
						jsdata = new JSONObject(data.getString(ExtraConstants.DATA));
					}
				} catch (JSONException exc) {
                    showSnackMessage("Something wrong");
					exc.printStackTrace();
					return;
				}
				if(msg.what != Constants.MSG_ERROR) showSnackMessage(message);
				switch (msg.what) {
					case Constants.MSG_UPDATE_PROFILE_SUCCESS: {
						if(jsdata != null) {
							profileDto.parseFromJson(jsdata.toString());
							launchMainActivity();
						} else {
                            showSnackMessage("Something wrong");
                        }
						break;
					}
					case Constants.MSG_UPDATE_PROFILE_FAIL: {
						break;
					}
					//No connection
					case Constants.MSG_ERROR: {
						showSnackMessage("Something wrong");
						break;
					}
				}
			}
		};
	}

	//Всплывающее сообщение
	private void showSnackMessage(String msg) {
		Snackbar.make(findViewById(R.id.layoutRootProfile), msg, Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnButtonFinish:
			requestPasswordAction();
			break;
		case R.id.textNamSinh:
			showDialogNamSinh();
			break;
		case R.id.layoutCity:
			if (listCity != null)
				showDialogCity(listCity);
			else {
				isClick = true;
                listCity = CityDTO.getListCity();
                showDialogCity(listCity);
			}
			break;
		case R.id.textChuKy:
			showDialogChuKy();
			break;
		case R.id.textRedday:
			showDialogRedDay();
			break;
		case R.id.textNgayCuoi:
			showDialogNgayCuoi();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
	}

    //Запуск диалога выбора даты рождения
	private void showDialogNamSinh() {
		if (dialogChoice != null && dialogChoice.isShowing()) return;
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int size = year - 1970;
		String[] listYear = new String[size + 1];
		for (int i = 1970, j = 0; i <= year; i++, j++) {
			listYear[j] = i + "";
		}
		String strYear = (profileDto.getBirthYear() > 0) ? profileDto.getBirthYear() + "" : "1970";
		dialogChoice = DialogUtilities.createListChoiceDialog(baseActivity, listYear, strYear, this, TYPE_DIALOG_CHOICE_NAM_SINH);
		dialogChoice.show();
	}

    //Запуск диалога выбора городов
	private void showDialogCity(List<CityDTO> listCity) {
		if (dialogChoice != null && dialogChoice.isShowing())
			return;
        Long myCityID = (profileDto.getCity() != null && profileDto.getCity().getId() != null) ? profileDto.getCity().getId() : null;
		dialogChoice = DialogUtilities.createListCityDialog(baseActivity, listCity, myCityID, this, TYPE_DIALOG_CHOICE_CITY);
		dialogChoice.show();
	}

    //Запуск диалога выбора даты начала последних месячных
	private void showDialogNgayCuoi() {
		if (dialogDate != null && dialogDate.isShowing()) return;
		dialogDate = DialogUtilities.createDatePickerDialog(baseActivity, this, TYPE_DIALOG_DATE);
		dialogDate.show();
	}

    //Запуск диалога выбора длительности цикла
	private void showDialogChuKy() {
		if (dialogDate != null && dialogDate.isShowing()) return;
		dialogDate = DialogUtilities.createDayPickerDialog(baseActivity, this, TYPE_DIALOG_DAY);
		dialogDate.show();
	}

    //Запуск диалога выбора длительности месячных
	private void showDialogRedDay() {
		if (dialogDate != null && dialogDate.isShowing()) return;
		dialogDate = DialogUtilities.createRedDayPickerDialog(baseActivity, this, TYPE_DIALOG_RED_DAY);
		dialogDate.show();
	}

	/**
	 * @author PvTai
	 * @description This method used for check information and call asyncTask
	 *              login
	 */
	private void requestPasswordAction() {
        //Проверка на заполнение всех полей и вывод AlertDialog, если что-то не заполнено
		String err_msg = getString(R.string.string_error_input_infor_profile_config);
		String selectNamSinh = ((TextViewCustom) findViewById(R.id.textNamSinh)).getText().toString().trim();
		String selectCity = ((ButtonCustom) findViewById(R.id.btnCity)).getText().toString().trim();
		String selectDiachi = ((EditTextCustom) findViewById(R.id.editDiaChi)).getText().toString().trim();
		String selectChuKy = ((TextViewCustom) findViewById(R.id.textChuKy)).getText().toString().trim();
		String selectRedday = ((TextViewCustom) findViewById(R.id.textRedday)).getText().toString().trim();
		String selectNgayCuoi = ((TextViewCustom) findViewById(R.id.textNgayCuoi)).getText().toString().trim();
		String selectHeight = ((EditTextCustom) findViewById(R.id.editChieuCao)).getText().toString().trim();
		String selectCannang = ((EditTextCustom) findViewById(R.id.editCanNang)).getText().toString().trim();
		if (TextUtils.isEmpty(selectNamSinh) || TextUtils.isEmpty(selectCity)
				|| TextUtils.isEmpty(selectDiachi) || TextUtils.isEmpty(selectChuKy)
				|| TextUtils.isEmpty(selectRedday) || TextUtils.isEmpty(selectNgayCuoi)
				|| TextUtils.isEmpty(selectHeight) || TextUtils.isEmpty(selectCannang)) {
			DialogUtilities.showAlertDialog(ProfileConfigActivity.this, R.layout.dialog_error_warning, getString(R.string.string_error), err_msg, null);
			return;
		}

        //Сохранение данных
		showProgress();
		stateAllButtons(false);// Disable all button when login progress
		profileDto.setAddress(selectDiachi);
		profileDto.setGender(((RadioButtonCustom) findViewById(R.id.radioMale)).isChecked());
        profileDto.setBirthday(Integer.parseInt((String) ((TextViewCustom) findViewById(R.id.textNamSinh)).getText()));
		profileDto.setHeight(Double.parseDouble(selectHeight));
		profileDto.setWeight(Double.parseDouble(selectCannang));
		updateUser();
	}

	private void updateUser() {
		UserDTO user = AppSession.getSession().getSystemUser();
		Commands.updateUserProfile(handler, user, user.getProfileDTO());
	}

	private void stateAllButtons(boolean value) {
	}

	private void launchMainActivity() {
		Intent intent = new Intent(ProfileConfigActivity.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	}

	 /* (non-Javadoc)
	 * @see
	 * com.weaved.connect.common.listener.OnEventControlListener#onEvent(int, android.view.View, java.lang.Object)  */
    //Обработка результатов диалогов
	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case TYPE_DIALOG_CHOICE_NAM_SINH:
			if (dialogChoice != null) {
				((TextViewCustom) findViewById(R.id.textNamSinh)).setText((String) data);
				profileDto.setBirthday(Integer.parseInt((String) data));
				dialogChoice.dismiss();
				dialogChoice = null;
			}
			break;
		case TYPE_DIALOG_CHOICE_CITY:
			if (dialogChoice != null) {
				CityDTO city = (CityDTO) data;
				((ButtonCustom) findViewById(R.id.btnCity)).setText(city.getName());
				profileDto.setCity(city);
				dialogChoice.dismiss();
				dialogChoice = null;
			}
			break;
		case TYPE_DIALOG_DATE:
			if (dialogDate != null) {
				String date = (String) data;
				String[] split = date.split("-");
				date = split[0] + getString(R.string.text_year) + " " + split[1] + getString(R.string.text_month) + " " + split[2] + getString(R.string.text_day);
				((TextViewCustom) findViewById(R.id.textNgayCuoi)).setText(date);
				profileDto.setLast_cycle((String) data);
				dialogDate.dismiss();
				dialogDate = null;
			}
			break;
		case TYPE_DIALOG_DAY:
			if (dialogDate != null) {
				int date = (int) data;
				String strDate = String.valueOf(date) + getString(R.string.text_day);
				((TextViewCustom) findViewById(R.id.textChuKy)).setText(strDate);
				profileDto.setMenstrual_cycle(date);
				dialogDate.dismiss();
				dialogDate = null;
			}
			break;
		case TYPE_DIALOG_RED_DAY:
			if (dialogDate != null) {
				int date = (int) data;
				String strDate = String.valueOf(date) + getString(R.string.text_day);
				((TextViewCustom) findViewById(R.id.textRedday)).setText(strDate);
				profileDto.setRed_days(date);
				dialogDate.dismiss();
				dialogDate = null;
			}
			break;
		default:
			break;
		}
	}
}
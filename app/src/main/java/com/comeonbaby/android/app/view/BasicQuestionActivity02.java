package com.comeonbaby.android.app.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.DialogUtilities;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.utils.PrefsHelper;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.CheckBoxCustom;
import com.comeonbaby.android.app.view.customview.EditTextCustom;
import com.comeonbaby.android.app.view.customview.MultiTextWatcher;
import com.comeonbaby.android.app.view.customview.MultiTextWatcher.OnTextChangedListener;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

public class BasicQuestionActivity02 extends BaseActivity implements OnClickListener, OnTextChangedListener {
    CheckBoxCustom checkboxFemale, checkboxMale;
    CheckBoxCustom checkbox21, checkbox22, checkbox23, checkbox24, checkbox25;
    CheckBoxCustom checkbox26, checkbox27, checkbox28, checkbox29, checkbox30;
    CheckBoxCustom checkbox31, checkbox32, checkbox33, checkbox34, checkbox35;
    CheckBoxCustom checkbox36, checkbox37, checkbox38, checkbox39, checkbox40;
    EditTextCustom editAnswer34, editAnswer40;

    @Override
    protected void onCreateContent(Bundle savedInstanceState) {
        setContentView(R.layout.activity_basic_question2);
        initObjectUI();
        setupHideKeyboard(findViewById(R.id.layoutRootQuestion));
    }

    private void initObjectUI() {
        ((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_basic_question);
        ((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
        ((TextViewCustom) findViewById(R.id.txtTitle4)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        ((ButtonCustom) findViewById(R.id.btnButtonNextStep)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgQuestionStep)).setVisibility(View.VISIBLE);
        ((ImageView) findViewById(R.id.imgQuestionStep)).setImageResource(R.drawable.bg_basic_2);
        checkboxFemale = (CheckBoxCustom) findViewById(R.id.checkboxFemale);
        checkboxMale = (CheckBoxCustom) findViewById(R.id.checkboxMale);
        checkboxFemale.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
        checkboxMale.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
        checkbox21 = (CheckBoxCustom) findViewById(R.id.rad21);
        checkbox22 = (CheckBoxCustom) findViewById(R.id.rad22);
        checkbox23 = (CheckBoxCustom) findViewById(R.id.rad23);
        checkbox24 = (CheckBoxCustom) findViewById(R.id.rad24);
        checkbox25 = (CheckBoxCustom) findViewById(R.id.rad25);
        checkbox26 = (CheckBoxCustom) findViewById(R.id.rad26);
        checkbox27 = (CheckBoxCustom) findViewById(R.id.rad27);
        checkbox28 = (CheckBoxCustom) findViewById(R.id.rad28);
        checkbox29 = (CheckBoxCustom) findViewById(R.id.rad29);
        checkbox30 = (CheckBoxCustom) findViewById(R.id.rad30);
        checkbox31 = (CheckBoxCustom) findViewById(R.id.rad31);
        checkbox32 = (CheckBoxCustom) findViewById(R.id.rad32);
        checkbox33 = (CheckBoxCustom) findViewById(R.id.rad33);
        checkbox34 = (CheckBoxCustom) findViewById(R.id.rad34);
        checkbox35 = (CheckBoxCustom) findViewById(R.id.rad35);
        checkbox36 = (CheckBoxCustom) findViewById(R.id.rad36);
        checkbox37 = (CheckBoxCustom) findViewById(R.id.rad37);
        checkbox38 = (CheckBoxCustom) findViewById(R.id.rad38);
        checkbox39 = (CheckBoxCustom) findViewById(R.id.rad39);
        checkbox40 = (CheckBoxCustom) findViewById(R.id.rad40);
        editAnswer34 = (EditTextCustom) findViewById(R.id.editAnswer34);
        editAnswer40 = (EditTextCustom) findViewById(R.id.editAnswer40);
        editAnswer34.addTextChangedListener(new MultiTextWatcher(editAnswer34, this));
        editAnswer40.addTextChangedListener(new MultiTextWatcher(editAnswer40, this));

        //Set enabled/disabled parts for male/female
        checkboxFemale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkboxFemale.isChecked()) {
                    stateAllButtonsFemale(true);
                    stateAllButtonsMale(false);
                    checkboxMale.setChecked(false);
                } else {
                    stateAllButtonsFemale(false);
                    stateAllButtonsMale(true);
                    checkboxMale.setChecked(true);
                }
            }
        });
        checkboxMale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkboxMale.isChecked()) {
                    stateAllButtonsFemale(false);
                    stateAllButtonsMale(true);
                    checkboxFemale.setChecked(false);
                } else {
                    stateAllButtonsFemale(true);
                    stateAllButtonsMale(false);
                    checkboxFemale.setChecked(true);
                }
            }
        });
        checkboxFemale.performClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnButtonNextStep:
                saveQuestionsData();
                break;
            default:
                break;
        }
    }

    //Установка активности всех элементов для женской части
    private void stateAllButtonsFemale(boolean value) {
        checkbox21.setEnabled(value);
        checkbox22.setEnabled(value);
        checkbox23.setEnabled(value);
        checkbox24.setEnabled(value);
        checkbox25.setEnabled(value);
        checkbox26.setEnabled(value);
        checkbox27.setEnabled(value);
        checkbox28.setEnabled(value);
        checkbox29.setEnabled(value);
        checkbox30.setEnabled(value);
        checkbox31.setEnabled(value);
        checkbox32.setEnabled(value);
        checkbox33.setEnabled(value);
        checkbox34.setEnabled(value);
        editAnswer34.setEnabled(value);
    }

    //Установка активности всех элементов для мужской части
    private void stateAllButtonsMale(boolean value) {
        checkbox35.setEnabled(value);
        checkbox36.setEnabled(value);
        checkbox37.setEnabled(value);
        checkbox38.setEnabled(value);
        checkbox39.setEnabled(value);
        checkbox40.setEnabled(value);
        editAnswer40.setEnabled(value);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //Save json data to prefences
    private void saveQuestionsData() {
        stateAllButtons(false);

        PrefsHelper prefsHelper = PrefsHelper.getPrefsHelper();
        String str = ((String) prefsHelper.getPref(PrefsHelper.PREF_BASIC_ANSWERS_1));

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("!!!!!!!!!!B_Q_2:JSON", jsonObject.toString());

        try {
            //Save answers for female part checked
            if (checkboxFemale.isChecked()) {
                if (checkbox21.isChecked()) jsonObject.put("question_2_21", "true");
                if (checkbox22.isChecked()) jsonObject.put("question_2_22", "true");
                if (checkbox23.isChecked()) jsonObject.put("question_2_23", "true");
                if (checkbox24.isChecked()) jsonObject.put("question_2_24", "true");
                if (checkbox25.isChecked()) jsonObject.put("question_2_25", "true");
                if (checkbox26.isChecked()) jsonObject.put("question_2_26", "true");
                if (checkbox27.isChecked()) jsonObject.put("question_2_27", "true");
                if (checkbox28.isChecked()) jsonObject.put("question_2_28", "true");
                if (checkbox29.isChecked()) jsonObject.put("question_2_29", "true");
                if (checkbox30.isChecked()) jsonObject.put("question_2_30", "true");
                if (checkbox31.isChecked()) jsonObject.put("question_2_31", "true");
                if (checkbox32.isChecked()) jsonObject.put("question_2_32", "true");
                if (checkbox33.isChecked()) jsonObject.put("question_2_33", "true");
                if (checkbox34.isChecked()) {
                    if (TextUtils.isEmpty(editAnswer34.getText().toString().trim())) {
                        DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question, null);
                        stateAllButtons(true);
                        return;
                    }
                    else jsonObject.put("question_2_34", editAnswer34.getText().toString().trim());
                }
            }
            //Save answers for male part checked
            else {
                if (checkbox35.isChecked()) jsonObject.put("question_2_35", "true");
                if (checkbox36.isChecked()) jsonObject.put("question_2_36", "true");
                if (checkbox37.isChecked()) jsonObject.put("question_2_37", "true");
                if (checkbox38.isChecked()) jsonObject.put("question_2_38", "true");
                if (checkbox39.isChecked()) jsonObject.put("question_2_39", "true");
                if (checkbox40.isChecked()) {
                    if (TextUtils.isEmpty(editAnswer40.getText().toString().trim())) {
                        DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question, null);
                        stateAllButtons(true);
                        return;
                    }
                    else  jsonObject.put("question_2_40", editAnswer40.getText().toString().trim());
                }
            }
        }
        catch (JSONException exc) {
            exc.printStackTrace();
            stateAllButtons(true);
            return;
        }

        //Alert if no selected items
        if (jsonObject.length() < 1) {
            DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question, null);
            stateAllButtons(true);
            return;
        }

        //Save answers to prefences
        PrefsHelper.getPrefsHelper().savePref(PrefsHelper.PREF_BASIC_ANSWERS_2, jsonObject.toString());

        Log.d("BQ2", (String) PrefsHelper.getPrefsHelper().getPref(PrefsHelper.PREF_BASIC_ANSWERS_2));  //временный лог

        //Start next activity
        stateAllButtons(true);
        startActivity(new Intent(this, BasicQuestionActivity03.class));
    }

    private void stateAllButtons(boolean value) {
        ((ButtonCustom) findViewById(R.id.btnButtonNextStep)).setEnabled(value);
    }

    //Set checkbox setted when typing in text field
    @Override
    public void onEvent(int id, String text) {
        switch (id) {
            case R.id.editAnswer34:
                if (TextUtils.isEmpty(text)) checkbox34.setChecked(false);
                else checkbox34.setChecked(true);
                break;
            case R.id.editAnswer40:
                if (TextUtils.isEmpty(text)) checkbox40.setChecked(false);
                else checkbox40.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
    }
}

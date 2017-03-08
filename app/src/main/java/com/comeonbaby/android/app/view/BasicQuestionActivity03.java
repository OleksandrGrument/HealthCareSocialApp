package com.comeonbaby.android.app.view;

/**
 * Created by olegs on 20.12.2016.
 */
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

public class BasicQuestionActivity03 extends BaseActivity implements OnClickListener, OnTextChangedListener {
    CheckBoxCustom checkbox41, checkbox42, checkbox43, checkbox44, checkbox45;
    CheckBoxCustom checkbox46, checkbox47, checkbox48, checkbox49, checkbox50;
    CheckBoxCustom checkbox51, checkbox52, checkbox53, checkbox54, checkbox55;
    CheckBoxCustom checkbox56, checkbox57;
    EditTextCustom editAnswer48, editAnswer57;

    @Override
    protected void onCreateContent(Bundle savedInstanceState) {
        setContentView(R.layout.activity_basic_question3);
        initObjectUI();
        setupHideKeyboard(findViewById(R.id.layoutRootQuestion));
    }

    private void initObjectUI() {
        ((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_basic_question);
        ((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
        ((TextViewCustom) findViewById(R.id.txtTitle5)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
        ((TextViewCustom) findViewById(R.id.txtTitle6)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);

        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        ((ButtonCustom) findViewById(R.id.btnButtonNextStep)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgQuestionStep)).setVisibility(View.VISIBLE);
        ((ImageView) findViewById(R.id.imgQuestionStep)).setImageResource(R.drawable.bg_basic_3);

        checkbox41 = (CheckBoxCustom) findViewById(R.id.rad41);
        checkbox42 = (CheckBoxCustom) findViewById(R.id.rad42);
        checkbox43 = (CheckBoxCustom) findViewById(R.id.rad43);
        checkbox44 = (CheckBoxCustom) findViewById(R.id.rad44);
        checkbox45 = (CheckBoxCustom) findViewById(R.id.rad45);
        checkbox46 = (CheckBoxCustom) findViewById(R.id.rad46);
        checkbox47 = (CheckBoxCustom) findViewById(R.id.rad47);
        checkbox48 = (CheckBoxCustom) findViewById(R.id.rad48);
        checkbox49 = (CheckBoxCustom) findViewById(R.id.rad49);
        checkbox50 = (CheckBoxCustom) findViewById(R.id.rad50);
        checkbox51 = (CheckBoxCustom) findViewById(R.id.rad51);
        checkbox52 = (CheckBoxCustom) findViewById(R.id.rad52);
        checkbox53 = (CheckBoxCustom) findViewById(R.id.rad53);
        checkbox54 = (CheckBoxCustom) findViewById(R.id.rad54);
        checkbox55 = (CheckBoxCustom) findViewById(R.id.rad55);
        checkbox56 = (CheckBoxCustom) findViewById(R.id.rad56);
        checkbox57 = (CheckBoxCustom) findViewById(R.id.rad57);
        editAnswer48 = (EditTextCustom) findViewById(R.id.editAnswer48);
        editAnswer57 = (EditTextCustom) findViewById(R.id.editAnswer57);
        editAnswer48.addTextChangedListener(new MultiTextWatcher(editAnswer48, this));
        editAnswer57.addTextChangedListener(new MultiTextWatcher(editAnswer57, this));
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

    private void saveQuestionsData() {
        stateAllButtons(false);

        PrefsHelper prefsHelper = PrefsHelper.getPrefsHelper();
        String str = ((String) prefsHelper.getPref(PrefsHelper.PREF_BASIC_ANSWERS_2));
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("!!!!!!!!!!B_Q_3:JSON", jsonObject.toString());
        try {
            //First part
            if (checkbox41.isChecked()) jsonObject.put("question_3_41", "true");
            if (checkbox42.isChecked()) jsonObject.put("question_3_42", "true");
            if (checkbox43.isChecked()) jsonObject.put("question_3_43", "true");
            if (checkbox44.isChecked()) jsonObject.put("question_3_44", "true");
            if (checkbox45.isChecked()) jsonObject.put("question_3_45", "true");
            if (checkbox46.isChecked()) jsonObject.put("question_3_46", "true");
            if (checkbox47.isChecked()) jsonObject.put("question_3_47", "true");
            if (checkbox48.isChecked()) {
                if (TextUtils.isEmpty(editAnswer48.getText().toString().trim())) {
                    DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question, null);
                    stateAllButtons(true);
                    return;
                }
                else  jsonObject.put("question_3_48", editAnswer48.getText().toString().trim());
            }
            //Check if one or more items checked in first part
            if (jsonObject.length() < 1) {
                DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question, null);
                stateAllButtons(true);
                return;
            }

            //Second part
            int counter = 0;    //counter to check number of selected items
            if (checkbox49.isChecked()) {
                jsonObject.put("question_3_49", "true");
                counter++;
            }
            if (checkbox50.isChecked()) {
                jsonObject.put("question_3_50", "true");
                counter++;
            }
            if (checkbox51.isChecked()) {
                jsonObject.put("question_3_51", "true");
                counter++;
            }
            if (checkbox52.isChecked()) {
                jsonObject.put("question_3_52", "true");
                counter++;
            }
            if (checkbox53.isChecked()) {
                jsonObject.put("question_3_53", "true");
                counter++;
            }
            if (checkbox54.isChecked()) {
                jsonObject.put("question_3_54", "true");
                counter++;
            }
            if (checkbox55.isChecked()) {
                jsonObject.put("question_3_55", "true");
                counter++;
            }
            if (checkbox56.isChecked()) {
                jsonObject.put("question_3_56", "true");
                counter++;
            }
            if (checkbox57.isChecked()) {
                if (TextUtils.isEmpty(editAnswer57.getText().toString().trim())) {
                    DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question, null);
                    stateAllButtons(true);
                    return;
                } else {
                    jsonObject.put("question_3_57", editAnswer57.getText().toString().trim());
                    counter++;
                }
            }
            //Check if one or more items checked in second part
            if(counter == 0) {
                Log.d("COUNTER", String.valueOf(counter));
                DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question, null);
                stateAllButtons(true);
                return;
            }
        }
        catch (JSONException exc) {
            exc.printStackTrace();
            stateAllButtons(true);
            return;
        }

        //Save answers to prefences
        PrefsHelper.getPrefsHelper().savePref(PrefsHelper.PREF_BASIC_ANSWERS_3, jsonObject.toString());

        Log.d("BQ3", (String) PrefsHelper.getPrefsHelper().getPref(PrefsHelper.PREF_BASIC_ANSWERS_3));  //временный лог

        //Start next activity
        stateAllButtons(true);
        startActivity(new Intent(this, BasicQuestionActivityFinish.class));
    }

    private void stateAllButtons(boolean value) {
        ((ButtonCustom) findViewById(R.id.btnButtonNextStep)).setEnabled(value);
    }

    //Set checkbox setted when typing in text field
    @Override
    public void onEvent(int id, String text) {
        switch (id) {
            case R.id.editAnswer48:
                if (TextUtils.isEmpty(text)) checkbox48.setChecked(false);
                else checkbox48.setChecked(true);
                break;
            case R.id.editAnswer57:
                if (TextUtils.isEmpty(text)) checkbox57.setChecked(false);
                else checkbox57.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
    }
}
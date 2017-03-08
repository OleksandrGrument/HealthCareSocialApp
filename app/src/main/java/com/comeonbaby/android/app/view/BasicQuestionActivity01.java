package com.comeonbaby.android.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.DialogUtilities;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.utils.PrefsHelper;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.CheckBoxCustom;
import com.comeonbaby.android.app.view.customview.EditTextCustom;
import com.comeonbaby.android.app.view.customview.MultiTextWatcher;
import com.comeonbaby.android.app.view.customview.RadioButtonCustom;
import com.comeonbaby.android.app.view.customview.TextViewCustom;
import com.comeonbaby.android.app.view.customview.ToggleButtonGroupTableLayout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by olegs on 19.12.2016.
 */

public class BasicQuestionActivity01 extends BaseActivity implements View.OnClickListener, MultiTextWatcher.OnTextChangedListener {

    ToggleButtonGroupTableLayout radGroup1, radGroup2;

    @Override
    protected void onCreateContent(Bundle savedInstanceState) {
        setContentView(R.layout.activity_basic_question);
        initObjectUI();
        setupHideKeyboard(findViewById(R.id.layoutRootQuestion));
    }

    //Инициализация UI
    private void initObjectUI() {
        ((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_basic_question);
        ((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
        ((TextViewCustom) findViewById(R.id.txtTitle1)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
        ((TextViewCustom) findViewById(R.id.txtTitle2)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
        ((TextViewCustom) findViewById(R.id.txtTitle3)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
        radGroup1 = (ToggleButtonGroupTableLayout) findViewById(R.id.radGroup1);
        radGroup2 = (ToggleButtonGroupTableLayout) findViewById(R.id.radGroup2);

        ((ImageView) findViewById(R.id.imgBack)).setVisibility(View.GONE);
        ((ButtonCustom) findViewById(R.id.btnButtonNextStep)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgQuestionStep)).setVisibility(View.VISIBLE);
        ((ImageView) findViewById(R.id.imgQuestionStep)).setImageResource(R.drawable.bg_basic_1);

        ((EditTextCustom) findViewById(R.id.editAnswer11)).addTextChangedListener(new MultiTextWatcher((EditTextCustom) findViewById(R.id.editAnswer11), this));
        ((EditTextCustom) findViewById(R.id.editAnswer12)).addTextChangedListener(new MultiTextWatcher((EditTextCustom) findViewById(R.id.editAnswer12), this));
        ((EditTextCustom) findViewById(R.id.editAnswer13)).addTextChangedListener(new MultiTextWatcher((EditTextCustom) findViewById(R.id.editAnswer13), this));
        ((EditTextCustom) findViewById(R.id.editAnswer14)).addTextChangedListener(new MultiTextWatcher((EditTextCustom) findViewById(R.id.editAnswer14), this));
        ((EditTextCustom) findViewById(R.id.editAnswer15)).addTextChangedListener(new MultiTextWatcher((EditTextCustom) findViewById(R.id.editAnswer15), this));
        ((EditTextCustom) findViewById(R.id.editAnswer16)).addTextChangedListener(new MultiTextWatcher((EditTextCustom) findViewById(R.id.editAnswer16), this));
        ((EditTextCustom) findViewById(R.id.editAnswer17)).addTextChangedListener(new MultiTextWatcher((EditTextCustom) findViewById(R.id.editAnswer17), this));
        ((EditTextCustom) findViewById(R.id.editAnswer18)).addTextChangedListener(new MultiTextWatcher((EditTextCustom) findViewById(R.id.editAnswer18), this));
        ((EditTextCustom) findViewById(R.id.editAnswer19)).addTextChangedListener(new MultiTextWatcher((EditTextCustom) findViewById(R.id.editAnswer19), this));
        ((EditTextCustom) findViewById(R.id.editAnswer20)).addTextChangedListener(new MultiTextWatcher((EditTextCustom) findViewById(R.id.editAnswer20), this));
    }

    //Слушатель нажатия кнопок (Next step)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        //Если на это активити перебросило после ошибки, то выводим сообщение: начните сначала
        if(getIntent().getBooleanExtra("AFTER_ERR", false) == true) {
            DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.basic_questions_error, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //Метод проверяет заполнение обязательных полей и галок и создает JSON обьект на основе ответов
    //Если что-то не заполнено выводит предупреждение
    private void saveQuestionsData() {
        stateAllButtons(false);// Disable all button when login progress
        // going on
        JSONObject jsonObject = new JSONObject();
        try {
            //Проверка заполнения первой группы RadioButton, вывод AlertDialog если не заполнено; запись в JSON
             if (radGroup1.getCheckedRadioButtonId() <= 0) {
                DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question_1, null);
                stateAllButtons(true);
                return;
            }
            String answer = "";
            int checked = radGroup1.getCheckedRadioButtonId();
            if(checked == R.id.rad1) answer = "0";
            else if (checked == R.id.rad2) answer = "1-2";
            else if (checked == R.id.rad3) answer = "2-3";
            else if (checked == R.id.rad4) answer = "3-5";
            else if (checked == R.id.rad5) answer = "5+";
            jsonObject.put("question_1_1", answer);

            //Проверка заполнения второй группы RadioButton, вывод AlertDialog если не заполнено; запись в JSON
            if (radGroup2.getCheckedRadioButtonId() <= 0) {
                DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question_1, null);
                stateAllButtons(true);
                return;
            }
            answer = "";
            checked = radGroup2.getCheckedRadioButtonId();
            if(checked == R.id.rad6) answer = "0";
            else if (checked == R.id.rad7) answer = "1";
            else if (checked == R.id.rad8) answer = "2";
            else if (checked == R.id.rad9) answer = "3";
            else if (checked == R.id.rad10) answer = "4+";
            jsonObject.put("question_1_2", answer);

            //Проверяем чекбоксы у третьей группы и заполнение соответствующих полей, записываем JSON
            if (((CheckBoxCustom) findViewById(R.id.rad11)).isChecked()) {
                if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.editAnswer11)).getText().toString())) {
                    DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question_1, null);
                    stateAllButtons(true);
                    return;
                }
                else jsonObject.put("question_1_3_11", ((EditTextCustom) findViewById(R.id.editAnswer11)).getText().toString());
            }
            if (((CheckBoxCustom) findViewById(R.id.rad12)).isChecked()) {
                if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.editAnswer12)).getText().toString())) {
                    DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question_1, null);
                    stateAllButtons(true);
                    return;
                }
                else jsonObject.put("question_1_3_12", ((EditTextCustom) findViewById(R.id.editAnswer12)).getText().toString());
            }
            if (((CheckBoxCustom) findViewById(R.id.rad13)).isChecked()) {
                if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.editAnswer13)).getText().toString())) {
                    DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question_1, null);
                    stateAllButtons(true);
                    return;
                }
                else jsonObject.put("question_1_3_13", ((EditTextCustom) findViewById(R.id.editAnswer13)).getText().toString());
            }
            if (((CheckBoxCustom) findViewById(R.id.rad14)).isChecked()) {
                if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.editAnswer14)).getText().toString())) {
                    DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question_1, null);
                    stateAllButtons(true);
                    return;
                }
                else jsonObject.put("question_1_3_14", ((EditTextCustom) findViewById(R.id.editAnswer14)).getText().toString());
            }
            if (((CheckBoxCustom) findViewById(R.id.rad15)).isChecked()) {
                if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.editAnswer15)).getText().toString())) {
                    DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question_1, null);
                    stateAllButtons(true);
                    return;
                }
                else jsonObject.put("question_1_3_15", ((EditTextCustom) findViewById(R.id.editAnswer15)).getText().toString());
            }
            if (((CheckBoxCustom) findViewById(R.id.rad16)).isChecked()) {
                if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.editAnswer16)).getText().toString())) {
                    DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question_1, null);
                    stateAllButtons(true);
                    return;
                }
                else jsonObject.put("question_1_3_16", ((EditTextCustom) findViewById(R.id.editAnswer16)).getText().toString());
            }
            if (((CheckBoxCustom) findViewById(R.id.rad17)).isChecked()) {
                if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.editAnswer17)).getText().toString())) {
                    DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question_1, null);
                    stateAllButtons(true);
                    return;
                }
                else jsonObject.put("question_1_3_17", ((EditTextCustom) findViewById(R.id.editAnswer17)).getText().toString());
            }
            if (((CheckBoxCustom) findViewById(R.id.rad18)).isChecked()) {
                if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.editAnswer18)).getText().toString())) {
                    DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question_1, null);
                    stateAllButtons(true);
                    return;
                }
                else jsonObject.put("question_1_3_18", ((EditTextCustom) findViewById(R.id.editAnswer18)).getText().toString());
            }
            if (((CheckBoxCustom) findViewById(R.id.rad19)).isChecked()) {
                if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.editAnswer19)).getText().toString())) {
                    DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question_1, null);
                    stateAllButtons(true);
                    return;
                }
                else jsonObject.put("question_1_3_19", ((EditTextCustom) findViewById(R.id.editAnswer19)).getText().toString());
            }
            if (((CheckBoxCustom) findViewById(R.id.rad20)).isChecked()) {
                if (TextUtils.isEmpty(((EditTextCustom) findViewById(R.id.editAnswer20)).getText().toString())) {
                    DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question_1, null);
                    stateAllButtons(true);
                    return;
                }
                else jsonObject.put("question_1_3_20", ((EditTextCustom) findViewById(R.id.editAnswer20)).getText().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            stateAllButtons(true);
            return;
        }

        //Колличество ответов в трех группах должно быть 3 и более, иначе AlertDialog
        if(jsonObject.length() < 3) {
            DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.string_error_check_basic_question_1, null);
            stateAllButtons(true);
            return;
        }

        //Save answers to prefences
        PrefsHelper.getPrefsHelper().savePref(PrefsHelper.PREF_BASIC_ANSWERS_1, jsonObject.toString());

        Log.d("BQ1", (String) PrefsHelper.getPrefsHelper().getPref(PrefsHelper.PREF_BASIC_ANSWERS_1));  //временный лог

        //Переход на след активити
        Intent intent = new Intent(this, BasicQuestionActivity02.class);
        stateAllButtons(true);
        startActivity(intent);
    }

    //Включение-отключение кнопки перехода
    private void stateAllButtons(boolean value) {
        ((ButtonCustom) findViewById(R.id.btnButtonNextStep)).setEnabled(value);
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
    }

    //Слушатель событий текстовых полей, переключает CheckBoxCustom в зависимости от того,
    //есть ли текст в поле справа
    @Override
    public void onEvent(int id, String text) {
        switch (id) {
            case R.id.editAnswer11:
                if (TextUtils.isEmpty(text)) ((CheckBoxCustom) findViewById(R.id.rad11)).setChecked(false);
                else ((CheckBoxCustom) findViewById(R.id.rad11)).setChecked(true);
                break;
            case R.id.editAnswer12:
                if (TextUtils.isEmpty(text)) ((CheckBoxCustom) findViewById(R.id.rad12)).setChecked(false);
                else ((CheckBoxCustom) findViewById(R.id.rad12)).setChecked(true);
                break;
            case R.id.editAnswer13:
                if (TextUtils.isEmpty(text)) ((CheckBoxCustom) findViewById(R.id.rad13)).setChecked(false);
                else ((CheckBoxCustom) findViewById(R.id.rad13)).setChecked(true);
                break;
            case R.id.editAnswer14:
                if (TextUtils.isEmpty(text)) ((CheckBoxCustom) findViewById(R.id.rad14)).setChecked(false);
                else ((CheckBoxCustom) findViewById(R.id.rad14)).setChecked(true);
                break;
            case R.id.editAnswer15:
                if (TextUtils.isEmpty(text)) ((CheckBoxCustom) findViewById(R.id.rad15)).setChecked(false);
                else ((CheckBoxCustom) findViewById(R.id.rad15)).setChecked(true);
                break;
            case R.id.editAnswer16:
                if (TextUtils.isEmpty(text)) ((CheckBoxCustom) findViewById(R.id.rad16)).setChecked(false);
                else ((CheckBoxCustom) findViewById(R.id.rad16)).setChecked(true);
                break;
            case R.id.editAnswer17:
                if (TextUtils.isEmpty(text)) ((CheckBoxCustom) findViewById(R.id.rad17)).setChecked(false);
                else ((CheckBoxCustom) findViewById(R.id.rad17)).setChecked(true);
                break;
            case R.id.editAnswer18:
                if (TextUtils.isEmpty(text)) ((CheckBoxCustom) findViewById(R.id.rad18)).setChecked(false);
                else ((CheckBoxCustom) findViewById(R.id.rad18)).setChecked(true);
                break;
            case R.id.editAnswer19:
                if (TextUtils.isEmpty(text)) ((CheckBoxCustom) findViewById(R.id.rad19)).setChecked(false);
                else ((CheckBoxCustom) findViewById(R.id.rad19)).setChecked(true);
                break;
            case R.id.editAnswer20:
                if (TextUtils.isEmpty(text)) ((CheckBoxCustom) findViewById(R.id.rad20)).setChecked(false);
                else ((CheckBoxCustom) findViewById(R.id.rad20)).setChecked(true);
                break;
            default:
                break;
        }
    }
}
package com.comeonbabys.android.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.DialogUtilities;
import com.comeonbabys.android.app.common.Globals;
import com.comeonbabys.android.app.db.dto.NoteDTO;
import com.comeonbabys.android.app.view.customview.ButtonCustom;
import com.comeonbabys.android.app.view.customview.CheckBoxCustom;
import com.comeonbabys.android.app.view.customview.TextViewCustom;

public class DayNutsActivity extends BaseActivity implements OnClickListener {

    RadioGroup radioGroup;
    RadioButton radioDung, radioNoDung;
    CheckBoxCustom checkBox1, checkBox2, checkBox3, checkBox4;
    TextViewCustom text1, text2, text3, text4;
    NoteDTO note;

    protected void onCreateContent(Bundle savedInstanceState) {
        setContentView(R.layout.activity_day_nuts);
        note = (NoteDTO) getIntent().getSerializableExtra("VALUE1");
        initObjectUI();
        setupHideKeyboard(findViewById(R.id.layoutRootHat));
    }

    private void initObjectUI() {
        ((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_hat);
        ((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
        ((ButtonCustom) findViewById(R.id.btnButtonFinishHat)).setOnClickListener(this);
        checkBox1 = (CheckBoxCustom) findViewById(R.id.checkbox_Hat_Goiy_1);
        checkBox2 = (CheckBoxCustom) findViewById(R.id.checkbox_Hat_Goiy_2);
        checkBox3 = (CheckBoxCustom) findViewById(R.id.checkbox_Hat_Goiy_3);
        checkBox4 = (CheckBoxCustom) findViewById(R.id.checkbox_Hat_Goiy_4);
        text1 = (TextViewCustom) findViewById(R.id.txtNuts1);
        text2 = (TextViewCustom) findViewById(R.id.txtNuts2);
        text3 = (TextViewCustom) findViewById(R.id.txtNuts3);
        text4 = (TextViewCustom) findViewById(R.id.txtNuts4);
        radioDung = (RadioButton) findViewById(R.id.checboxDung);
        radioNoDung = (RadioButton) findViewById(R.id.checboxNoDung);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupNuts);
        /* Attach CheckedChangeListener to radio group */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    if (checkedId == radioNoDung.getId()) {
                        stateAllButtons(false);
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(false);
                    } else stateAllButtons(true);
                }
            }
        });
        showChooseFirst();
    }

    //If note allready contains some nuts info, then set checkboxes and radiobuttons
    private void showChooseFirst() {
        if (note != null) {
            if (!TextUtils.isEmpty(note.isHas_nut()) && !note.isHas_nut().equals("null") && note.isHas_nut().equals("true")) {
                radioDung.setChecked(true);
                radioNoDung.setChecked(false);
                stateAllButtons(true);
                if (note.getRecommended_nuts() != null) {
                    if (note.getRecommended_nuts().contains(",")) {
                        String[] splitStrArr = note.getRecommended_nuts().split(",");
                        for (String str : splitStrArr) {
                            try {
                                if(str.equals(text1.getText().toString())) checkBox1.setChecked(true);
                                else if(str.equals(text2.getText().toString())) checkBox2.setChecked(true);
                                else if(str.equals(text3.getText().toString())) checkBox3.setChecked(true);
                                else if(str.equals(text4.getText().toString())) checkBox4.setChecked(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            String str = note.getRecommended_nuts();
                            try {
                                if(str.equals(text1.getText().toString())) checkBox1.setChecked(true);
                                else if(str.equals(text2.getText().toString())) checkBox2.setChecked(true);
                                else if(str.equals(text3.getText().toString())) checkBox3.setChecked(true);
                                else if(str.equals(text4.getText().toString())) checkBox4.setChecked(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                radioDung.setChecked(false);
                radioNoDung.setChecked(true);
                stateAllButtons(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnButtonFinishHat:
                actionFinishButton();
                break;
            default:
                break;
        }
    }

    // @author PvTai This method used for set enable/disable all button
    private void stateAllButtons(boolean value) {
        checkBox1.setEnabled(value);
        checkBox2.setEnabled(value);
        checkBox3.setEnabled(value);
        checkBox4.setEnabled(value);
    }

    // @author PvTai This method used for check exist checked option
    private boolean isCheckedOption() {
        return (checkBox1.isChecked() | checkBox2.isChecked() | checkBox3.isChecked() | checkBox4.isChecked());
    }

    //Check for required fields, if all ok start saveDataAndFinish() method
    private void actionFinishButton() {
        if (radioDung.isChecked()) {
            if (isCheckedOption() == false) {
                DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.error_enter_hat, null);
                return;
            } else {
                saveDataAndFinish();
            }
        } else if (radioNoDung.isChecked()) {
            saveDataAndFinish();
        } else {
            DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.error_enter_hat, null);
            return;
        }
    }

    private void saveDataAndFinish() {
        String nuts = "";
        if (radioDung.isChecked()) {
            if (checkBox1.isChecked())
                nuts = text1.getText().toString();
            if (checkBox2.isChecked()) {
                if (TextUtils.isEmpty(nuts)) nuts = text2.getText().toString();
                else nuts += "," + text2.getText().toString();
            }
            if (checkBox3.isChecked()) {
                if (TextUtils.isEmpty(nuts)) nuts = text3.getText().toString();
                else nuts += "," + text3.getText().toString();
            }
            if (checkBox4.isChecked()) {
                if (TextUtils.isEmpty(nuts)) nuts = text4.getText().toString();
                else nuts += "," + text4.getText().toString();
            }
            note.setHas_nut("true");
            note.setRecommended_nuts(nuts);
        } else {
            note.setHas_nut("false");
            note.setRecommended_nuts(null);
        }
        Intent intent = new Intent();
        intent.putExtra("NOTE", note);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
    }
}
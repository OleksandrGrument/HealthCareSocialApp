package com.comeonbaby.android.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.DialogUtilities;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.db.dto.NoteDTO;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.CheckBoxCustom;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

public class DayTeaActivity extends BaseActivity implements OnClickListener {

	RadioGroup radioGroup;
	RadioButton radioDung, radioNoDung;
	CheckBoxCustom checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6;
	TextViewCustom text1, text2, text3, text4, text5, text6;
	NoteDTO note;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_day_tea);
		note = (NoteDTO) getIntent().getSerializableExtra("VALUE1");
		initObjectUI();
		setupHideKeyboard(findViewById(R.id.layoutRootTea));
	}

	private void initObjectUI() {
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_tra);
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.btnButtonFinishTra)).setOnClickListener(this);
		checkBox1 = (CheckBoxCustom) findViewById(R.id.checkbox_Tra_Goiy_1);
		checkBox2 = (CheckBoxCustom) findViewById(R.id.checkbox_Tra_Goiy_2);
		checkBox3 = (CheckBoxCustom) findViewById(R.id.checkbox_Tra_Goiy_3);
		checkBox4 = (CheckBoxCustom) findViewById(R.id.checkbox_Tra_Goiy_4);
		checkBox5 = (CheckBoxCustom) findViewById(R.id.checkbox_Tra_Goiy_5);
		checkBox6 = (CheckBoxCustom) findViewById(R.id.checkbox_Tra_Goiy_6);
		text1 = (TextViewCustom) findViewById(R.id.textGoiy1);
		text2 = (TextViewCustom) findViewById(R.id.textGoiy2);
		text3 = (TextViewCustom) findViewById(R.id.textGoiy3);
		text4 = (TextViewCustom) findViewById(R.id.textGoiy4);
		text5 = (TextViewCustom) findViewById(R.id.textGoiy5);
		text6 = (TextViewCustom) findViewById(R.id.textGoiy6);
		radioDung = (RadioButton) findViewById(R.id.checboxDung);
		radioNoDung = (RadioButton) findViewById(R.id.checboxNoDung);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroupTea);
		/* Attach CheckedChangeListener to radio group */
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton rb = (RadioButton) group.findViewById(checkedId);
				if (null != rb && checkedId > -1) {
					if (checkedId == radioNoDung.getId()) {
                        stateAllButtons(false);
                        checkBox1.setChecked(false); checkBox2.setChecked(false); checkBox3.setChecked(false);
                        checkBox4.setChecked(false); checkBox5.setChecked(false); checkBox6.setChecked(false);
                    }
					else
						stateAllButtons(true);
				}
			}
		});
		if (note != null) {
			if (!TextUtils.isEmpty(note.isHas_tea()) && !note.isHas_tea().equals("null") && note.isHas_tea().equals("true")) {
				radioDung.setChecked(true);
				radioNoDung.setChecked(false);
				stateAllButtons(true);
				if (note.getRecommended_teas() != null) {
					if (note.getRecommended_teas().contains(",")) {
						String[] split = note.getRecommended_teas().split(",");
						for (String str : split) {
							try {
								if (str.equals(text1.getText().toString()))
									checkBox1.setChecked(true);
								else if (str.equals(text2.getText().toString()))
									checkBox2.setChecked(true);
								else if (str.equals(text3.getText().toString()))
									checkBox3.setChecked(true);
								else if (str.equals(text4.getText().toString()))
									checkBox4.setChecked(true);
								else if (str.equals(text5.getText().toString()))
									checkBox5.setChecked(true);
								else if (str.equals(text6.getText().toString()))
									checkBox6.setChecked(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						try {
							String str = note.getRecommended_teas();
							if (str.equals(text1.getText().toString()))
								checkBox1.setChecked(true);
							else if (str.equals(text2.getText().toString()))
								checkBox2.setChecked(true);
							else if (str.equals(text3.getText().toString()))
								checkBox3.setChecked(true);
							else if (str.equals(text4.getText().toString()))
								checkBox4.setChecked(true);
							else if (str.equals(text5.getText().toString()))
								checkBox5.setChecked(true);
							else if (str.equals(text6.getText().toString()))
								checkBox6.setChecked(true);
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
		case R.id.btnButtonFinishTra:
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
		checkBox5.setEnabled(value);
		checkBox6.setEnabled(value);
	}

	// @author PvTai This method used for check exist checked option
	private boolean isCheckedOption() {
		return (checkBox1.isChecked() | checkBox2.isChecked() | checkBox3.isChecked()
                | checkBox4.isChecked() | checkBox5.isChecked() | checkBox6.isChecked());
	}

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
        String teas = "";
		if (radioDung.isChecked()) {
			if (checkBox1.isChecked())
				teas = text1.getText().toString();
			if (checkBox2.isChecked()) {
				if (TextUtils.isEmpty(teas))
					teas = text2.getText().toString();
				else
					teas += "," + text2.getText().toString();
			}
			if (checkBox3.isChecked()) {
				if (TextUtils.isEmpty(teas))
					teas = text3.getText().toString();
				else
					teas += "," + text3.getText().toString();
			}
			if (checkBox4.isChecked()) {
				if (TextUtils.isEmpty(teas))
					teas = text4.getText().toString();
				else
					teas += "," + text4.getText().toString();
			}
			if (checkBox5.isChecked()) {
				if (TextUtils.isEmpty(teas))
					teas = text5.getText().toString();
				else
					teas += "," + text5.getText().toString();
			}
			if (checkBox6.isChecked()) {
				if (TextUtils.isEmpty(teas))
					teas = text6.getText().toString();
				else
					teas += "," + text6.getText().toString();
			}
            note.setHas_tea("true");
            note.setRecommended_teas(teas);
		} else {
            note.setHas_tea("false");
            note.setRecommended_teas(null);
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
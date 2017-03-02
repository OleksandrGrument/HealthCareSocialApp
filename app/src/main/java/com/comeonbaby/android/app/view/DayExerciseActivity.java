package com.comeonbaby.android.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class DayExerciseActivity extends BaseActivity implements OnClickListener {

    private String EX_RUNNING;
    private String EX_SWIMMING;
    private String EX_CLIMB;
    private String EX_YOGA;

	RadioGroup radioGroup;
	RadioButton radioDung, radioNoDung;
	CheckBoxCustom checkBox1, checkBox2, checkBox3, checkBox4;

	NoteDTO note;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_day_exercise);
		note = (NoteDTO) getIntent().getSerializableExtra("VALUE1");
		initObjectUI();
		setupHideKeyboard(findViewById(R.id.layoutRootExercise));
	}

	private void initObjectUI() {
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_vandong);
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.btnButtonFinishVandong)).setOnClickListener(this);
		checkBox1 = (CheckBoxCustom) findViewById(R.id.checkbox_Vandong_Goiy_1);
		checkBox2 = (CheckBoxCustom) findViewById(R.id.checkbox_Vandong_Goiy_2);
		checkBox3 = (CheckBoxCustom) findViewById(R.id.checkbox_Vandong_Goiy_3);
		checkBox4 = (CheckBoxCustom) findViewById(R.id.checkbox_Vandong_Goiy_4);

		EX_RUNNING = ((TextViewCustom) findViewById(R.id.txtExercise1)).getText().toString();
		EX_SWIMMING = ((TextViewCustom) findViewById(R.id.txtExercise2)).getText().toString();
		EX_CLIMB = ((TextViewCustom) findViewById(R.id.txtExercise3)).getText().toString();
		EX_YOGA = ((TextViewCustom) findViewById(R.id.txtExercise4)).getText().toString();

		radioDung = (RadioButton) findViewById(R.id.checboxDung);
		radioNoDung = (RadioButton) findViewById(R.id.checboxNoDung);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroupExercise);
		/* Attach CheckedChangeListener to radio group */
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton rb = (RadioButton) group.findViewById(checkedId);
				if (null != rb && checkedId > -1) {
					if (checkedId == radioNoDung.getId()) {
                        stateAllButtons(false);
                        checkBox1.setChecked(false); checkBox2.setChecked(false);
                        checkBox3.setChecked(false); checkBox4.setChecked(false);
                    }
					else
						stateAllButtons(true);
				}
			}
		});
		if (note != null) {
			if (!TextUtils.isEmpty(note.isHas_exercise()) && !note.isHas_exercise().equals("null") && note.isHas_exercise().equals("true")) {
				radioDung.setChecked(true);
				radioNoDung.setChecked(false);
				stateAllButtons(true);
				if (note.getRecommended_exercise() != null) {
					if (note.getRecommended_exercise().contains(",")) {
						String[] split = note.getRecommended_exercise().split(",");
						for (String str : split) {
							try {
								if (str.equals(EX_RUNNING)) checkBox1.setChecked(true);
								else if (str.equals(EX_SWIMMING)) checkBox2.setChecked(true);
								else if (str.equals(EX_CLIMB)) checkBox3.setChecked(true);
								else if (str.equals(EX_YOGA)) checkBox4.setChecked(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						try {
							String str = note.getRecommended_exercise();
							if (str.equals(EX_RUNNING)) checkBox1.setChecked(true);
							else if (str.equals(EX_SWIMMING)) checkBox2.setChecked(true);
							else if (str.equals(EX_CLIMB)) checkBox3.setChecked(true);
							else if (str.equals(EX_YOGA)) checkBox4.setChecked(true);
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
		case R.id.btnButtonFinishVandong:
			actionFinishButton();
			break;
		default:
			break;
		}
	}

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

	private void actionFinishButton() {
		if (radioDung.isChecked()) {
			if (isCheckedOption() == false) {
				DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.error_enter_vandong, null);
				return;
			} else {
                saveDataAndFinish();
			}
		} else if (radioNoDung.isChecked()) {
			saveDataAndFinish();
		} else {
			DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.error_enter_vandong, null);
			return;
		}
	}

	private void saveDataAndFinish() {
        String exercises = "";
		if (radioDung.isChecked()) {
			if (checkBox1.isChecked()) exercises = EX_RUNNING;
			if (checkBox2.isChecked()) {
				if (TextUtils.isEmpty(exercises)) exercises = EX_SWIMMING;
				else exercises += "," + EX_SWIMMING;
			}
			if (checkBox3.isChecked()) {
				if (TextUtils.isEmpty(exercises)) exercises = EX_CLIMB;
				else exercises += "," + EX_CLIMB;
			}
			if (checkBox4.isChecked()) {
				if (TextUtils.isEmpty(exercises)) exercises = EX_YOGA;
				else exercises += "," + EX_YOGA;
			}
            note.setHas_exercise("true");
            note.setRecommended_exercise(exercises);
		} else {
            note.setHas_exercise("false");
            note.setRecommended_exercise(null);
		}
        Log.d("EXERCISES", note.isHas_exercise() + note.getRecommended_exercise());
        Intent intent = new Intent();
        intent.putExtra("NOTE", note);
        setResult(RESULT_OK, intent);
        finish();
	}

    @Override
    public void onEvent(int eventType, View control, Object data) {
    }
}
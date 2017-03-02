package com.comeonbaby.android.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.db.dto.NoteDTO;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.RadioButtonCustom;
import com.comeonbaby.android.app.view.customview.TextViewCustom;
import com.comeonbaby.android.app.view.customview.ToggleButtonGroupTableLayout;

public class DayEmotionActivity extends BaseActivity implements OnClickListener {

	RadioButtonCustom checkbox1, checkbox2, checkbox3, checkbox4, checkbox5;
	NoteDTO note;
	ToggleButtonGroupTableLayout radGroup1;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_day_emotion);
		note = (NoteDTO) getIntent().getSerializableExtra("VALUE1");
		initObjectUI();
		setupHideKeyboard(findViewById(R.id.layoutRootCamxuc));
	}

	private void initObjectUI() {
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_camxuc);
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		radGroup1 = (ToggleButtonGroupTableLayout) findViewById(R.id.radGroup1);
		checkbox1 = (RadioButtonCustom) findViewById(R.id.checboxCamxuc1);
		checkbox2 = (RadioButtonCustom) findViewById(R.id.checboxCamxuc2);
		checkbox3 = (RadioButtonCustom) findViewById(R.id.checboxCamxuc3);
		checkbox4 = (RadioButtonCustom) findViewById(R.id.checboxCamxuc4);
		checkbox5 = (RadioButtonCustom) findViewById(R.id.checboxCamxuc5);

		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.btnButtonFinishCamxuc)).setOnClickListener(this);

		if (note.getEmotional_state() != null && !TextUtils.isEmpty(note.getEmotional_state())) {
			if (note.getEmotional_state().equals("0")) {
				checkbox1.setChecked(true);
				checkbox2.setChecked(false);
				checkbox3.setChecked(false);
				checkbox4.setChecked(false);
				checkbox5.setChecked(false);
				radGroup1.setCheckedRadioButton(checkbox1);
			} else if (note.getEmotional_state().equals("1")) {
				checkbox2.setChecked(true);
				checkbox1.setChecked(false);
				checkbox3.setChecked(false);
				checkbox4.setChecked(false);
				checkbox5.setChecked(false);
				radGroup1.setCheckedRadioButton(checkbox2);
			} else if (note.getEmotional_state().equals("2")) {
				checkbox3.setChecked(true);
				checkbox2.setChecked(false);
				checkbox1.setChecked(false);
				checkbox4.setChecked(false);
				checkbox5.setChecked(false);
				radGroup1.setCheckedRadioButton(checkbox3);
			} else if (note.getEmotional_state().equals("3")) {
				checkbox3.setChecked(true);
				checkbox2.setChecked(false);
				checkbox1.setChecked(false);
				checkbox4.setChecked(false);
				checkbox5.setChecked(false);
				radGroup1.setCheckedRadioButton(checkbox4);
			} else {
				checkbox5.setChecked(true);
				checkbox4.setChecked(false);
				checkbox2.setChecked(false);
				checkbox3.setChecked(false);
				checkbox1.setChecked(false);
				radGroup1.setCheckedRadioButton(checkbox5);
			}
		} else {
			checkbox3.setChecked(true);
			checkbox2.setChecked(false);
			checkbox1.setChecked(false);
			checkbox4.setChecked(false);
			checkbox5.setChecked(false);
			radGroup1.setCheckedRadioButton(checkbox3);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBack:
			finish();
			break;
		case R.id.btnButtonFinishCamxuc:
			saveDataAndFinish();
			break;
		default:
			break;
		}
	}

	private void saveDataAndFinish() {
		if (checkbox1.isChecked()) {
			note.setEmotional_state("0");
		} else if (checkbox2.isChecked()) {
			note.setEmotional_state("1");
		} else if (checkbox3.isChecked()) {
			note.setEmotional_state("2");
		} else if (checkbox4.isChecked()) {
			note.setEmotional_state("3");
		} else if (checkbox5.isChecked()) {
			note.setEmotional_state("4");
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
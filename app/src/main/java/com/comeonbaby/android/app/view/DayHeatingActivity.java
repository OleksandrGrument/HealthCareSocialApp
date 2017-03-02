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

public class DayHeatingActivity extends BaseActivity implements OnClickListener {

	ToggleButtonGroupTableLayout radGroup1;
	RadioButtonCustom checkbox1, checkbox2, checkbox3, checkbox4;
	NoteDTO note;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_day_heating);
		note = (NoteDTO) getIntent().getSerializableExtra("VALUE1");
		initObjectUI();
		setupHideKeyboard(findViewById(R.id.layoutRootTam));
	}

	private void initObjectUI() {
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_tam);
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);

		radGroup1 = (ToggleButtonGroupTableLayout) findViewById(R.id.radGroup1);
		checkbox1 = (RadioButtonCustom) findViewById(R.id.checboxTam1);
		checkbox2 = (RadioButtonCustom) findViewById(R.id.checboxTam2);
		checkbox3 = (RadioButtonCustom) findViewById(R.id.checboxTam3);
		checkbox4 = (RadioButtonCustom) findViewById(R.id.checboxTam4);

		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.btnButtonFinishTam)).setOnClickListener(this);

        //Get info from NoteDTO
		if (note.getHip_bath() != null && !TextUtils.isEmpty(note.getHip_bath())) {
			if (note.getHip_bath().equals("10")) {
				checkbox1.setChecked(true);
				checkbox2.setChecked(false);
				checkbox3.setChecked(false);
				checkbox4.setChecked(false);
				radGroup1.setCheckedRadioButton(checkbox1);
			} else if (note.getHip_bath().equals("20")) {
				checkbox2.setChecked(true);
				checkbox1.setChecked(false);
				checkbox3.setChecked(false);
				checkbox4.setChecked(false);
				radGroup1.setCheckedRadioButton(checkbox2);
			} else if (note.getHip_bath().equals("30")) {
				checkbox3.setChecked(true);
				checkbox2.setChecked(false);
				checkbox1.setChecked(false);
				checkbox4.setChecked(false);
				radGroup1.setCheckedRadioButton(checkbox3);
			} else {
				checkbox4.setChecked(true);
				checkbox2.setChecked(false);
				checkbox3.setChecked(false);
				checkbox1.setChecked(false);
				radGroup1.setCheckedRadioButton(checkbox4);
			}
		} else
			radGroup1.setCheckedRadioButton(checkbox2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBack:
			finish();
			break;
		case R.id.btnButtonFinishTam:
			saveDataAndFinish();
			break;
		default:
			break;
		}
	}

	private void saveDataAndFinish() {
		if (checkbox1.isChecked()) {
            note.setHip_bath("10");
		} else if (checkbox2.isChecked()) {
            note.setHip_bath("20");
		} else if (checkbox3.isChecked()) {
            note.setHip_bath("30");
		} else if (checkbox4.isChecked()) {
            note.setHip_bath("40");
		}
		Log.d("Heating", note.getHip_bath());
		Intent intent = new Intent();
		intent.putExtra("NOTE", note);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}
}
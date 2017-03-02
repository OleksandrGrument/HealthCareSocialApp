package com.comeonbaby.android.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.db.dto.NoteDTO;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

public class DayTemperatureActivity extends BaseActivity implements OnClickListener {
	NumberPicker numberPicker1;
	NumberPicker numberPicker2;
	NumberPicker numberPicker3;
	NoteDTO note;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_day_temperature);
		note = (NoteDTO) getIntent().getSerializableExtra("VALUE1");
		initObjectUI();
		setupHideKeyboard(findViewById(R.id.layoutRootTemperature));
	}

	private void initObjectUI() {
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_temperature);
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((TextViewCustom) findViewById(R.id.textTemperature)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);

		numberPicker1 = (NumberPicker) findViewById(R.id.picker_number1);
		numberPicker2 = (NumberPicker) findViewById(R.id.picker_number2);
		numberPicker3 = (NumberPicker) findViewById(R.id.picker_number3);

		numberPicker1.setMinValue(1);
		numberPicker1.setMaxValue(4);
		numberPicker1.setValue(3);
		numberPicker2.setMinValue(0);
		numberPicker2.setMaxValue(9);
		numberPicker2.setValue(6);
		numberPicker3.setMinValue(0);
		numberPicker3.setMaxValue(9);
		numberPicker3.setValue(5);
		if (!TextUtils.isEmpty(note.getBbt())) {
			try {
				if (note.getBbt().contains(".")) {
					String[] split = note.getBbt().split("\\.");
					int num = Integer.parseInt(split[0]);
					int value1 = num / 10;
					int value2 = num % 10;
					int value3 = Integer.parseInt(split[1]);
					numberPicker1.setValue(value1);
					numberPicker2.setValue(value2);
					numberPicker3.setValue(value3);
				} else {
					int num = Integer.parseInt(note.getBbt());
					int value1 = num / 10;
					int value2 = num % 10;
					numberPicker1.setValue(value1);
					numberPicker2.setValue(value2);
					numberPicker3.setValue(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.btnButtonFinishTemperature)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBack:
			finish();
		case R.id.btnButtonFinishTemperature:
			saveDataAndFinish();
			break;
		default:
			break;
		}
	}

	private void saveDataAndFinish() {
		String temperatue = numberPicker1.getValue() + "" + numberPicker2.getValue() + "." + numberPicker3.getValue();
		note.setBbt(temperatue);
		Intent intent = new Intent();
		intent.putExtra("NOTE", note);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}
}
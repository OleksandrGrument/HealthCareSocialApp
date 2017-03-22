package com.comeonbabys.android.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Globals;
import com.comeonbabys.android.app.db.dto.NoteDTO;
import com.comeonbabys.android.app.view.customview.ButtonCustom;
import com.comeonbabys.android.app.view.customview.TextViewCustom;

import java.util.Locale;

public class DaySleepActivity extends BaseActivity implements OnClickListener {

	NumberPicker numberPicker1;
	NumberPicker numberPicker2;
	NumberPicker numberPicker3;
	NumberPicker numberPicker4;
	NoteDTO note;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_day_sleep);
		note = (NoteDTO) getIntent().getSerializableExtra("VALUE1");
		initObjectUI();
		setupHideKeyboard(findViewById(R.id.layoutRootNgu));
	}

	private void initObjectUI() {
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_ngu);
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);

		numberPicker1 = (NumberPicker) findViewById(R.id.picker_number1);
		numberPicker2 = (NumberPicker) findViewById(R.id.picker_number2);
		numberPicker3 = (NumberPicker) findViewById(R.id.picker_number3);
		numberPicker4 = (NumberPicker) findViewById(R.id.picker_number4);
		numberPicker1.setMinValue(0);
		numberPicker1.setMaxValue(23);
		numberPicker1.setValue(7);

		numberPicker2.setMinValue(0);
		numberPicker2.setMaxValue(59);
		numberPicker2.setValue(0);
		numberPicker2.setFormatter(new NumberPicker.Formatter() {
			@Override
			public String format(int i) {
				return String.format(Locale.US, "%02d", i);
			}
		});

		numberPicker3.setMinValue(0);
		numberPicker3.setMaxValue(23);
		numberPicker3.setValue(23);
		numberPicker4.setMinValue(0);
		numberPicker4.setMaxValue(59);
		numberPicker4.setValue(0);
		numberPicker4.setFormatter(new NumberPicker.Formatter() {
			@Override
			public String format(int i) {
				return String.format(Locale.US, "%02d", i);
			}
		});

		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.btnButtonFinishNgu)).setOnClickListener(this);

		//Get values from NoteDTO if exists
		if (note.getGoing_to_bed_from() != null && !TextUtils.isEmpty(note.getGoing_to_bed_from())) {
			try {
				// from
				String[] sp1 = note.getGoing_to_bed_from().split(":");
				int h = Integer.parseInt(sp1[0]);
				int m = Integer.parseInt(sp1[1]);
				numberPicker1.setValue(h);
				numberPicker2.setValue(m);
				// to
				String[] sp2 = note.getGoing_to_bed_to().split(":");
				int h1 = Integer.parseInt(sp2[0]);
				int m1 = Integer.parseInt(sp2[1]);
				numberPicker3.setValue(h1);
				numberPicker4.setValue(m1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBack:
			finish();
			break;
		case R.id.btnButtonFinishNgu:
			saveDataAndFinish();
			break;
		default:
			break;
		}
	}

	private void saveDataAndFinish() {
		String from = numberPicker1.getValue() + ":" + numberPicker2.getValue();
		String to = numberPicker3.getValue() + ":" + numberPicker4.getValue();
		note.setGoing_to_bed_from(from);
		note.setGoing_to_bed_to(to);
		Log.d("SLEEP", note.getGoing_to_bed_from() + " " + note.getGoing_to_bed_to());
		Intent intent = new Intent();
		intent.putExtra("NOTE", note);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}
}
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

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.db.dto.NoteDTO;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

public class DayFolicActivity extends BaseActivity implements OnClickListener {

	RadioButton radioDung, radioNoDung;
	NoteDTO note;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_day_folic);
		note = (NoteDTO) getIntent().getSerializableExtra("VALUE1");
		initObjectUI();
		setupHideKeyboard(findViewById(R.id.layoutRootAxic));
	}

	private void initObjectUI() {
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_axic);
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.btnButtonFinishAxic)).setOnClickListener(this);
		radioDung = (RadioButton) findViewById(R.id.checboxDung);
		radioNoDung = (RadioButton) findViewById(R.id.checboxNoDung);
		if (note.getFolate() != null && !TextUtils.isEmpty(note.getFolate()) && note.getFolate().equals("true")) {
			radioDung.setChecked(true);
			radioNoDung.setChecked(false);
		} else {
			radioDung.setChecked(false);
			radioNoDung.setChecked(true);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBack:
			finish();
			break;
		case R.id.btnButtonFinishAxic:
			saveDataAndFinish();
			break;
		default:
			break;
		}
	}

	private void saveDataAndFinish() {
		if (radioDung.isChecked()) {
            note.setFolate("true");
		} else {
            note.setFolate("false");
		}
        Log.d("Folic acid", note.getFolate());
        Intent intent = new Intent();
        intent.putExtra("NOTE", note);
        setResult(RESULT_OK, intent);
        finish();
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}
}
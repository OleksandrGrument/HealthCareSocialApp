package com.comeonbabys.android.app.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Globals;
import com.comeonbabys.android.app.db.dto.NoteDTO;
import com.comeonbabys.android.app.view.customview.ButtonCustom;
import com.comeonbabys.android.app.view.customview.TextViewCustom;

public class DayAlcoholActivity extends BaseActivity implements OnClickListener {
	SeekBar seekbarRuou;
	TextViewCustom img1, img2, img3, img4;
	TextViewCustom Tip2, Tip3, Tip4;
	NoteDTO note;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_day_alcohol);
		note = (NoteDTO) getIntent().getSerializableExtra("VALUE1");
		initObjectUI();
		setupHideKeyboard(findViewById(R.id.layoutRootRuou));
	}

	@SuppressLint("NewApi")
	private void initObjectUI() {
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_ruou);
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		seekbarRuou = (SeekBar) findViewById(R.id.seekbarRuou);
		img1 = (TextViewCustom) findViewById(R.id.img1);
		img2 = (TextViewCustom) findViewById(R.id.img2);
		img3 = (TextViewCustom) findViewById(R.id.img3);
		img4 = (TextViewCustom) findViewById(R.id.img4);
		img1.setSelected(true);
		img2.setSelected(true);
		Tip2 = (TextViewCustom) findViewById(R.id.textTip2);
		Tip3 = (TextViewCustom) findViewById(R.id.textTip3);
		Tip4 = (TextViewCustom) findViewById(R.id.textTip4);
		Tip2.setSelected(true);

		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.btnButtonFinishRuou)).setOnClickListener(this);

		if (note.getAlcohol_consumption() != null && !TextUtils.isEmpty(note.getAlcohol_consumption())) {
			if (note.getAlcohol_consumption().equals("0")) {
				seekbarRuou.setProgress(0);
				img1.setSelected(true);
			} else if (note.getAlcohol_consumption().equals("1")) {
				seekbarRuou.setProgress(10);
				img1.setSelected(true);
				img2.setSelected(true);
				Tip2.setSelected(true);
			} else if (note.getAlcohol_consumption().equals("2")) {
				seekbarRuou.setProgress(20);
				img1.setSelected(true);
				img2.setSelected(true);
				img3.setSelected(true);
				Tip2.setSelected(true);
				Tip3.setSelected(true);
			} else if (note.getAlcohol_consumption().equals("3")) {
				seekbarRuou.setProgress(30);
				img1.setSelected(true);
				img2.setSelected(true);
				img3.setSelected(true);
				img4.setSelected(true);
				Tip2.setSelected(true);
				Tip3.setSelected(true);
				Tip4.setSelected(true);
			}
		} else {
			seekbarRuou.setProgress(10);
			img1.setSelected(true);
			img2.setSelected(true);
			Tip2.setSelected(true);
		}
		seekbarRuou.setMax(32);
		seekbarRuou.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (progress < 10)
					progress = 0;
				else if (progress < 20)
					progress = 10;
				else if (progress < 30)
					progress = 20;
				else
					progress = 30;
				seekBar.setProgress(progress);
				changeStateSeekbar(progress);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBack:
			finish();
			break;
		case R.id.btnButtonFinishRuou:
			saveDataAndFinish();
			break;
		default:
			break;
		}
	}

	private void changeStateSeekbar(int progess) {
		if (progess <= 0) {
			img1.setSelected(true);
			img2.setSelected(false);
			img3.setSelected(false);
			img4.setSelected(false);
			Tip2.setSelected(false);
			Tip3.setSelected(false);
			Tip4.setSelected(false);
		} else if (progess <= 10) {
			img1.setSelected(true);
			img2.setSelected(true);
			img3.setSelected(false);
			img4.setSelected(false);
			Tip2.setSelected(true);
			Tip3.setSelected(false);
			Tip4.setSelected(false);
		} else if (progess <= 20) {
			img1.setSelected(true);
			img2.setSelected(true);
			img3.setSelected(true);
			img4.setSelected(false);
			Tip2.setSelected(true);
			Tip3.setSelected(true);
			Tip4.setSelected(false);
		} else {
			img1.setSelected(true);
			img2.setSelected(true);
			img3.setSelected(true);
			img4.setSelected(true);
			Tip2.setSelected(true);
			Tip3.setSelected(true);
			Tip4.setSelected(true);
		}
	}

	private void saveDataAndFinish() {
		if (seekbarRuou.getProgress() == 0) {
			note.setAlcohol_consumption("0");
		} else if (seekbarRuou.getProgress() == 10) {
			note.setAlcohol_consumption("1");
		} else if (seekbarRuou.getProgress() == 20) {
			note.setAlcohol_consumption("2");
		} else if (seekbarRuou.getProgress() == 30) {
			note.setAlcohol_consumption("3");
		}
		Log.d("Alcohol", note.getAlcohol_consumption());
		Intent intent = new Intent();
		intent.putExtra("NOTE", note);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}
}
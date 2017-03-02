package com.comeonbaby.android.app.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.db.dto.CityDTO;
import com.comeonbaby.android.app.db.dto.NoteDTO;
import com.comeonbaby.android.app.db.dto.NotesHolder;
import com.comeonbaby.android.app.db.dto.UserDTO;
import com.comeonbaby.android.app.requests.Constants;
import com.comeonbaby.android.app.requests.ExtraConstants;
import com.comeonbaby.android.app.requests.commands.Commands;
import com.comeonbaby.android.app.utils.AppSession;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.Day;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DayHomeActivity extends BaseActivity implements OnClickListener {

	private Calendar cal;
	private Day day;
	private NoteDTO note;
	private NotesHolder notesHolder;

	private final int REQUEST_CODE = 10;

	private TextView textViewDate;
	private RelativeLayout layoutTopDate;
	private ImageView next, prev;

    private Button btnSaveNote;

    //Переменная отвечает за то, заходил ли пользователь в настройки для изменения BMI, если да,
    //то при следующей перерисовке берем BMI не из NoteDTO, а из ProfileDTO
    boolean changeBMI = false;

    private static final String TAG = "DayHomeActivity";

    Handler handler;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_day_home);
		initTitleDate();
		setListeners();
        notesHolder = NotesHolder.getInstanse();
        note = getNoteForThisDay();
        initHandler();
	}

	@Override
	public void onResume() {
		super.onResume();
        try {
            rebuildUI();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
	}

	//Initialize top date view
	private void initTitleDate() {
		day = (Day) getIntent().getParcelableExtra("DAY");
		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, day.getYear());
		cal.set(Calendar.MONTH, day.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, day.getDay());

		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_list_note);
		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		layoutTopDate = (RelativeLayout) findViewById(R.id.layoutDate);
		LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		param.addRule(RelativeLayout.BELOW, R.id.layoutTitle);
		layoutTopDate.setLayoutParams(param);
		layoutTopDate.setMinimumHeight(40);

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = 20;
		params.topMargin = 20;
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		prev = new ImageView(DayHomeActivity.this);
		prev.setId(R.id.image_prev_date);
		prev.setPadding((int) getResources().getDimension(
						R.dimen.padding_left_right_next_previous), (int) getResources().getDimension(
						R.dimen.padding_top_bottom_next_previous), (int) getResources().getDimension(
						R.dimen.padding_left_right_next_previous), (int) getResources().getDimension(
						R.dimen.padding_top_bottom_next_previous));
		prev.setLayoutParams(params);
		prev.setImageResource(R.drawable.navigation_previous_item);
		prev.setOnClickListener(this);
		layoutTopDate.addView(prev);

		params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		textViewDate = new TextView(DayHomeActivity.this);
		textViewDate.setId(R.id.text_top_date);
		textViewDate.setLayoutParams(params);
		textViewDate.setTextAppearance(DayHomeActivity.this, android.R.style.TextAppearance_Large);
		textViewDate.setTextSize(25);
		textViewDate.setTypeface(null, Typeface.BOLD);
		layoutTopDate.addView(textViewDate);

		params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.rightMargin = 20;
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		next = new ImageView(DayHomeActivity.this);
		next.setImageResource(R.drawable.navigation_next_item);
		next.setLayoutParams(params);
		next.setId(R.id.image_next_date);
		next.setPadding((int) getResources().getDimension(
				R.dimen.padding_left_right_next_previous), (int) getResources().getDimension(
				R.dimen.padding_top_bottom_next_previous), (int) getResources().getDimension(
				R.dimen.padding_left_right_next_previous), (int) getResources().getDimension(
				R.dimen.padding_top_bottom_next_previous));
		next.setOnClickListener(this);
		layoutTopDate.addView(next);
        btnSaveNote = (ButtonCustom) findViewById(R.id.btnSaveDayNote);
	}

	private void setListeners() {
		((LinearLayout) findViewById(R.id.layoutTemperature)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutFood)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutNuts)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutTea)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutExercise)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutSleep)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutWater)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutHeating)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutVitamin)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutFolicAcid)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutCoffee)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutAlcohol)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutSmoking)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutEmotion)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layoutDayHomeBmi)).setOnClickListener(this);
        ((ButtonCustom) findViewById(R.id.btnSaveDayNote)).setOnClickListener(this);
	}

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Bundle data = msg.getData();
                String message = "";
                if (data.containsKey(ExtraConstants.MESSAGE)) message = data.getString(ExtraConstants.MESSAGE);
                if (msg.what != Constants.MSG_ERROR) showSnackMessage(message);
                switch (msg.what) {
                    //Success registration
                    case Constants.MSG_SAVE_NOTE_SUCCESS: {
                        //notesHolder.saveNote(note, true);
                        setNoteID(note);
                        notesHolder.saveNote(note, false);
                        finish();
                        break;
                    }
                    //Unsuccessful login
                    case Constants.MSG_SAVE_NOTE_FAIL: {
                        break;
                    }
                    //No connection
                    case Constants.MSG_ERROR: {
                        showSnackMessage(Constants.ERROR_MESSAGE_UNKNOWN);
                        break;
                    }
                }
                hideProgress();
                stateAllButtons(true);
            }
        };
    }

    private void stateAllButtons(boolean value) {
        btnSaveNote.setEnabled(value);
    }

    //Всплывающее сообщение
    private void showSnackMessage(String msg) {
        Snackbar.make(findViewById(R.id.layoutRootQuestion), msg, Snackbar.LENGTH_LONG).show();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_prev_date:				//Click previous date image
			cal.add(Calendar.DAY_OF_MONTH, -1);
            note = getNoteForThisDay();
			rebuildUI();
			break;
		case R.id.image_next_date:				//Click next date image
			cal.add(Calendar.DAY_OF_MONTH, 1);
            note = getNoteForThisDay();
			rebuildUI();
			break;
		case R.id.imgBack:						//Click back image
			finish();
			break;
		case R.id.layoutTemperature:			//Click temperature layout
			leaveActivityForResult(this, DayTemperatureActivity.class.toString(), false, false, null, note, REQUEST_CODE);
			break;
		case R.id.layoutFood:
			leaveActivityForResult(this, DayFoodActivity.class.toString(), false, false, null, note, REQUEST_CODE);
			break;
		case R.id.layoutNuts:
			leaveActivityForResult(this, DayNutsActivity.class.toString(), false, false, null, note, REQUEST_CODE);
			break;
		case R.id.layoutTea:
			leaveActivityForResult(this, DayTeaActivity.class.toString(), false, false, null, note, REQUEST_CODE);
			break;
		case R.id.layoutExercise:
			leaveActivityForResult(this, DayExerciseActivity.class.toString(), false, false, null, note, REQUEST_CODE);
			break;
        case R.id.layoutSleep:
			leaveActivityForResult(this, DaySleepActivity.class.toString(), false, false, null, note, REQUEST_CODE);
			break;
		case R.id.layoutWater:
			leaveActivityForResult(this, DayWaterActivity.class.toString(), false, false, null, note, REQUEST_CODE);
			break;
        case R.id.layoutHeating:
			leaveActivityForResult(this, DayHeatingActivity.class.toString(), false, false, null, note, REQUEST_CODE);
			break;
		case R.id.layoutVitamin:
			leaveActivityForResult(this, DayVitaminActivity.class.toString(), false, false, null, note, REQUEST_CODE);
			break;
		case R.id.layoutFolicAcid:
			leaveActivityForResult(this, DayFolicActivity.class.toString(), false, false, null, note, REQUEST_CODE);
			break;
		case R.id.layoutCoffee:
			leaveActivityForResult(this, DayCoffeeActivity.class.toString(), false, false, null, note, REQUEST_CODE);
			break;
        case R.id.layoutAlcohol:
            leaveActivityForResult(this, DayAlcoholActivity.class.toString(), false, false, null, note, REQUEST_CODE);
            break;
		case R.id.layoutSmoking:
			leaveActivityForResult(this, DaySmokingActivity.class.toString(), false, false, null, note, REQUEST_CODE);
			break;
		case R.id.layoutEmotion:
			leaveActivityForResult(this, DayEmotionActivity.class.toString(), false, false, null, note, REQUEST_CODE);
			break;
		case R.id.layoutDayHomeBmi:
			leaveActivityForResult(this, SettingActivity.class.toString(), false, false, null, note, REQUEST_CODE);
            changeBMI = true;
			break;
		case R.id.btnSaveDayNote:
			//TODO save data
            showProgress();
            stateAllButtons(false);
			Commands.saveNoteOperation(handler, AppSession.getSession().getSystemUser(), note);
			break;
		default:
			break;
		}
	}

	private void setNoteID(NoteDTO note) {
		if(note != null) {
			note.setId(Long.parseLong(("" + note.getYear() +
					(note.getMonth() < 10 ? "0" : "") + note.getMonth() +
					(note.getDay() < 10 ? "0" : "") + note.getDay())));
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

    //Rebuild top date text initialize text fields using info from NoteDTO if exists
	private void rebuildUI() {
		showProgress();
		//Set current calendar top date text
		if (textViewDate != null) {
			textViewDate.setText(cal.get(Calendar.YEAR) + ". " + (cal.get(Calendar.MONTH) > 8 ? (cal.get(Calendar.MONTH) + 1)
						: "0" + (cal.get(Calendar.MONTH) + 1)) + ". " + (cal.get(Calendar.DAY_OF_MONTH) > 9 ? cal.get(Calendar.DAY_OF_MONTH)
						: "0" + cal.get(Calendar.DAY_OF_MONTH)));
		}
		setNextDateBtnVisibility();			//Check if it is current date
		try {
			updateUI();   				//Update all fields using NoteDTO
		} catch (Exception e) {
			e.printStackTrace();
		}
		hideProgress();
	}

	//Set next date button disabled if it is current date
	private void setNextDateBtnVisibility() {
		Calendar currentDate = Calendar.getInstance();
		if(cal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
				(cal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
						(cal.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)))) {
			next.setVisibility(View.INVISIBLE);
		} else {
			next.setVisibility(View.VISIBLE);
		}
	}

    //Метод получает NoteDTO для текущего дня или создает новый
    private NoteDTO getNoteForThisDay() {
        NoteDTO curnote = notesHolder.getNote(cal);
        if (curnote == null) {
			curnote = new NoteDTO();
            curnote.setYear(cal.get(Calendar.YEAR));
            curnote.setMonth(cal.get(Calendar.MONTH) + 1);
            curnote.setDay(cal.get(Calendar.DAY_OF_MONTH));
        }
        return curnote;
    }

	//Initializes text fields, using info from NoteDTO if exists
	@SuppressLint("SimpleDateFormat")
	private void updateUI() throws Exception {
//		if (note != null && note.getIdString() != null && !TextUtils.isEmpty(note.getIdString()) && !note.getIdString().equals("0")) {
		if (note != null) {
			//Basic body temperature
			if (note.getBbt() != null) {
				if (Double.parseDouble(note.getBbt()) > 0) {
					((LinearLayout) findViewById(R.id.layoutTemperature)).setSelected(true);
					((TextViewCustom) findViewById(R.id.textNhietke)).setText(String.format(getString(R.string.text_unit_nhietke), note.getBbt()));
				} else {
					((LinearLayout) findViewById(R.id.layoutTemperature)).setSelected(false);
					((TextViewCustom) findViewById(R.id.textNhietke)).setText(String.format(getString(R.string.text_unit_nhietke), "0"));
				}
			} else {
				((LinearLayout) findViewById(R.id.layoutTemperature)).setSelected(false);
				((TextViewCustom) findViewById(R.id.textNhietke)).setText(String.format(getString(R.string.text_unit_nhietke), "0"));
			}
			//Recommended foods
			if (note.getRecommended_foods() != null) {			//if was previous selection
				((LinearLayout) findViewById(R.id.layoutFood)).setSelected(true);
				String foodText = TextUtils.isEmpty(note.getRecommended_foods())
						? getResources().getString(R.string.text_monan_no_dung)		//if empty string (no intake selected)
						: getResources().getString(R.string.text_monan_dung);		//if not empty string
				((TextViewCustom) findViewById(R.id.textMonan)).setText(foodText);
			} else {											//if no previous selection
				((LinearLayout) findViewById(R.id.layoutFood)).setSelected(false);
				((TextViewCustom) findViewById(R.id.textMonan)).setText(R.string.text_monan_no_dung);
			}
			//Recommended nuts
			if (note.isHas_nut() != null) {
                ((LinearLayout) findViewById(R.id.layoutNuts)).setSelected(true);
                if(note.isHas_nut().equals("true")) {
                    ((TextViewCustom) findViewById(R.id.textHat)).setText(R.string.text_checkbox_hat_dung);
                } else {
                    ((TextViewCustom) findViewById(R.id.textHat)).setText(R.string.text_checkbox_hat_no_dung);
                }
            } else {
                ((LinearLayout) findViewById(R.id.layoutNuts)).setSelected(false);
                ((TextViewCustom) findViewById(R.id.textHat)).setText(R.string.text_checkbox_hat_no_dung);
            }
			//Recommended teas
            if(note.isHas_tea() != null) {
                ((LinearLayout) findViewById(R.id.layoutTea)).setSelected(true);
                if(note.isHas_tea().equals("true")) {
                    ((TextViewCustom) findViewById(R.id.textTra)).setText(getString(R.string.text_checkbox_tra_dung));
                } else {
                    ((TextViewCustom) findViewById(R.id.textTra)).setText(getString(R.string.text_checkbox_tra_no_dung));
                }
            } else {
                ((LinearLayout) findViewById(R.id.layoutTea)).setSelected(false);
                ((TextViewCustom) findViewById(R.id.textTra)).setText(R.string.text_checkbox_tra_no_dung);
            }
			//Recommended exercises
            if(note.isHas_exercise() != null) {
                ((LinearLayout) findViewById(R.id.layoutExercise)).setSelected(true);
                if(note.isHas_exercise().equals("true")) {
                    ((TextViewCustom) findViewById(R.id.textVandong)).setText(getString(R.string.text_checkbox_vandong_dung));
                } else {
                    ((TextViewCustom) findViewById(R.id.textVandong)).setText(getString(R.string.text_checkbox_vandong_no_dung));
                }
            } else {
                ((LinearLayout) findViewById(R.id.layoutExercise)).setSelected(false);
                ((TextViewCustom) findViewById(R.id.textVandong)).setText(R.string.text_checkbox_vandong_no_dung);
            }
            //Sleep
            if(note.getGoing_to_bed_from() != null && note.getGoing_to_bed_to() != null) {
                ((LinearLayout) findViewById(R.id.layoutSleep)).setSelected(true);
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                Date _24HourDt = _24HourSDF.parse(note.getGoing_to_bed_from()); //wake up
                ((TextViewCustom) findViewById(R.id.textNgu)).setText(_12HourSDF.format(_24HourDt));
                Date _24HourDt1 = _24HourSDF.parse(note.getGoing_to_bed_to());  //go to bed
                ((TextViewCustom) findViewById(R.id.textNgu1)).setText(_12HourSDF.format(_24HourDt1));
            } else {
                ((LinearLayout) findViewById(R.id.layoutSleep)).setSelected(false);
                ((TextViewCustom) findViewById(R.id.textNgu)).setText("");
                ((TextViewCustom) findViewById(R.id.textNgu1)).setText("");

            }
			//Water intake
			if (note.getWater_intake() != null) {
                ((LinearLayout) findViewById(R.id.layoutWater)).setSelected(true);
                ((TextViewCustom) findViewById(R.id.textUongnuoc)).setText(String.format(getString(R.string.text_unit_uongnuoc), note.getWater_intake()));
			} else {
				((LinearLayout) findViewById(R.id.layoutWater)).setSelected(false);
				((TextViewCustom) findViewById(R.id.textUongnuoc)).setText(String.format(getString(R.string.text_unit_uongnuoc), "0"));
			}
            //Water heating
            if (note.getHip_bath() != null) {
                ((LinearLayout) findViewById(R.id.layoutHeating)).setSelected(true);
                ((TextViewCustom) findViewById(R.id.textTam)).setText(String.format(getString(R.string.text_unit_tam), note.getHip_bath()));
            } else {
                ((LinearLayout) findViewById(R.id.layoutHeating)).setSelected(false);
                ((TextViewCustom) findViewById(R.id.textTam)).setText(String.format(getString(R.string.text_unit_tam), "0"));
            }
            //Vitamins
            if (note.getVitamin() != null) {
                ((LinearLayout) findViewById(R.id.layoutVitamin)).setSelected(true);
                if(note.getVitamin().equals("true")) {
                    ((TextViewCustom) findViewById(R.id.textVitamin)).setText(R.string.text_checkbox_vitamin_dung);
                } else {
                    ((TextViewCustom) findViewById(R.id.textVitamin)).setText(R.string.text_checkbox_vitamin_no_dung);
                }
            } else {
                ((LinearLayout) findViewById(R.id.layoutVitamin)).setSelected(false);
                ((TextViewCustom) findViewById(R.id.textVitamin)).setText(R.string.text_checkbox_vitamin_no_dung);
            }
            //Folic acid
            if (note.getFolate() != null) {
                ((LinearLayout) findViewById(R.id.layoutFolicAcid)).setSelected(true);
                if(note.getFolate().equals("true")) {
                    ((TextViewCustom) findViewById(R.id.textAxicfolic)).setText(R.string.text_checkbox_axic_dung);
                } else {
                    ((TextViewCustom) findViewById(R.id.textAxicfolic)).setText(R.string.text_checkbox_axic_no_dung);
                }
            } else {
                ((LinearLayout) findViewById(R.id.layoutFolicAcid)).setSelected(false);
                ((TextViewCustom) findViewById(R.id.textAxicfolic)).setText(R.string.text_checkbox_axic_no_dung);
            }
            //Coffee
            if (note.getCoffee_intake() != null) {
                ((LinearLayout) findViewById(R.id.layoutCoffee)).setSelected(true);
                if (note.getCoffee_intake().equals("1"))
                    ((TextViewCustom) findViewById(R.id.textCafe)).setText(R.string.text_checkbox_cafe_dung);
                else
                    ((TextViewCustom) findViewById(R.id.textCafe)).setText(R.string.text_checkbox_cafe_no_dung);
            } else {
                ((LinearLayout) findViewById(R.id.layoutCoffee)).setSelected(false);
                ((TextViewCustom) findViewById(R.id.textCafe)).setText(String.format(getString(R.string.text_unit_cafe), "0"));
            }
			//Alcohol
			if (note.getAlcohol_consumption() != null) {
				try {
					if (Integer.parseInt(note.getAlcohol_consumption()) > 0) {
						((LinearLayout) findViewById(R.id.layoutAlcohol)).setSelected(true);
						((TextViewCustom) findViewById(R.id.textRuou)).setText(String.format(getString(R.string.text_unit_ruou), note.getAlcohol_consumption()));
					} else {
						((LinearLayout) findViewById(R.id.layoutAlcohol)).setSelected(true);
						((TextViewCustom) findViewById(R.id.textRuou)).setText(String.format(getString(R.string.text_unit_ruou), "0"));
					}
				} catch (NumberFormatException exc) {}
			} else {
				((LinearLayout) findViewById(R.id.layoutAlcohol)).setSelected(false);
				((TextViewCustom) findViewById(R.id.textRuou)).setText(String.format(getString(R.string.text_unit_ruou), "0"));
			}
			//Smoking
			if (note.getSmoking() != null) {
				((LinearLayout) findViewById(R.id.layoutSmoking)).setSelected(true);
				if (note.getSmoking().equals("true"))
					((TextViewCustom) findViewById(R.id.textThuocla)).setText(R.string.text_checkbox_thuocla_dung);
				else
					((TextViewCustom) findViewById(R.id.textThuocla)).setText(R.string.text_checkbox_thuocla_no_dung);
			} else {
				((LinearLayout) findViewById(R.id.layoutSmoking)).setSelected(false);
				((TextViewCustom) findViewById(R.id.textThuocla)).setText(R.string.text_checkbox_thuocla_no_dung);
			}
			//Emotional state
			if (note.getEmotional_state() != null) {
				((LinearLayout) findViewById(R.id.layoutEmotion)).setSelected(true);
				if (note.getEmotional_state().equals("0"))
					((TextViewCustom) findViewById(R.id.textCamxuc)).setText(R.string.text_checkbox_camxuc1);
				if (note.getEmotional_state().equals("1"))
					((TextViewCustom) findViewById(R.id.textCamxuc)).setText(R.string.text_checkbox_camxuc2);
				if (note.getEmotional_state().equals("2"))
					((TextViewCustom) findViewById(R.id.textCamxuc)).setText(R.string.text_checkbox_camxuc3);
				if (note.getEmotional_state().equals("3"))
					((TextViewCustom) findViewById(R.id.textCamxuc)).setText(R.string.text_checkbox_camxuc4);
				if (note.getEmotional_state().equals("4"))
					((TextViewCustom) findViewById(R.id.textCamxuc)).setText(R.string.text_checkbox_camxuc5);
			} else {
				((LinearLayout) findViewById(R.id.layoutEmotion)).setSelected(false);
				((TextViewCustom) findViewById(R.id.textCamxuc)).setText(R.string.text_checkbox_camxuc3);
			}
            //BMI
            if (note.getBmi() != null && !changeBMI) {
                ((TextViewCustom) findViewById(R.id.textThietlap)).setText(String.format(getString(R.string.text_unit_thietlap), note.getBmi()));
                changeBMI = false;
            } else {
                String bmi = String.valueOf(AppSession.getSession().getSystemUser().getProfileDTO().getBmi());
                note.setBmi(bmi);
                ((TextViewCustom) findViewById(R.id.textThietlap)).setText(String.format(getString(R.string.text_unit_thietlap), bmi));
            }

		} else {	//Set default values, if no previous record
			((LinearLayout) findViewById(R.id.layoutTemperature)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textNhietke)).setText(String.format(getString(R.string.text_unit_nhietke), "0"));
			((LinearLayout) findViewById(R.id.layoutFood)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textMonan)).setText(R.string.text_monan_no_dung);
			((LinearLayout) findViewById(R.id.layoutNuts)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textHat)).setText(R.string.text_checkbox_hat_no_dung);
			((LinearLayout) findViewById(R.id.layoutTea)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textTra)).setText(R.string.text_checkbox_tra_no_dung);
			((LinearLayout) findViewById(R.id.layoutExercise)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textVandong)).setText(R.string.text_checkbox_vandong_no_dung);
			((LinearLayout) findViewById(R.id.layoutAlcohol)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textRuou)).setText(String.format(getString(R.string.text_unit_ruou), "0"));
			((LinearLayout) findViewById(R.id.layoutHeating)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textTam)).setText(String.format(getString(R.string.text_unit_tam), "0"));
			((LinearLayout) findViewById(R.id.layoutWater)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textUongnuoc)).setText(String.format(getString(R.string.text_unit_uongnuoc), "0"));
			((LinearLayout) findViewById(R.id.layoutVitamin)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textVitamin)).setText(R.string.text_checkbox_vitamin_no_dung);
			((LinearLayout) findViewById(R.id.layoutCoffee)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textCafe)).setText(String.format(getString(R.string.text_unit_cafe), "0"));
			((LinearLayout) findViewById(R.id.layoutSmoking)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textThuocla)).setText(R.string.text_checkbox_thuocla_no_dung);
			((LinearLayout) findViewById(R.id.layoutEmotion)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textCamxuc)).setText(R.string.text_checkbox_camxuc3);
			((LinearLayout) findViewById(R.id.layoutSleep)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textNgu)).setText("");
			((TextViewCustom) findViewById(R.id.textNgu1)).setText("");
			((LinearLayout) findViewById(R.id.layoutFolicAcid)).setSelected(false);
			((TextViewCustom) findViewById(R.id.textAxicfolic)).setText(R.string.text_checkbox_axic_no_dung);

            String bmi = String.valueOf(AppSession.getSession().getSystemUser().getProfileDTO().getBmi());
            note.setBmi(bmi);
            ((TextViewCustom) findViewById(R.id.textThietlap)).setText(String.format(getString(R.string.text_unit_thietlap), bmi));
		}
		((LinearLayout) findViewById(R.id.layoutDayHomeBmi)).setSelected(true);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}

	//Set results after leaveActivityForResult method
	//arg0 - REQUEST_CODE, arg1 - RESULT
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == REQUEST_CODE && arg1 == RESULT_OK) {
			try {
				note = (NoteDTO) arg2.getSerializableExtra("NOTE");
				day.addNote(note);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

//	@Override
//	public void onPause() {
//		removeActions();
//		super.onPause();
//	}
//
//	private void addActions() {
//		addAction(ServiceConsts.NOTE_BY_DAY_SUCCESS_ACTION,
//				new ListNoteSuccessAction());
//		addAction(ServiceConsts.NOTE_BY_DAY_FAIL_ACTION,
//				new ListNoteFailAction());
//		updateBroadcastActionList();
//	}
//
//	private void removeActions() {
//		removeAction(ServiceConsts.NOTE_BY_DAY_SUCCESS_ACTION);
//		removeAction(ServiceConsts.NOTE_BY_DAY_FAIL_ACTION);
//		updateBroadcastActionList();
//	}
//
//	private class ListNoteSuccessAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			hideProgress();
//			NoteDTO notes = (NoteDTO) bundle
//					.getSerializable(ServiceConsts.EXTRA_NOTE);
//			if (notes == null)
//				notes = new NoteDTO();
//			day = new Day(baseActivity, cal.get(Calendar.DAY_OF_MONTH),
//					cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
//			day.addNote(notes);
//			note = notes;
//			try {
//				updateUI();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	private class ListNoteFailAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			hideProgress();
//		}
//	}
}
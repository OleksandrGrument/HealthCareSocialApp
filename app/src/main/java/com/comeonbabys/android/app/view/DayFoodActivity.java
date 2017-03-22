package com.comeonbabys.android.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.DialogUtilities;
import com.comeonbabys.android.app.common.Globals;
import com.comeonbabys.android.app.db.dto.NoteDTO;
import com.comeonbabys.android.app.view.customview.ButtonCustom;
import com.comeonbabys.android.app.view.customview.CheckBoxCustom;
import com.comeonbabys.android.app.view.customview.TextViewCustom;

import java.util.ArrayList;
import java.util.Random;

public class DayFoodActivity extends BaseActivity implements OnClickListener {

	String[] listFood;
	ArrayList<Integer> randomFoodList;
    ArrayList<Integer> showedFoodList;  //try to not show many same products
	NoteDTO note;

	TextViewCustom text_Goiy_1, text_Goiy_2, text_Goiy_3, text_Khongthich;
	CheckBoxCustom checkBox1, checkBox2, checkBox3, checkBox4;

	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_day_food);
        listFood = getResources().getStringArray(R.array.list_foods);
        showedFoodList = new ArrayList<Integer>();
		note = (NoteDTO) getIntent().getSerializableExtra("VALUE1");
		initObjectUI();
		setupHideKeyboard(findViewById(R.id.layoutRootMonan));
	}

	private void initObjectUI() {
		((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_monan);
		((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		text_Goiy_1 = (TextViewCustom) findViewById(R.id.text_Goiy_1);
		text_Goiy_2 = (TextViewCustom) findViewById(R.id.text_Goiy_2);
		text_Goiy_3 = (TextViewCustom) findViewById(R.id.text_Goiy_3);
		text_Khongthich = (TextViewCustom) findViewById(R.id.text_Khongthich);

		checkBox1 = (CheckBoxCustom) findViewById(R.id.checkbox_Goiy_1);
		checkBox2 = (CheckBoxCustom) findViewById(R.id.checkbox_Goiy_2);
		checkBox3 = (CheckBoxCustom) findViewById(R.id.checkbox_Goiy_3);
		checkBox4 = (CheckBoxCustom) findViewById(R.id.checkbox_Khongthich);
        checkBox1.setOnClickListener(this);
        checkBox2.setOnClickListener(this);
        checkBox3.setOnClickListener(this);
        checkBox4.setOnClickListener(this);

		((ImageView) findViewById(R.id.imgBack)).setOnClickListener(this);
		((ButtonCustom) findViewById(R.id.btnButtonFinishMonan)).setOnClickListener(this);
		((ImageView) findViewById(R.id.imgRefresh)).setOnClickListener(this);
        generateRandomListOfFood();
		showChooseFirst();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBack:
			finish();
			break;
		case R.id.btnButtonFinishMonan:
			actionFinishButton();
			break;
		case R.id.imgRefresh:
			// if (listFood == null) {
			// showProgress();
			// ListFoodCommand.start(baseActivity);
			// } else
			generateRandomListOfFood();
			break;
        case R.id.checkbox_Goiy_1:
        case R.id.checkbox_Goiy_2:
        case R.id.checkbox_Goiy_3:
            checkBox4.setChecked(false);
            break;
        case R.id.checkbox_Khongthich:
            checkBox1.setChecked(false);
            checkBox2.setChecked(false);
            checkBox3.setChecked(false);
            break;
        default:
			break;
		}
	}

    //Check if some option checked and save data, if not than show alert dialog
	private void actionFinishButton() {
		if (!(checkBox1.isChecked() | checkBox2.isChecked() | checkBox3.isChecked() | checkBox4.isChecked())) {
			DialogUtilities.showAlertDialog(baseActivity, R.layout.dialog_error_warning, R.string.string_error, R.string.error_enter_hat, null);
			return;
		} else {
			saveDataAndFinish();
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}

	private void generateRandomListOfFood() {
		checkBox1.setChecked(false);
		checkBox2.setChecked(false);
		checkBox3.setChecked(false);

		if (listFood != null) {
			randomFoodList = new ArrayList<Integer>(3);
			Random r = new Random();
			while (randomFoodList.size() < 3) {
				int i = r.nextInt(listFood.length);
				if (!isFoodAllreadyInList(i)) {
                    randomFoodList.add(i);
                    showedFoodList.add(i);
                }
			}
		}
		if (randomFoodList != null && randomFoodList.size() > 0) {
			for (int i = 0; i < randomFoodList.size(); i++) {
				switch (i) {
				case 0:
					text_Goiy_1.setText(listFood[randomFoodList.get(0)]);
					break;
				case 1:
					text_Goiy_2.setText(listFood[randomFoodList.get(1)]);
					break;
				case 2:
					text_Goiy_3.setText(listFood[randomFoodList.get(2)]);
					break;
				default:
					break;
				}
			}
		}
		showChoose();
	}

    //Check if some random food allready added to random food list
	private boolean isFoodAllreadyInList(int i) {
        if(showedFoodList.size() == listFood.length-3) {
            showedFoodList.clear();
        }
		if (randomFoodList != null) {
			for (int j : randomFoodList) {
				if (i == j) return true;
			}
            //if allready showed, than return true
            for (int j : showedFoodList) {
                if (i == j) return true;
            }
		}
		return false;
	}

    //
    private boolean isFoodShowedAllready(int i) {
        return false;
    }

    //If note allready contains some food, then fill fields with this food
	private void showChooseFirst() {
		if (note.getRecommended_foods() != null) {
			if (note.getRecommended_foods().contains(",")) {
				String[] split = note.getRecommended_foods().split(",");
				for (int i = 0; i < split.length; i++) {
					switch (i) {
					case 0:
						text_Goiy_1.setText(split[0]);
						checkBox1.setChecked(true);
						break;
					case 1:
						text_Goiy_2.setText(split[1]);
						checkBox2.setChecked(true);
						break;
					case 2:
						text_Goiy_3.setText(split[2]);
						checkBox3.setChecked(true);
						break;
					default:
						break;
					}
				}
			} else {
				try {
					String str = note.getRecommended_foods();
					if (!TextUtils.isEmpty(str)) {
						text_Goiy_1.setText(str);
						checkBox1.setChecked(true);
					} else {    //If empty string, then choosed no food
                        checkBox4.setChecked(true);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	//Set checked food wich is allready in NoteDTO after random list generated
	private void showChoose() {
		if (note.getRecommended_foods() != null) {
			if (note.getRecommended_foods().contains(",")) {
				String[] split = note.getRecommended_foods().split(",");
				for (String str : split) {
					try {
						if (str.equals(text_Goiy_1.getText().toString()))
							checkBox1.setChecked(true);
						else if (str.equals(text_Goiy_2.getText().toString()))
							checkBox2.setChecked(true);
						else if (str.equals(text_Goiy_3.getText().toString()))
							checkBox3.setChecked(true);
						else if (str.equals(text_Khongthich.getText().toString()))
							checkBox4.setChecked(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				try {
					String str = note.getRecommended_foods();
					if (str.equals(text_Goiy_1.getText().toString()))
						checkBox1.setChecked(true);
					else if (str.equals(text_Goiy_2.getText().toString()))
						checkBox2.setChecked(true);
					else if (str.equals(text_Goiy_3.getText().toString()))
						checkBox3.setChecked(true);
					else if (str.equals(text_Khongthich.getText().toString()))
						checkBox4.setChecked(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void saveDataAndFinish() {
		String foods = "";
        if (!checkBox4.isChecked()) {
            if (checkBox1.isChecked())
                foods = text_Goiy_1.getText().toString();
            if (checkBox2.isChecked()) {
                if (TextUtils.isEmpty(foods))
                    foods = text_Goiy_2.getText().toString();
                else
                    foods += "," + text_Goiy_2.getText().toString();
            }
            if (checkBox3.isChecked()) {
                if (TextUtils.isEmpty(foods))
                    foods = text_Goiy_3.getText().toString();
                else
                    foods += "," + text_Goiy_3.getText().toString();
            }
        }
        note.setRecommended_foods(foods);
        Intent intent = new Intent();
        intent.putExtra("NOTE", note);
        setResult(RESULT_OK, intent);
        finish();
	}

//	private void addActions() {
//		addAction(ServiceConsts.NEW_NOTE_SUCCESS_ACTION,
//				new NewNoteSuccessAction());
//		addAction(ServiceConsts.NEW_NOTE_FAIL_ACTION, new NewNoteFailAction());
//		addAction(ServiceConsts.GET_LIST_FOOD_SUCCESS_ACTION,
//				new ListFoodSuccessAction());
//		addAction(ServiceConsts.GET_LIST_FOOD_FAIL_ACTION,
//				new ListFoodFailAction());
//		updateBroadcastActionList();
//	}
//
//	private void removeActions() {
//		removeAction(ServiceConsts.NEW_NOTE_SUCCESS_ACTION);
//		removeAction(ServiceConsts.NEW_NOTE_FAIL_ACTION);
//		removeAction(ServiceConsts.GET_LIST_FOOD_SUCCESS_ACTION);
//		removeAction(ServiceConsts.GET_LIST_FOOD_FAIL_ACTION);
//		updateBroadcastActionList();
//	}
//
//	private class NewNoteSuccessAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			hideProgress();
//			NoteDTO note = (NoteDTO) bundle
//					.getSerializable(ServiceConsts.EXTRA_NOTE);
//			if (note != null) {
//				Intent intent = new Intent();
//				intent.putExtra("VALUE", note);
//				setResult(RESULT_OK, intent);
//				finish();
//			} else {
//				DialogUtilities.showAlertDialog(baseActivity,
//						R.layout.dialog_error_warning, R.string.string_error,
//						R.string.error_message_for_service_unavailable, null);
//			}
//		}
//	}
//
//	private class NewNoteFailAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			hideProgress();
//			DialogUtilities.showAlertDialog(baseActivity,
//					R.layout.dialog_error_warning, R.string.string_error,
//					R.string.error_message_for_service_unavailable, null);
//		}
//	}
//
//	private class ListFoodSuccessAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			hideProgress();
//			// FoodDTO food = (FoodDTO) bundle
//			// .getSerializable(ServiceConsts.EXTRA_LIST_FOOD);
//			// listFood = food.getListFood();
//			generateRandomListOfFood();
//		}
//	}
//
//	private class ListFoodFailAction implements Command {
//		@Override
//		public void execute(Bundle bundle) {
//			hideProgress();
//		}
//	}
}
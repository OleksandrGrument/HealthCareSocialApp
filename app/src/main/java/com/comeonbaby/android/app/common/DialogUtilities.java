package com.comeonbaby.android.app.common;

import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.adapter.ListChoiceAdapter;
import com.comeonbaby.android.app.adapter.ListCityAdapter;
import com.comeonbaby.android.app.db.dto.CityDTO;
import com.comeonbaby.android.app.db.dto.ProfileDTO;
import com.comeonbaby.android.app.utils.AppSession;
import com.comeonbaby.android.app.view.BaseActivity;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.EditTextCustom;
import com.comeonbaby.android.app.view.customview.TextViewCustom;


@SuppressLint("InflateParams")
public class DialogUtilities {

	static public void showConfirmDialog(Context context, int title, int message, int yesButtonText, int noButtonText,
										 final OnClickListener yesButtonListener, final OnClickListener noButtonListener) {

		LayoutInflater factory = LayoutInflater.from(context);
		View view = factory.inflate(R.layout.dialog_general_confirm, null);

		TextView txtMessage = (TextView) view.findViewById(R.id.txtConfirmMessage);
		TextView txtTitle = (TextView) view.findViewById(R.id.txtConfirmTitle);
		txtTitle.setText(title);
		txtMessage.setText(message);

		Button btnYes = (Button) view.findViewById(R.id.btnConfirmYes);
		Button btnNo = (Button) view.findViewById(R.id.btnConfirmNo);
		btnYes.setText(yesButtonText);
		btnNo.setText(noButtonText);

		createDialog(context, view, yesButtonListener, noButtonListener, btnYes, btnNo);
	}

	static public void showConfirmDialog(Context context, String title, String message, final OnClickListener yesButtonListener,
										 final OnClickListener noButtonListener) {

		LayoutInflater factory = LayoutInflater.from(context);
		View view = factory.inflate(R.layout.dialog_general_confirm, null);

		TextView txtMessage = (TextView) view.findViewById(R.id.txtConfirmMessage);
		TextView txtTitle = (TextView) view.findViewById(R.id.txtConfirmTitle);
		txtTitle.setText(title);
		txtMessage.setText(message);

		Button btnYes = (Button) view.findViewById(R.id.btnConfirmYes);
		Button btnNo = (Button) view.findViewById(R.id.btnConfirmNo);

		createDialog(context, view, yesButtonListener, noButtonListener, btnYes, btnNo);
	}

	static public void showConfirmDialog(Context context, String title, String message, String yes, String no,
										 final OnClickListener yesButtonListener, final OnClickListener noButtonListener) {

		LayoutInflater factory = LayoutInflater.from(context);
		View view = factory.inflate(R.layout.dialog_general_confirm, null);

		TextView txtMessage = (TextView) view.findViewById(R.id.txtConfirmMessage);
		TextView txtTitle = (TextView) view.findViewById(R.id.txtConfirmTitle);
		txtTitle.setText(title);
		txtMessage.setText(message);

		Button btnYes = (Button) view.findViewById(R.id.btnConfirmYes);
		btnYes.setText(yes);
		Button btnNo = (Button) view.findViewById(R.id.btnConfirmNo);
		btnNo.setText(no);

		createDialog(context, view, yesButtonListener, noButtonListener, btnYes, btnNo);
	}

	static public void showConfirmDialog(Context context, int title, int message,
                                         final OnClickListener yesButtonListener, final OnClickListener noButtonListener) {

		LayoutInflater factory = LayoutInflater.from(context);
		View view = factory.inflate(R.layout.dialog_general_confirm, null);

		TextView txtMessage = (TextView) view.findViewById(R.id.txtConfirmMessage);
		TextView txtTitle = (TextView) view.findViewById(R.id.txtConfirmTitle);
		txtTitle.setText(title);
		txtMessage.setText(message);

		Button btnYes = (Button) view.findViewById(R.id.btnConfirmYes);
		Button btnNo = (Button) view.findViewById(R.id.btnConfirmNo);

		createDialog(context, view, yesButtonListener, noButtonListener, btnYes, btnNo);
	}

	static public void showNoticeDialog(Context context, View view, int title, int message,
                                        final OnClickListener yesButtonListener, final OnClickListener noButtonListener) {

		TextView txtTitle = (TextView) view.findViewById(R.id.txtConfirmTitle);
		TextView txtMessage = (TextView) view.findViewById(R.id.txtConfirmMessage);
		txtTitle.setText(title);
		txtMessage.setText(message);

		Button btnYes = (Button) view.findViewById(R.id.btnConfirmYes);
		Button btnNo = (Button) view.findViewById(R.id.btnConfirmNo);

		createDialog(context, view, yesButtonListener, noButtonListener, btnYes, btnNo);
	}

	static public void showNoticeSharedCameraDialog(Context context, View view, int title, int message,
                                                    final OnClickListener yesButtonListener) {

		TextView txtTitle = (TextView) view.findViewById(R.id.txtConfirmTitle);
		TextView txtMessage = (TextView) view.findViewById(R.id.txtConfirmMessage);
		txtTitle.setText(title);
		txtMessage.setText(message);

		Button btnYes = (Button) view.findViewById(R.id.btnConfirmYes);
		createDialog(context, view, yesButtonListener, null, btnYes, null);
	}

	static public Dialog showAlertDialog(Context context, int id, int title,
										 int message, final OnClickListener okButtonListener) {
		return showAlertDialog(context, id, context.getString(title), context.getString(message), okButtonListener);
	}

	static public Dialog showAlertDialog(Context context, int id, String title,
										 String message, final OnClickListener okButtonListener) {

		LayoutInflater factory = LayoutInflater.from(context);
		View view = factory.inflate(id, null);

		LinearLayout header_dialog_one_button = null;
		TextView txtMsg = null;
		TextView txtSubMsg = null;

		Button btnOK = null;

		switch (id) {
			case R.layout.dialog_error_warning:
				header_dialog_one_button = (LinearLayout) view.findViewById(R.id.header_dialog_one_button);
				txtMsg = (TextView) view.findViewById(R.id.txtErrWarDialog);
				txtSubMsg = (TextView) view.findViewById(R.id.txtErrWarDialogSubMsg);
				btnOK = (Button) view.findViewById(R.id.btnErrWarDialogOk);
				break;

			case R.layout.dialog_login_successful:
				txtMsg = (TextView) view.findViewById(R.id.txtLoginSuccessDialog);
				txtSubMsg = (TextView) view.findViewById(R.id.txtLoginSuccessMsg);
				btnOK = (Button) view.findViewById(R.id.btnLoginSuccessDialogOk);
				break;
		}

		if (TextUtils.isEmpty(title)) {
			if (header_dialog_one_button != null)
                header_dialog_one_button.setVisibility(View.GONE);
			txtMsg.setVisibility(View.GONE);
		} else {
			header_dialog_one_button.setVisibility(View.VISIBLE);
			txtMsg.setText(title);
		}
		txtSubMsg.setText(message);

		return createDialog(context, view, okButtonListener, null, btnOK, null);
	}

	public static Dialog createDialog(Context context, final View view, final OnClickListener yesButtonListener,
									  final OnClickListener noButtonListener, Button btnYes, Button btnNo) {

		final Dialog dialog = new Dialog(context, R.style.CustomAlertDialogTheme);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);

		btnYes.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				if (yesButtonListener != null) {
					yesButtonListener.onClick(v);
				}
			}
		});

		if (btnNo != null) {
			btnNo.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
					if (noButtonListener != null) {
						noButtonListener.onClick(v);
					}
				}
			});
		}

		try {
			dialog.show();
			if (Common.isTablet(context))
				dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		} catch (Exception e) {
			System.out.print(e.toString());
		}
		return dialog;
	}

	public static Dialog createDialog(Context context, final View view, final OnClickListener yesButtonListener,
									  final OnClickListener noButtonListener, Button btnYes, Button btnNo, boolean cancelable) {

		final Dialog dialog = new Dialog(context, R.style.CustomAlertDialogTheme);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(cancelable);
		dialog.setCancelable(cancelable);

		btnYes.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				if (yesButtonListener != null) {
					yesButtonListener.onClick(v);
				}
			}
		});

		if (btnNo != null) {
			btnNo.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
					if (noButtonListener != null) {
						noButtonListener.onClick(v);
					}
				}
			});
		}

		try {
			Window w = dialog.getWindow();
			w.setGravity(Gravity.CENTER);
			WindowManager.LayoutParams lp = w.getAttributes();

			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			if (dm.widthPixels <= 480)
				lp.width = dm.widthPixels - 10;
			else
				lp.width = (3 * dm.widthPixels) / 5;
			w.setAttributes(lp);
			dialog.show();
		} catch (Exception e) {
			System.out.print(e.toString());
		}
		return dialog;
	}

	public static Dialog showNoNetworkDialog(Context context) {
		try {
			if ((Globals.lostConnectionDialog == null || !Globals.lostConnectionDialog.isShowing())) {
				LayoutInflater factory = LayoutInflater.from(context);
				View noWifiView = factory.inflate(R.layout.dialog_no_network_connectivity, null);
				final Dialog noWifiDialog = new Dialog(context, R.style.CustomAlertDialogTheme);
				noWifiDialog.setCanceledOnTouchOutside(false);
				noWifiDialog.setCancelable(false);
				noWifiDialog.setContentView(noWifiView);
				noWifiDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						Globals.lostConnectionDialog = null;
						ILog.e("showNoNetworkDialog => OnCancelListener ");
					}
				});
				noWifiDialog.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						Globals.lostConnectionDialog = null;
						ILog.e("showNoNetworkDialog => OnDismissListener ");
					}
				});
				noWifiDialog.show();
				if (Common.isTablet(context))
					noWifiDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				ILog.e("NoNetwork - showNoNetworkDialog show()");
				Globals.lostConnectionDialog = noWifiDialog;
				return noWifiDialog;
			}

			return Globals.lostConnectionDialog;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Globals.lostConnectionDialog;
	}

	public static Dialog createListChoiceDialog(Context context, String[] objects, String selected,
												final OnEventControlListener onclick, final int type) {
		LayoutInflater factory = LayoutInflater.from(context);
		View noWifiView = factory.inflate(R.layout.dialog_list_choice, null);

		final ListView list = (ListView) noWifiView.findViewById(R.id.listChoice);
		ListChoiceAdapter adapter = new ListChoiceAdapter(objects, context, Globals.size, selected, onclick, type);
		list.setAdapter(adapter);
		final Dialog choiceDialog = new Dialog(context, R.style.CustomAlertDialogTheme);
		choiceDialog.setCanceledOnTouchOutside(true);
		choiceDialog.setCancelable(true);
		choiceDialog.setContentView(noWifiView);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				String str = (String) list.getItemAtPosition(position);
				onclick.onEvent(type, null, str);
			}
		});
		Window w = choiceDialog.getWindow();
		w.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = w.getAttributes();

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int max_height = (int) (dm.heightPixels * 0.90);

		lp.width = (3 * dm.widthPixels) / 5;
		lp.height = max_height - 20;
		w.setAttributes(lp);
		return choiceDialog;
	}

	public static Dialog createListCityDialog(Context context, List<CityDTO> objects, Long selected,
											  final OnEventControlListener onclick, final int type) {
		LayoutInflater factory = LayoutInflater.from(context);
		View noWifiView = factory.inflate(R.layout.dialog_list_choice, null);

		final ListView list = (ListView) noWifiView.findViewById(R.id.listChoice);
		ListCityAdapter adapter = new ListCityAdapter(objects, context, Globals.size, selected, onclick, type);
		list.setAdapter(adapter);
		final Dialog choiceDialog = new Dialog(context, R.style.CustomAlertDialogTheme);
		choiceDialog.setCanceledOnTouchOutside(true);
		choiceDialog.setCancelable(true);
		choiceDialog.setContentView(noWifiView);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				CityDTO str = (CityDTO) list.getItemAtPosition(position);
				onclick.onEvent(type, null, str);
			}
		});
		Window w = choiceDialog.getWindow();
		w.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = w.getAttributes();

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int max_height = (int) (dm.heightPixels * 0.90);

		lp.width = (3 * dm.widthPixels) / 5;
		lp.height = max_height - 20;
		w.setAttributes(lp);
		return choiceDialog;
	}

	public static Dialog createDatePickerDialog(final Context context, final OnEventControlListener listener, final int type) {
		final int MAX_YEAR = 2099;
		LayoutInflater factory = LayoutInflater.from(context);
		Calendar cal = Calendar.getInstance();

		View dialog = factory.inflate(R.layout.dialog_date_picker, null);
		final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
		final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);
		final NumberPicker dayPicker = (NumberPicker) dialog.findViewById(R.id.picker_day);

		TextViewCustom txtClose = (TextViewCustom) dialog.findViewById(R.id.txtClose);
		TextViewCustom txtConfirm = (TextViewCustom) dialog.findViewById(R.id.txtConfirm);

		((BaseActivity) context).setSizeTextView(dialog.findViewById(R.id.layoutRootDate), Globals.size);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		try {
			ProfileDTO profile = AppSession.getSession().getSystemUser().getProfileDTO();
			if (profile != null && profile.getLast_cycle() != null && !TextUtils.isEmpty(profile.getLast_cycle())) {
				String[] split = profile.getLast_cycle().split("-");
				year = Integer.parseInt(split[0]);
				month = Integer.parseInt(split[1]);
				if (split[2].length() > 2) {
					String d = split[2].substring(0, 2);
					day = Integer.parseInt(d);
				} else
					day = Integer.parseInt(split[2]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		monthPicker.setMinValue(1);
		monthPicker.setMaxValue(12);
		monthPicker.setValue(month);

		yearPicker.setMinValue(year-1);
		yearPicker.setMaxValue(MAX_YEAR);
		yearPicker.setValue(year);

		dayPicker.setMinValue(1);
		int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		dayPicker.setMaxValue(maxDays);
		dayPicker.setValue(day);

		final Dialog dateDialog = new Dialog(context, R.style.CustomAlertDialogTheme);
		dateDialog.setCanceledOnTouchOutside(true);
		dateDialog.setCancelable(true);
		dateDialog.setContentView(dialog);

		txtClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dateDialog.dismiss();
			}
		});
		txtConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String date = yearPicker.getValue() + "-" +
                        ((monthPicker.getValue() > 9) ? monthPicker.getValue() : ("0" + monthPicker.getValue())) + "-"
						+ ((dayPicker.getValue() > 9) ? (dayPicker.getValue()) : ("0" + dayPicker.getValue()));
				listener.onEvent(type, null, date);
			}
		});
		monthPicker.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if (oldVal != newVal) {
					updateDays(yearPicker, picker, dayPicker);
				}
			}
		});
		yearPicker.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if (oldVal != newVal) {
					updateDays(picker, monthPicker, dayPicker);
				}
			}
		});
		return dateDialog;
	}

	/**
	 * Updates day wheel. Sets max days according to selected month and year
	 */
	static void updateDays(NumberPicker year, NumberPicker month, NumberPicker day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year.getValue());
		calendar.set(Calendar.MONTH, month.getValue() - 1);

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int curDay = Math.min(maxDays, day.getValue());
		day.setValue(curDay);
		day.setMaxValue(maxDays);
	}

    //Диалог выбора продолжительности цикла
	public static Dialog createDayPickerDialog(final Context context, final OnEventControlListener listener, final int type) {
		LayoutInflater factory = LayoutInflater.from(context);
		View dialog = factory.inflate(R.layout.dialog_day_picker, null);
		final NumberPicker dayPicker = (NumberPicker) dialog.findViewById(R.id.picker_day);
		TextViewCustom txtClose = (TextViewCustom) dialog.findViewById(R.id.txtClose);
		TextViewCustom txtConfirm = (TextViewCustom) dialog.findViewById(R.id.txtConfirm);
		((BaseActivity) context).setSizeTextView(dialog.findViewById(R.id.layoutRootDay), Globals.size);
		int value = 28;
		try {
			ProfileDTO profile = AppSession.getSession().getSystemUser().getProfileDTO();
			if (profile != null && profile.getMenstrual_cycle() != 0) {
				value = profile.getMenstrual_cycle();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		dayPicker.setMinValue(20);
		dayPicker.setMaxValue(60);
		dayPicker.setValue(value);

		final Dialog dayDialog = new Dialog(context, R.style.CustomAlertDialogTheme);
		dayDialog.setCanceledOnTouchOutside(true);
		dayDialog.setCancelable(true);
		dayDialog.setContentView(dialog);

		txtClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dayDialog.dismiss();
			}
		});
		txtConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onEvent(type, null, dayPicker.getValue());
			}
		});
		Window w = dayDialog.getWindow();
		w.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = w.getAttributes();

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		if (dm.widthPixels <= 480)
			lp.width = dm.widthPixels - 10;
		else
			lp.width = (3 * dm.widthPixels) / 5;
		w.setAttributes(lp);
		return dayDialog;
	}

    //Диалог выбора продолжительности месячных
	public static Dialog createRedDayPickerDialog(final Context context, final OnEventControlListener listener, final int type) {
		LayoutInflater factory = LayoutInflater.from(context);
		View dialog = factory.inflate(R.layout.dialog_day_picker, null);
		final NumberPicker dayPicker = (NumberPicker) dialog.findViewById(R.id.picker_day);
		((TextViewCustom) dialog.findViewById(R.id.txtTitle)).setText(context
				.getString(R.string.text_profile_config_redday));
		TextViewCustom txtClose = (TextViewCustom) dialog.findViewById(R.id.txtClose);
		TextViewCustom txtConfirm = (TextViewCustom) dialog.findViewById(R.id.txtConfirm);
		((BaseActivity) context).setSizeTextView(dialog.findViewById(R.id.layoutRootDay), Globals.size);
		int value = 7;
		try {
			ProfileDTO profile = AppSession.getSession().getSystemUser().getProfileDTO();
			if (profile != null && profile.getRed_days() != 0) {
				value = profile.getRed_days();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		dayPicker.setMinValue(2);
		dayPicker.setMaxValue(7);
		dayPicker.setValue(value);

		final Dialog dayDialog = new Dialog(context, R.style.CustomAlertDialogTheme);
		dayDialog.setCanceledOnTouchOutside(true);
		dayDialog.setCancelable(true);
		dayDialog.setContentView(dialog);

		txtClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dayDialog.dismiss();
			}
		});
		txtConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onEvent(type, null, dayPicker.getValue());
			}
		});
		Window w = dayDialog.getWindow();
		w.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = w.getAttributes();

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		if (dm.widthPixels <= 480)
			lp.width = dm.widthPixels - 10;
		else
			lp.width = (3 * dm.widthPixels) / 5;
		w.setAttributes(lp);
		return dayDialog;
	}

    //Диалог при первом запуске календаря
	public static void showHomeFirstDialog(Context context) {
		LayoutInflater factory = LayoutInflater.from(context);
		View noWifiView = factory.inflate(R.layout.dialog_home_first, null);
		final Dialog choiceDialog = new Dialog(context, R.style.CustomHomeDialogTheme);
		choiceDialog.setCanceledOnTouchOutside(false);
		choiceDialog.setCancelable(false);
		choiceDialog.setContentView(noWifiView);

		ButtonCustom btnStart = (ButtonCustom) noWifiView.findViewById(R.id.btnButtonClose);
		btnStart.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		btnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				choiceDialog.dismiss();
			}
		});
		choiceDialog.show();
	}

	static public void showCommunityDialog(Context context, final OnClickListener yesButtonListener,
										   final OnClickListener noButtonListener) {
		LayoutInflater factory = LayoutInflater.from(context);
		View view = factory.inflate(R.layout.dialog_edit_community, null);

		Button btnYes = (Button) view.findViewById(R.id.btnConfirmYes);
		Button btnNo = (Button) view.findViewById(R.id.btnConfirmNo);
		btnYes.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		btnNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		createDialog(context, view, yesButtonListener, noButtonListener, btnYes, btnNo, true);
	}

    //Диалог выбора способа загрузки аватара
	static public void showAddPhotoDialog(Context context, final OnClickListener yesButtonListener,
										  final OnClickListener noButtonListener) {
		LayoutInflater factory = LayoutInflater.from(context);
		View view = factory.inflate(R.layout.dialog_add_photo_community, null);

		Button btnYes = (Button) view.findViewById(R.id.btnConfirmYes);
		Button btnNo = (Button) view.findViewById(R.id.btnConfirmNo);
		btnYes.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		btnNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		createDialog(context, view, yesButtonListener, noButtonListener, btnYes, btnNo, true);
	}

    //Диалог выбора пола
	static public void showMaleFemaleDialog(Context context, final OnClickListener yesButtonListener,
											final OnClickListener noButtonListener, int strYes, int strNo) {
		LayoutInflater factory = LayoutInflater.from(context);
		View view = factory.inflate(R.layout.dialog_male_female, null);

		Button btnYes = (Button) view.findViewById(R.id.btnConfirmYes);
		Button btnNo = (Button) view.findViewById(R.id.btnConfirmNo);
		btnYes.setText(strYes);
		btnNo.setText(strNo);
		btnYes.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		btnNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, Globals.size);
		createDialog(context, view, yesButtonListener, noButtonListener, btnYes, btnNo, true);
	}

    //Диалог BMI из SettingsFragment
	public static Dialog createBMIDialog(final Context context, final OnEventControlListener listener, final int type) {
		LayoutInflater factory = LayoutInflater.from(context);
		View dialog = factory.inflate(R.layout.dialog_bmi, null);
		TextViewCustom txtClose = (TextViewCustom) dialog.findViewById(R.id.txtClose);
		TextViewCustom txtConfirm = (TextViewCustom) dialog.findViewById(R.id.txtConfirm);
		final EditTextCustom editChieuCao = (EditTextCustom) dialog.findViewById(R.id.editChieuCao);
		final EditTextCustom editCanNang = (EditTextCustom) dialog.findViewById(R.id.editCanNang);

		ProfileDTO profileDTO = AppSession.getSession().getSystemUser().getProfileDTO();
		double height = profileDTO.getHeight();
		double weight = profileDTO.getWeight();
		editChieuCao.setText(String.valueOf(height));
		editCanNang.setText(String.valueOf(weight));

		((BaseActivity) context).setSizeTextView(dialog.findViewById(R.id.layoutRootBmi), Globals.size);
		final Dialog dateDialog = new Dialog(context, R.style.CustomAlertDialogTheme);
		dateDialog.setCanceledOnTouchOutside(true);
		dateDialog.setCancelable(true);
		dateDialog.setContentView(dialog);

		txtClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dateDialog.dismiss();
			}
		});
		txtConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String cm = editChieuCao.getText().toString();
				String kg = editCanNang.getText().toString();
				if (TextUtils.isEmpty(cm) || TextUtils.isEmpty(kg)) {
					showAlertDialog(context, R.layout.dialog_error_warning, R.string.string_error, R.string.string_enter_your_information_bmi, null);
					return;
				}
				listener.onEvent(type, null, kg + "-" + cm);
			}
		});
		Window w = dateDialog.getWindow();
		w.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = w.getAttributes();

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		if (dm.widthPixels <= 480) lp.width = dm.widthPixels - 10;
		else lp.width = (3 * dm.widthPixels) / 5;
		w.setAttributes(lp);
		return dateDialog;
	}
}
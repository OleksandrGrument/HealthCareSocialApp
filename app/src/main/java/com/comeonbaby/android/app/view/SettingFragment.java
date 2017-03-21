package com.comeonbaby.android.app.view;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.Common;
import com.comeonbaby.android.app.common.DialogUtilities;
import com.comeonbaby.android.app.common.Globals;
import com.comeonbaby.android.app.db.dto.CityDTO;
import com.comeonbaby.android.app.db.dto.ProfileDTO;
import com.comeonbaby.android.app.db.dto.UserDTO;
import com.comeonbaby.android.app.requests.Constants;
import com.comeonbaby.android.app.requests.ExtraConstants;
import com.comeonbaby.android.app.requests.commands.Commands;
import com.comeonbaby.android.app.utils.AppSession;
import com.comeonbaby.android.app.utils.ImageUtils;
import com.comeonbaby.android.app.utils.PermHelper;
import com.comeonbaby.android.app.utils.PrefsHelper;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.CircularImageView;
import com.comeonbaby.android.app.view.customview.EditTextCustom;
import com.comeonbaby.android.app.view.customview.TextViewCustom;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.theartofdev.edmodo.cropper.CropImage;
//import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class SettingFragment extends BaseContainerFragment implements OnClickListener, OnDateSetListener {
    private final static String TAG = "SettingFragment";
    public final static int TYPE_DIALOG_CHOICE_BIRTHDAY = 1;
    public final static int TYPE_DIALOG_CHOICE_CITY = 2;
    public final static int TYPE_DIALOG_CHOICE_STATE = 3;
    public final static int TYPE_DIALOG_DATE = 4;
    public final static int TYPE_DIALOG_DAY = 5;
    public final static int TYPE_DIALOG_BMI = 6;
    public final static int TYPE_DIALOG_RED_DAY = 7;
    final int REQUEST_CAMERA = 1;
    final int SELECT_FILE = 2;
    private ProgressBar progress; // Создание прогресбара
    private AppSession session;

    private Dialog dialogChoice;
    private PrefsHelper prefsHelper;
    private Dialog dialogDate;
    private ProfileDTO profileDto;
    private UserDTO userDTO;
    private boolean fromDayNote;
    private boolean isClick = false;

    //Вьюшки
    private CircularImageView imgAvatar;
    private TextViewCustom txtGender;
    private TextViewCustom txtBirthYear;
    private TextViewCustom txtCycleLength;
    private TextViewCustom txtRedDays;
    private TextViewCustom txtLastMenstrDate;
    private TextViewCustom txtBMI;
    private EditTextCustom txtEmail;
    private EditTextCustom txtPassword;
    private EditTextCustom txtNickname;
    private EditTextCustom txtAdress;
    private ButtonCustom btnCity;
    private ToggleButton togglePush;
    private Handler handler;

    public void setFromDayNote(boolean fromDayNote) {
        this.fromDayNote = fromDayNote;
    }

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prefsHelper = PrefsHelper.getPrefsHelper();
        session = AppSession.getSession();
        initObjectUI();
        initHandler();
        setProfileInfo();
        loadAvatar();
        ((BaseActivity) getActivity()).setupHideKeyboard(getActivity().findViewById(R.id.layoutRootProfile));
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Bundle data = msg.getData();
                String operation = "", result = "", message = "", fullMsg = "";
                JSONObject user = null, jsdata = null;
                if(data.containsKey(ExtraConstants.OPERATION)) operation = data.getString(ExtraConstants.OPERATION);
                if(data.containsKey(ExtraConstants.RESULT)) result = data.getString(ExtraConstants.RESULT);
                if(data.containsKey(ExtraConstants.MESSAGE)) message = data.getString(ExtraConstants.MESSAGE);
                try {
                    if (data.containsKey(ExtraConstants.USER)) {
                        user = new JSONObject(data.getString(ExtraConstants.USER));
                    }
                    if (data.containsKey(ExtraConstants.DATA)) {
                        jsdata = new JSONObject(data.getString(ExtraConstants.DATA));
                    }
                } catch (JSONException exc) {
                    exc.printStackTrace();
                    return;
                }
                //progress.setVisibility(View.INVISIBLE);
                //if(msg.what != Constants.MSG_ERROR) showSnackMessage(message);
                switch (msg.what) {
                    case Constants.MSG_EMAIL_UPDATE_SUCCESS: {
                        if(user != null) {
                            try {
                                String newEmail = user.getString(Constants.EMAIL);
                                session.getSystemUser().setEmail(newEmail);
                                session.save();
                                txtEmail.setText(AppSession.getSession().getSystemUser().getEmail());
                            } catch (JSONException e) {e.printStackTrace();}
                        }
                        break;
                    }
                    case Constants.MSG_EMAIL_UPDATE_FAIL: {
                        txtEmail.setText(AppSession.getSession().getSystemUser().getEmail());
                        break;
                    }
                    case Constants.MSG_PASSWORD_UPDATE_SUCCESS: {
                        if(user != null) {
                            try {
                                String newPass = user.getString(Constants.PASSWORD);
                                session.getSystemUser().setPassword(newPass);
                                session.save();
                                txtPassword.setText(AppSession.getSession().getSystemUser().getPassword());
                            } catch (JSONException e) {e.printStackTrace();}
                        }
                        break;
                    }
                    case Constants.MSG_PASSWORD_UPDATE_FAIL: {
                        txtPassword.setText(AppSession.getSession().getSystemUser().getPassword());
                        break;
                    }
                    case Constants.MSG_UPDATE_PROFILE_SUCCESS: {
                        if(jsdata != null) {
                            ProfileDTO newProfile = new ProfileDTO();
                            newProfile.parseFromJson(jsdata.toString());
                            AppSession.getSession().getSystemUser().setProfileDTO(newProfile);
                            setProfileInfo();
                        }
                        break;
                    }
                    case Constants.MSG_UPDATE_PROFILE_FAIL: {
                        break;
                    }
                    case Constants.MSG_UPDATE_AVATAR_SUCCESS: {
                        Log.d(TAG, "Success update avatar");
                        try {
                            if (jsdata.has(Constants.AVATAR)) {
                                profileDto.setAvatar(jsdata.getString(Constants.AVATAR));
                                loadAvatar();
                            }
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                        break;
                    }
                    case Constants.MSG_UPDATE_AVATAR_FAIl: {
                        break;
                    }
                    //No connection
                    case Constants.MSG_ERROR: {
                        showSnackMessage("Something wrong");
                        break;
                    }
                }
                if(activity != null) activity.hideProgress();
            }
        };
    }

    //Всплывающее сообщение
    private void showSnackMessage(String msg) {
        Snackbar.make(getActivity().getCurrentFocus(), msg, Snackbar.LENGTH_LONG).show();
    }

    private void setProfileInfo() {
        //If ProfileDTO exist, get it and set info, else create new ProfileDTO
        userDTO = AppSession.getSession().getSystemUser();
        profileDto = userDTO.getProfileDTO();
        //Заполнение полей данными из ProfileDTO

        txtGender.setText(profileDto.isGender() ? getString(R.string.text_male) : getString(R.string.text_female));
        txtBirthYear.setText(String.valueOf(profileDto.getBirthYear()));
        txtCycleLength.setText(profileDto.getMenstrual_cycle() + " " + getString(R.string.text_day));
        txtRedDays.setText(profileDto.getRed_days() + " " + getString(R.string.text_day));
        if (!TextUtils.isEmpty(profileDto.getLast_cycle())) {
            String[] split = profileDto.getLast_cycle().split("-");
            String text = split[0] + getString(R.string.text_year) + " " + split[1] + getString(R.string.text_month) + " " + split[2] + getString(R.string.text_day);
            txtLastMenstrDate.setText(text);
        }
        txtBMI.setText(String.valueOf(profileDto.getBmi()));
        txtAdress.setText(profileDto.getAddress());
        txtNickname.setText(profileDto.getNickname());
        Log.d("CITY", profileDto.getCity().getName());
        if (profileDto.getCity() != null) {
            btnCity.setText(profileDto.getCity().getName());
        }
    }

    private void loadAvatar() {
        if (!TextUtils.isEmpty(profileDto.getAvatar())) {
            String imageName = profileDto.getAvatar();
            String imageUrl = Constants.IMAGES_URL + imageName;
            ImageLoader.getInstance().displayImage(imageUrl,
                    ((CircularImageView) getActivity().findViewById(R.id.imgAvatar)),
                    profileDto.isGender() ? com.comeonbaby.android.app.common.Constants.PROFILE_AVATAR_MALE_DISPLAY_OPTIONS
                            : com.comeonbaby.android.app.common.Constants.PROFILE_AVATAR_FEMALE_DISPLAY_OPTIONS);
        }
    }

    private void initObjectUI() {
        //progress = (ProgressBar) getActivity().findViewById(R.id.progress); // Инициализация прогрессбара
        ((BaseActivity) getActivity()).setSizeTextView(getActivity().findViewById(R.id.layoutRootProfile), Globals.size);
        ((ImageView) getActivity().findViewById(R.id.imgBack)).setVisibility(View.GONE);
        //If activity started from DayHomeActivity
        if (fromDayNote) {
            ((ImageView) getActivity().findViewById(R.id.imgBack)).setVisibility(View.VISIBLE);
            ((ImageView) getActivity().findViewById(R.id.imgBack)).setOnClickListener(this);
            ((TextViewCustom) getActivity().findViewById(R.id.txtTitle)).setText(R.string.text_thietlap_modified);
            ((ScrollView) getActivity().findViewById(R.id.scrollSetting)).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((ScrollView) getActivity().findViewById(R.id.scrollSetting)).fullScroll(ScrollView.FOCUS_DOWN);
                }
            }, 600);
        }

        imgAvatar = (CircularImageView) getActivity().findViewById(R.id.imgAvatar);
        txtGender = (TextViewCustom) getActivity().findViewById(R.id.textGioitinh);
        txtBirthYear = (TextViewCustom) getActivity().findViewById(R.id.textNamSinh);
        txtCycleLength = (TextViewCustom) getActivity().findViewById(R.id.textChuKy);
        txtRedDays = (TextViewCustom) getActivity().findViewById(R.id.textRedday);
        txtLastMenstrDate = (TextViewCustom) getActivity().findViewById(R.id.textNgayCuoi);
        txtBMI = (TextViewCustom) getActivity().findViewById(R.id.textBMI);
        txtEmail = (EditTextCustom) getActivity().findViewById(R.id.textEmail);
        txtPassword = (EditTextCustom) getActivity().findViewById(R.id.editPassword);
        txtNickname = (EditTextCustom) getActivity().findViewById(R.id.editNickname);
        txtAdress = (EditTextCustom) getActivity().findViewById(R.id.editDiaChi);
        btnCity = (ButtonCustom) getActivity().findViewById(R.id.btnCity);
        togglePush = (ToggleButton) getActivity().findViewById(R.id.togglePush);

        imgAvatar.setOnClickListener(this);
        txtGender.setOnClickListener(this);
        txtBirthYear.setOnClickListener(this);
        txtCycleLength.setOnClickListener(this);
        txtRedDays.setOnClickListener(this);
        txtLastMenstrDate.setOnClickListener(this);
        txtBMI.setOnClickListener(this);
        togglePush.setOnClickListener(this);
        ((TextViewCustom) getActivity().findViewById(R.id.textLogout)).setOnClickListener(this);
        ((TextViewCustom) getActivity().findViewById(R.id.textDelete)).setOnClickListener(this);
        ((LinearLayout) getActivity().findViewById(R.id.layoutCity)).setOnClickListener(this);
        togglePush.setChecked(prefsHelper.getPref(PrefsHelper.SHARED_PREF_TOGGLE_PUSH + AppSession.getSession().getSystemUser().getSystemID(), true));

        txtEmail.setText(AppSession.getSession().getSystemUser().getEmail());
        txtPassword.setText(AppSession.getSession().getSystemUser().getPassword());
        //If LoginType is not EMAIL than set email field enabled to make possible type user email
		if (!AppSession.getSession().getSystemUser().getLoginType().equals(Constants.LOGIN_EMAIL)) {
			txtEmail.setEnabled(true);
			txtEmail.setFocusable(true);
			txtEmail.setFocusableInTouchMode(true);
		}

        //Слушатели полей (используется слушатели потери фокуса)
        //email
        txtEmail.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false && !((EditTextCustom) v).getText().toString().equals(AppSession.getSession().getSystemUser().getEmail())) {
                    Commands.startUpdateEmail(handler, userDTO, ((EditTextCustom) v).getText().toString().trim());
                }
            }
        });
        //password
        txtPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false && !((EditTextCustom) v).getText().toString().equals(AppSession.getSession().getSystemUser().getPassword())) {
                    Commands.startUpdatePassword(handler, userDTO, ((EditTextCustom) v).getText().toString().trim());
                }
            }
        });
        //nickname
        txtNickname.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false && !((EditTextCustom) v).getText().toString().equals(profileDto.getNickname())) {
                    profileDto.setNickname(((EditTextCustom) v).getText().toString());
                    updateUser();
                }
            }
        });
        //adress
        txtAdress.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false && !((EditTextCustom) v).getText().toString().equals(profileDto.getAddress())) {
                    profileDto.setAddress(((EditTextCustom) v).getText().toString());
                    updateUser();
                }
            }
        });

//        txtAdress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                ((BaseActivity) getActivity()).hideSoftKeyboard(getActivity(), v);
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    if (!((EditTextCustom) v).getText().toString().equals(profileDto.getAddress())) {
//                        ProfileDTO profile = AppSession.getSession().getSystemUser().getProfileDTO();
//                        profile.setAddress(((EditTextCustom) v).getText().toString());
//                        updateUser();
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                if (fromDayNote) ((SettingActivity) getActivity()).finish();
                break;
            case R.id.imgAvatar: {
                checkStringFieldsChanged();
                //Запрос разрешений
                if(!PermHelper.checkPermissionGranted(getActivity(), PermHelper.REQUEST_CAMERA) || !PermHelper.checkPermissionGranted(getActivity(), PermHelper.REQUEST_STORAGE)) {
                    PermHelper.verifyCameraPermissions(getActivity());
                    PermHelper.verifyStoragePermissions(getActivity());
                    break;
                }
                OnClickListener yesButtonListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        getActivity().startActivityForResult(intent, REQUEST_CAMERA);
                    }
                };
                OnClickListener noButtonListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        getActivity().startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    }
                };
                DialogUtilities.showAddPhotoDialog(getActivity(), yesButtonListener, noButtonListener);
                break;
            }
            case R.id.textGioitinh:
                checkStringFieldsChanged();
                showDialogGender();
                break;
            case R.id.textNamSinh:
                checkStringFieldsChanged();
                showDialogBirthday();
                break;
            case R.id.layoutCity:
                checkStringFieldsChanged();
                showDialogCity(CityDTO.getListCity());
                break;
            case R.id.textChuKy:
                checkStringFieldsChanged();
                showDialogCycle();
                break;
            case R.id.textRedday:
                checkStringFieldsChanged();
                showDialogRedday();
                break;
            case R.id.textNgayCuoi:
                checkStringFieldsChanged();
                showDialogLastMenstrDate();
                break;
            case R.id.textBMI:
                checkStringFieldsChanged();
                showDialogBMI();
                break;
            case R.id.textLogout:
                checkStringFieldsChanged();
                OnClickListener yesButtonListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((BaseActivity) getActivity()).showProgress();
                        ((BaseActivity) getActivity()).logOut(getActivity());
                        ((BaseActivity) getActivity()).hideProgress();
                    }
                };
                OnClickListener noButtonListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                };
                DialogUtilities.showConfirmDialog(getActivity(),
                        R.string.string_error, R.string.string_confirm_logout,
                        R.string.string_yes, R.string.string_no, yesButtonListener, noButtonListener);
                break;
            case R.id.textDelete:
                yesButtonListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((BaseActivity) getActivity()).showProgress();
                        Commands.deleteUser(userDTO);
                        ((BaseActivity) getActivity()).logOut(getActivity());
                        ((BaseActivity) getActivity()).hideProgress();
                    }
                };
                noButtonListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                };
                DialogUtilities.showConfirmDialog(getActivity(), R.string.string_error,
                        R.string.string_confirm_delete_account,
                        R.string.string_yes, R.string.string_no, yesButtonListener, noButtonListener);
                break;
            case R.id.togglePush:
                checkStringFieldsChanged();
                if (togglePush.isChecked()) {
                    ((BaseActivity) getActivity()).showProgress();
                    prefsHelper.savePref(PrefsHelper.SHARED_PREF_TOGGLE_PUSH + AppSession.getSession().getSystemUser().getSystemID(), true);
                    //------------------RegisterDeviceGCMCommand.start(getActivity());
                    ((BaseActivity) getActivity()).hideProgress();
                } else {
                    ((BaseActivity) getActivity()).showProgress();
                    prefsHelper.savePref(PrefsHelper.SHARED_PREF_TOGGLE_PUSH + AppSession.getSession().getSystemUser().getSystemID(), false);
                    //-------------------UnRegisterDeviceGCMCommand.start(getActivity());
                    ((BaseActivity) getActivity()).hideProgress();
                }
                break;
            default:
                break;
        }

    }

    //Проверка на измененные данные текстовых полей при нажатии на другие элементы
    //Если изменились, то запросс на сервер
    private void checkStringFieldsChanged() {
        if (!txtEmail.getText().toString().equals(AppSession.getSession().getSystemUser().getEmail())) {
            Commands.startUpdateEmail(handler, userDTO, txtEmail.getText().toString().trim());
        }
        if (!txtPassword.getText().toString().equals(AppSession.getSession().getSystemUser().getPassword())) {
            Commands.startUpdatePassword(handler, userDTO, txtPassword.getText().toString().trim());
        }
        if (!txtNickname.getText().toString().equals(profileDto.getNickname())) {
            profileDto.setNickname(txtNickname.getText().toString());
            updateUser();
        }
        if (!txtAdress.getText().toString().equals(profileDto.getAddress())) {
            profileDto.setAddress(txtAdress.getText().toString());
            updateUser();
        }
    }

    String selectedImagePath = "";
    byte[] bitmapdata;

    private void startCropImage(Uri imgUri) {
        CropImage.activity(imgUri)
                .setMinCropResultSize(100, 100)
                .setAspectRatio(1, 1)
                .setRequestedSize(300, 300)
                //.setMaxCropResultSize(400,400)
                .start(getActivity());
    }

    //Обработка результата выбора аватара
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = null;
            if (requestCode == REQUEST_CAMERA) {
                activity.showProgress();
                bitmap = (Bitmap) data.getExtras().get("data");
//                try {
//                    Uri imgUri = ImageUtils.getImageUri(getActivity(), bitmap);
//                    startCropImage(imgUri);
//                } catch (Exception e) {e.printStackTrace();}
                Commands.updateAvatarCommand(handler, userDTO, bitmap);
            } else if (requestCode == SELECT_FILE) {
                Uri imgUri = data.getData();
                startCropImage(imgUri);
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {
                    activity.showProgress();
                    Uri resultUri = result.getUri();
                    Log.d(TAG, "Cropped URI = " + resultUri == null ? "null" : resultUri.getPath());
                    Commands.updateAvatarCommand(handler, userDTO, ImageUtils.decodeBitmapFromFile(getContext(), resultUri));
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    error.printStackTrace();
                    activity.hideProgress();
                }
            }



//                Log.d("!!!!!IMAGE1", selectedImageUri.getEncodedPath());
//                String[] projection = {MediaColumns.DATA};
//                Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, projection, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
//                cursor.moveToFirst();
//                selectedImagePath = cursor.getString(column_index);
//                Log.d("!!!!!IMAGE2", selectedImagePath);
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(selectedImagePath, options);
//                final int REQUIRED_SIZE = 200;
//                int scale = 1;
//                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
//                    scale *= 2;
//                options.inSampleSize = scale;
//                options.inJustDecodeBounds = false;
                //bitmap = BitmapFactory.decodeFile(selectedImagePath, options);



//            }
        }
    }

    //Обработка результатов диалогов
    @Override
    public void onEvent(int eventType, View control, Object data) {
        switch (eventType) {
            case TYPE_DIALOG_CHOICE_BIRTHDAY:
                if (dialogChoice != null) {
                    txtBirthYear.setText((String) data);
                    dialogChoice.dismiss();
                    dialogChoice = null;
                    profileDto.setBirthday(Integer.parseInt((String) data)); //INTEGER???
                    updateUser();
                }
                break;
            case TYPE_DIALOG_CHOICE_CITY:
                if (dialogChoice != null) {
                    CityDTO city = (CityDTO) data;
                    btnCity.setText(city.getName());
                    dialogChoice.dismiss();
                    dialogChoice = null;
                    profileDto.setCity(city);
                    updateUser();
                }
                break;
            case TYPE_DIALOG_DATE:
                if (dialogDate != null) {
                    String date = (String) data;
                    String[] split = date.split("-");
                    date = split[0] + getString(R.string.text_year) + " " + split[1] + getString(R.string.text_month) + " " + split[2] + getString(R.string.text_day);
                    txtLastMenstrDate.setText(date);
                    dialogDate.dismiss();
                    dialogDate = null;
                    profileDto.setLast_cycle((String) data);
                    updateUser();
                }
                break;
            case TYPE_DIALOG_DAY:
                if (dialogDate != null) {
                    int date = (int) data;
                    String strDate = String.valueOf(date) + getString(R.string.text_day);
                    txtCycleLength.setText(strDate);
                    dialogDate.dismiss();
                    dialogDate = null;
                    profileDto.setMenstrual_cycle(date);
                    updateUser();
                }
                break;
            case TYPE_DIALOG_RED_DAY:
                if (dialogDate != null) {
                    int date = (int) data;
                    String strDate = String.valueOf(date) + getString(R.string.text_day);
                    txtRedDays.setText(strDate);
                    dialogDate.dismiss();
                    dialogDate = null;
                    profileDto.setRed_days(date);
                    updateUser();
                }
                break;
            case TYPE_DIALOG_BMI:
                if (dialogDate != null) {
                    String[] values = ((String) data).split("-");
                    profileDto.setWeight(Double.parseDouble(values[0]));
                    profileDto.setHeight(Double.parseDouble(values[1]));
                    txtBMI.setText(String.valueOf(profileDto.getBmi()));
                    dialogDate.dismiss();
                    dialogDate = null;
                    updateUser();
                }
                break;
            default:
                break;
        }
    }

    //Gender dialod
    private void showDialogGender() {
        OnClickListener yesButtonListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGender.setText(R.string.text_male);
                profileDto.setGender(true);
                updateUser();
            }
        };
        OnClickListener noButtonListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGender.setText(R.string.text_female);
                profileDto.setGender(false);
                updateUser();
            }
        };
        DialogUtilities.showMaleFemaleDialog(getActivity(), yesButtonListener, noButtonListener, R.string.text_male, R.string.text_female);
    }
    //Birthday dialog
    private void showDialogBirthday() {
        if (dialogChoice != null && dialogChoice.isShowing())
            return;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int size = year - 1970;
        String[] listYear = new String[size + 1];
        for (int i = 1970, j = 0; i <= year; i++, j++) {
            listYear[j] = i + "";
        }
        String strYear = (profileDto.getBirthYear() > 0) ? profileDto.getBirthYear() + "" : "1970";
        dialogChoice = DialogUtilities.createListChoiceDialog(getActivity(), listYear, strYear, this, TYPE_DIALOG_CHOICE_BIRTHDAY);
        dialogChoice.show();
    }
    //List city dialog
    private void showDialogCity(List<CityDTO> listCity) {
        if (dialogChoice != null && dialogChoice.isShowing())
            return;
        Long myCityID = (profileDto.getCity() != null && profileDto.getCity().getId() != null) ? profileDto.getCity().getId() : null;
        dialogChoice = DialogUtilities.createListCityDialog(getActivity(), listCity, myCityID, this, TYPE_DIALOG_CHOICE_CITY);
        dialogChoice.show();
    }

    //Last menstr date dialog
    private void showDialogLastMenstrDate() {
        if (dialogDate != null && dialogDate.isShowing())
            return;
        dialogDate = DialogUtilities.createDatePickerDialog(getActivity(), this, TYPE_DIALOG_DATE);
        dialogDate.show();
    }
    //BMI dialog
    private void showDialogBMI() {
        if (dialogDate != null && dialogDate.isShowing())
            return;
        dialogDate = DialogUtilities.createBMIDialog(getActivity(), this, TYPE_DIALOG_BMI);
        dialogDate.show();
    }

    //Cycle length dialog
    private void showDialogCycle() {
        if (dialogDate != null && dialogDate.isShowing())
            return;
        dialogDate = DialogUtilities.createDayPickerDialog(getActivity(), this, TYPE_DIALOG_DAY);
        dialogDate.show();
    }
    //Red days dialog
    private void showDialogRedday() {
        if (dialogDate != null && dialogDate.isShowing())
            return;
        dialogDate = DialogUtilities.createRedDayPickerDialog(getActivity(), this, TYPE_DIALOG_RED_DAY);
        dialogDate.show();
    }

    //	@Override
//	public void onResume() {
//		super.onResume();
//		addActions();
//		((BaseActivity) getActivity()).showProgress();
//		ListCityCommand.start(getActivity());
//	}
//
//	@Override
//	public void onPause() {
//		isClick = false;
//		removeActions();
//		super.onPause();
//	}
//
    private void updateUser() {
        Commands.updateUserProfile(handler, userDTO, profileDto);
    }
}
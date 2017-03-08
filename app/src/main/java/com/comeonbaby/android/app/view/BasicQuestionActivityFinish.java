package com.comeonbaby.android.app.view;

/**
 * Created by olegs on 20.12.2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.common.DialogUtilities;
import com.comeonbaby.android.app.db.dto.UserDTO;
import com.comeonbaby.android.app.requests.commands.Commands;
import com.comeonbaby.android.app.utils.AppSession;
import com.comeonbaby.android.app.utils.PrefsHelper;
import com.comeonbaby.android.app.view.customview.ButtonCustom;
import com.comeonbaby.android.app.view.customview.TextViewCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BasicQuestionActivityFinish extends BaseActivity implements OnClickListener {
    boolean backBtnEnabled; //uses to prevent back button use when saving progress started
    JSONObject jsonObject;  //final json
    UserDTO userDTO;
    Handler handler;

    @Override
    protected void onCreateContent(Bundle savedInstanceState) {
        setContentView(R.layout.activity_basic_question_finish);
        initObjectUI();
        userDTO = AppSession.getSession().getSystemUser();

        backBtnEnabled = true;
        setupHideKeyboard(findViewById(R.id.layoutRootQuestion));
    }

    private void initObjectUI() {
        ((TextViewCustom) findViewById(R.id.txtTitle)).setText(R.string.text_title_basic_question_finish);
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        float sp = ((ButtonCustom) findViewById(R.id.btnButtonFinishStep)).getTextSize() / scaledDensity;
        ((TextViewCustom) findViewById(R.id.txtTitle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        ((ImageView) findViewById(R.id.imgBack)).setVisibility(View.GONE);
        ((ButtonCustom) findViewById(R.id.btnButtonFinishStep)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnButtonFinishStep:
                saveAndSendQuestionsData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(backBtnEnabled) super.onBackPressed();
    }

    //This method generates JSON string of all answers and call pushQuestionsDataToServer() method if all ok
    private void saveAndSendQuestionsData() {
        stateAllButtons(false);                                 //Disable all button when saving data
        showProgress();

        PrefsHelper prefsHelper = PrefsHelper.getPrefsHelper();
        String str = ((String) prefsHelper.getPref(PrefsHelper.PREF_BASIC_ANSWERS_3));
        Log.e("!!!!!!!!B_Q_FINISH:STR:", str);

        jsonObject = null;

        try {
            jsonObject = new JSONObject(str);
            jsonObject.put("user_id", userDTO.getSystemID().toString());
            Log.e("!!!!!!!!B_Q_FINISH:JSON", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            moveToBasic01AfterError();
            return;
        }
        if (!pushQuestionsDataToServer()) {
            DialogUtilities.showNoNetworkDialog(BasicQuestionActivityFinish.this);
            return;
        }
        launchMainActivity();
    }

    //Try to push JSON to server
    private boolean pushQuestionsDataToServer() {
        PrefsHelper.getPrefsHelper().savePref(PrefsHelper.PREF_BASIC_QUESTIONS_COMPLETED, new Boolean(true));

        AppSession.getSession().getSystemUser().getProfileDTO().setIs_finish_question(true);
        Log.d("!!!Answers", jsonObject.toString());

        //Отправка на сервер //TODO Отправка на сервер BQ;
        Commands.uploadBasicQuestion(userDTO, jsonObject);

        return true;
    }

    private void stateAllButtons(boolean value) {
        ((ButtonCustom) findViewById(R.id.btnButtonFinishStep)).setEnabled(value);
        backBtnEnabled = value;                        //Disable back button use
    }

    //Launch next activity and clear activities stack
    private void launchMainActivity() {
        Intent intent = new Intent(this, ProfileConfigActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //If some error move user to BasicQuestions01 to try againe and clear activities stack
    private void moveToBasic01AfterError() {
        Intent intent = new Intent(this, BasicQuestionActivity01.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("AFTER_ERR", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
    }
}
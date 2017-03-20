package com.comeonbaby.android.app.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.comeonbaby.android.R;
import com.comeonbaby.android.app.db.dto.CityDTO;
import com.comeonbaby.android.app.db.dto.NotesHolder;
import com.comeonbaby.android.app.db.dto.ProfileDTO;
import com.comeonbaby.android.app.db.dto.UserDTO;
import com.comeonbaby.android.app.requests.Constants;
import com.comeonbaby.android.app.requests.ExtraConstants;
import com.comeonbaby.android.app.requests.commands.Commands;
import com.comeonbaby.android.app.utils.AppSession;
import com.comeonbaby.android.app.utils.PrefsHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


/**
 * Created by olegs on 16.12.2016.
 */

public class ChooseLoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ChooseLoginActivity";
    AppSession appSession;
    UserDTO user;
    ProfileDTO profile;
    private SessionCallback callback;
    private Handler handler;
    Dialog noNetwork;
    public static boolean isSuccess = false;
    private final int REQUEST_CODE = 123;

    // Facebook
    private LoginButton btnLoginFacebook;
    private CallbackManager callbackManager;
    private Button btnLoginEmail;
    private Button btnRegisterUser;

    //Метод для автоматического входа в аккаунт, используя информацию предыдущего входа
    private void autoLoginAction() {
        Log.d(TAG, "Autologin Action Started");
        if (appSession == null) return;
        if(PrefsHelper.getPrefsHelper().getPref((PrefsHelper.PREF_REMEMBER_ME), false) && user.getLoginType() != null) {
            switch (user.getLoginType()) {
                case Constants.LOGIN_KAKAO:
                case Constants.LOGIN_FACEBOOK: {
                    Log.d(TAG, "Try to login via " + user.getLoginType());
                    if(user.getSocialID() == null) break;
                    Commands.startLoginSocial(handler, user.getLoginType(), user.getEmail(), user.getSocialID());
                    break;
                }
                case Constants.LOGIN_EMAIL: {
                    Log.d(TAG, "Try to login via " + user.getLoginType());
                    if(user.getEmail() == null || user.getPassword() == null) break;
                    Commands.startLoginEmail(handler, user.getEmail(), user.getPassword());
                    break;
                }
                default: {
                    hideProgress();
                    Log.d(TAG, "No previous login");
                }
            }
        } else {
            hideProgress();
            Log.d(TAG, "Autologin prefence disabled. Continue...");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgress();
        if(noNetwork != null) {
            noNetwork.dismiss();
            noNetwork = null;
        }
        if(!AppSession.isLogined) {
            autoLoginAction();
        } else {
            hideProgress();
        }
    }

    @Override
    protected void onCreateContent(Bundle savedInstanceState) {
        appSession = AppSession.getSession();  //сессия
        user = appSession.getSystemUser();
        profile = user.getProfileDTO();
        setContentView(R.layout.activity_choose_login);
        initHandler();
        initFacebook();
        initKakao();
        initEmail();
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Bundle data = msg.getData();
                String message = "";
                JSONObject user = null, jsdata = null;
                if(data.containsKey(ExtraConstants.MESSAGE)) message = data.getString(ExtraConstants.MESSAGE);
                if(msg.what != Constants.MSG_ERROR) showSnackMessage(message);
                switch (msg.what) {
                    //Success login
                    case Constants.MSG_LOGIN_EMAIL_SUCCESS:
                    case Constants.MSG_LOGIN_SOCIAL_SUCCESS:
                        Log.d(TAG, "LOGIN SOCIAL EMAIL SUCCESS");
                        CityDTO.updateCityList();
                    case Constants.MSG_UPDATE_PROFILE_SUCCESS: {
                        //Обновляем список городов
                        try {
                            if (data.containsKey(ExtraConstants.USER) && data.containsKey(ExtraConstants.DATA)) {
                                user = new JSONObject(data.getString(ExtraConstants.USER));
                                jsdata = new JSONObject(data.getString(ExtraConstants.DATA));
                            }
                        } catch (JSONException exc) {exc.printStackTrace();}
                        if(user != null && jsdata != null) {
                            AppSession.getSession().getSystemUser().setFromJSON(user.toString());
                            AppSession.getSession().getSystemUser().getProfileDTO().parseFromJson(jsdata.toString());
                            NotesHolder.updateNotes();
                            AppSession.isLogined = true;
                            AppSession.saveRememberMe(true);
                            AppSession.getSession().save();
                            startNextActivity();
                        } else {
                            AppSession.getSession().closeAndClear();
                            showSnackMessage(Constants.ERROR_MESSAGE_UNKNOWN);
                        }
                        break;
                    }
                    //Unsuccessful login
                    case Constants.MSG_LOGIN_EMAIL_FAIL:
                    case Constants.MSG_LOGIN_SOCIAL_FAIL:
                    case Constants.MSG_UPDATE_PROFILE_FAIL: {
                        AppSession.getSession().closeAndClear();
                        break;
                    }
                    //No connection
                    case Constants.MSG_ERROR: {
                        //noNetwork = DialogUtilities.showNoNetworkDialog(ChooseLoginActivity.this);
                        //AppSession.getSession().closeAndClear();
                        showSnackMessage(Constants.ERROR_MESSAGE_NO_CONNECTION);
                        break;
                    }
                }
                hideProgress();
            }
        };
    }

    //Всплывающее сообщение
    private void showSnackMessage(String msg) {
        Snackbar.make(findViewById(R.id.layoutRootLogin), msg, Snackbar.LENGTH_LONG).show();
    }

    private void initFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext()); // Facebook session
        callbackManager = CallbackManager.Factory.create();  // CallbackManager Facebook
        btnLoginFacebook = (LoginButton)findViewById(R.id.btnLoginFacebook);
        //btnLoginFacebook.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        btnLoginFacebook.setReadPermissions(Arrays.asList("public_profile", "email"));
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("FACEBOOK", "ON SUCCESS!!!!!!!!!!!!!");
                        showProgress();
                        UserDTO userDTO = new UserDTO();
                        // Application code
                        try {
                                Long socialID = object.getLong("id");
                                if(socialID != null) {
                                    userDTO.setSocialID(socialID);
                                    user.getProfileDTO().setNickname(socialID+"");
                                }

//                            //Get name from facebook
//                                String name = object.getString("name");
//                                if (name != null && !name.equals("")) {
//                                    Log.d("Nickname", name);
//                                    userDTO.getProfileDTO().setNickname(name);
//                                }

                            //Get email from facebook
                                String email = object.getString("email");
                                if (email != null && !email.equals("")) {
                                    userDTO.setEmail(email);
                                }
                            //Get gerne from facebook
                            String gender = object.getString("gender");
                            if (gender != null) {
                                if (gender.equals("male")) profile.setGender(true);
                                else if (gender.equals("femail")) profile.setGender(false);
                            }
                            Log.d("FaceBook", object.toString());
                            Commands.startLoginSocial(handler, Constants.LOGIN_FACEBOOK, userDTO.getEmail(), userDTO.getSocialID());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("GraphResponse", "-------------" + response.toString());
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender,birthday,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        });
    }

    private void initKakao() {
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩 ~
            }
        });
    }

    private void initEmail(){
        btnLoginEmail = (Button)findViewById(R.id.btnLoginApp);
        btnLoginEmail.setOnClickListener(this);
        btnRegisterUser = (Button)findViewById(R.id.btnRegisterUser);
        btnRegisterUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginApp: {
                //Intent intent = new Intent(ChooseLoginActivity.this, LoginMainActivity.class);
                //startActivity(intent);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                leaveActivity(ChooseLoginActivity.this, LoginActivity.class.toString(), false, false);
                //finish();
                break;
            }
            case R.id.btnRegisterUser: {
                leaveActivity(ChooseLoginActivity.this, CreateAccountActivity.class.toString(), false, false);
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {  //kakaotalk
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);  //facebook

        //Возврат от PrivacyTermsActivity
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            showProgress();
            user.getProfileDTO().setIs_agreement(true);
            Commands.updateUserProfile(handler, user, profile);
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_CANCELED) {
            hideProgress();
            AppSession.getSession().closeAndClear();
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {

    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    if (!AppSession.isKakaoSuccess) {
                        AppSession.isKakaoSuccess = true;
                        //if(baseActivity != null && baseActivity.hasWindowFocus()) showProgress();
                        Log.e("Kakao UserProfile", userProfile.toString());
                        user.setSocialID(userProfile.getId());

                        if (user.getProfileDTO().getNickname() == null || user.getProfileDTO().getNickname().equals("")) {
                            String name = userProfile.getNickname();
                            if (name != null && !name.equals("")) {
                                Log.d("Nickname", name);
                                user.getProfileDTO().setNickname(name);
                            } else {
                                user.getProfileDTO().setNickname(userProfile.getId()+"");
                            }
                        }
                        Commands.startLoginSocial(handler, Constants.LOGIN_KAKAO, user.getEmail(), user.getSocialID());
                    }
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
        }
    }

    //Переброс на договор, если ранее не принимался
    private boolean checkTermPolicy() {
        if(user.getProfileDTO() == null) user.setProfileDTO(new ProfileDTO());
        if(user.getProfileDTO().isIs_agreement()) {
            return true;
        } else {
            leaveActivityForResult(ChooseLoginActivity.this, TermPolicyActivity.class.toString(), false, false, null, null, REQUEST_CODE);
            return false;
        }
    }

    void startNextActivity(){
        hideProgress();
        baseActivity.hideProgress();
        if(checkTermPolicy()) {
            Intent intent;
            if(profile.isIs_finish_question()) {
                intent = new Intent(ChooseLoginActivity.this, MainActivity.class);
            } else {
                intent = new Intent(ChooseLoginActivity.this, BasicQuestionActivity01.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}

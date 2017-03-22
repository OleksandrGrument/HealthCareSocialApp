package com.comeonbabys.android.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.db.dto.NotesHolder;
import com.comeonbabys.android.app.requests.Constants;
import com.comeonbabys.android.app.utils.AppSession;
import com.comeonbabys.android.app.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SplashActivity extends BaseActivity {

    private final long MIN_DELAY = 2000L;
    private Thread thrGetNotes;

    //Временная переменная для перехода на нужный активити
    //private Class startNextActivity = LoginActivity.class;
    Class nextActivity = ChooseLoginActivity.class;
//    Class nextActivity = LoginMainActivity.class;


    @Override
    protected int getContentViewID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onBeforeCreate(Bundle savedInstanceState) {
        super.onBeforeCreate(savedInstanceState);
    }

    @Override
    protected void onCreateContent(Bundle savedInstanceState) {
        super.onCreateContent(savedInstanceState);
    }

    @Override
    protected void onAfterCreate(Bundle savedInstanceState) {
        //initImageLoader();
        ImageLoader.getInstance().init(ImageUtils.getImageLoaderConfiguration(getApplicationContext()));
        showProgress();

        //Init sugar
        //NoteDTO.findById(NoteDTO.class, 1L);

        thrGetNotes = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //----------getNotesFromDB();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        thrGetNotes.setDaemon(true);
        thrGetNotes.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if(thrGetNotes.isAlive()) thrGetNotes.join();
                } catch (InterruptedException e) {}
                hideProgress();
                launchMainActivity();
            }
        }, MIN_DELAY);
    }

    //Переход на след активити с эффектами и очисткой стека
    private void launchMainActivity() {
        Intent intent = new Intent(SplashActivity.this, nextActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent);
        finish();
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {

    }

    private void getNotesFromDB() {
        String loginType = AppSession.getSession().getSystemUser().getLoginType();
        if(loginType == null) return;
        switch (loginType) {
            case Constants.LOGIN_FACEBOOK:
            case Constants.LOGIN_KAKAO:
            case Constants.LOGIN_EMAIL: {
                NotesHolder.getInstanse();
                break;
            }
            default: {
                //TODO
            }
        }
    }

}
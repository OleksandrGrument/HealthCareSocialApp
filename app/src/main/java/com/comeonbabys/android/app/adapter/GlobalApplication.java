package com.comeonbabys.android.app.adapter;


import android.app.Activity;
import android.util.Log;

import com.kakao.auth.KakaoSDK;
import com.orm.SugarApp;

public class GlobalApplication extends SugarApp {
    private static volatile GlobalApplication obj = null;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        obj = this;
        KakaoSDK.init(new KakaoSDKAdapter());
        Log.d(" XXXXXXXXXXXXX","XXXXXXXXXX");
        Log.d("----INIT-----","------KAKAOSDK init  --------");
        Log.d(" XXXXXXXXXXXXX","XXXXXXXXXX");
    }

    public static GlobalApplication getGlobalApplicationContext() {
        return obj;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }
}


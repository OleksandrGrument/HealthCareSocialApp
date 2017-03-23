package com.comeonbabys.android.app.adapter;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.comeonbabys.android.app.view.SplashActivity;
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
        Log.d(" XXXXXXXXXXXXX", "XXXXXXXXXX");
        Log.d("----INIT-----", "------KAKAOSDK init  --------");
        Log.d(" XXXXXXXXXXXXX", "XXXXXXXXXX");
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                Log.i("GLOBAL", "KILL");

                Intent intent = new Intent(GlobalApplication.this, SplashActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);


                PendingIntent pendingIntent = PendingIntent.getActivity(
                        GlobalApplication.getGlobalApplicationContext().getBaseContext(), 0, intent, intent.getFlags());

                AlarmManager mgr = (AlarmManager) GlobalApplication.getGlobalApplicationContext().getBaseContext()
                        .getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() +500,
                        pendingIntent);

                System.exit(1);


            }
        });
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


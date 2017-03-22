package com.comeonbabys.android.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

//Сохранение параметров приложения
public class PrefsHelper {
    public static final String PREF_BASIC_ANSWERS_1 = "basic_answers1";
    public static final String PREF_BASIC_ANSWERS_2 = "basic_answers2";
    public static final String PREF_BASIC_ANSWERS_3 = "basic_answers3";
    public static final String PREF_BASIC_QUESTIONS_COMPLETED = "basic_questions_completed"; //Boolean
    public static final String PREF_SESSION_TOKEN = "session_token";        //session token
    public static final String PREF_REMEMBER_ME = "remember_me";            //запомнить данные входа
    public static final String SHARED_PREF_RUN_FIRST = "run_first";         //при первом запуске отображать окно помощи
    public static final String SHARED_PREF_TAB_COMEON = "tab_comeon";       //видимость вкладок инициализируется в AppSession
    public static final String SHARED_PREF_TAB_COMMUNITY = "tab_community"; //видимость вкладок инициализируется в AppSession
    public static final String SHARED_PREF_TAB_REPORT = "tab_report";       //1 или 2 (1 - текущая вкладка - месяц)
    public static final String SHARED_PREF_TOGGLE_PUSH = "toggle_push";
//    public static final String SHARED_PREF_CITY = "city";
//    public static final String SHARED_PREF_WEIGTH = "weight";
//    public static final String SHARED_PREF_HEIGHT = "height";
//    public static final String SHARED_PREF_GENDER = "gender";
//    public static final String SHARED_PREF_ADDRESS = "address";
//    public static final String SHARED_PREF_MENSTRUAL_CYCLE = "menstrual_cycle";
//    public static final String SHARED_PREF_RED_DAYS = "red_days";
//    public static final String SHARED_PREF_LAST_CYCLE = "last_cycle";
//    public static final String SHARED_PREF_BIRTHDAY = "birthday";
//    public static final String SHARED_PREF_NICKNAME = "nickname";

    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_USER_EMAIL = "email";
    public static final String PREF_USER_PASSWORD = "password";
    public static final String PREF_USER_SOCIAL_ID = "social_id";
    public static final String PREF_LOGIN_TYPE = "type_login";              //тип входа


    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private static PrefsHelper instance;

    //Always returns one instance of PrefsHelper
    public static PrefsHelper getPrefsHelper() {
        return instance;
    }

    //Инициализируется в BaseActivity в методе onCreate
    public PrefsHelper(Context context) {
        instance = this;
        String prefsFile = context.getPackageName();
        sharedPreferences = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //Saves prefence to storage
    public void savePref(String key, Object value) {
        delete(key);
        Log.d("PrefsHelper", "Save prefence. Key: " + key + " Value: " + value);
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            editor.putString(key, value.toString());
        } else if (value != null) {
            throw new RuntimeException("Attempting to save non-primitive preference");
        }
        editor.commit();
    }

    //Removes specified key from storage
    public void delete(String key) {
        if (sharedPreferences.contains(key)) {
            editor.remove(key).commit();
        }
    }

    //Returns prefence value if key exist or null
    @SuppressWarnings("unchecked")
    public <T> T getPref(String key) {
        return (T) sharedPreferences.getAll().get(key);
    }

    //Returns prefence value if key exist or default value if not
    @SuppressWarnings("unchecked")
    public <T> T getPref(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    //Returns true if specified key contains in prefences
    public boolean isPrefExists(String key) {
        return sharedPreferences.contains(key);
    }

    //Clear all prefences from storage
    public void deleteAll() {
        editor.clear();
        editor.commit();
    }


    public static final String PREF_EMAIL_PHONE_LOGIN = "email_phone_login";
    public static final String PREF_USER_FULL_NAME = "full_name";
    public static final String PREF_SESSION_SYSTEM_TOKEN = "session_system_token";
    public static final String PREF_USERNAME = "username";
    public static final int NOT_INITIALIZED_VALUE = -1;
    public static final String PREF_STATUS = "status";
    public static final String SHARED_PREF_STATE = "state";


}
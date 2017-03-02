package com.comeonbaby.android.app.utils;

import android.util.Log;

import com.comeonbaby.android.app.db.dto.NoteDTO;
import com.comeonbaby.android.app.db.dto.NotesHolder;
import com.comeonbaby.android.app.db.dto.UserDTO;
import com.facebook.login.LoginManager;
import com.kakao.auth.Session;

import java.io.Serializable;

public class AppSession implements Serializable {

	private static final long serialVersionUID = -3494043991838529139L;
	private static final Object lock = new Object();
    private static final String TAG = "AppSession";
	private static AppSession activeSession;
	private UserDTO systemUser;
    public static boolean isLogined = false;
    public static boolean isKakaoSuccess = false;

    public UserDTO getSystemUser() {return systemUser;}
    public void setSystemUser(UserDTO system_user) {this.systemUser = system_user;}


    //Используем метод startNewSession() для создания сессии
	private AppSession(UserDTO systemUser) {
		this.systemUser = systemUser;
	}

    //Возвращает текущую сессию или создает новую если нет
    public static AppSession getSession() {
        if (activeSession == null) {
            activeSession = AppSession.load();
        }
        return getActiveSession();
    }

    //Возвращает текущую сессию
	private static AppSession getActiveSession() {
		synchronized (lock) {
			return activeSession;
		}
	}
    //Загрузить сессию
	private static AppSession load() {
		Log.d(TAG, "CREATE NEW APP SESSION");
		UserDTO systemUser = getUserFromPrefs(PrefsHelper.getPrefsHelper());
        Log.d(TAG, "Loaded user from prefs: " + systemUser.toString());
        activeSession = new AppSession(systemUser);
		return activeSession;
	}

    //Создание новой сессии
    public static void startNewSession(UserDTO system_user) {
        activeSession = new AppSession(system_user);
    }

    //Сохранение в настройки опции запоминания входа
	public static void saveRememberMe(boolean value) {
		PrefsHelper.getPrefsHelper().savePref(PrefsHelper.PREF_REMEMBER_ME, value);
	}

	public static boolean isSessionExistOrNotExpired(long expirationTime) {
		return false;
	}

    //Очистить настройки сессии
	public void closeAndClear() {
        Log.d(TAG, "Close session and clear prefences");
        clearUserPrefs(PrefsHelper.getPrefsHelper());
        PrefsHelper.getPrefsHelper().deleteAll();
        Session.getCurrentSession().removeAccessToken();
        Session.getCurrentSession().close();
        LoginManager.getInstance().logOut();
        isKakaoSuccess = false;
        isLogined = false;
		NoteDTO.deleteAll(NoteDTO.class);
		NotesHolder.newInstance();
		activeSession = load();

	}

    //Вызывается в LoginActivity для включения отображения вкладок внизу?
	public void initPrefsHelper(){
		PrefsHelper helper = PrefsHelper.getPrefsHelper();
		helper.savePref(PrefsHelper.SHARED_PREF_TAB_COMEON, 1);
		helper.savePref(PrefsHelper.SHARED_PREF_TAB_COMMUNITY, 1);
		helper.savePref(PrefsHelper.SHARED_PREF_TAB_REPORT, 1);
	}

	public void save() {
		saveUserToPrefs(PrefsHelper.getPrefsHelper(), getSystemUser());
	}

	public void updateUser(UserDTO newUser) {
		systemUser.setSystemID(newUser.getSystemID());
        saveUserToPrefs(PrefsHelper.getPrefsHelper(), systemUser);
	}

	private static void saveUserToPrefs(PrefsHelper prefsHelper, UserDTO user) {
        Log.d(TAG, "Start save user to preferences... " + user.toString());
        clearUserPrefs(PrefsHelper.getPrefsHelper());
        if(user.getSystemID() != null) prefsHelper.savePref(PrefsHelper.PREF_USER_ID, user.getSystemID());
        if(user.getLoginType() != null) prefsHelper.savePref(PrefsHelper.PREF_LOGIN_TYPE, user.getLoginType());
        if(user.getEmail() != null) prefsHelper.savePref(PrefsHelper.PREF_USER_EMAIL, user.getEmail());
        if(user.getPassword() != null) prefsHelper.savePref(PrefsHelper.PREF_USER_PASSWORD, user.getPassword());
        if(user.getSocialID() != null) prefsHelper.savePref(PrefsHelper.PREF_USER_SOCIAL_ID, user.getSocialID());
        Log.d(TAG, "Save user to preferences complete");
	}

	private static UserDTO getUserFromPrefs(PrefsHelper prefsHelper) {
        Log.d(TAG, "Get user from preferences start...");
		UserDTO user = new UserDTO();
		if(prefsHelper.isPrefExists(PrefsHelper.PREF_USER_ID)) user.setSystemID(prefsHelper.<Long> getPref(PrefsHelper.PREF_USER_ID));
        if(prefsHelper.isPrefExists(PrefsHelper.PREF_USER_SOCIAL_ID)) user.setSocialID(prefsHelper.<Long> getPref(PrefsHelper.PREF_USER_SOCIAL_ID));
        if(prefsHelper.isPrefExists(PrefsHelper.PREF_USER_EMAIL)) user.setEmail(prefsHelper.<String>getPref(PrefsHelper.PREF_USER_EMAIL));
        if(prefsHelper.isPrefExists(PrefsHelper.PREF_USER_PASSWORD)) user.setPassword(prefsHelper.<String> getPref(PrefsHelper.PREF_USER_PASSWORD));
        if(prefsHelper.isPrefExists(PrefsHelper.PREF_LOGIN_TYPE)) user.setLoginType(prefsHelper.<String> getPref(PrefsHelper.PREF_LOGIN_TYPE));
		return user;
	}

    private static void clearUserPrefs(PrefsHelper prefsHelper) {
        prefsHelper.delete(PrefsHelper.PREF_USER_ID);
        prefsHelper.delete(PrefsHelper.PREF_USER_SOCIAL_ID);
        prefsHelper.delete(PrefsHelper.PREF_USER_EMAIL);
        prefsHelper.delete(PrefsHelper.PREF_USER_PASSWORD);
        prefsHelper.delete(PrefsHelper.PREF_LOGIN_TYPE);
    }
}

package com.comeonbaby.android.app.requests;


public class Constants {

    //public static final String BASE_URL = "http://89.223.27.239:8080/ComeOnBaby/"; // Базовый домен по которому идёт запрос (можно указывать полный путь, тогда опускать анотацию в интерфейсе)
    public static final String BASE_URL = "http://192.168.0.32:8080/";
    //public static final String BASE_URL = "http://10.0.2.2:8080/";

    public static final String IMAGES_URL = BASE_URL + "images/";

    public static final String SECRET = "U6e33rAjBt";

    public final static String SOCIAL_OPERATION = "loginsocial";
    public static final String REGISTER_OPERATION = "registration"; // Константы для опираций
    public static final String LOGIN_OPERATION = "loginemail"; // авторизация
    public static final String CHANGE_PASSWORD_OPERATION = "chgPass"; //константа смена пароля
    public static final String UPDATE_EMAIL_OPERATION = "emailupdate"; //константа смена мыла
    public static final String UPDATE_PASSWORD_OPERATION = "passwordupdate"; //константа смена мыла
    public static final String LIST_CITIES_OPERATION = "listcities";
    public static final String GET_PROFILE_OPERATION = "getprofile";
    public static final String UPDATE_PROFILE_OPERATION = "updateprofile";

    public static final String UPLOAD_BASIC_QUESTION = "uploadbasicquestion";
    public static final int MSG_UPLOAD_BASIC_QUESTION_FAIL = 666;
    public static final int MSG_UPLOAD_BASIC_QUESTION_SUCCESS = 111;



    public static final String DELETE_USER_OPERATION = "deleteUser";


    public static final String SAVE_NOTE_OPERATION = "savenote";
    public static final String GET_NOTES_OPERATION = "getnotes";
    public static final String GET_GUIDE_OPERATION = "getguide";
    public static final String GET_RECIPE_OPERATION = "getrecipe";
    public static final String UPDATE_AVATAR_OPERATION = "updateavatar";
    public static final String SAVE_COMUNITY_RECORD_OPERATION = "saverecord";
    public static final String EDIT_COMUNITY_RECORD_OPERATION = "editrecord";
    public static final String GET_COMUNITY_RECORDS_OPERATION = "getrecords";
    public static final String GET_Q_A_OPERATION = "getqa";
    public static final String SAVE_QA_OPERATION = "saveqa";

    public static final int MSG_EDIT_COMMUNITY_SUCCESS = 43;
    public static final int MSG_EDIT_COMMUNITY_FAIL = 44;

    public static final String SAVE_COMMENT_OPERATION = "savecomment";
    public static final String GET_COMMENTS_OPERATION = "getcomments";
    public static final String GET_NOTICES_OPERATION = "getnotices";
    public static final String DELETE_COMUNITY_RECORD_OPERATION = "deleterecord";
    public static final String DELETE_Q_A_RECORD_OPERATION = "delete-q-a";

    //Broadcasts
    public static final String GET_NOTES_SUCCESS_ACTION = "getnotessuccess";

    public static final String SUCCESS = "success"; // ответ ПОДТВЕРЖДЕНО
    public static final String FAILURE = "failure"; // ответ ОШИБКА
    public static final String IS_LOGGED_IN = "isLoggedIn"; // ответ "ВОШЕЛ"

    public static final String NICKNAME = "nickname";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String UNIQUE_ID = "id";
    public static final String NEW_EMAIL = "newemail";
    public static final String NEW_PASSWORD = "newpassword";
    public static final String PROFILE = "profile";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String AVATAR = "avatar";

    public static final String TAG = "ComeOnBaby"; // Подпись авторизации в активити
    public static final String LOGIN_EMAIL = "EMAIL"; // Подпись авторизации в активити
    public final static String LOGIN_KAKAO = "KAKAO"; // Подпись авторизации в активити
    public final static String LOGIN_FACEBOOK = "FACEBOOK"; // Подпись авторизации в активити

    //ERRORS
    public static final String ERROR_MESSAGE_UNKNOWN = "Error! Something wrong.";
    public static final String ERROR_MESSAGE_NO_CONNECTION = "No server connection";
    public static final String ERROR_MESSAGE_NULL_VALUE = "Internal error! Required value NULL";

    //MESSAGE "WHAT" VALUES
    public static final int MSG_ERROR = 999;
    public static final int MSG_EMAIL_UPDATE_SUCCESS = 1;
    public static final int MSG_EMAIL_UPDATE_FAIL = 2;
    public static final int MSG_PASSWORD_UPDATE_SUCCESS = 3;
    public static final int MSG_PASSWORD_UPDATE_FAIL = 4;
    public static final int MSG_REGISTER_USER_SUCCESS = 5;
    public static final int MSG_REGISTER_USER_FAIL = 6;
    public static final int MSG_LOGIN_EMAIL_FAIL = 7;
    public static final int MSG_LOGIN_EMAIL_SUCCESS = 8;
    public static final int MSG_LOGIN_SOCIAL_FAIL = 9;
    public static final int MSG_LOGIN_SOCIAL_SUCCESS = 10;
    public static final int MSG_LIST_CITY_FAIL= 11;
    public static final int MSG_LIST_CITY_SUCCESS = 12;
    public static final int MSG_GET_PROFILE_FAIL = 13;
    public static final int MSG_GET_PROFILE_SUCCESS = 14;
    public static final int MSG_UPDATE_PROFILE_FAIL = 15;
    public static final int MSG_UPDATE_PROFILE_SUCCESS = 16;
    public static final int MSG_SAVE_NOTE_FAIL = 17;
    public static final int MSG_SAVE_NOTE_SUCCESS = 18;
    public static final int MSG_GET_NOTES_FAIL = 19;
    public static final int MSG_GET_NOTES_SUCCESS = 20;
    public static final int MSG_UPDATE_AVATAR_FAIl = 23;
    public static final int MSG_UPDATE_AVATAR_SUCCESS = 24;
    public static final int MSG_GET_GUIDE_SUCCESS = 21;
    public static final int MSG_GET_GUIDE_FAIL = 22;
    public static final int MSG_GET_RECIPE_SUCCESS = 25;
    public static final int MSG_GET_RECIPE_FAIL = 26;
    public static final int MSG_SAVE_COMUNITY_FAIL = 27;
    public static final int MSG_SAVE_COMUNITY_SUCCESS = 28;
    public static final int MSG_GET_COMUNITY_FAIL= 29;
    public static final int MSG_GET_COMUNITY_SUCCESS = 30;
    public static final int MSG_SAVE_COMMENT_FAIL = 31;
    public static final int MSG_SAVE_COMMENT_SUCCESS = 32;
    public static final int MSG_GET_COMMENTS_FAIL = 33;
    public static final int MSG_GET_COMMENTS_SUCCESS = 34;
    public static final int MSG_GET_NOTICES_FAIL = 35;
    public static final int MSG_GET_NOTICES_SUCCESS = 36;
    public static final int MSG_DELETE_COMMUNITY_SUCCESS = 37;
    public static final int MSG_DELETE_COMMUNITY_FAIL = 38;
    public static final int MSG_GET_Q_A_SUCCESS = 39;
    public static final int MSG_GET_Q_A_FAIL = 40;
    public static final int MSG_SAVE_QA_SUCCESS = 41;
    public static final int MSG_SAVE_QA_FAIL = 42;

    public static final int DELETE_USER_SUCCESS = 43;
    public static final int DELETE_USER_FAIL = 44;
}

package com.comeonbabys.android.app.common;


public class ServerPath {
	// Amazon
//	public static String SERVER = "http://54.92.0.11/api/v1";
//	public static String SERVER_MEDIA = "http://54.92.0.11";
//	public static String SERVER_RECIPE = "http://54.92.0.11";
	public static String SERVER = "http://10.0.2.2";
	public static String SERVER_MEDIA = /*Constants.BASE_URL + */ "http://89.223.27.239:8080/show-image/";
	public static String SERVER_RECIPE = "http://10.0.2.2";


	// Uyen
//	public static String SERVER = "http://128.199.132.13:80/api/v1";
//	public static String SERVER_MEDIA = "http://128.199.132.13:80";
//	public static String SERVER_RECIPE = "http://128.199.132.13:80";
	// Thien
//	public static String SERVER = "http://192.168.1.6:8001/api/v1";
//	public static String SERVER_MEDIA = "http://192.168.1.6:8001";
//	public static String SERVER_RECIPE = "http://192.168.1.6:8001";

	public static String REQ_ACCESS_TOKEN = "/create_user/login/";
	public static String REQ_LOGIN_FACEBOOK = "/create_user/login/";
	public static String REQ_LOGIN_KAKAOTALK = "/create_user/login/";
	public static String REQ_CREATE_ACCOUNT = "/create_user/";
	public static String REQ_FORGOT_PASSWORD = "/create_user/request_reset_password/";
	public static String REQ_CHANGE_PASSWORD = "/user/change_password/";
	public static String REQ_GET_LIST_NOTE_CALENDAR = "/note/getNoteForCalendar/";
	public static String REQ_POST_NEW_NOTE = "/note/";
	public static String REQ_LIST_NOTE_BY_DAY = "/note/";
	public static String REQ_GET_USER_PROFILE = "/user_profile/";
	public static String REQ_UPDATE_USER_PROFILE = "/user_profile/";
	public static String REQ_UPDATE_BASIC_QUESTION = "/answer/";
	public static String REQ_LOGOUT = "/create_user/logout/";
	public static String REQ_DELETE = "/user/";
	public static String REQ_GET_LIST_FOOD = "/food/";
	public static String REQ_GET_LIST_CITY = "/city/";

	public static String REQ_UPDATE_AVATAR = "/avatar/";
	public static String REQ_GET_AVATAR = "/avatar/";

	public static String REQ_GET_COMMUNITY = "/community/";
	public static String REQ_POST_COMMUNITY = "/community/";
	public static String REQ_PUT_COMMUNITY = "/community/";
	public static String REQ_DELETE_COMMUNITY = "/community/";
	public static String REQ_POST_LIKE_COMMUNITY = "/community/like/";
	public static String REQ_POST_COMMENT_COMMUNITY = "/community_comment/";
	public static String REQ_GET_COMMENT_COMMUNITY = "/community_comment/";

	public static String REQ_GET_COMMUNITY_QA = "/community_qa/";
	public static String REQ_POST_COMMUNITY_QA = "/community_qa/";
	public static String REQ_PUT_COMMUNITY_QA = "/community_qa/";
	public static String REQ_DELETE_COMMUNITY_QA = "/community_qa/";

	public static String REQ_POST_COMMUNITY_IMAGE = "/community_image/";
	public static String REQ_PUT_COMMUNITY_IMAGE = "/community_image/";
	public static String REQ_DELETE_COMMUNITY_IMAGE = "/community_image/";

	public static String REQ_GET_RECIPE = "/recipe/";
	public static String REQ_GET_NANIN = "/namin/";

	public static String REQ_GET_REPORT_MONTH = "/report/report_by_month/";
	public static String REQ_GET_REPORT_MONTH_DETAIL = "/report/report_by_month_detail/";
	public static String REQ_GET_REPORT_WEEK = "/report/report_by_week/";
	public static String REQ_GET_REPORT_WEEK_DETAIL = "/report/report_by_week_detail/";
	
	public static String REQ_GET_APP_VERSION = "/app_version_info/";
	public static String REQ_POST_DEVICE_REGISTER = "/device/register/";
	public static String REQ_POST_DEVICE_UNREGISTER = "/device/unregister/";
	
	public static String REQ_PUT_UPDATE_USER = "/user/";
}
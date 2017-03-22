package com.comeonbabys.android.app.common;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.comeonbabys.android.R;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

	public static final int MSG_CAMERA_IS_SETUP_START = 20;
	public static final int MSG_CAMERA_IS_SETUP_END = 21;
	public static final int MSG_CAMERA_STATE = 22;
	public static final int MSG_CAMERA_STATE_IP = 23;
	public static final int MSG_CAMERA_STATE_BACKGROUND = 24;
	public static final int MSG_RESCAN_CAM_CONN_CHANGE = 9;
	public static final int MSG_RESCAN_CAM_CONF_CHANGE = 10;

	// lengthOfFile
	public static int CONTENT_LENGHT = 50;

	// using for get wifi state
	public static WifiInfo WIFI_INFO = null;
	public static String BSSID_WIFI = "";
	public static int NETWORK_STATE = -1;

	public enum ENUM_CONNECTION_TYPE {
		TBD, // to be determine
		LOCAL, P2P, PROXY
	}

	public enum ENUM_CONNECTION_STATUS {
		NONE, // not yet initialize
		CONNECTED, FAILED
	}

	public static final String ALLOWED_CHARACTERS2 = "0123456789qwertyuiopasdfghjklzxcvbnm";

	public static String getRandomString(final int sizeOfRandomString) {
		final Random random = new Random();
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sizeOfRandomString; ++i)
			sb.append(ALLOWED_CHARACTERS2.charAt(random
					.nextInt(ALLOWED_CHARACTERS2.length())));
		return sb.toString();
	}

	public static String generateMd5Hash(String inString) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(inString.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String md5 = number.toString(16);

			while (md5.length() < 32) {
				md5 = "0" + md5;
			}
			return md5;
		} catch (NoSuchAlgorithmException e) {
			Log.e("MD5", e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * To check device is tablet or not.
	 * 
	 * @param context
	 * @return true if device is tablet
	 */
	@SuppressLint("InlinedApi")
	public static boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
		return (xlarge || large);
	}

	public static int MENU_SLIDING_HEIGHT_SIZE = 520;
	public static int MENU_SLIDING_WIDTH_SIZE = 520;

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Check an email is valid or not
	 * 
	 * @param email
	 *            the email need to check
	 * @return {@code true} if valid, {@code false} if invalid
	 */
	public static boolean isValidEmail(Context context, String email) {
		boolean result = false;
		String regex = context.getString(R.string.regex_email);
		Pattern pt = Pattern.compile(regex);
		Matcher mt = pt.matcher(email);
		if (mt.matches())
			result = true;
		return result;
	}

	/**
	 * @author PvTai
	 * @param color
	 * @return the hexa code of color
	 */
	public static String convertColorToHexa(int color) {
		return String.format("#%02x%02x%02x", Color.red(color),
				Color.green(color), Color.blue(color));
	}

	/**
	 * @author PvTai
	 * @Description Compare two float number
	 * @param v1
	 *            first number
	 * @param v2
	 *            second number
	 * @return the positive number if v1 is greater than v2, negative value is
	 *         v1 is less than v2, and 0 if equal
	 */
	public static int compareFloat(float v1, float v2) {
		int real1 = (int) v1;
		int real2 = (int) v2;
		float remain1 = v1 - real1;
		float remain2 = v2 - real2;

		if (real1 != real2)
			return (real1 - real2);
		else {
			int temp1 = (int) (remain1 * Globals.EPSILON);
			int temp2 = (int) (remain2 * Globals.EPSILON);
			return (temp1 - temp2);
		}
	}

	/**
	 * @author PvTai
	 * @Description Compare two double number
	 * @param v1
	 *            first number
	 * @param v2
	 *            second number
	 * @return the positive number if v1 is greater than v2, negative value is
	 *         v1 is less than v2, and 0 if equal
	 */
	public static int compareDouble(double v1, double v2) {
		int real1 = (int) v1;
		int real2 = (int) v2;
		double remain1 = v1 - real1;
		double remain2 = v2 - real2;

		if (real1 != real2)
			return (real1 - real2);
		else {
			int temp1 = (int) (remain1 * Globals.EPSILON);
			int temp2 = (int) (remain2 * Globals.EPSILON);
			return (temp1 - temp2);
		}
	}

	/**
	 * @author PvTai
	 * @param num
	 * @param delta
	 * @return string number after round
	 */
	public static String roundNumber(double num, int delta) {
		String result = "0.00";
		long a = (long) (num * Math.pow(10, delta + 1));
		int du = (int) (a % 10);
		a = a / 10;

		double rs;
		if (du < 5)
			rs = a / Math.pow(10, delta);
		else
			rs = (a + 1) / Math.pow(10, delta);
		try {
			result = String.format(Locale.ENGLISH, "%." + delta + "f", rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @author PvTai
	 * @return Application's version code from the {@code PackageManager}.
	 */
	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "noname";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	public static String getDateToShowEvent(String date) {
		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		String day = date.substring(6, 8);
		int mYear = Integer.parseInt(year);
		int mMonth = Integer.parseInt(month);
		int mDay = Integer.parseInt(day);
		Date dateCurrent = new Date();
		Date dateShow = (Date) dateCurrent.clone();
		dateShow.setYear(mYear - 1900);
		dateShow.setMonth(mMonth - 1);
		dateShow.setDate(mDay);
		ILog.e("dateCurrent: " + dateCurrent.toString());
		ILog.e("dateShow: " + dateShow.toString());
		if (dateShow.compareTo(dateCurrent) == 0)
			return "Today";
		SimpleDateFormat format = new SimpleDateFormat("EEEE yyyy-MM-dd");
		return format.format(dateShow);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	public static String getTimeToShowEvent(String time) {
		String hour = time.substring(0, 2);
		String minute = time.substring(2, 4);
		String second = time.substring(4, 6);
		int mHour = Integer.parseInt(hour);
		int mMinute = Integer.parseInt(minute);
		int mSecond = Integer.parseInt(second);
		Date dateShow = new Date();
		dateShow.setHours(mHour);
		dateShow.setMinutes(mMinute);
		dateShow.setSeconds(mSecond);
		SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
		return format.format(dateShow);
	}

	public static boolean isApplicationSentToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 *
	 * @param dp
	 *            A value in dp (density independent pixels) unit. Which we need
	 *            to convert into pixels
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on
	 *         device density
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * This method converts device specific pixels to density independent
	 * pixels.
	 *
	 * @param px
	 *            A value in px (pixels) unit. Which we need to convert into db
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	public static boolean isAppInstalled(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}

	public static String getApplicationNameByPackageName(Context context,
														 String packageName) {
		PackageManager packageManager = context.getPackageManager();
		ApplicationInfo applicationInfo;
		try {
			applicationInfo = packageManager.getApplicationInfo(packageName, 0);
			return (String) ((applicationInfo != null) ? packageManager
					.getApplicationLabel(applicationInfo) : "???");
		} catch (final NameNotFoundException e) {
		}
		return "???";
	}

	// public static void setListViewHeightBasedOnChildren(ListView listView) {
	// ListAdapter listAdapter = listView.getAdapter();
	// if (listAdapter == null) {
	// return;
	// }
	// int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
	// MeasureSpec.AT_MOST);
	// int totalHeight = 0;
	// View view = null;
	// for (int i = 0; i < listAdapter.getCount(); i++) {
	// view = listAdapter.getView(i, view, listView);
	// if (i == 0) {
	// view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
	// LayoutParams.WRAP_CONTENT));
	// }
	// view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	// totalHeight += view.getMeasuredHeight();
	// }
	// ViewGroup.LayoutParams params = listView.getLayoutParams();
	// params.height = totalHeight
	// + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	// listView.setLayoutParams(params);
	// listView.requestLayout();
	// }

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop()
				+ listView.getPaddingBottom();
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
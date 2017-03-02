/**
 * @author phuc.vu
 * @description customize log
 */
package com.comeonbaby.android.app.common;

public class ILog {
	
	public static boolean IS_DEBUG = true;
	
	public static String tag ="ComeOnBaby" ;

	
	public static void obj(Object object) {
		if (IS_DEBUG)
			android.util.Log.i(tag, String.valueOf(object));
	}
	
	public static void obj(String desc, Object object) {
		if (IS_DEBUG)
			android.util.Log.i(tag + " " + desc + ": ", String.valueOf(object));
	}
	
	public static void obj(String desc, Object object1, Object object2) {
		if (IS_DEBUG){
			String mess = "";
			if(object1 != null && object2 != null){
				mess = "Obj 1: "+ String.valueOf(object1) + " / " +"Obj 2: "+ String.valueOf(object2);
			}else if (object1 == null && object2 != null) {
				mess = "Obj 1: null " + " / " + "Obj 2: "+ String.valueOf(object2);
			}else if (object1 != null && object2 == null) {
				mess = "Obj 1: " + String.valueOf(object1) + " / " + "Obj 2: null ";
			}else {
				mess = "Obj 1: null " + " / " + "Obj 2: null ";
			}
			android.util.Log.i(tag + " " + desc + ": ", mess);
		}
	}

	public static void i(String msg) {
		if (IS_DEBUG)
			android.util.Log.i(tag, msg);
	}
	
	public static void d(String msg) {
		if (IS_DEBUG)
			android.util.Log.d(tag, msg);
	}

	public static void e(String msg) {
		if (IS_DEBUG)
			android.util.Log.e(tag, msg);
	}

	public static void v(String msg) {
		if (IS_DEBUG)
			android.util.Log.v(tag, msg);
	}

	public static void w(String msg) {
		if (IS_DEBUG)
			android.util.Log.w(tag, msg);
	}

	public static void i(String msg, Throwable tr) {
		if (IS_DEBUG)
			android.util.Log.i(tag, msg, tr);
	}

	public static void d(String msg, Throwable tr) {
		if (IS_DEBUG)
			android.util.Log.d(tag, msg, tr);
	}

	public static void e(String msg, Throwable tr) {
		if (IS_DEBUG)
			android.util.Log.e(tag, msg, tr);
	}

	public static void v(String msg, Throwable tr) {
		if (IS_DEBUG)
			android.util.Log.v(tag, msg, tr);
	}

	public static void w(String msg, Throwable tr) {
		if (IS_DEBUG)
			android.util.Log.w(tag, msg, tr);
	}
}

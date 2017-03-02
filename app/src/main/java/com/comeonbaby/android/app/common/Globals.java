package com.comeonbaby.android.app.common;

import android.app.Dialog;

/**
 * Created by olegs on 19.12.2016.
 */

public class Globals {
    public static String HOCKEYAPP_KEY = "73da638f3d5b057bd35dcb66d462fde2";
    public static String FLURRY_KEY = "C3T5DMM22ZB995YY6PZ7";
    public final static int EPSILON = 100;

    public static final String GCM_DELETED_MESSAGE = "Deleted messages on server: ";
    public static final String GCM_INTENT_SERVICE = "GcmIntentService";
    public static final String GCM_SEND_ERROR = "Send error: ";
    public static final String GCM_RECEIVED = "Received: ";
    public static String GCM_SENDER_ID = "360553502040";
    public static int MIN_PASSWORD_LEN = 4;
    public static int MAX_PASSWORD_LEN = 12;
    public static int MIN_NICKNAME_LEN = 2;
    public static int MAX_NICKNAME_LEN = 10;
    public static Dialog lostConnectionDialog = null;

    public static float size = 18;
}

package com.drkeironbrown.lifecoach.helper;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

public class PrefUtils {

    public static String USER_ID = "UserId";
    public static String IS_LOGIN = "is_login";
    public static String FIRST_TIME = "first_time";
    public static String USER_PROFILE_KEY = "USER_PROFILE_KEY";

    public static void setIsFirstTime(Context context, boolean isFirstTime) {
        Prefs.with(context).save(FIRST_TIME, isFirstTime);
    }

    public static boolean isFirstTime(Context context) {
        return Prefs.with(context).getBoolean(FIRST_TIME, true);
    }

    public static void setIsLogin(Context context, boolean isLogin) {
        Prefs.with(context).save(IS_LOGIN, isLogin);
    }

    public static boolean isLogin(Context context) {
        return Prefs.with(context).getBoolean(IS_LOGIN, false);
    }

}

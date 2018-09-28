package com.drkeironbrown.lifecoach.helper;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.drkeironbrown.lifecoach.model.User;
import com.google.gson.Gson;

public class PrefUtils {

    public static String USER_ID = "UserId";
    public static String IS_LOGIN = "is_login";
    public static String FIRST_TIME = "first_time";
    public static String USER_PROFILE_KEY = "USER_PROFILE_KEY";
    public static String INSPIRATIONAL = "inspirational";
    public static String P_INSPIRATIONAL = "p_inspirational";
    public static String INSPIRATIONAL_NOTI_TIME = "inspirational_noti_time";
    public static String P_INSPIRATIONAL_NOTI_TIME = "p_inspirational_noti_time";

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

    public static void setIsInspirationalSet(Context context, boolean isInspirational) {
        Prefs.with(context).save(INSPIRATIONAL, isInspirational);
    }

    public static boolean isInspirational(Context context) {
        return Prefs.with(context).getBoolean(INSPIRATIONAL, false);
    }

    public static void setIsPInspirationalSet(Context context, boolean isPInspirational) {
        Prefs.with(context).save(P_INSPIRATIONAL, isPInspirational);
    }

    public static boolean isPInspirational(Context context) {
        return Prefs.with(context).getBoolean(P_INSPIRATIONAL, false);
    }

    public static void setInspirationalNotiTime(Context context, String time) {
        Prefs.with(context).save(INSPIRATIONAL_NOTI_TIME, time);
    }

    public static String getInspirationalNotiTime(Context context) {
        return Prefs.with(context).getString(INSPIRATIONAL_NOTI_TIME,null);
    }

    public static void setPInspirationalNotiTime(Context context, String time) {
        Prefs.with(context).save(P_INSPIRATIONAL_NOTI_TIME, time);
    }

    public static String getPInspirationalNotiTime(Context context) {
        return Prefs.with(context).getString(P_INSPIRATIONAL_NOTI_TIME,null);
    }

    public static void setUserFullProfileDetails(Context context, User userProfile) {

        String toJson = new Gson().toJson(userProfile);
        Prefs.with(context).save(USER_PROFILE_KEY, toJson);
    }

    public static User getUserFullProfileDetails(Context context) {
        Gson gson = new Gson();

        User userProfileDetails = null;

        String getCityString = Prefs.with(context).getString(USER_PROFILE_KEY, "");

        try {
            userProfileDetails = gson.fromJson(getCityString, User.class);

        } catch (Exception e) {

        }
        return userProfileDetails;
    }
}

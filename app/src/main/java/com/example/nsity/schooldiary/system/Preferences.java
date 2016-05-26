package com.example.nsity.schooldiary.system;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nsity on 12.11.15.
 */
public class Preferences {

    public static final String NAME = "name";
    public static final String CLASSID = "class_id";
    public static final String CLASSNAME = "class_name";
    public static final String PUPILID = "pupil_id";
    public static final String FIO = "fio";
    public static final String NOTIFICATION_LESSON_SETTING = "notification_lesson_setting";
    public static final String NOTIFICATION_SETTING = "notification_setting";
    public static final String FIRST_LOGIN = "first_login";
    public static final String GCM_TOKEN = "gcm_token";


    public static final String accountType = "com.schooldiary";

    private static SharedPreferences.Editor editor;
    private static SharedPreferences sPref;

    public static void set(String fieldName, String value, Context context){
        if (sPref == null)
            sPref = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);
        if (editor == null)
            editor = sPref.edit();
        editor.putString(fieldName, value);
        editor.apply();
    }

    public static void set(String fieldName, boolean value, Context context){
        if (sPref == null)
            sPref = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);
        if (editor == null)
            editor = sPref.edit();
        editor.putBoolean(fieldName, value);
        editor.apply();
    }

    public static String get(String fieldName, Context context){
        if (sPref == null)
            sPref = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);
        if (editor == null)
            editor = sPref.edit();
        return sPref.getString(fieldName, "");
    }

    public static boolean getBoolean(String fieldName, boolean defaultValue, Context context){
        if (sPref == null)
            sPref = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);
        if (editor == null)
            editor = sPref.edit();
        return sPref.getBoolean(fieldName, defaultValue);
    }

    public static void clear(Context context){
        if (sPref == null)
            sPref = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);
        if (editor == null)
            editor = sPref.edit();
        editor.clear();
        editor.commit();
    }
}

package com.example.nsity.schooldiary.system;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nsity on 15.11.15.
 */
public class CommonFunctions {
    public static final String FORMAT_DD_MM_YYYY = "dd.MM.yyyy";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_HH_MM = "HH:mm";

    public static boolean StringIsNullOrEmpty(String string) {
        return (string == null) || (string.isEmpty());
    }


    public static String getCurrentDateAndTime() {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        return simple.format(new Date());
    }

    public static String getCurrentMonthAndYear() {
        SimpleDateFormat simple = new SimpleDateFormat("MM.yyyy", Locale.ENGLISH);
        return simple.format(new Date());
    }

    public static long getDifferentBeetweenDates(String start, String end) {
        SimpleDateFormat simple = new SimpleDateFormat("MM.yyyy", Locale.ENGLISH);
        try {
            Date startDate = simple.parse(start);
            Date endDate = simple.parse(end);
            return endDate.getTime() - startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String getCurrentDateyyyyMMdd() {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return simple.format(new Date());
    }

    public static String getDate(String date, String enterFormat, String endFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(enterFormat, Locale.ENGLISH);
            SimpleDateFormat df = new SimpleDateFormat(endFormat, Locale.ENGLISH);

            return df.format(sdf.parse(date));
        } catch (Exception e) {
            return date;
        }
    }

    public static String getDate(Date date, String endFormat) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(endFormat, Locale.ENGLISH);

            return df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFieldString(JSONObject jsonObject, String field) {
        try {
            return jsonObject.getString(field);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getStringFromJSONArray(JSONArray jsonArray, String field) {
        try {
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            return jsonObject.getString(field);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean getFieldBoolean(JSONObject jsonObject, String field) {
        try {
            return !jsonObject.isNull(field) && (jsonObject.getString(field).equals("1") || (jsonObject.getString(field).equals("-1")));
        } catch (Exception e) {
            return false;
        }
    }

    public static int getFieldInt(JSONObject jsonObject, String field) {
        try {
            return jsonObject.getInt(field);
        } catch (Exception e) {
            return 0;
        }
    }

    public static Double getFieldDouble(JSONObject jsonObject, String field) {
        try {
            return jsonObject.getDouble(field);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static String deleteLastChar(String value, char lastChar) {
        if (value.length() > 0 && value.charAt(value.length() - 1) == lastChar)
            value = value.substring(0, value.length() - 1);
        return value;
    }

    public static ProgressDialog showProgressDialog(Context context, String message) {
        return ProgressDialog.show(context, "", message, true);
    }
}
package com.example.nsity.schooldiary.system;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.nsity.schooldiary.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nsity on 15.11.15.
 */
public class CommonFunctions {
    public static final String FORMAT_DD_MM_YYYY = "dd.MM.yyyy";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_D_MMMM_YYYY = "d MMMM yyyy";
    public static final String FORMAT_E_D_MMMM_YYYY = "d MMMM, yyyy";

    public static boolean StringIsNullOrEmpty(String string) {
        return (string == null) || (string.isEmpty());
    }

    public static String getCurrentDateyyyyMMdd() {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return simple.format(new Date());
    }

    public static String getDate(String date, String enterFormat, String endFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(enterFormat, new Locale("ru"));
            SimpleDateFormat df = new SimpleDateFormat(endFormat, new Locale("ru"));

            return df.format(sdf.parse(date));
        } catch (Exception e) {
            return date;
        }
    }

    public static String getDate(Date date, String endFormat) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(endFormat, new Locale("ru"));

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


    public static int setColor(Context context, int color) {
        int[] colors = context.getResources().getIntArray(R.array.colors);

        return colors[color % colors.length];
    }

    public static int setMarkColor(Context context, int value) {
        int[] colors = context.getResources().getIntArray(R.array.mark_colors);

        switch (value) {
            case 5:
                return colors[0];
            case 4:
                return colors[1];
            case 3:
                return colors[2];
            case 2:
                return colors[3];
            default:
                return -1;
        }
    }

    public static String getTime(String time) {
        return time.substring(0, time.length() - 3);
    }

    public static int getDayOfWeek(Date d) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            switch (dayOfWeek) {
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    dayOfWeek--;
                    break;
                case 1:
                    dayOfWeek = 7;
                    break;
            }
            return dayOfWeek;
    }


    public static Date getMonday(Date datetime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(datetime);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                dayOfWeek--;
                break;
            case 1:
                dayOfWeek = 7;
                break;
        }
        cal.add(Calendar.DATE, -dayOfWeek + 1);
        Date d = cal.getTime();
        return d;
    }

    public static Date addDays(int daysForAdd, Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DATE, daysForAdd);
        currentDate = cal.getTime();
        return currentDate;
    }


    public static void setRefreshActionButtonState(final boolean refreshing, final Menu optionsMenu) {
        if (optionsMenu == null)
            return;

        final MenuItem refreshItem = optionsMenu.findItem(R.id.action_refresh);
        if (refreshItem == null)
            return;

        if (refreshing)
            refreshItem.setActionView(R.layout.actionbar);
        else
            refreshItem.setActionView(null);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, Context context, final View mFormView, final View mProgressView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
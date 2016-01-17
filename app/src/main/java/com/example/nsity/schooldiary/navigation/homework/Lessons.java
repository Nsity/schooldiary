package com.example.nsity.schooldiary.navigation.homework;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.lesson.Lesson;
import com.example.nsity.schooldiary.navigation.marks.Subject;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.CommonManager;
import com.example.nsity.schooldiary.system.ListBaseEntity;
import com.example.nsity.schooldiary.system.database.tables.LessonDBInterface;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nsity on 17.01.16.
 */
public class Lessons extends ListBaseEntity {

    private ArrayList<Lesson> arrayList;
    private LessonDBInterface db;

    public Lessons(Context context) {
        super(context);
        arrayList = new ArrayList<>();
        db = new LessonDBInterface(context);
        loadFromDB();
    }


    public Lessons(Context context, String beginDate, String endDate, boolean withHomework) {
        super(context);
        arrayList = new ArrayList<>();
        db = new LessonDBInterface(context);
        loadFromDB(beginDate, endDate, withHomework);
    }

    @Override
    protected void loadFromDB() {
        arrayList = db.getLessons();
    }

    private void loadFromDB(String beginDate, String endDate, boolean withHomework) {
        arrayList = db.getLessons(beginDate, endDate, withHomework);
    }

    public ArrayList<Lesson> getLessons() {
        return arrayList;
    }

    public Lesson getItem(int position) {
        return arrayList.get(position);
    }


    public void loadHomework(final String beginDate, final String endDate, final CallBack callBack) {
        if (!Server.isOnline(context)) {
            callBack.onFail(context.getString(R.string.internet_problem));
            return;
        }

        CommonManager.getHomework(context, beginDate, endDate, new CallBack<String>() {
            @Override
            public void onSuccess(String date) {
                loadFromDB(beginDate, endDate, false);
                callBack.onSuccess(date);

            }

            @Override
            public void onFail(String error) {
                callBack.onFail(error);
            }

        });
    }

    public ArrayList<String> getDates() {
        ArrayList<String> dates = new ArrayList<>();

        for(Lesson lesson: arrayList) {
            if(!dates.contains(lesson.getDate())) {
                dates.add(lesson.getDate());
            }
        }

        return dates;
    }

    public ArrayList<String> getHeaders() {
        ArrayList<String> headers = new ArrayList<>();

        for(Lesson lesson: arrayList) {
            if(!headers.contains(lesson.getDate())) {
                headers.add(lesson.getDate());
            }
        }

        for(int i = 0; i < headers.size(); i++) {
            try {
                Date date = new SimpleDateFormat(CommonFunctions.FORMAT_YYYY_MM_DD, new Locale("ru")).parse(headers.get(i));
                String header = CommonFunctions.getDate(date, "E").toUpperCase() + " " +
                        CommonFunctions.getDate(date, CommonFunctions.FORMAT_E_D_MMMM_YYYY);
                headers.set(i, header);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        
        return headers;
    }
}

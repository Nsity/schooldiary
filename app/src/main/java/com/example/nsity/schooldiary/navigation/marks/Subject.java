package com.example.nsity.schooldiary.navigation.marks;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.progress.ProgressItem;
import com.example.nsity.schooldiary.navigation.marks.subjects.SubjectMark;
import com.example.nsity.schooldiary.system.BaseEntity;
import com.example.nsity.schooldiary.system.CommonManager;
import com.example.nsity.schooldiary.system.database.tables.ProgressDBInterface;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nsity on 24.11.15.
 */
public class Subject extends BaseEntity {

    private int color;
    private String name;

    public Subject(int id, String name, int color) {
        this.color = color;
        this.id = id;
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void loadMarks(Context context, int periodId, final CallBack callBack) {
        if (!Server.isOnline(context)) {
            callBack.onFail(context.getString(R.string.internet_problem));
            return;
        }

        CommonManager.getMarks(context, id, periodId, new CallBack<ArrayList<SubjectMark>>() {
            @Override
            public void onSuccess(ArrayList<SubjectMark> marksArrayList) {
                callBack.onSuccess(marksArrayList);
            }

            @Override
            public void onFail(String error) {
                callBack.onFail(error);
            }
        });
    }


    public ArrayList<ProgressItem> loadProgressFromDB(Context context) {
        ProgressDBInterface dbProgress = new ProgressDBInterface(context);
        return dbProgress.getSubjectProgress(id);
    }
}

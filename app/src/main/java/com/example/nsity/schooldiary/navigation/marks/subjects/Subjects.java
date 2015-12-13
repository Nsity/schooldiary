package com.example.nsity.schooldiary.navigation.marks.subjects;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.Subject;
import com.example.nsity.schooldiary.navigation.marks.progress.ProgressItem;
import com.example.nsity.schooldiary.system.CommonManager;
import com.example.nsity.schooldiary.system.database.tables.ProgressDBInterface;
import com.example.nsity.schooldiary.system.database.tables.SubjectsClassDBInterface;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.util.ArrayList;

/**
 * Created by nsity on 04.12.15.
 */
public class Subjects {
    ArrayList<Subject> subjectsArrayList;
    private SubjectsClassDBInterface db;

    public Subjects(Context context) {
        this.db = new SubjectsClassDBInterface(context);
        subjectsArrayList = loadFromDB();
    }

    public ArrayList<Subject> loadFromDB() {
        return db.getSubjects();
    }

    public ArrayList<Subject> getSubjects() {
        return subjectsArrayList;
    }

    public Subject getItem(int position) {
        return subjectsArrayList.get(position);
    }


    public void loadProgress(Context context, final CallBack callBack) {
        if (!Server.isOnline(context)) {
            callBack.onFail(context.getString(R.string.internet_problem));
            return;
        }

        CommonManager.getProgress(context, new CallBack() {
            @Override
            public void onSuccess() {
                callBack.onSuccess();
            }

            @Override
            public void onFail(String error) {
                    callBack.onFail(error);
                }

        });
    }

    public Boolean existProgress(Context context) {
        ProgressDBInterface dbProgress = new ProgressDBInterface(context);
        return dbProgress.existProgress();
    }


    public Subject findSubjectById(int id) {
        if(subjectsArrayList == null) {
            return null;
        }
        for(Subject subject: subjectsArrayList) {
            if(subject.getId() == id) {
                return subject;
            }
        }
        return null;
    }

}

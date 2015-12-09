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
    private Context context;

    public Subjects(Context context) {
        this.db = new SubjectsClassDBInterface(context);
        subjectsArrayList = loadFromDB();
        this.context = context;
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


    public void loadProgress(final CallBack callBack) {
        if (!Server.isOnline(context)) {
            callBack.onFail(context.getString(R.string.internet_problem));
            return;
        }

        CommonManager.getProgress(context, new CallBack() {
            @Override
            public void onSuccess() {
                callBack.onSuccess(loadProgressFromDB());
            }

            @Override
            public void onFail(String error) {
                    callBack.onFail(error);
                }

        });
    }

    public ArrayList<ArrayList<ProgressItem>> loadProgressFromDB() {
        if(subjectsArrayList == null) {
            return null;
        }

        ArrayList<ArrayList<ProgressItem>> progress = new ArrayList<>();

        ProgressDBInterface dbProgress = new ProgressDBInterface(context);
        for (Subject subject: subjectsArrayList) {
            ArrayList<ProgressItem> progressArrayList = dbProgress.getSubjectProgress(subject.getId());
            progress.add(progressArrayList);
        }

        Boolean flag = false;
        for(ArrayList<ProgressItem> item: progress) {
            if(item != null) {
                flag = true;
            }
        }

        if(!flag) {
            return null;
        } else {
            return progress;
        }
    }

}

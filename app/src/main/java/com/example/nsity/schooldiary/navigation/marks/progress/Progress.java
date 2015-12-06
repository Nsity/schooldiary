package com.example.nsity.schooldiary.navigation.marks.progress;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.Subject;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.database.tables.ProgressDBInterface;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.util.ArrayList;

/**
 * Created by nsity on 06.12.15.
 */
public class Progress {

    private ArrayList<ProgressItem> arrayList;
    private ProgressDBInterface db;
    private Context context;

    public Progress(Context context) {
        this.db = new ProgressDBInterface(context);
        loadFromDB();
        this.context = context;
    }

    public void loadFromDB() {
        arrayList = db.getProgress();
    }

    public int getSize() {
        if(arrayList == null) {
            return -1;
        } else {
            return arrayList.size();
        }
    }

    public void load(final CallBack callBack) {
        if (!Server.isOnline(context)) {
            callBack.onFail(context.getString(R.string.internet_problem));
            return;
        }

        ProgressManager.getProgress(context, new CallBack() {
            @Override
            public void onSuccess() {
                loadFromDB();
                callBack.onSuccess();
            }

            @Override
            public void onFail(String error) {
               callBack.onFail(error);
           }
        });
    }
}

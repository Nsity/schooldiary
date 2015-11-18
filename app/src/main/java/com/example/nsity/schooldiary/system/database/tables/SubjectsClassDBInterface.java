package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 15.11.15.
 */
public class SubjectsClassDBInterface extends ADBWorker {

    public static final String SUBJECTS_CLASS_TABLE_NAME = "SUBJECTS_CLASS";
    public static final String SUBJECTS_CLASS_COLUMN_ID = "SUBJECTS_CLASS_ID";
    public static final String SUBJECTS_CLASS_COLUMN_SUBJECT_NAME = "SUBJECT_NAME";
    public static final String SUBJECTS_CLASS_COLUMN_COLOR = "SUBJECTS_COLOR";


    public static final String SUBJECTS_CLASS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + SUBJECTS_CLASS_TABLE_NAME + "("
            + SUBJECTS_CLASS_COLUMN_ID + " INTEGER PRIMARY KEY , "
            + SUBJECTS_CLASS_COLUMN_SUBJECT_NAME + " INTEGER, "
            + SUBJECTS_CLASS_COLUMN_COLOR + " INTEGER "
            + ");";

    @Override
    public int save(JSONArray clients, boolean dropAllData) {
        if (dropAllData) {
            delete(SUBJECTS_CLASS_TABLE_NAME, null, null);
        }

        return addSubjectsClassData(clients);
    }


    public int addSubjectsClassData(JSONArray subjectsClass) {
        if ((CommonFunctions.StringIsNullOrEmpty(subjectsClass.toString())) || (subjectsClass.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> subjectsClassValues = new ArrayList<>();
            for (int i = 0; i < subjectsClass.length(); i++) {
                JSONObject subject = subjectsClass.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put(SUBJECTS_CLASS_COLUMN_ID, CommonFunctions.getFieldInt(subject, context.getString(R.string.id)));
                cv.put(SUBJECTS_CLASS_COLUMN_SUBJECT_NAME, CommonFunctions.getFieldString(subject, context.getString(R.string.name)));
                cv.put(SUBJECTS_CLASS_COLUMN_COLOR, i);

                subjectsClassValues.add(cv);
            }
            int result = insert(SUBJECTS_CLASS_TABLE_NAME, ADBWorker.REPLACE, subjectsClassValues);
            return result;
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public SubjectsClassDBInterface(Context context) {
        super(context);
    }
}

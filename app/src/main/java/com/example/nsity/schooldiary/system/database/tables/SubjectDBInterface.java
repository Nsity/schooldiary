package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 15.11.15.
 */
public class SubjectDBInterface extends ADBWorker {

    public static final String SUBJECT_TABLE_NAME = "SUBJECT";
    public static final String SUBJECT_COLUMN_ID = "SUBJECT_ID";
    public static final String SUBJECT_COLUMN_NAME = "SUBJECT_NAME";


    public static final String SUBJECT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + SUBJECT_TABLE_NAME + "("
            + SUBJECT_COLUMN_ID + " INTEGER PRIMARY KEY , "
            + SUBJECT_COLUMN_NAME + " VARCHAR(255) " + ");";


    public SubjectDBInterface(Context context) {
        super(context);
    }

    @Override
    public int save(JSONArray subjects, boolean dropAllData) {
        if (dropAllData) {
            deleteAllSubjects();
        }

        return addSubjects(subjects, false);
    }


    private int addSubjects(JSONArray subjects, boolean isTemporaryClients) {
        if ((CommonFunctions.StringIsNullOrEmpty(subjects.toString())) || (subjects.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> subjectsValues = new ArrayList<>();
            for (int i = 0; i < subjects.length(); i++) {
                JSONObject client = subjects.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put(SUBJECT_COLUMN_ID, CommonFunctions.getFieldInt(client, context.getString(R.string.id)));
                cv.put(SUBJECT_COLUMN_NAME, CommonFunctions.getFieldInt(client, context.getString(R.string.name)));
                subjectsValues.add(cv);
            }
            int result = insert(SUBJECT_TABLE_NAME, ADBWorker.REPLACE, subjectsValues);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    private void deleteAllSubjects() {
        delete(SUBJECT_TABLE_NAME, null, null);
    }

}
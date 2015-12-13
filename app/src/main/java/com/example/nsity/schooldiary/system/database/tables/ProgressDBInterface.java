package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.renderscript.Sampler;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.Period;
import com.example.nsity.schooldiary.navigation.Subject;
import com.example.nsity.schooldiary.navigation.marks.progress.ProgressItem;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 06.12.15.
 */
public class ProgressDBInterface extends ADBWorker {

    public static final String PROGRESS_TABLE_NAME = "PROGRESS";
    public static final String PROGRESS_COLUMN_ID = "PROGRESS_ID";
    public static final String PROGRESS_COLUMN_PERIOD_ID = "PERIOD_ID";
    public static final String PROGRESS_COLUMN_SUBJECTS_CLASS_ID = "SUBJECTS_CLASS_ID";
    public static final String PROGRESS_COLUMN_MARK = "PROGRESS_MARK";


    public static final String PROGRESS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + PROGRESS_TABLE_NAME + "("
            + PROGRESS_COLUMN_ID + " INTEGER PRIMARY KEY , "
            + PROGRESS_COLUMN_PERIOD_ID + " INTEGER, "
            + PROGRESS_COLUMN_SUBJECTS_CLASS_ID + " INTEGER, "
            + PROGRESS_COLUMN_MARK + " INTEGER "
            + ");";

    public ProgressDBInterface(Context context) {
        super(context);
    }

    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        if (dropAllData) {
            delete(PROGRESS_TABLE_NAME, null, null);
        }

        return addProgress(objects);
    }

    public int addProgress(JSONArray progress) {
        if ((CommonFunctions.StringIsNullOrEmpty(progress.toString())) || (progress.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> progressValues = new ArrayList<>();
            for (int i = 0; i < progress.length(); i++) {
                JSONObject progressItem = progress.getJSONObject(i);
                JSONArray marks = progressItem.getJSONArray(context.getString(R.string.marks));

                for (int j = 0; j < marks.length(); j++) {
                    ContentValues cv = new ContentValues();
                    JSONObject mark = marks.getJSONObject(j);

                    cv.put(PROGRESS_COLUMN_SUBJECTS_CLASS_ID, CommonFunctions.getFieldInt(progressItem, context.getString(R.string.subject_id)));
                    cv.put(PROGRESS_COLUMN_ID, CommonFunctions.getFieldInt(mark, context.getString(R.string.id)));
                    cv.put(PROGRESS_COLUMN_MARK, CommonFunctions.getFieldInt(mark, context.getString(R.string.mark)));
                    cv.put(PROGRESS_COLUMN_PERIOD_ID, CommonFunctions.getFieldInt(mark, context.getString(R.string.period_id)));

                    progressValues.add(cv);
                }

            }
            return insert(PROGRESS_TABLE_NAME, ADBWorker.REPLACE, progressValues);
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public ArrayList<ProgressItem> getProgress() {
        String selectQuery = "SELECT * FROM " + PROGRESS_TABLE_NAME + " p JOIN " + PeriodDBInterface.PERIOD_TABLE_NAME +
                " pr ON p." + PROGRESS_COLUMN_PERIOD_ID + " = pr." + PeriodDBInterface.PERIOD_COLUMN_ID +
                " JOIN " + SubjectsClassDBInterface.SUBJECTS_CLASS_TABLE_NAME + " s ON p." + PROGRESS_COLUMN_SUBJECTS_CLASS_ID +
                " = s." + SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_ID + " ORDER BY " + PeriodDBInterface.PERIOD_COLUMN_NAME + ", " +
                SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_SUBJECT_NAME;

        Cursor cursor = getCursor(selectQuery, new String[]{});

        if(cursor == null)
            return null;

        ArrayList<ProgressItem> progress = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                ProgressItem progressItem = new ProgressItem();

                progressItem.setId(cursor.getInt(cursor.getColumnIndex(PROGRESS_COLUMN_ID)));
                progressItem.setValue(cursor.getInt(cursor.getColumnIndex(PROGRESS_COLUMN_MARK)));

                progressItem.setPeriod(new Period(cursor.getInt(cursor.getColumnIndex(PROGRESS_COLUMN_PERIOD_ID)),
                        cursor.getString(cursor.getColumnIndex(PeriodDBInterface.PERIOD_COLUMN_START)),
                        cursor.getString(cursor.getColumnIndex(PeriodDBInterface.PERIOD_COLUMN_END)),
                        cursor.getString(cursor.getColumnIndex(PeriodDBInterface.PERIOD_COLUMN_NAME))));

                progress.add(progressItem);
            }
            while (cursor.moveToNext());
        }

        cursor.close();

        if(progress.size() == 0) {
            return null;
        } else {
            return progress;
        }
    }


    public ArrayList<ProgressItem> getSubjectProgress(int subjectId) {
        String selectQuery = "SELECT * FROM " + PROGRESS_TABLE_NAME + " p JOIN " + PeriodDBInterface.PERIOD_TABLE_NAME +
                " pr ON p." + PROGRESS_COLUMN_PERIOD_ID + " = pr." + PeriodDBInterface.PERIOD_COLUMN_ID +
                " WHERE " + PROGRESS_COLUMN_SUBJECTS_CLASS_ID + " =? " +
                " ORDER BY " + PeriodDBInterface.PERIOD_COLUMN_NAME;

        Cursor cursor = getCursor(selectQuery, new String[]{String.valueOf(subjectId)});

        if(cursor == null)
            return null;

        ArrayList<ProgressItem> progress = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                ProgressItem progressItem = new ProgressItem();

                progressItem.setId(cursor.getInt(cursor.getColumnIndex(PROGRESS_COLUMN_ID)));
                progressItem.setValue(cursor.getInt(cursor.getColumnIndex(PROGRESS_COLUMN_MARK)));

                progressItem.setPeriod(new Period(cursor.getInt(cursor.getColumnIndex(PROGRESS_COLUMN_PERIOD_ID)),
                        cursor.getString(cursor.getColumnIndex(PeriodDBInterface.PERIOD_COLUMN_START)),
                        cursor.getString(cursor.getColumnIndex(PeriodDBInterface.PERIOD_COLUMN_END)),
                        cursor.getString(cursor.getColumnIndex(PeriodDBInterface.PERIOD_COLUMN_NAME))));

                progress.add(progressItem);
            }
            while (cursor.moveToNext());
        }

        cursor.close();

        if(progress.size() == 0) {
            return null;
        } else {
            return progress;
        }
    }

    public Boolean existProgress() {
        String selectQuery = "SELECT * FROM " + PROGRESS_TABLE_NAME;

        Cursor cursor = getCursor(selectQuery, new String[]{});

        if(cursor == null)
            return null;

        int count = cursor.getCount();
        cursor.close();

        return count != 0;
    }
}

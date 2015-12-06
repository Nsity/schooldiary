package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.Subject;
import com.example.nsity.schooldiary.navigation.Time;
import com.example.nsity.schooldiary.navigation.lesson.Lesson;
import com.example.nsity.schooldiary.navigation.marks.Mark;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 15.11.15.
 */
public class LessonDBInterface extends ADBWorker {

    public static final String LESSON_TABLE_NAME = "LESSON";
    public static final String LESSON_COLUMN_ID = "LESSON_ID";
    public static final String LESSON_COLUMN_THEME = "LESSON_THEME";
    public static final String LESSON_COLUMN_SUBJECTS_CLASS_ID = "SUBJECTS_CLASS_ID";
    public static final String LESSON_COLUMN_HOMEWORK = "LESSON_HOMEWORK";
    public static final String LESSON_COLUMN_STATUS = "LESSON_STATUS";
    public static final String LESSON_COLUMN_TIME_ID = "TIME_ID";
    public static final String LESSON_COLUMN_DATE = "LESSON_DATE";
    public static final String LESSON_COLUMN_PASS = "LESSON_PASS";
    public static final String LESSON_COLUMN_NOTE = "LESSON_NOTE";


    public static final String LESSON_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + LESSON_TABLE_NAME + "("
            + LESSON_COLUMN_ID + " INTEGER PRIMARY KEY, "
            + LESSON_COLUMN_HOMEWORK + " VARCHAR(5000), "
            + LESSON_COLUMN_NOTE + " VARCHAR(1000), "
            + LESSON_COLUMN_THEME + " VARCHAR(300), "
            + LESSON_COLUMN_PASS + " VARCHAR(10), "
            + LESSON_COLUMN_SUBJECTS_CLASS_ID + " INTEGER, "
            + LESSON_COLUMN_DATE + " DATE, "
            + LESSON_COLUMN_STATUS + " BOOLEAN, "
            + LESSON_COLUMN_TIME_ID + " INTEGER " + ");";


    public static final String MARK_TABLE_NAME = "MARK";
    public static final String MARK_COLUMN_ID = "MARK_ID";
    public static final String MARK_COLUMN_VALUE = "MARK_VALUE";
    public static final String MARK_COLUMN_LESSON_ID = "LESSON_ID";
    public static final String MARK_COLUMN_TYPE = "MARK_TYPE";


    public static final String MARK_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + MARK_TABLE_NAME + "("
            + MARK_COLUMN_ID + " INTEGER PRIMARY KEY, "
            + MARK_COLUMN_VALUE + " INTEGER, "
            + MARK_COLUMN_TYPE + " VARCHAR(500), "
            + MARK_COLUMN_LESSON_ID + " INTEGER " + ");";


    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        if (dropAllData) {
            delete(LESSON_TABLE_NAME, null, null);
            delete(MARK_TABLE_NAME, null, null);
        }
        return addLesson(objects);
    }


    private int addLesson(JSONArray lessons) {
        if ((CommonFunctions.StringIsNullOrEmpty(lessons.toString())) || (lessons.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> lessonsValues = new ArrayList<>();
            ArrayList<ContentValues> marksValues = new ArrayList<>();
            for (int i = 0; i < lessons.length(); i++) {
                JSONObject lesson = lessons.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put(LESSON_COLUMN_ID, CommonFunctions.getFieldInt(lesson, context.getString(R.string.id)));
                cv.put(LESSON_COLUMN_HOMEWORK, CommonFunctions.getFieldString(lesson, context.getString(R.string.homework)));
                cv.put(LESSON_COLUMN_STATUS, CommonFunctions.getFieldInt(lesson, context.getString(R.string.status)));
                cv.put(LESSON_COLUMN_THEME, CommonFunctions.getFieldString(lesson, context.getString(R.string.theme)));
                cv.put(LESSON_COLUMN_TIME_ID, CommonFunctions.getFieldInt(lesson, context.getString(R.string.time_id)));
                cv.put(LESSON_COLUMN_SUBJECTS_CLASS_ID, CommonFunctions.getFieldInt(lesson, context.getString(R.string.subject_id)));
                cv.put(LESSON_COLUMN_DATE, CommonFunctions.getFieldString(lesson, context.getString(R.string.date)));
                cv.put(LESSON_COLUMN_NOTE, CommonFunctions.getFieldString(lesson, context.getString(R.string.note)));
                cv.put(LESSON_COLUMN_PASS, CommonFunctions.getFieldString(lesson, context.getString(R.string.pass)));

                lessonsValues.add(cv);

                JSONArray marks = lesson.getJSONArray(context.getString(R.string.marks));

                for (int j = 0; j < marks.length(); j++) {
                    JSONObject mark = marks.getJSONObject(j);

                    cv = new ContentValues();
                    cv.put(MARK_COLUMN_ID, CommonFunctions.getFieldInt(mark, context.getString(R.string.id)));
                    cv.put(MARK_COLUMN_VALUE, CommonFunctions.getFieldInt(mark, context.getString(R.string.mark)));
                    cv.put(MARK_COLUMN_LESSON_ID, CommonFunctions.getFieldInt(lesson, context.getString(R.string.id)));
                    cv.put(MARK_COLUMN_TYPE, CommonFunctions.getFieldString(mark, context.getString(R.string.type)));
                    marksValues.add(cv);
                }
            }

            insert(MARK_TABLE_NAME, ADBWorker.REPLACE, marksValues);
            return insert(LESSON_TABLE_NAME, ADBWorker.REPLACE, lessonsValues);
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public LessonDBInterface(Context context) {
        super(context);
    }


    public Cursor getLesson(String id) {
        String selectQuery = "SELECT * FROM " + LESSON_TABLE_NAME + " t JOIN " + SubjectsClassDBInterface.SUBJECTS_CLASS_TABLE_NAME
                + " s ON t." + LESSON_COLUMN_SUBJECTS_CLASS_ID + " = s." + SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_ID +
                " JOIN " + TimeDBInterface.TIME_TABLE_NAME + " time ON t." + LESSON_COLUMN_TIME_ID + " = time." +
                TimeDBInterface.TIME_COLUMN_ID + " WHERE " + LESSON_COLUMN_ID + " =? ";
        return getCursor(selectQuery, new String[] {id});
    }

    public Lesson getLesson(String date, int timeId, int subjectId) {
        String selectQuery = "SELECT * FROM " + LESSON_TABLE_NAME + " t JOIN " + SubjectsClassDBInterface.SUBJECTS_CLASS_TABLE_NAME
                + " s ON t." + LESSON_COLUMN_SUBJECTS_CLASS_ID + " = s." + SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_ID +
                " JOIN " + TimeDBInterface.TIME_TABLE_NAME + " time ON t." + LESSON_COLUMN_TIME_ID + " = time." +
                TimeDBInterface.TIME_COLUMN_ID + " WHERE " + LESSON_COLUMN_DATE + " =? AND t." +
                LESSON_COLUMN_TIME_ID + " =? AND t." + LESSON_COLUMN_SUBJECTS_CLASS_ID + " =? ";

        Cursor cursor = getCursor(selectQuery, new String[]{date, String.valueOf(timeId), String.valueOf(subjectId)});

        if(cursor == null) {
            return null;
        }

        Lesson lesson = new Lesson();

        if (cursor.moveToFirst()) {
            do {
                lesson.setId(cursor.getInt(cursor.getColumnIndex(LessonDBInterface.LESSON_COLUMN_ID)));
                lesson.setDate(cursor.getString(cursor.getColumnIndex(LessonDBInterface.LESSON_COLUMN_DATE)));
                lesson.setHomework(cursor.getString(cursor.getColumnIndex(LessonDBInterface.LESSON_COLUMN_HOMEWORK)));
                lesson.setTheme(cursor.getString(cursor.getColumnIndex(LessonDBInterface.LESSON_COLUMN_THEME)));
                lesson.setNote(cursor.getString(cursor.getColumnIndex(LessonDBInterface.LESSON_COLUMN_NOTE)));
                lesson.setPass(cursor.getString(cursor.getColumnIndex(LessonDBInterface.LESSON_COLUMN_PASS)));

                lesson.setSubject(new Subject(cursor.getInt(cursor.getColumnIndex(LESSON_COLUMN_SUBJECTS_CLASS_ID)),
                        cursor.getString(cursor.getColumnIndex(SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_SUBJECT_NAME)),
                        cursor.getInt(cursor.getColumnIndex(SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_COLOR))));


                lesson.setTime(new Time(cursor.getInt(cursor.getColumnIndex(LESSON_COLUMN_TIME_ID)),
                        cursor.getString(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_START)),
                        cursor.getString(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_END))));
            }
            while (cursor.moveToNext());
        } else {
            lesson.setId(-1);
        }

        cursor.close();

        if(lesson.getId() == -1) {
            return null;
        } else {
            return lesson;
        }
    }


    public void deleteLesson(int lessonId) {
        delete(LESSON_TABLE_NAME, LESSON_COLUMN_ID + " =?", new String[] {String.valueOf(lessonId)});
        delete(MARK_TABLE_NAME, LESSON_COLUMN_ID + " =?", new String[] {String.valueOf(lessonId)});
    }


    public ArrayList<Mark> getMarks(int lessonId) {
        String selectQuery = "SELECT * FROM " + MARK_TABLE_NAME + " WHERE " + MARK_COLUMN_LESSON_ID + " =?";

        Cursor cursor = getCursor(selectQuery, new String[]{String.valueOf(lessonId)});

        if(cursor == null) {
            return null;
        }

        ArrayList<Mark> marks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Mark mark = new Mark(cursor.getInt(cursor.getColumnIndex(LessonDBInterface.MARK_COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(LessonDBInterface.MARK_COLUMN_VALUE)),
                        cursor.getString(cursor.getColumnIndex(LessonDBInterface.MARK_COLUMN_TYPE)));
                marks.add(mark);
            }
            while (cursor.moveToNext());
        }

        cursor.close();

        if(marks.size() == 0) {
            return null;
        } else {
            return marks;
        }
    }
}

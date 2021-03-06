package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.Subject;
import com.example.nsity.schooldiary.navigation.timetable.Time;
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
public class LessonsDBInterface extends ADBWorker {

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



    public static final String HOMEWORK_TABLE_NAME = "HOMEWORK";
    public static final String HOMEWORK_COLUMN_ID = "HOMEWORK_ID";
    public static final String HOMEWORK_COLUMN_LESSON_ID = "LESSON_ID";
    public static final String HOMEWORK_COLUMN_COMPLETE = "HOMEWORK_COMPLETE";


    public static final String HOMEWORK_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + HOMEWORK_TABLE_NAME + "("
            + HOMEWORK_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HOMEWORK_COLUMN_COMPLETE + " INTEGER, "
            + HOMEWORK_COLUMN_LESSON_ID + " INTEGER " + ");";


    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        if (dropAllData) {
            delete(LESSON_TABLE_NAME, null, null);
            delete(MARK_TABLE_NAME, null, null);
        }
        return addLessons(objects);
    }

    public void deleteHomeworkForWeek(String beginDate, String endDate) {
        delete(LESSON_TABLE_NAME, LESSON_COLUMN_DATE + " >= ? AND " + LESSON_COLUMN_DATE + " <= ?", new String[]{beginDate, endDate});
    }


    private int addLessons(JSONArray lessons) {
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

    public LessonsDBInterface(Context context) {
        super(context);
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
                lesson = setLessonFields(cursor);
            }
            while (cursor.moveToNext());
        } else {
            lesson.setId(-1);
        }

        cursor.close();

        return lesson;
    }


    public void deleteLesson(int lessonId) {
        delete(MARK_TABLE_NAME, LESSON_COLUMN_ID + " =?", new String[]{String.valueOf(lessonId)});
        delete(LESSON_TABLE_NAME, LESSON_COLUMN_ID + " =?", new String[]{String.valueOf(lessonId)});
        //delete(HOMEWORK_TABLE_NAME, HOMEWORK_COLUMN_LESSON_ID + " =?", new String[]{String.valueOf(lessonId)});
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
                Mark mark = new Mark(cursor.getInt(cursor.getColumnIndex(LessonsDBInterface.MARK_COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(LessonsDBInterface.MARK_COLUMN_VALUE)),
                        cursor.getString(cursor.getColumnIndex(LessonsDBInterface.MARK_COLUMN_TYPE)));
                marks.add(mark);
            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return marks.size() == 0? null : marks;
    }


    public ArrayList<Lesson> getLessons(String beginDate, String endDate, boolean withHomework) {
        ArrayList<Lesson> lessons = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + LESSON_TABLE_NAME + " t JOIN " + SubjectsClassDBInterface.SUBJECTS_CLASS_TABLE_NAME
                + " s ON t." + LESSON_COLUMN_SUBJECTS_CLASS_ID + " = s." + SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_ID +
                " JOIN " + TimeDBInterface.TIME_TABLE_NAME + " time ON t." + LESSON_COLUMN_TIME_ID + " = time." +
                TimeDBInterface.TIME_COLUMN_ID + " WHERE " + LESSON_COLUMN_DATE + " >= ? AND " + LESSON_COLUMN_DATE + " <= ? ";
        if(!withHomework) {
            selectQuery += " AND " + LESSON_COLUMN_HOMEWORK + " != '' ";
        }

        selectQuery += " ORDER BY " + LESSON_COLUMN_DATE;

        Cursor cursor = getCursor(selectQuery, new String[]{beginDate, endDate});

        if(cursor == null) {
            return null;
        }

        if (cursor.moveToFirst()) {
            do {
                lessons.add(setLessonFields(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return lessons;
    }

    public ArrayList<Lesson> getLessons() {
        ArrayList<Lesson> lessons = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + LESSON_TABLE_NAME + " t JOIN " + SubjectsClassDBInterface.SUBJECTS_CLASS_TABLE_NAME
                + " s ON t." + LESSON_COLUMN_SUBJECTS_CLASS_ID + " = s." + SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_ID +
                " JOIN " + TimeDBInterface.TIME_TABLE_NAME + " time ON t." + LESSON_COLUMN_TIME_ID + " = time." +
                TimeDBInterface.TIME_COLUMN_ID + " ORDER BY " + LESSON_COLUMN_DATE;

        Cursor cursor = getCursor(selectQuery, new String[]{});

        if(cursor == null) {
            return null;
        }

        if (cursor.moveToFirst()) {
            do {
                lessons.add(setLessonFields(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return lessons;
    }


    private Lesson setLessonFields(Cursor cursor) {
        Lesson lesson = new Lesson();

        int lessonId = cursor.getInt(cursor.getColumnIndex(LessonsDBInterface.LESSON_COLUMN_ID));

        lesson.setId(lessonId);
        lesson.setDate(cursor.getString(cursor.getColumnIndex(LessonsDBInterface.LESSON_COLUMN_DATE)));
        lesson.setHomework(cursor.getString(cursor.getColumnIndex(LessonsDBInterface.LESSON_COLUMN_HOMEWORK)));
        lesson.setTheme(cursor.getString(cursor.getColumnIndex(LessonsDBInterface.LESSON_COLUMN_THEME)));
        lesson.setNote(cursor.getString(cursor.getColumnIndex(LessonsDBInterface.LESSON_COLUMN_NOTE)));
        lesson.setPass(cursor.getString(cursor.getColumnIndex(LessonsDBInterface.LESSON_COLUMN_PASS)));

        lesson.setSubject(new Subject(cursor.getInt(cursor.getColumnIndex(LESSON_COLUMN_SUBJECTS_CLASS_ID)),
                cursor.getString(cursor.getColumnIndex(SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_SUBJECT_NAME)),
                cursor.getInt(cursor.getColumnIndex(SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_COLOR))));


        lesson.setTime(new Time(cursor.getInt(cursor.getColumnIndex(LESSON_COLUMN_TIME_ID)),
                cursor.getString(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_START)),
                cursor.getString(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_END))));


        lesson.setIsHomeworkCompleted(getLessonIsHomeworkCompleted(lessonId));

        return lesson;
    }


    public boolean getLessonIsHomeworkCompleted(int lessonId) {
        String selectQuery = "SELECT * FROM " + HOMEWORK_TABLE_NAME + " WHERE " + HOMEWORK_COLUMN_LESSON_ID + " =?";

        Cursor cursor = getCursor(selectQuery, new String[]{String.valueOf(lessonId)});

        if(cursor == null) {
            return false;
        }

        boolean isHomeworkCompleted = false;

        if (cursor.moveToFirst()) {
            isHomeworkCompleted = cursor.getInt(cursor.getColumnIndex(LessonsDBInterface.HOMEWORK_COLUMN_COMPLETE)) == 1;
        }

        cursor.close();

        return isHomeworkCompleted;
    }


    public void setLessonIsHomeworkCompleted(int lessonId, int isChecked) {
        String selectQuery = "SELECT * FROM " + HOMEWORK_TABLE_NAME + " WHERE " + HOMEWORK_COLUMN_LESSON_ID + " =?";

        Cursor cursor = getCursor(selectQuery, new String[]{String.valueOf(lessonId)});

        if(cursor == null) {
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(HOMEWORK_COLUMN_LESSON_ID, lessonId);
        cv.put(HOMEWORK_COLUMN_COMPLETE, isChecked);

        if(cursor.getCount() == 0) {
            insert(HOMEWORK_TABLE_NAME, cv);
        } else {

            int homeworkId = -1;

            if (cursor.moveToFirst()) {
                homeworkId = cursor.getInt(cursor.getColumnIndex(LessonsDBInterface.HOMEWORK_COLUMN_ID));
            }

            update(HOMEWORK_TABLE_NAME, cv, HOMEWORK_COLUMN_ID + " = ?", new String [] {String.valueOf(homeworkId)});

        }

        cursor.close();
        closeDB();
    }
}

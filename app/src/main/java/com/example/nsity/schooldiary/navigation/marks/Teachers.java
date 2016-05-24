package com.example.nsity.schooldiary.navigation.marks;

import android.content.Context;

import com.example.nsity.schooldiary.system.ListBaseEntity;
import com.example.nsity.schooldiary.system.database.tables.TeachersDBInterface;

import java.util.ArrayList;

/**
 * Created by nsity on 19.04.16.
 */
public class Teachers extends ListBaseEntity {

    private ArrayList<Teacher> teachersArrayList;
    private TeachersDBInterface db;

    public Teachers(Context context) {
        super(context);
        db = new TeachersDBInterface(context);
        teachersArrayList = new ArrayList<>();
        loadFromDB();
    }

    @Override
    protected void loadFromDB() {
        teachersArrayList.clear();
        teachersArrayList = db.getTeachers();
    }

    public ArrayList<Teacher> getTeachers() {
        return teachersArrayList;
    }


    public String[] getNames() {
        String[] names = new String[teachersArrayList.size()];

        for (int i=0; i < teachersArrayList.size(); i++) {
            names[i] = teachersArrayList.get(i).getName();
        }

        return names;
    }


    public Teacher findTeacherById(int id) {
        for(Teacher teacher: teachersArrayList) {
            if(teacher.getId() == id) {
                return teacher;
            }
        }

        return null;
    }

}

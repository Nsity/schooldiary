package com.example.nsity.schooldiary.navigation.marks;

import com.example.nsity.schooldiary.system.BaseEntity;

/**
 * Created by nsity on 17.04.16.
 */
public class Teacher extends BaseEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

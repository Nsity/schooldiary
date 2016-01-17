package com.example.nsity.schooldiary.navigation.marks;

import com.example.nsity.schooldiary.system.BaseEntity;

/**
 * Created by nsity on 30.11.15.
 */
public class Mark extends BaseEntity {

    private int value;
    private String type;

    public Mark() {
    }

    public Mark(int id, int value, String type) {
        this.id = id;
        this.value = value;
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

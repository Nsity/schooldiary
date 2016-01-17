package com.example.nsity.schooldiary.system;

import java.io.Serializable;

/**
 * Created by nsity on 03.01.16.
 */
public abstract class BaseEntity implements Serializable {

    protected int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}

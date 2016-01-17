package com.example.nsity.schooldiary.system;

import android.content.Context;

/**
 * Created by nsity on 03.01.16.
 */
public abstract class ListBaseEntity {

    protected Context context;
    protected abstract void loadFromDB();

    public ListBaseEntity(Context context){
        this.context = context;
    }
}
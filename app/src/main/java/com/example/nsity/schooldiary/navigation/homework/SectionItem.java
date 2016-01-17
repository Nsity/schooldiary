package com.example.nsity.schooldiary.navigation.homework;

import com.example.nsity.schooldiary.system.BaseEntity;

/**
 * Created by nsity on 17.01.16.
 */
public class SectionItem extends BaseEntity {

    private String title;

    public SectionItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

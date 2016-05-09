package com.example.nsity.schooldiary.navigation.messages;

import com.example.nsity.schooldiary.navigation.marks.Teacher;
import com.example.nsity.schooldiary.system.BaseEntity;

/**
 * Created by nsity on 22.04.16.
 */
public class Message extends BaseEntity {
    private String message;
    private String createdAt;
    private int userId;

    public Message() {
    }

    public Message(int id, String message, String createdAt) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
package com.example.nsity.schooldiary.navigation.messages;

import com.example.nsity.schooldiary.system.BaseEntity;

/**
 * Created by nsity on 22.04.16.
 */
public class Message extends BaseEntity {
    private String message;
    private String createdAt;
    private int userId;
    private int type;
    private int read;

    public Message() {
    }

    public Message(int id, String message, String createdAt, int userId) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.userId = userId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }
}
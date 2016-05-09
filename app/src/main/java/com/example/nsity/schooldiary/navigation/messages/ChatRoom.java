package com.example.nsity.schooldiary.navigation.messages;

import com.example.nsity.schooldiary.system.BaseEntity;

/**
 * Created by nsity on 22.04.16.
 */
public class ChatRoom extends BaseEntity {

    private String name;
    private String lastMessage;
    private String timestamp;
    private int unreadCount;

    public ChatRoom() {
    }

    public ChatRoom(int id, String name, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

package com.example.nsity.schooldiary.navigation.messages;


import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.BaseEntity;
import com.example.nsity.schooldiary.system.database.tables.MessageDBInterface;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.util.ArrayList;

/**
 * Created by nsity on 22.04.16.
 */
public class ChatRoom extends BaseEntity {

    private ArrayList<Message> messageArrayList;
    private Context context;
    private MessageDBInterface db;
    private int teacherId;

    private String lastMessage;
    private String timestamp;
    private int unreadCount;


    public ChatRoom(int teacherId, Context context) {
        this.db = new MessageDBInterface(context);
        this.context = context;
        this.messageArrayList = new ArrayList<>();
        this.teacherId = teacherId;

        loadMessagesFromDB();
    }

    public ArrayList<Message> getMessages() {
        return messageArrayList;
    }


    public void loadMessagesFromDB() {
        if(teacherId == 0) {
            return;
        }
        messageArrayList.clear();
        messageArrayList = db.getMessagesInConversation(teacherId);
    }


    public ChatRoom(int id, int teacherId, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
        this.teacherId = teacherId;
    }


    public int getTeacherId() {
        return teacherId;
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



    public void loadMessages(final CallBack callBack) {
        if (!Server.isOnline(context)) {
            callBack.onFail(context.getString(R.string.internet_problem));
            return;
        }

        MessageManager.getMessagesInConversation(teacherId, context, new CallBack() {
            @Override
            public void onSuccess() {
                loadMessagesFromDB();
                callBack.onSuccess();
            }

            @Override
            public void onFail(String error) {
                callBack.onFail(error);
            }
        });
    }
}

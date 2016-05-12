package com.example.nsity.schooldiary.navigation.messages;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonManager;
import com.example.nsity.schooldiary.system.ListBaseEntity;
import com.example.nsity.schooldiary.system.database.tables.MessageDBInterface;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.util.ArrayList;

/**
 * Created by nsity on 11.05.16.
 */
public class ChatRooms extends ListBaseEntity {

    private ArrayList<ChatRoom> arrayList;
    private MessageDBInterface db;

    public ChatRooms(Context context) {
        super(context);
        db = new MessageDBInterface(context);
        arrayList = new ArrayList<>();
        loadFromDB();
    }


    @Override
    protected void loadFromDB() {
        arrayList.clear();
        arrayList = db.getChatRooms();
    }


    public void loadLastMessages(final CallBack callBack) {
        if (!Server.isOnline(context)) {
            callBack.onFail(context.getString(R.string.internet_problem));
            return;
        }

        MessageManager.getLastMessages(context, new CallBack<String>() {
            @Override
            public void onSuccess(String date) {
                loadFromDB();
                callBack.onSuccess(date);
            }

            @Override
            public void onFail(String error) {
                callBack.onFail(error);
            }

        });
    }
}

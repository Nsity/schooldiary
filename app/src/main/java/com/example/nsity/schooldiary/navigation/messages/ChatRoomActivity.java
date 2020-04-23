package com.example.nsity.schooldiary.navigation.messages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.Teacher;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.Utils;
import com.example.nsity.schooldiary.system.database.tables.MessageDBInterface;
import com.example.nsity.schooldiary.system.network.CallBack;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by nsity on 19.04.16.
 */
public class ChatRoomActivity extends AppCompatActivity {


    public static final String CHAT_ROOM_RECEIVER = "chat_room_receiver";

    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private BroadcastReceiver mBroadcastReceiver;
    private EditText inputMessage;
    private ArrayList<Message> messageArrayList;
    private View mProgressView;
    private Teacher teacher;

    // private MaterialProgressBar sendProgressBar;

    private ChatRoom chatRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        teacher = (Teacher)intent.getSerializableExtra(Utils.TEACHER);


        chatRoom = new ChatRoom(teacher.getId(), this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(toolbar != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(teacher.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
       // sendProgressBar = (MaterialProgressBar) findViewById(R.id.progress_item);
        mProgressView = findViewById(R.id.progress);

        inputMessage = (EditText) findViewById(R.id.message);

        Button btnSend = (Button) findViewById(R.id.btn_send);
        if (btnSend != null) {
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage();
                }
            });
        }

        messageArrayList = new ArrayList<>();
        mAdapter = new ChatRoomThreadAdapter(this, messageArrayList);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setScrollContainer(true);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handlePushNotification(intent);
            }
        };

        fetchChatThread();


        MessageManager.readConversation(teacher.getId(), this, new CallBack());
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(CHAT_ROOM_RECEIVER));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     */
    private void handlePushNotification(Intent intent) {
        Message message = (Message) intent.getSerializableExtra(Utils.MESSAGE_PUSH);

        if (message != null && message.getUserId() == teacher.getId()) {
            MessageManager.readConversation(teacher.getId(), this, new CallBack());
            messageArrayList.add(message);
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }
        }
    }


    private void sendMessage() {
        final String strMessage = inputMessage.getText().toString().trim();
        if (TextUtils.isEmpty(strMessage)) {
            return;
        }

        final Message message = new Message();
        message.setMessage(strMessage);
        message.setCreatedAt(CommonFunctions.getDate(Calendar.getInstance().getTime(), CommonFunctions.FORMAT_YYYY_MM_DD_HH_MM_SS));
        message.setUserId(MessageDBInterface.PUPIL_TYPE);
        message.setRead(0);

        inputMessage.setText("");

       // sendProgressBar.setVisibility(View.VISIBLE);
        MessageManager.createMessage(this, message, chatRoom.getTeacherId(), new CallBack<Integer>() {
            @Override
            public void onSuccess(Integer messageId) {
                // sendProgressBar.setVisibility(View.GONE);

                message.setId(messageId);
                messageArrayList.add(message);

                mAdapter.notifyDataSetChanged();
                if (mAdapter.getItemCount() > 1) {
                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void onFail(String message) {
                //  sendProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchChatThread() {
        if (chatRoom.getMessages().size() != 0) {
            messageArrayList.addAll(chatRoom.getMessages());

            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }

            return;
        }
        CommonFunctions.showProgress(true, getApplicationContext(), recyclerView, mProgressView);
        chatRoom.loadMessages(new CallBack() {
            @Override
            public void onSuccess() {
                CommonFunctions.showProgress(false, getApplicationContext(), recyclerView, mProgressView);

                messageArrayList.addAll(chatRoom.getMessages());

                mAdapter.notifyDataSetChanged();
                if (mAdapter.getItemCount() > 1) {
                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void onFail(String message) {
                CommonFunctions.showProgress(false, getApplicationContext(), recyclerView, mProgressView);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
       // overridePendingTransition(R.anim.s_in, R.anim.s_out);
    }

}
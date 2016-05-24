package com.example.nsity.schooldiary.navigation.messages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.Teacher;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.Utils;
import com.example.nsity.schooldiary.system.database.tables.MessageDBInterface;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Handler;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by nsity on 19.04.16.
 */
public class ChatRoomActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private EditText inputMessage;
    private ArrayList<Message> messageArrayList;
    private View mProgressView;

   // private MaterialProgressBar sendProgressBar;

    private ChatRoom chatRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        Teacher teacher = (Teacher)intent.getSerializableExtra(Utils.TEACHER);

        chatRoom = new ChatRoom(teacher.getId(), this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(toolbar != null) {
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



       /* recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                //add progress item
                messageArrayList.add(null);
                mAdapter.notifyItemInserted(messageArrayList.size());


                messageArrayList.remove(messageArrayList.size() - 1);
                        mAdapter.notifyItemRemoved(messageArrayList.size());

            }
        });*/


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Utils.MESSAGE_PUSH)) {
                    handlePushNotification(intent);
                }
            }
        };

        fetchChatThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Utils.MESSAGE_PUSH));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     */
    private void handlePushNotification(Intent intent) {
        Message message = (Message) intent.getSerializableExtra("message");
        String chatRoomId = intent.getStringExtra("chat_room_id");

        if (message != null && chatRoomId != null) {
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
        finish();
        overridePendingTransition(R.anim.s_in, R.anim.s_out);
        hideKeyboard();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Server.getHttpClient().cancelRequests(this, true);
        hideKeyboard();
    }


    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
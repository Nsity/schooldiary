package com.example.nsity.schooldiary.navigation.messages;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.MainActivity;
import com.example.nsity.schooldiary.navigation.marks.Teacher;
import com.example.nsity.schooldiary.navigation.marks.Teachers;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.Utils;
import com.example.nsity.schooldiary.system.database.tables.MessageDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TeachersDBInterface;
import com.example.nsity.schooldiary.system.gcm.GCMIntentService;
import com.example.nsity.schooldiary.system.gcm.ServiceRegister;
import com.example.nsity.schooldiary.system.network.CallBack;

import java.util.ArrayList;

/**
 * Created by nsity on 17.04.16.
 */
public class MessagesFragment extends Fragment {

    private ArrayList<ChatRoom> chatRoomArrayList;
    private RecyclerView recyclerView;
    private ChatRoomsAdapter mAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ChatRooms chatRooms;

    private BroadcastReceiver mBroadcastReceiver;

    public static final String MESSAGES_RECEIVER = "messages_receiver";

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    private String query = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && isAdded()) {
            actionBar.setTitle(getString(R.string.nav_messages));
        }

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.addOnItemTouchListener(new ChatRoomsAdapter.RecyclerTouchListener(getActivity(),
                recyclerView, new ChatRoomsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                openNewChat(new TeachersDBInterface(getActivity()).getTeacherById(chatRoomArrayList.get(position).getTeacherId()));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        setView();

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                chatRooms.update(new CallBack() {
                    @Override
                    public void onSuccess() {
                        swipeRefreshLayout.setRefreshing(false);
                        setView();
                    }

                    @Override
                    public void onFail(String message) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                });


                setView();
            }
        });

        if (ServiceRegister.checkPlayServices(getActivity())) {
            registerGCM();
        }


        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Message message = (Message) intent.getSerializableExtra(Utils.MESSAGE_PUSH);

                if (message != null) {
                    for(int i = 0; i < chatRoomArrayList.size(); i++) {
                        ChatRoom chatRoom = chatRoomArrayList.get(i);
                        if(message.getUserId() == chatRoom.getTeacherId()) {

                            int count = chatRoom.getUnreadCount();
                            count = count + 1;
                            chatRoom.setUnreadCount(count);
                            chatRoom.setLastMessage(message.getMessage());
                            chatRoom.setTimestamp(message.getCreatedAt());

                            chatRoomArrayList.remove(i);
                            chatRoomArrayList.add(0, chatRoom);

                            mAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }
            }
        };


        return rootView;
    }


    private void registerGCM() {
        Intent intent = new Intent(getActivity(), GCMIntentService.class);
        intent.putExtra(GCMIntentService.KEY, GCMIntentService.REGISTER);
        getActivity().startService(intent);
    }


    private void setView() {
        chatRoomArrayList = new ArrayList<>();
        if(query != null && !query.equals(""))  {
            ArrayList<ChatRoom> searchChatRooms = new MessageDBInterface(getActivity()).findChatRoomsByQuery(query);
            chatRoomArrayList.addAll(searchChatRooms);
        } else {
            chatRooms = new ChatRooms(getActivity());
            chatRoomArrayList.addAll(chatRooms.getChatRooms());
        }

        mAdapter = new ChatRoomsAdapter(getActivity(), chatRoomArrayList);
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, new IntentFilter(MESSAGES_RECEIVER));
        setView();

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.messages_menu, menu);


        SearchManager searchManager =  (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
        {
            public boolean onQueryTextChange(String newText)
            {
                query = newText;
                setView();
                return true;
            }

            public boolean onQueryTextSubmit(String newText)
            {
                query = newText;
                setView();
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);

    }


    private void searchData(String query) {
        if(query.equals("")) {
            setView();
        } else {
            ArrayList<ChatRoom> searchChatRooms = new MessageDBInterface(getActivity()).findChatRoomsByQuery(query);
            chatRoomArrayList = new ArrayList<>();
            chatRoomArrayList.addAll(searchChatRooms);
            mAdapter = new ChatRoomsAdapter(getActivity(), chatRoomArrayList);
            recyclerView.setAdapter(mAdapter);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_chat:
                showDialog();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean isTeacherInChat(Teacher teacher) {
        for(ChatRoom chat: chatRoomArrayList) {
            if(teacher.getId() == chat.getTeacherId()) {
                return true;
            }
        }
        return false;
    }

    private void showDialog() {
        final Teachers teachers = new Teachers(getActivity());


        final ArrayList<Teacher> teachersForDialog = new ArrayList<>();
        for(Teacher teacher: teachers.getTeachers()) {
            boolean flag = isTeacherInChat(teacher);
            if(!flag) {
                teachersForDialog.add(teacher);
            }
        }


        if(teachersForDialog.size() == 0) {
            Toast.makeText(getActivity(), R.string.not_available_teachers, Toast.LENGTH_SHORT).show();
            return;
        }

        String[] names = new String[teachersForDialog.size()];

        for(int i = 0; i < names.length; i++) {
            names[i] = teachersForDialog.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.choose_interlocutor))
                .setItems(names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        openNewChat(teachersForDialog.get(item));
                    }
                })
                .setCancelable(true)
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void openNewChat(Teacher teacher) {
        Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
        intent.putExtra(Utils.TEACHER, teacher);
        startActivity(intent);
    }
}

package com.example.nsity.schooldiary.navigation.messages;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


        setView();

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
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

        return rootView;
    }


    private void registerGCM() {
        Intent intent = new Intent(getActivity(), GCMIntentService.class);
        intent.putExtra(GCMIntentService.KEY, GCMIntentService.REGISTER);
        getActivity().startService(intent);
    }


    private void setView() {
        loadChatRooms();
    }


    @Override
    public void onResume() {
        super.onResume();

        setView();
    }

    private void loadChatRooms() {
        setRecyclerView();

        /*CommonFunctions.showProgress(true, getActivity(),  recyclerView, mProgressView);
        chatRooms.loadLastMessages(new CallBack() {
            @Override
            public void onSuccess() {
                CommonFunctions.showProgress(false, getActivity(),  recyclerView, mProgressView);
                setRecyclerView();
            }

            @Override
            public void onFail(String error) {
                recyclerView.setVisibility(View.GONE);
                try {
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
                final boolean show = false;
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mProgressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            }
        });*/
    }


    private void setRecyclerView() {
        chatRoomArrayList = new ArrayList<>();
        chatRooms = new ChatRooms(getActivity());
        chatRoomArrayList.addAll(chatRooms.getChatRooms());
        mAdapter = new ChatRoomsAdapter(getActivity(), chatRoomArrayList);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.messages_menu, menu);
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


    private void showDialog() {
        final Teachers teachers = new Teachers(getActivity());
        final String[] mTeachersName = teachers.getNames();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.choose_interlocutor))
                .setItems(mTeachersName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        openNewChat(teachers.getTeachers().get(item));
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

package com.example.nsity.schooldiary.navigation.messages;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.MainActivity;
import com.example.nsity.schooldiary.navigation.marks.Teacher;
import com.example.nsity.schooldiary.navigation.marks.Teachers;
import com.example.nsity.schooldiary.system.Utils;

import java.util.ArrayList;

/**
 * Created by nsity on 17.04.16.
 */
public class MessagesFragment extends Fragment {

    private ArrayList<ChatRoom> chatRoomArrayList;
    private RecyclerView recyclerView;
    private ChatRoomsAdapter mAdapter;
    private View mProgressView;

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
        mProgressView = rootView.findViewById(R.id.progress);

        setView();

        return rootView;
    }


    private void setView() {
        chatRoomArrayList = new ArrayList<>();
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
                ChatRoom chatRoom = chatRoomArrayList.get(position);
                Intent intent = new Intent(getActivity(), ChatRoomActivity.class);

                startActivity(intent);
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

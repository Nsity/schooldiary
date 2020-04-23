package com.example.nsity.schooldiary.navigation.login;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.Preferences;
import com.rey.material.widget.Switch;


/**
 * Created by nsity on 15.11.15.
 */
public class ProfileFragment extends Fragment {

    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && isAdded()) {
            actionBar.setTitle(getString(R.string.nav_profile));
        }

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView mUserTextView = (TextView) rootView.findViewById(R.id.user);
        mUserTextView.setText(Preferences.get(Preferences.FIO, getActivity()));

        TextView mClassTextView = (TextView) rootView.findViewById(R.id.class_text);
        mClassTextView.setText(Preferences.get(Preferences.CLASSNAME, getActivity()));


        Switch notificationLessonSwitch = (Switch) rootView.findViewById(R.id.notification_lesson_switch);
        Switch notificationMarkSwitch = (Switch) rootView.findViewById(R.id.notification_mark_switch);

        if(Preferences.getBoolean(Preferences.NOTIFICATION_LESSON_SETTING, false, getActivity()))
            notificationLessonSwitch.setChecked(true);

        notificationLessonSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                Preferences.set(Preferences.NOTIFICATION_LESSON_SETTING, checked, getActivity());
            }
        });

        if(Preferences.getBoolean(Preferences.NOTIFICATION_SETTING, false, getActivity()))
            notificationMarkSwitch.setChecked(true);

        notificationMarkSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                Preferences.set(Preferences.NOTIFICATION_SETTING, checked, getActivity());
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    private void exit() {
        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.exit))
                .setMessage(getString(R.string.exit_question))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.yes).toUpperCase(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManager.logoff(getActivity());
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                                getActivity().overridePendingTransition(0, 0);
                                alertDialog.dismiss();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.no).toUpperCase(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        })
                .create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                exit();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
}

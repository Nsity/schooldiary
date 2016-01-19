package com.example.nsity.schooldiary.navigation.marks.progress;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.subjects.Subjects;
import com.example.nsity.schooldiary.navigation.timetable.CalendarActivity;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.CommonManager;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by nsity on 06.12.15.
 */
public class ProgressFragment extends Fragment {

    private ListView listView;
    private Subjects subjects;
    private Menu optionsMenu;

    private View mProgressView;
    private View mProgressFormView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && isAdded()) {
            actionBar.setTitle(getString(R.string.nav_progress));
        }

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_progress, container, false);

        listView = (ListView) rootView.findViewById(R.id.progress_list);

        ArrayList<ImageView> imagesArrayList = new ArrayList<>();
        imagesArrayList.add((ImageView) rootView.findViewById(R.id.image_view4));
        imagesArrayList.add((ImageView) rootView.findViewById(R.id.image_view3));
        imagesArrayList.add((ImageView) rootView.findViewById(R.id.image_view2));
        imagesArrayList.add((ImageView) rootView.findViewById(R.id.image_view1));
        imagesArrayList.add((ImageView) rootView.findViewById(R.id.image_view));

        String[] headers = getResources().getStringArray(R.array.periods_short);

        for(int i = 0; i < imagesArrayList.size(); i++) {
            setHeader(imagesArrayList.get(i), headers[i]);
        }

        mProgressView = rootView.findViewById(R.id.progress);
        mProgressFormView = rootView.findViewById(R.id.progress_form);

        setView();

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        optionsMenu = menu;
        inflater.inflate(R.menu.refresh_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                CommonFunctions.setRefreshActionButtonState(true, optionsMenu);
                subjects.loadProgress(new CallBack() {
                    @Override
                    public void onSuccess() {
                        CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
                        listView.setAdapter(new ProgressAdapter(getActivity(), subjects.getSubjects()));

                        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
                        listView.setVisibility(View.INVISIBLE);
                        listView.animate().setDuration(shortAnimTime).alpha(1).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                listView.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onFail(String error) {
                        if (isAdded() && getActivity() != null) {
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
                        }
                    }
                });
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setHeader(ImageView image, String text) {
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .bold()
                .useFont(Typeface.DEFAULT)
                .fontSize(30)
                .endConfig()
                .buildRoundRect(text, Color.GRAY, 8);

        image.setImageDrawable(drawable);
    }

    private void setView() {
        subjects = new Subjects(getActivity());
        if(!subjects.existProgress()) {
            CommonFunctions.showProgress(true, getActivity(), mProgressFormView, mProgressView);
            subjects.loadProgress(new CallBack() {
                @Override
                public void onSuccess() {
                    if (isAdded() && getActivity() != null) {
                        CommonFunctions.showProgress(false, getActivity(), mProgressFormView, mProgressView);
                        listView.setAdapter(new ProgressAdapter(getActivity(), subjects.getSubjects()));
                    }
                }
                @Override
                public void onFail(String error) {
                    if (isAdded() && getActivity() != null) {
                        CommonFunctions.showProgress(false, getActivity(), mProgressFormView, mProgressView);
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            listView.setAdapter(new ProgressAdapter(getActivity(), subjects.getSubjects()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Server.getHttpClient().cancelRequests(getActivity(), true);
    }


}

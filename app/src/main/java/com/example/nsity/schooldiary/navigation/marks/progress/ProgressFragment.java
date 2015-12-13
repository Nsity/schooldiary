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

import java.util.ArrayList;

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
                subjects.loadProgress(getActivity(), new CallBack() {
                    @Override
                    public void onSuccess() {
                        listView.setAdapter(new ProgressAdapter(getActivity(), subjects.getSubjects()));
                        CommonFunctions.setRefreshActionButtonState(false, optionsMenu);

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
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
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
        if(!subjects.existProgress(getActivity())) {
            showProgress(true);
            subjects.loadProgress(getActivity(), new CallBack() {
                @Override
                public void onSuccess() {
                    showProgress(false);
                    listView.setAdapter(new ProgressAdapter(getActivity(), subjects.getSubjects()));
                }

                @Override
                public void onFail(String error) {
                    showProgress(false);
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
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


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mProgressFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

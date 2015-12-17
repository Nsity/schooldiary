package com.example.nsity.schooldiary.navigation.homework.recent;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.homework.Homework;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.CommonManager;
import com.example.nsity.schooldiary.system.network.CallBack;

import java.util.ArrayList;


/**
 * Created by nsity on 13.12.15.
 */
public class RecentHomeworkFragment extends Fragment {

    private HomeworkAdapter homeworkAdapter;
    private EndlessListView endlessListView;

    private ArrayList<Homework> arrayList;

    private View mProgressView;

    private int mCount;
    private boolean mHaveMoreDataToLoad;

    private Menu optionsMenu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_recent_homework, container, false);

        endlessListView = (EndlessListView) rootView.findViewById(R.id.homework);
        mProgressView = rootView.findViewById(R.id.progress);

        arrayList = new ArrayList<>();
        setView(1);


        endlessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Intent intent = new Intent(getActivity(), HomeworkActivity.class);
                intent.putExtra("homework", arrayList.get(position));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });


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
                setView(1);
                return false;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setView(int count) {
        mCount = count;
        showProgress(true);

        arrayList = new ArrayList<>();

        CommonManager.getHomework(getActivity(), mCount, new CallBack<ArrayList<Homework>>() {
            @Override
            public void onSuccess(ArrayList<Homework> homeworkArrayList) {
                showProgress(false);
                homeworkAdapter = new HomeworkAdapter(getActivity(), homeworkArrayList);
                endlessListView.setAdapter(homeworkAdapter);
                endlessListView.setOnLoadMoreListener(loadMoreListener);
                mHaveMoreDataToLoad = true;

                arrayList.addAll(homeworkArrayList);

                CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
            }

            @Override
            public void onFail(String error) {
                showProgress(false);
                mHaveMoreDataToLoad = false;
                CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
            }
        });
    }

    private void loadMoreData() {
        mCount++;
        CommonManager.getHomework(getActivity(), mCount, new CallBack<ArrayList<Homework>>() {
            @Override
            public void onSuccess(ArrayList<Homework> homeworkArrayList) {
                homeworkAdapter.addItems(homeworkArrayList);

                arrayList.addAll(homeworkArrayList);
                endlessListView.loadMoreComplete();

                if(homeworkArrayList.size() == 0) {
                    //mHaveMoreDataToLoad = false;
                    mCount--;
                }

            }

            @Override
            public void onFail(String error) {

            }
        });
    }


    private EndlessListView.OnLoadMoreListener loadMoreListener = new EndlessListView.OnLoadMoreListener() {
        @Override
        public boolean onLoadMore() {
            if (mHaveMoreDataToLoad) {
                loadMoreData();
            }

            return mHaveMoreDataToLoad;
        }
    };



    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            endlessListView.setVisibility(show ? View.GONE : View.VISIBLE);
            endlessListView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    endlessListView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            endlessListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

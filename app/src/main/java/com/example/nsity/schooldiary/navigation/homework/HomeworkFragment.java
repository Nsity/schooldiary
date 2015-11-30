package com.example.nsity.schooldiary.navigation.homework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.nsity.schooldiary.R;

import java.util.ArrayList;

/**
 * Created by nsity on 15.11.15.
 */
public class HomeworkFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView mHomeworkListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.activity_calendar, container, false);

        /*mHomeworkListView = (ListView) rootView.findViewById(R.id.homework_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        setView();*/


/*
        CompactCalendarView compactCalendarView = (CompactCalendarView) rootView.findViewById(R.id.compactcalendar_view);
        compactCalendarView.drawSmallIndicatorForEvents(true);
        compactCalendarView.setLocale(new Locale("ru"));
        compactCalendarView.setUseThreeLetterAbbreviation(true);*/

        return rootView;
    }


    private void setView() {

        /*ArrayList<Homework> homeworkArrayList = new ArrayList<>();

        homeworkArrayList .add(new Homework(1, "sdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwet", "sdgsdgsdsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwet"));
        homeworkArrayList .add(new Homework(1, "sdgds", "sdgsdgsd"));
        homeworkArrayList .add(new Homework(1, "sdgds", "sdgsdgsd"));

        homeworkArrayList .add(new Homework(1, "sdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwet", "sdgsdsdgsdgsdsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwgsd"));
        homeworkArrayList .add(new Homework(1, "sdgdssdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwet", "sdgsdsdgsdgsdsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwgsdgsdgsdsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwsdgsdgsdsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwsd"));
        homeworkArrayList .add(new Homework(1, "sdgdsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwets", "sdgsdgsd"));
        homeworkArrayList .add(new Homework(1, "sdgsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetds", "sdgsdgsd"));
        homeworkArrayList .add(new Homework(1, "sdsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetgds", "sdgsdgssdgsdgsdsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwsdgsdgsdsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwd"));
        homeworkArrayList .add(new Homework(1, "sdgds", "sdgsdgsdgsdgsdsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwsd"));
        homeworkArrayList .add(new Homework(1, "sdgds", "sdgsdgsd"));
        homeworkArrayList .add(new Homework(1, "sdgds", "sdgsdgsd"));
        homeworkArrayList .add(new Homework(1, "sdgds", "sdgssdgsdgsdsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwdgsd"));
        homeworkArrayList .add(new Homework(1, "sdgds", "sdgsdgsd"));
        homeworkArrayList .add(new Homework(1, "sdgds", "sdgsdsdgsdgsdsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwsdgsdgsdsdfsdgsdgwetwtwtwetwtwetwtwtwtwetewtwetwetsdfsdgsdgwetwtwgsd"));
        homeworkArrayList .add(new Homework(1, "sdgds", "sdgsdgsd"));

        HomeworkAdapter homeworkAdapter = new HomeworkAdapter(getActivity(), homeworkArrayList);

        mHomeworkListView.setAdapter(homeworkAdapter);*/
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
package com.example.nsity.schooldiary.navigation.timetable;


import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.MainActivity;
import com.example.nsity.schooldiary.navigation.lesson.LessonActivity;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.database.tables.TimetableDBInterface;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by nsity on 15.11.15.
 */
public class TimetableFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView mTodayView;
    private ArrayList<Timetable> timetableArrayList;
    private TextView mTextView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_timetable, container, false);
        mTodayView = (ListView) rootView.findViewById(R.id.timetable);
        mTextView = (TextView) rootView.findViewById(R.id.text);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        setView();
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.timetable_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_week_view:
                startActivity(new Intent(getActivity(), CalendarActivity.class));
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }


    public void setView () {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        timetableArrayList = new TimetableDBInterface(getActivity()).getTimetable(dayOfWeek);
        if(timetableArrayList.size() == 0) {

            mTodayView.setVisibility(View.INVISIBLE);
            mTextView.setVisibility(View.VISIBLE);

            Random rnd = new Random();
            int k = rnd.nextInt(3);
            switch (k) {
                case 0:
                    mTextView.setText(getActivity().getResources().getString(R.string.no_lessons_1));
                    break;
                case 1:
                    mTextView.setText(getActivity().getResources().getString(R.string.no_lessons_2));
                    break;
                case 2:
                    mTextView.setText(getActivity().getResources().getString(R.string.no_lessons_3));
                    break;

                default:
                    break;
            }
        } else {
            mTodayView.setVisibility(View.VISIBLE);
            mTextView.setVisibility(View.INVISIBLE);

            mTodayView.setAdapter(new TimetableAdapter(getActivity(), timetableArrayList));
            mTodayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                        long id) {
                    Timetable timetable = timetableArrayList.get(position);
                    Intent intent = new Intent(getActivity(), LessonActivity.class);
                    intent.putExtra("subjectId", timetable.getSubjectId());
                    intent.putExtra("subjectName", timetable.getSubject());
                    intent.putExtra("timeId", timetable.getTimeId());

                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String currentDate = sdf.format(cal.getTime());

                    intent.putExtra("day", currentDate);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        if (!Server.isOnline(getActivity())) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.internet_problem), Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

         TimetableManager.getTimetable(getActivity(), Preferences.get(Preferences.CLASSID, getActivity()), new CallBack() {
             @Override
             public void onSuccess() {
                 setView();
                 mSwipeRefreshLayout.setRefreshing(false);
             }

             @Override
             public void onFail(String message) {
                 Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                 mSwipeRefreshLayout.setRefreshing(false);
             }
         });
    }
}

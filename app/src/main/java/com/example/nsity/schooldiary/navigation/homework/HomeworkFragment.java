package com.example.nsity.schooldiary.navigation.homework;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.lesson.Lesson;
import com.example.nsity.schooldiary.system.SwipeListener;
import com.example.nsity.schooldiary.system.BaseEntity;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nsity on 17.01.16.
 */
public class HomeworkFragment extends Fragment  {

    private Menu optionsMenu;

    private Date monday;

    private View mProgressView;
    private ListView mHomeworkView;

    private Lessons lessons;

    private ArrayList<BaseEntity> arrayList;

    private TextView mTitleTextView;

    private View swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && isAdded()) {
            actionBar.setTitle(getString(R.string.nav_homework));
        }

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_homework, container, false);

        mProgressView = rootView.findViewById(R.id.progress);
        mHomeworkView = (ListView) rootView.findViewById(R.id.homework_list);

        mTitleTextView = (TextView) rootView.findViewById(R.id.title);

        mTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monday = CommonFunctions.getMonday(Calendar.getInstance().getTime());
                setView();
            }
        });

       // ActivitySwipeDetector swipe = new ActivitySwipeDetector(getActivity(), this);
        //mHomeworkView.setOnTouchListener(swipe);


        mHomeworkView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                BaseEntity lesson = arrayList.get(position);
                if (lesson instanceof Lesson) {
                    Intent intent = new Intent(getActivity(), HomeworkActivity.class);
                    intent.putExtra("lesson", lesson);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            }
        });

        swipeLayout = rootView.findViewById(R.id.swipe_layout);

        mHomeworkView.setOnTouchListener(swipe);
        swipeLayout.setOnTouchListener(swipe);

        monday = CommonFunctions.getMonday(Calendar.getInstance().getTime());
        setView();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        optionsMenu = menu;
        inflater.inflate(R.menu.homework_menu, menu);
    }


    private SwipeListener swipe = new SwipeListener(){
        @Override
        public void onLeftToRightSwipe() {
            CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
            monday = CommonFunctions.addDays(-7, monday);
            Server.getHttpClient().cancelRequests(getActivity(), true);
            setView();
        }

        @Override
        public void onRightToLeftSwipe() {
            CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
            monday = CommonFunctions.addDays(7, monday);
            Server.getHttpClient().cancelRequests(getActivity(), true);
            setView();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                CommonFunctions.setRefreshActionButtonState(true, optionsMenu);
                lessons.loadHomework(CommonFunctions.getDate(monday, CommonFunctions.FORMAT_YYYY_MM_DD),
                        CommonFunctions.getDate(CommonFunctions.addDays(6, monday), CommonFunctions.FORMAT_YYYY_MM_DD), new CallBack<String>() {
                            @Override
                            public void onSuccess(String date) {
                                if (date.equals(CommonFunctions.getDate(monday, CommonFunctions.FORMAT_YYYY_MM_DD))) {
                                    CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
                                    setListAdapter(lessons);
                                }
                            }

                            @Override
                            public void onFail(String error) {
                                if (isAdded() && getActivity() != null) {
                                    mHomeworkView.setAdapter(null);
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                    CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
                                }
                            }
                        });
                return false;
            case R.id.action_next:
                CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
                monday = CommonFunctions.addDays(7, monday);
                Server.getHttpClient().cancelRequests(getActivity(), true);
                setView();
                return false;
            case R.id.action_back:
                CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
                monday = CommonFunctions.addDays(-7, monday);
                Server.getHttpClient().cancelRequests(getActivity(), true);
                setView();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setView() {
        if(monday == null) {
            monday = CommonFunctions.getMonday(Calendar.getInstance().getTime());
        }

        String title = CommonFunctions.getDate(monday, CommonFunctions.FORMAT_DD_MM_YYYY) + " - " +
                CommonFunctions.getDate(CommonFunctions.addDays(6, monday), CommonFunctions.FORMAT_DD_MM_YYYY);

        mTitleTextView.setText(title);

        lessons = new Lessons(getActivity(), CommonFunctions.getDate(monday, CommonFunctions.FORMAT_YYYY_MM_DD),
                CommonFunctions.getDate(CommonFunctions.addDays(6, monday), CommonFunctions.FORMAT_YYYY_MM_DD), false);

        if(lessons.getLessons().size() == 0) {
            CommonFunctions.showProgress(true, getActivity(), mHomeworkView, mProgressView);

            lessons.loadHomework(CommonFunctions.getDate(monday, CommonFunctions.FORMAT_YYYY_MM_DD),
                    CommonFunctions.getDate(CommonFunctions.addDays(6, monday), CommonFunctions.FORMAT_YYYY_MM_DD), new CallBack<String>() {
                        @Override
                        public void onSuccess(String date) {
                            if(date.equals(CommonFunctions.getDate(monday, CommonFunctions.FORMAT_YYYY_MM_DD))) {
                                setListAdapter(lessons);
                            }
                        }

                        @Override
                        public void onFail(String error) {
                            if (isAdded() && getActivity() != null) {
                                swipeLayout.setOnTouchListener(swipe);
                                mHomeworkView.setAdapter(null);
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                CommonFunctions.showProgress(false, getActivity(), mHomeworkView, mProgressView);
                            }
                        }
                    });
        } else {
            setListAdapter(lessons);
        }
    }

    private void setListAdapter(Lessons lessons) {
        CommonFunctions.showProgress(false, getActivity(), mHomeworkView, mProgressView);
        arrayList = new ArrayList<>();
        ArrayList<String> dates = lessons.getDates();

        for (String date : dates) {
            try {
                Date headerDate = new SimpleDateFormat(CommonFunctions.FORMAT_YYYY_MM_DD, new Locale("ru")).parse(date);
                String header = CommonFunctions.getDate(headerDate, "E").toUpperCase() + " " +
                        CommonFunctions.getDate(headerDate, CommonFunctions.FORMAT_E_D_MMMM_YYYY);
                arrayList.add(new SectionItem(header));
                for (Lesson lesson : lessons.getLessons()) {
                    if (lesson.getDate().equals(date)) {
                        arrayList.add(lesson);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        mHomeworkView.setAdapter(new HomeworkAdapter(getActivity(), arrayList));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Server.getHttpClient().cancelRequests(getActivity(), true);
    }
}

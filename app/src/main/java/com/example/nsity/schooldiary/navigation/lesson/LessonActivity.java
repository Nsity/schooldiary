package com.example.nsity.schooldiary.navigation.lesson;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.network.CallBack;

/**
 * Created by nsity on 18.11.15.
 */
public class LessonActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {


    private AppBarLayout appBarLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setSubtitle("ergre");
        toolbar.setTitle(getIntent().getStringExtra("subjectName"));

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRefresh() {

        Intent intent = getIntent();

        String subjectId = intent.getStringExtra("subjectId");
        String timeId = intent.getStringExtra("timeId");
        String day = intent.getStringExtra("day");

        LessonManager.getLesson(this, Preferences.get(Preferences.PUPILID, this), subjectId, day, timeId, new CallBack() {
            @Override
            public void onSuccess() {
                mSwipeRefreshLayout.setRefreshing(false);
                //todayView.setAdapter(new TimetableAdapter(getActivity(), new TimetableDBInterface(getActivity()).getTimetable(dayOfWeek)));
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.s_in, R.anim.s_out);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            mSwipeRefreshLayout.setEnabled(true);
        } else {
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }
}

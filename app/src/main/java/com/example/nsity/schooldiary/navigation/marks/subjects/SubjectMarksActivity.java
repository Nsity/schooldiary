package com.example.nsity.schooldiary.navigation.marks.subjects;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.timetable.Period;
import com.example.nsity.schooldiary.navigation.timetable.Periods;
import com.example.nsity.schooldiary.navigation.marks.Subject;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.network.CallBack;

import java.util.ArrayList;

/**
 * Created by nsity on 06.12.15.
 */
public class SubjectMarksActivity extends AppCompatActivity {

    private Subject subject;
    private Periods periods;

    private View mProgressView;
    private View mSubjectFormView;
    private ListView marksListView;
    private TextView textView;
    private TextView mPeriodTextView;

    private Menu optionsMenu;
    private MenuItem checkedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_marks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        subject = (Subject) getIntent().getSerializableExtra("subject");
        if(subject != null) {
            toolbar.setTitle(subject.getName());
            toolbar.setBackgroundColor(CommonFunctions.setColor(this, subject.getColor()));
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        marksListView = (ListView) findViewById(R.id.marks);
        mProgressView = findViewById(R.id.progress);
        mSubjectFormView = findViewById(R.id.subject_form);
        textView = (TextView) findViewById(R.id.text);
        mPeriodTextView = (TextView) findViewById(R.id.period);

        periods = new Periods(this);
        setView(periods.getCurrentPeriod().getName());
    }


    private void setView(String name) {
        if(name.equals(getResources().getString(R.string.all_period)))
            name = getResources().getString(R.string.year_period);

        Period period = periods.getPeriodByName(name);

        if(period == null)
            return;

        String periodStr = (period.getName().equals(getResources().getString(R.string.year_period)) ?
                getResources().getString(R.string.all_period) : period.getName()) + " (" +
                CommonFunctions.getDate(period.getPeriodStart(), CommonFunctions.FORMAT_YYYY_MM_DD, CommonFunctions.FORMAT_DD_MM_YYYY) + " - " +
                CommonFunctions.getDate(period.getPeriodEnd(), CommonFunctions.FORMAT_YYYY_MM_DD, CommonFunctions.FORMAT_DD_MM_YYYY) +  ")";

        mPeriodTextView.setText(periodStr);

        CommonFunctions.showProgress(true, getApplicationContext(), mSubjectFormView, mProgressView);
        subject.loadMarks(getApplicationContext(), period.getId(), new CallBack<ArrayList<SubjectMark>>() {
            @Override
            public void onSuccess(ArrayList<SubjectMark> marks) {
                CommonFunctions.showProgress(false, getApplicationContext(), mSubjectFormView, mProgressView);
                CommonFunctions.setRefreshActionButtonState(false, optionsMenu);

                if (marks.size() == 0) {
                    textView.setVisibility(View.VISIBLE);
                    marksListView.setVisibility(View.GONE);
                } else {
                    textView.setVisibility(View.GONE);
                    marksListView.setVisibility(View.VISIBLE);
                    marksListView.setAdapter(new MarksAdapter(getApplicationContext(), marks));
                }
            }

            @Override
            public void onFail(String error) {
               // mProgressView.setVisibility(View.GONE);
                mSubjectFormView.setVisibility(View.GONE);
                CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subject_menu, menu);
        this.optionsMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        Period period = periods.getCurrentPeriod();
        MenuItem menuItem;

        if(period.getName().equals(getResources().getString(R.string.first_period))) {
            menuItem = menu.findItem(R.id.first_period);
            menuItem.setChecked(true);
            checkedItem = menuItem;
        }
        if(period.getName().equals(getResources().getString(R.string.second_period))) {
            menuItem = menu.findItem(R.id.second_period);
            menuItem.setChecked(true);
            checkedItem = menuItem;
        }
        if(period.getName().equals(getResources().getString(R.string.third_period))) {
            menuItem = menu.findItem(R.id.third_period);
            menuItem.setChecked(true);
            checkedItem = menuItem;
        }
        if(period.getName().equals(getResources().getString(R.string.forth_period))) {
            menuItem = menu.findItem(R.id.forth_period);
            menuItem.setChecked(true);
            checkedItem = menuItem;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_refresh:
                CommonFunctions.setRefreshActionButtonState(true, optionsMenu);
                setView(checkedItem.getTitle().toString());
                return true;
            case R.id.first_period:
                checkedItem = item;
                item.setChecked(true);
                setView(getResources().getString(R.string.first_period));
                return true;
            case R.id.second_period:
                checkedItem = item;
                item.setChecked(true);
                setView(getResources().getString(R.string.second_period));
                return true;
            case R.id.third_period:
                checkedItem = item;
                item.setChecked(true);
                setView(getResources().getString(R.string.third_period));
                return true;
            case R.id.forth_period:
                item.setChecked(true);
                setView(getResources().getString(R.string.forth_period));
                return true;
            case R.id.all_period:
                checkedItem = item;
                item.setChecked(true);
                setView(getResources().getString(R.string.year_period));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.s_in, R.anim.s_out);
    }
}

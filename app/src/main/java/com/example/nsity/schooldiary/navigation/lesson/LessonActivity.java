package com.example.nsity.schooldiary.navigation.lesson;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.Mark;
import com.example.nsity.schooldiary.navigation.timetable.TimetableItem;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by nsity on 18.11.15.
 */
public class LessonActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TimetableItem timetableItem;

    private View mProgressView;
    private View mLessonFormView;

    private TextView mDateTextView;
    private TextView mTimeTextView;
    private TextView mThemeTextView;
    private TextView mNoteTextView;
    private TextView mPassTextView;
    private TextView mMarksTextView;

    private ExpandableTextView mHomeWorkTextView;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    private Lesson lesson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        timetableItem = (TimetableItem) getIntent().getSerializableExtra("timetableItem");
        if(timetableItem != null) {
            toolbar.setTitle(timetableItem.getSubject());
            toolbar.setBackgroundColor(CommonFunctions.setColor(this, timetableItem.getColor()));
        }

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


        mProgressView = findViewById(R.id.progress);
        mLessonFormView = findViewById(R.id.lesson_form);

        mDateTextView = (TextView) findViewById(R.id.date);
        mTimeTextView = (TextView) findViewById(R.id.time);
        mThemeTextView = (TextView) findViewById(R.id.theme);
        mPassTextView = (TextView) findViewById(R.id.pass);
        mMarksTextView = (TextView) findViewById(R.id.marks);
        mNoteTextView = (TextView) findViewById(R.id.note);
        mHomeWorkTextView = (ExpandableTextView) findViewById(R.id.homework);

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        final TextView mHomeworkText = (TextView) findViewById(R.id.expandable_text);

        mHomeworkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mHomeworkText.getText().toString();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.text_copied),
                        Toast.LENGTH_SHORT).show();
            }
        });

        setView();

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


    private void setView() {
        if(timetableItem == null) {
            return;
        }

        lesson = new Lesson(this, getIntent().getStringExtra("day"), timetableItem.getTimeId(), timetableItem.getSubjectId());
        if(lesson.getId() == -1) {
            showProgress(true);

            LessonManager.getLesson(this, lesson.getId(), Preferences.get(Preferences.PUPILID, this), String.valueOf(timetableItem.getSubjectId()),
                    getIntent().getStringExtra("day"), String.valueOf(timetableItem.getTimeId()), new CallBack() {
                        @Override
                        public void onSuccess() {
                            showProgress(false);
                            setView();
                        }

                        @Override
                        public void onFail(String message) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            mProgressView.setVisibility(View.GONE);
                            mLessonFormView.setVisibility(View.GONE);
                        }
                    });
        } else {

            String date = CommonFunctions.getDate(lesson.getDate(), CommonFunctions.FORMAT_YYYY_MM_DD, CommonFunctions.FORMAT_D_MMMM_YYYY) +
                    " " + getResources().getString(R.string.year);
            mDateTextView.setText(date);

            String time = CommonFunctions.getTime(lesson.getTimeStart()) + " - " + CommonFunctions.getTime(lesson.getTimeEnd());
            mTimeTextView.setText(time);

            mThemeTextView.setText(lesson.getTheme());

            mHomeWorkTextView = null;
            mHomeWorkTextView = (ExpandableTextView) findViewById(R.id.homework);
            mHomeWorkTextView.setText(lesson.getHomework().equals("") ? getResources().getString(R.string.missing) : lesson.getHomework());

            if(lesson.getNote().equals("")) {
                RelativeLayout mNoteLayout = (RelativeLayout) findViewById(R.id.note_layout);
                mNoteLayout.setVisibility(View.GONE);
            }
            else {
                RelativeLayout mNoteLayout = (RelativeLayout) findViewById(R.id.note_layout);
                mNoteLayout.setVisibility(View.VISIBLE);
                mNoteTextView.setText(lesson.getNote());
            }

            String pass = lesson.getPass();

            if(pass.equals("")) {
                RelativeLayout mPassLayout = (RelativeLayout) findViewById(R.id.pass_layout);
                mPassLayout.setVisibility(View.GONE);
            } else {
                RelativeLayout mPassLayout = (RelativeLayout) findViewById(R.id.pass_layout);
                mPassLayout.setVisibility(View.VISIBLE);

                if (pass.equals(getResources().getString(R.string.ill_short))) {
                    mPassTextView.setText(getResources().getString(R.string.ill));
                }
                if (pass.equals(getResources().getString(R.string.good_reason_short))) {
                    mPassTextView.setText(getResources().getString(R.string.good_reason));
                }
                if (pass.equals(getResources().getString(R.string.bad_reason_short))) {
                    mPassTextView.setText(getResources().getString(R.string.bad_reason));
                }
            }

            if(lesson.getMarks() == null) {
                RelativeLayout mMarksLayout = (RelativeLayout) findViewById(R.id.marks_layout);
                mMarksLayout.setVisibility(View.GONE);
            }
            else {
                RelativeLayout mMarksLayout = (RelativeLayout) findViewById(R.id.marks_layout);
                mMarksLayout.setVisibility(View.VISIBLE);

                String markStr = "";
                ArrayList<Mark> marks = lesson.getMarks();
                for(Mark mark: marks) {
                    markStr += mark.getValue() + ", ";
                }

                mMarksTextView.setText(CommonFunctions.removeLastChar(CommonFunctions.removeLastChar(markStr)));
            }

        }
    }

    @Override
    public void onRefresh() {
        if (!Server.isOnline(this)) {
            Toast.makeText(this, getString(R.string.internet_problem), Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

        LessonManager.getLesson(this, lesson.getId(), Preferences.get(Preferences.PUPILID, this), String.valueOf(timetableItem.getSubjectId()),
                getIntent().getStringExtra("day"), String.valueOf(timetableItem.getTimeId()), new CallBack() {
                    @Override
                    public void onSuccess() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mLessonFormView.setVisibility(View.VISIBLE);
                        setView();
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                        mLessonFormView.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.s_in, R.anim.s_out);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setView();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLessonFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLessonFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLessonFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLessonFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

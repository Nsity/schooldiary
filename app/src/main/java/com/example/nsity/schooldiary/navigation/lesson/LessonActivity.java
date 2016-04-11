package com.example.nsity.schooldiary.navigation.lesson;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.Mark;
import com.example.nsity.schooldiary.navigation.timetable.TimetableItem;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.Utils;
import com.example.nsity.schooldiary.system.database.tables.LessonDBInterface;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.util.ArrayList;


/**
 * Created by nsity on 18.11.15.
 */
public class LessonActivity extends AppCompatActivity {

    private TimetableItem timetableItem;

    private View mProgressView;
    private View mLessonFormView;

    private TextView mDateTextView;
    private TextView mTimeTextView;
    private TextView mThemeTextView;
    private TextView mNoteTextView;
    private TextView mPassTextView;

    private TextView mHomeworkTextView;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    private Lesson lesson;

    private Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        timetableItem = (TimetableItem) getIntent().getSerializableExtra(Utils.TIMETABLE_ITEM);
        if (timetableItem != null) {
            int color = CommonFunctions.setColor(this, timetableItem.getSubject().getColor());

            toolbar.setTitle(timetableItem.getSubject().getName());
            toolbar.setBackgroundColor(color);

            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            collapsingToolbarLayout.setBackgroundColor(color);
            collapsingToolbarLayout.setContentScrimColor(color);
        }

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mProgressView = findViewById(R.id.progress);
        mLessonFormView = findViewById(R.id.lesson_form);

        mDateTextView = (TextView) findViewById(R.id.date);
        mTimeTextView = (TextView) findViewById(R.id.time);
        mThemeTextView = (TextView) findViewById(R.id.theme);
        mPassTextView = (TextView) findViewById(R.id.pass);

        mNoteTextView = (TextView) findViewById(R.id.note);
        mHomeworkTextView = (TextView) findViewById(R.id.homework);

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        mHomeworkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mHomeworkTextView.getText().toString();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.text_copied),
                        Toast.LENGTH_SHORT).show();
            }
        });

        lesson = new Lesson(this, getIntent().getStringExtra(Utils.DAY), timetableItem.getTime().getId(), timetableItem.getSubject().getId());


        //if notification appears, update lesson
        if(getIntent().getBooleanExtra("update", false) && lesson.getId() != -1) {
            new LessonDBInterface(this).deleteLesson(lesson.getId());
            lesson.setId(-1);
        }

        setView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        this.optionsMenu = menu;
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        setView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                CommonFunctions.setRefreshActionButtonState(true, optionsMenu);
                lesson.load(this, timetableItem.getSubject().getId(),
                        getIntent().getStringExtra(Utils.DAY), timetableItem.getTime().getId(), new CallBack() {
                            @Override
                            public void onSuccess() {
                                CommonFunctions.setRefreshActionButtonState(false, optionsMenu);

                                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
                                mLessonFormView.setVisibility(View.INVISIBLE);
                                mLessonFormView.animate().setDuration(shortAnimTime).alpha(1).setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mLessonFormView.setVisibility(View.VISIBLE);
                                    }
                                });

                                setView();
                            }

                            @Override
                            public void onFail(String message) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
                            }
                        });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setView() {
        if (lesson.getId() == -1) {
            CommonFunctions.showProgress(true, getApplicationContext(), mLessonFormView, mProgressView);
            lesson.load(this, timetableItem.getSubject().getId(),
                    getIntent().getStringExtra(Utils.DAY), timetableItem.getTime().getId(), new CallBack() {
                        @Override
                        public void onSuccess() {
                            CommonFunctions.showProgress(false, getApplicationContext(), mLessonFormView, mProgressView);
                            setView();
                        }

                        @Override
                        public void onFail(String message) {
                            //CommonFunctions.showProgress(false, getApplicationContext(), mLessonFormView, mProgressView);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            //mProgressView.setVisibility(View.GONE);
                            mLessonFormView.setVisibility(View.GONE);
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
        } else {
            String date = CommonFunctions.getDate(lesson.getDate(), CommonFunctions.FORMAT_YYYY_MM_DD, CommonFunctions.FORMAT_D_MMMM_YYYY) +
                    " " + getResources().getString(R.string.year);
            mDateTextView.setText(date);

            String time = CommonFunctions.getTime(lesson.getTime().getTimeStart()) + " - " + CommonFunctions.getTime(lesson.getTime().getTimeEnd());
            mTimeTextView.setText(time);

            mThemeTextView.setText(lesson.getTheme());

            mHomeworkTextView.setText(lesson.getHomework().equals("") ? getResources().getString(R.string.missing) : lesson.getHomework());

            if (lesson.getNote().equals("")) {
                RelativeLayout mNoteLayout = (RelativeLayout) findViewById(R.id.note_layout);
                mNoteLayout.setVisibility(View.GONE);
            } else {
                RelativeLayout mNoteLayout = (RelativeLayout) findViewById(R.id.note_layout);
                mNoteLayout.setVisibility(View.VISIBLE);
                mNoteTextView.setText(lesson.getNote());
            }

            String pass = lesson.getPass();

            if (pass.equals("")) {
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

            if (lesson.getMarks() == null) {
                RelativeLayout mMarksLayout = (RelativeLayout) findViewById(R.id.marks_layout);
                mMarksLayout.setVisibility(View.GONE);
            } else {
                RelativeLayout mMarksLayout = (RelativeLayout) findViewById(R.id.marks_layout);
                mMarksLayout.setVisibility(View.VISIBLE);

                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.marks);
                linearLayout.removeAllViews();

                ArrayList<Mark> marks = lesson.getMarks();
                for (final Mark mark : marks) {
                    ImageView image = new ImageView(this);

                    TextDrawable drawable = TextDrawable.builder()
                            .beginConfig()
                            .width(40)
                            .height(40)
                            .bold()
                            .useFont(Typeface.DEFAULT)
                            .fontSize(30)
                            .endConfig()
                            .buildRoundRect(String.valueOf(mark.getValue()), CommonFunctions.setMarkColor(this, mark.getValue()), 8);

                    image.setImageDrawable(drawable);

                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), mark.getType(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    //left, top, right, bottom
                    lp.setMargins(0, 8, 8, 8);
                    image.setLayoutParams(lp);

                    linearLayout.addView(image);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.s_in, R.anim.s_out);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Server.getHttpClient().cancelRequests(this, true);
    }
}

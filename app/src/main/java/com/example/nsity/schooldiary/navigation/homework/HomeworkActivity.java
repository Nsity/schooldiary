package com.example.nsity.schooldiary.navigation.homework;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.lesson.Lesson;
import com.example.nsity.schooldiary.system.CommonFunctions;

/**
 * Created by nsity on 16.12.15.
 */
public class HomeworkActivity extends AppCompatActivity {

    private TextView mHomeworkTextView, mDateTextView;
    private Lesson lesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        lesson = (Lesson) getIntent().getSerializableExtra("lesson");
        if(lesson != null) {
            int color = CommonFunctions.setColor(this, lesson.getSubject().getColor());

            toolbar.setTitle(lesson.getSubject().getName());
            toolbar.setBackgroundColor(color);
        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mHomeworkTextView = (TextView) findViewById(R.id.homework);
        mHomeworkTextView.setText(lesson.getHomework());

        mDateTextView = (TextView) findViewById(R.id.date);
        mDateTextView.setText(CommonFunctions.getDate(lesson.getDate(), CommonFunctions.FORMAT_YYYY_MM_DD, CommonFunctions.FORMAT_DD_MM_YYYY));

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.s_in, R.anim.s_out);
    }

}

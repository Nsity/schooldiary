package com.example.nsity.schooldiary.navigation.homework.recent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.homework.Homework;
import com.example.nsity.schooldiary.system.CommonFunctions;

/**
 * Created by nsity on 16.12.15.
 */
public class HomeworkActivity extends AppCompatActivity {

    private TextView mHomeworkTextView, mDateTextView;
    private Homework homework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        homework = (Homework) getIntent().getSerializableExtra("homework");
        if(homework != null) {
            int color = CommonFunctions.setColor(this, homework.getSubject().getColor());

            toolbar.setTitle(homework.getSubject().getName());
            toolbar.setBackgroundColor(color);
        }

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mHomeworkTextView = (TextView) findViewById(R.id.homework);
        mHomeworkTextView.setText(homework.getTask());

        mDateTextView = (TextView) findViewById(R.id.date);
        mDateTextView.setText(CommonFunctions.getDate(homework.getDate(), CommonFunctions.FORMAT_YYYY_MM_DD, CommonFunctions.FORMAT_DD_MM_YYYY));

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.s_in, R.anim.s_out);
    }

}

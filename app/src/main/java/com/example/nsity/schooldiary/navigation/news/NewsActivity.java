package com.example.nsity.schooldiary.navigation.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.Teacher;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.CommonManager;
import com.example.nsity.schooldiary.system.Utils;

import org.w3c.dom.Text;

/**
 * Created by nsity on 28.05.16.
 */
public class NewsActivity  extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();
        News news = (News)intent.getSerializableExtra(Utils.NEWS);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(toolbar != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.news));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }


        if(news == null) {
            return;
        }

        TextView theme = (TextView) findViewById(R.id.theme);
        theme.setText(news.getTitle());

        TextView text = (TextView) findViewById(R.id.text);
        text.setText(news.getText());

        TextView date = (TextView) findViewById(R.id.date);
        date.setText(CommonFunctions.getDate(news.getDate(),
                CommonFunctions.FORMAT_YYYY_MM_DD, CommonFunctions.FORMAT_E_D_MMMM_YYYY));

    }

}

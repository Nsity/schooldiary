package com.example.nsity.schooldiary.navigation.timetable;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.lesson.LessonActivity;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.Utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.nlmartian.silkcal.DatePickerController;
import me.nlmartian.silkcal.DayPickerView;
import me.nlmartian.silkcal.SimpleMonthAdapter;

/**
 * Created by nsity on 17.11.15.
 */
public class CalendarActivity extends AppCompatActivity implements DatePickerController {

    private ListView mTodayView;
    private ArrayList<TimetableItem> timetableItemArrayList;
    private DayPickerView calendarView;
    private LinearLayout mTimetableLinearLayout;
    private TextView mTextView;

    private Timetable timetable;

    private DateFormat format = new SimpleDateFormat(CommonFunctions.FORMAT_YYYY_MM_DD, new Locale("ru"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String languageToLoad  = "ru";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            }
        });

        mTodayView = (ListView) findViewById(R.id.timetable);
        mTimetableLinearLayout = (LinearLayout) findViewById(R.id.timetable_layout);
        mTextView = (TextView) findViewById(R.id.text);
        calendarView = (DayPickerView) findViewById(R.id.calendar_view);

        calendarView.setController(this);
        timetable = new Timetable(this);

        setView(Calendar.getInstance().getTime());
    }


    @Override
    public int getMaxYear() {
        return 3000;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        setView(cal.getTime());
    }


    public void setView(final Date date) {
        int dayOfWeek = CommonFunctions.getDayOfWeek(date);

        String[] timetableDays = getResources().getStringArray(R.array.timetable_days);
        ((TextView)findViewById(R.id.title)).setText(timetableDays[dayOfWeek - 1]);

        ArrayList<Period> periods = new Periods(this).getPeriods();

        try {
            for(Period period: periods) {
                Date dateStart = format.parse(period.getPeriodStart());
                Date dateEnd = format.parse(period.getPeriodEnd());

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateEnd);
                cal.add(Calendar.DATE, 1);
                dateEnd = cal.getTime();

                if(date.after(dateStart) && date.before(dateEnd) &&
                        !period.getName().equals(this.getResources().getString(R.string.year_period))) {

                    mTimetableLinearLayout.setVisibility(View.VISIBLE);
                    mTextView.setVisibility(View.GONE);

                    timetableItemArrayList = timetable.getTimetableOfDay(dayOfWeek);

                    if(timetableItemArrayList == null) {
                        mTimetableLinearLayout.setVisibility(View.GONE);
                        mTextView.setVisibility(View.VISIBLE);
                        mTextView.setText(getResources().getString(R.string.weekend));
                        mTodayView.setAdapter(null);
                    } else {
                        mTodayView.setAdapter(new TimetableAdapter(this, timetableItemArrayList));
                    }

                    mTodayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                                long id) {
                            TimetableItem timetableItem = timetableItemArrayList.get(position);

                            SimpleDateFormat sdf = new SimpleDateFormat(CommonFunctions.FORMAT_YYYY_MM_DD, Locale.getDefault());
                            String currentDate = sdf.format(date);

                            Intent intent = new Intent(CalendarActivity.this, LessonActivity.class);
                            intent.putExtra(Utils.TIMETABLE_ITEM, timetableItem);
                            intent.putExtra(Utils.DAY, currentDate);

                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        }
                    });
                    return;
                } else {
                    mTimetableLinearLayout.setVisibility(View.GONE);
                    mTextView.setVisibility(View.VISIBLE);
                    mTextView.setText(getResources().getString(R.string.holidays));
                    mTodayView.setAdapter(null);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.week_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_week_view:
                calendarView.scrollToToday();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
package com.example.nsity.schooldiary.navigation.timetable;

import android.app.Activity;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.database.tables.TimetableDBInterface;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by nsity on 17.11.15.
 */
public class CalendarActivity extends AppCompatActivity implements WeekView.MonthChangeListener,
        WeekView.EventClickListener {

    private WeekView mWeekView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        //getActionBar().setHomeButtonEnabled(true);
        //getActionBar().setIcon(R.mipmap.ic_schedule_white_24dp);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);

        setupDateTimeInterpreter(false);

        goToMondayOfCurrentWeek();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.week_view_menu, menu);
        return true;
    }

    private void goToMondayOfCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        mWeekView.goToDate(cal);
        mWeekView.goToHour(8);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_week_view:
                goToMondayOfCurrentWeek();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", new Locale("ru"));
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" d MMMM", new Locale("ru"));
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return String.valueOf(hour) + ":00";
            }
        });
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        List<WeekViewEvent> events = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, newYear);
        calendar.set(Calendar.MONTH, newMonth-1);
        int numDays = calendar.getActualMaximum(Calendar.DATE);

        for(int i = 1; i <= numDays; i++) {

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", new Locale("ru"));
            try {
                Date selectedDate = formatter.parse(String.valueOf(newYear) + "-" + String.valueOf(newMonth) + "-" + String.valueOf(i));

                Calendar c = Calendar.getInstance();
                c.setTime(selectedDate);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                ArrayList<Timetable> timetableArrayList = new TimetableDBInterface(this).getTimetable(dayOfWeek);

                if (timetableArrayList != null) {

                    for (int j = 0; j < timetableArrayList.size(); j++) {
                        Timetable timetable = timetableArrayList.get(j);

                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.DAY_OF_MONTH, i + 1);
                        startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timetable.getTimeStart().split(":")[0]));
                        startTime.set(Calendar.MINUTE, Integer.valueOf(timetable.getTimeStart().split(":")[1]));
                        startTime.set(Calendar.MONTH, newMonth - 1);
                        startTime.set(Calendar.YEAR, newYear);
                        Calendar endTime = (Calendar) startTime.clone();
                        endTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timetable.getTimeEnd().split(":")[0]));
                        endTime.set(Calendar.MINUTE, Integer.valueOf(timetable.getTimeEnd().split(":")[1]));

                        WeekViewEvent event = new WeekViewEvent(timetable.getId(), timetable.getTimeStart().substring(0, timetable.getTimeStart().length() - 3)
                                + "-" + timetable.getTimeEnd().substring(0, timetable.getTimeEnd().length() - 3) + "\n" +
                                timetable.getSubject()  + "\n" + timetable.getRoom(), startTime, endTime);

                        int[] colors = getResources().getIntArray(R.array.colors);

                        if (timetable.getColor() < colors.length) {
                            event.setColor(colors[timetable.getColor()]);
                        } else {
                            event.setColor(colors[timetable.getColor() - colors.length]);
                        }
                        events.add(event);
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return events;
    }


    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mWeekView.notifyDatasetChanged();
    }


    @Override
    public void onBackPressed() {
        finish();
    }

}
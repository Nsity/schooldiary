package com.example.nsity.schooldiary.navigation;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.tables.PeriodDBInterface;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nsity on 06.12.15.
 */
public class Periods {

    private ArrayList<Period> periodsArrayList;
    private PeriodDBInterface db;

    public Periods(Context context) {
        this.db = new PeriodDBInterface(context);
        periodsArrayList = loadFromDB();
    }

    public ArrayList<Period> loadFromDB() {
        return db.getPeriods();
    }

    public Period getCurrentPeriod() {
        DateFormat format = new SimpleDateFormat(CommonFunctions.FORMAT_YYYY_MM_DD, new Locale("ru"));
        try {
            /*Date date = format.parse(CommonFunctions.getCurrentDateyyyyMMdd());
            for(Period period: periodsArrayList) {
                Date dateStart = format.parse(period.getPeriodStart());
                Date dateEnd = format.parse(period.getPeriodEnd());

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateEnd);
                cal.add(Calendar.DATE, 1);
                dateEnd = cal.getTime();

                if(date.after(dateStart) && date.before(dateEnd) && !period.getName().equals(context.getResources().getString(R.string.year_period))) {
                    return period;
                }
            }*/

            Date date = format.parse(CommonFunctions.getCurrentDateyyyyMMdd());

            for(int i = 0; i < periodsArrayList.size() - 1; i++) {
                Date dateStart = format.parse(periodsArrayList.get(i).getPeriodStart());
                Date dateEnd;
                if(i == periodsArrayList.size() - 1)
                    dateEnd = format.parse(periodsArrayList.get(i).getPeriodEnd());
                else
                    dateEnd = format.parse(periodsArrayList.get(i + 1).getPeriodStart());

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateEnd);
                cal.add(Calendar.DATE, 1);
                dateEnd = cal.getTime();

                if (date.after(dateStart) && date.before(dateEnd)) {
                    return periodsArrayList.get(i);
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public Period getPeriodByName(String name) {
        for(Period period: periodsArrayList) {
            if(period.getName().equals(name)) {
                return period;
            }
        }
        return null;
    }

    public ArrayList<Period> getPeriods() {
        return periodsArrayList;
    }
}

package com.example.nsity.schooldiary.navigation.timetable;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.lesson.LessonActivity;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.Preferences;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by nsity on 15.11.15.
 */
public class TimetableFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private ListView mTimetableView;
    private ArrayList<TimetableItem> timetableItemArrayList;
    private TextView mTextView;
    private CardView mCardView;

    private Date selectedDate;
    private Timetable timetable;
    private Periods periods;

    private DateFormat format = new SimpleDateFormat(CommonFunctions.FORMAT_YYYY_MM_DD, new Locale("ru"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_timetable, container, false);

        mTimetableView = (ListView) rootView.findViewById(R.id.timetable);
        mCardView = (CardView) rootView.findViewById(R.id.card_view);
        mTextView = (TextView) rootView.findViewById(R.id.text);

        if (savedInstanceState != null) {
            try {
                selectedDate = format.parse(savedInstanceState.getString("date"));
            } catch (ParseException e) {
                selectedDate = Calendar.getInstance().getTime();
                e.printStackTrace();
            }
        } else {
            selectedDate = Calendar.getInstance().getTime();
        }

        mTimetableView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                TimetableItem timetableItem = timetableItemArrayList.get(position);

                String currentDate = CommonFunctions.getDate(selectedDate, CommonFunctions.FORMAT_YYYY_MM_DD);

                Intent intent = new Intent(getActivity(), LessonActivity.class);
                intent.putExtra("timetableItem", timetableItem);
                intent.putExtra("day", currentDate);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = Calendar.getInstance().getTime();
                setView();
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(selectedDate);

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        TimetableFragment.this,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(getActivity().getFragmentManager(), "DatePickerDialog");
                return true;
            }
        });

        timetable = new Timetable(getActivity());
        periods = new Periods(getActivity());

        setView();
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.timetable_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_week_view:
                startActivity(new Intent(getActivity(), CalendarActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                return false;
            case R.id.action_next:
                selectedDate = CommonFunctions.addDays(1, selectedDate);
                setView();
                return false;
            case R.id.action_back:
                selectedDate = CommonFunctions.addDays(-1, selectedDate);
                setView();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    public void setView() {
        if(selectedDate == null) {
            selectedDate = Calendar.getInstance().getTime();
        }

        int dayOfWeek = CommonFunctions.getDayOfWeek(selectedDate);
        setFragmentTitle();

        try {
            for(Period period: periods.getPeriods()) {
                Date dateStart = format.parse(period.getPeriodStart());
                Date dateEnd = format.parse(period.getPeriodEnd());

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateEnd);
                cal.add(Calendar.DATE, 1);
                dateEnd = cal.getTime();

                if(selectedDate.after(dateStart) && selectedDate.before(dateEnd) &&
                        !period.getName().equals(getResources().getString(R.string.year_period))) {

                    timetableItemArrayList = timetable.getTimetableOfDay(dayOfWeek);
                    if (timetableItemArrayList == null) {

                        mCardView.setVisibility(View.INVISIBLE);
                        mTextView.setVisibility(View.VISIBLE);

                        Random rnd = new Random();
                        int k = rnd.nextInt(3);
                        switch (k) {
                            case 0:
                                mTextView.setText(getActivity().getResources().getString(R.string.no_lessons_1));
                                break;
                            case 1:
                                mTextView.setText(getActivity().getResources().getString(R.string.no_lessons_2));
                                break;
                            case 2:
                                mTextView.setText(getActivity().getResources().getString(R.string.no_lessons_3));
                                break;
                            default:
                                break;
                        }
                    } else {
                        mCardView.setVisibility(View.VISIBLE);
                        mTextView.setVisibility(View.INVISIBLE);

                        mTimetableView.setAdapter(new TimetableAdapter(getActivity(), timetableItemArrayList));
                    }
                    break;
                } else {
                    mCardView.setVisibility(View.INVISIBLE);
                    mTextView.setVisibility(View.VISIBLE);

                    mTextView.setText(getResources().getString(R.string.holidays));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void setFragmentTitle() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && TimetableFragment.this.isAdded()) {
            String date = CommonFunctions.getDate(selectedDate, "E").toUpperCase() + " " +
                    CommonFunctions.getDate(selectedDate, CommonFunctions.FORMAT_E_D_MMMM_YYYY);
            actionBar.setTitle(date);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.YEAR, year);
        selectedDate = cal.getTime();
        setView();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("date", CommonFunctions.getDate(selectedDate, CommonFunctions.FORMAT_YYYY_MM_DD));
    }
}

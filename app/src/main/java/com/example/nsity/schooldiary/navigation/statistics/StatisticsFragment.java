package com.example.nsity.schooldiary.navigation.statistics;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.subjects.Subjects;
import com.example.nsity.schooldiary.navigation.timetable.Period;
import com.example.nsity.schooldiary.navigation.timetable.Periods;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.util.ArrayList;

/**
 * Created by nsity on 17.01.16.
 */
public class StatisticsFragment extends Fragment {

    private Menu optionsMenu;
    private Period period;

    private Subjects subjects;

    private View mProgressView;
    private View mStatisticsView;
    private ListView mStatisticsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && isAdded()) {
            actionBar.setTitle(getString(R.string.nav_statistics));
        }

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        period = new Periods(getActivity()).getCurrentPeriod();

        if(period == null) {
            return rootView;
        }

        subjects = new Subjects(getActivity());

        mProgressView = rootView.findViewById(R.id.progress);
        mStatisticsView = rootView.findViewById(R.id.statistics);

        mStatisticsListView = (ListView) rootView.findViewById(R.id.statistics_view);

        TextView mTitleTextView = (TextView) rootView.findViewById(R.id.title);

        mTitleTextView.setText(getResources().getString(R.string.average_score) + " (" + period.getName() + ")");

        setView();
        return rootView;
    }

    private void setView() {
        CommonFunctions.showProgress(true, getActivity(), mStatisticsView, mProgressView);
        subjects.loadStatistics(period.getId(), new CallBack<ArrayList<Score>>() {
            @Override
            public void onSuccess(ArrayList<Score> arrayList) {
                if (isAdded() && getActivity() != null) {
                    CommonFunctions.showProgress(false, getActivity(), mStatisticsView, mProgressView);
                    mStatisticsListView.setAdapter(new StatisticsAdapter(getActivity(), arrayList));
                    CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
                }
            }

            @Override
            public void onFail(String error) {
                if (isAdded() && getActivity() != null) {
                    CommonFunctions.showProgress(false, getActivity(), mStatisticsView, mProgressView);
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    CommonFunctions.setRefreshActionButtonState(false, optionsMenu);
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        optionsMenu = menu;
        inflater.inflate(R.menu.refresh_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                CommonFunctions.setRefreshActionButtonState(true, optionsMenu);
                setView();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Server.getHttpClient().cancelRequests(getActivity(), true);
    }
}

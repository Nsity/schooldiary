package com.example.nsity.schooldiary.navigation.timetable;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nsity.schooldiary.R;

/**
 * Created by nsity on 15.11.15.
 */
public class TimetableFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_timetable, container, false);

        return rootView;
    }
}

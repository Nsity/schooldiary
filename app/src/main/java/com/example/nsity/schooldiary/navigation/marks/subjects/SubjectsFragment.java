package com.example.nsity.schooldiary.navigation.marks.subjects;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.Subject;

/**
 * Created by nsity on 04.12.15.
 */
public class SubjectsFragment extends Fragment {

    private ListView mSubjectsListView;
    private Subjects subjects;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && isAdded()) {
            actionBar.setTitle(getString(R.string.nav_marks));
        }

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_subjects_marks, container, false);

        mSubjectsListView = (ListView) rootView.findViewById(R.id.subjects);

        mSubjectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Subject subject = subjects.getItem(position);

                Intent intent = new Intent(getActivity(), SubjectMarksActivity.class);
                intent.putExtra("subject", subject);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        setView();

        return rootView;
    }

    private void setView() {
        subjects = new Subjects(getActivity());
        mSubjectsListView.setAdapter(new SubjectsAdapter(getActivity(), subjects.getSubjects()));
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }
}

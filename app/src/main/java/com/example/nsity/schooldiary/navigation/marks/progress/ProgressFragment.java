package com.example.nsity.schooldiary.navigation.marks.progress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.subjects.Subjects;
import com.example.nsity.schooldiary.system.network.CallBack;

import java.util.ArrayList;

/**
 * Created by nsity on 06.12.15.
 */
public class ProgressFragment extends Fragment {

    private ListView listView;
    private Subjects subjects;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_progress, container, false);

        listView = (ListView) rootView.findViewById(R.id.progress);

        setView();

        return rootView;
    }

    private void setView() {
        subjects = new Subjects(getActivity());
        if(subjects.loadProgressFromDB() == null) {
            subjects.loadProgress(new CallBack<ArrayList<ArrayList<ProgressItem>>>() {
                @Override
                public void onSuccess(ArrayList<ArrayList<ProgressItem>> progress) {
                    listView.setAdapter(new ProgressAdapter(getActivity(), subjects.getSubjects(),progress));
                }

                @Override
                public void onFail(String error) {
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            listView.setAdapter(new ProgressAdapter(getActivity(), subjects.getSubjects(), subjects.loadProgressFromDB()));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        setView();
    }
}

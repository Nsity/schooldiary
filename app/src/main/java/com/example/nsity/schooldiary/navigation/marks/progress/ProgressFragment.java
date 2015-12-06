package com.example.nsity.schooldiary.navigation.marks.progress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.network.CallBack;

/**
 * Created by nsity on 06.12.15.
 */
public class ProgressFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_progress, container, false);

        setView();

        return rootView;
    }

    private void setView() {
        Progress progress = new Progress(getActivity());
        if(progress.getSize() == -1) {
            progress.load(new CallBack() {
                @Override
                public void onSuccess() {
                    fillData();
                }

                @Override
                public void onFail(String error) {
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            fillData();
        }
    }

    public void fillData() {

    }



    @Override
    public void onResume() {
        super.onResume();
        setView();
    }
}

package com.example.nsity.schooldiary.navigation.marks.subjects;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.Subject;
import com.example.nsity.schooldiary.system.CommonFunctions;

import java.util.ArrayList;

/**
 * Created by nsity on 04.12.15.
 */
public class SubjectsAdapter extends BaseAdapter {
    private ArrayList<Subject> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;

    public SubjectsAdapter(Context context, ArrayList<Subject> arrayList) {
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public static class ViewHolder {
        TextView mSubjectTextView;
        View mCircleView;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = convertView;
        final Subject subject = arrayList.get(position);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            customView = layoutInflater.inflate(R.layout.item_subject, parent, false);
            holder.mSubjectTextView = (TextView) customView.findViewById(R.id.subject);
            holder.mCircleView = customView.findViewById(R.id.circle);
            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }

        holder.mSubjectTextView.setText(subject.getName());
        ((GradientDrawable)holder.mCircleView.getBackground()).setColor(CommonFunctions.setColor(context, subject.getColor()));

        return customView;
    }
}

package com.example.nsity.schooldiary.navigation.homework.recent;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.homework.Homework;
import com.example.nsity.schooldiary.navigation.lesson.Lesson;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.CommonManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nsity on 13.12.15.
 */

public class HomeworkAdapter extends BaseAdapter {

    private ArrayList<Homework> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;

    public HomeworkAdapter(Context context, ArrayList<Homework> arrayList) {
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void addItems(List<Homework> newItems) {
        if (newItems == null) {
            return;
        }

        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }

        arrayList.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == arrayList) {
            return 0;
        }

        return arrayList.size();
    }

    public static class ViewHolder {
        TextView mSubjectTextView;
        TextView mTaskTextView;
        TextView mDateTextView;
        View mCircleView;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = convertView;
        final Homework homework = arrayList.get(position);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            customView = layoutInflater.inflate(R.layout.item_homework, parent, false);
            holder.mSubjectTextView = (TextView) customView.findViewById(R.id.subject);
            holder.mTaskTextView = (TextView) customView.findViewById(R.id.homework);
            holder.mDateTextView = (TextView) customView.findViewById(R.id.date);

            holder.mCircleView = customView.findViewById(R.id.circle);

            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }

        holder.mSubjectTextView.setText(homework.getSubject().getName());
        holder.mTaskTextView.setText(homework.getTask());
        holder.mDateTextView.setText(CommonFunctions.getDate(homework.getDate(), CommonFunctions.FORMAT_YYYY_MM_DD, CommonFunctions.FORMAT_DD_MM_YYYY));


        ((GradientDrawable)holder.mCircleView.getBackground()).setColor(CommonFunctions.setColor(context, homework.getSubject().getColor()));


        return customView;
    }

}
package com.example.nsity.schooldiary.navigation.marks.progress;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.Subject;
import com.example.nsity.schooldiary.system.CommonFunctions;

import java.util.ArrayList;

/**
 * Created by nsity on 06.12.15.
 */
public class ProgressAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Subject> subjects;
    private LayoutInflater layoutInflater;

    public ProgressAdapter(Context context, ArrayList<Subject> subjects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.subjects = subjects;
    }

    public static class ViewHolder {
        TextView mSubjectTextView;
        ImageView mFirstView;
        ImageView mSecondView;
        ImageView mThirdView;
        ImageView mForthView;
        ImageView mYearView;
    }

    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public Object getItem(int position) {
        return subjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = convertView;
        final Subject subject = subjects.get(position);

        ArrayList<ProgressItem> progress = subject.loadProgressFromDB(context);

        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            customView = layoutInflater.inflate(R.layout.item_progress, parent, false);
            holder.mSubjectTextView = (TextView) customView.findViewById(R.id.subject);
            holder.mFirstView = (ImageView) customView.findViewById(R.id.image_view4);
            holder.mSecondView = (ImageView) customView.findViewById(R.id.image_view3);
            holder.mThirdView = (ImageView) customView.findViewById(R.id.image_view2);
            holder.mForthView = (ImageView) customView.findViewById(R.id.image_view1);
            holder.mYearView = (ImageView) customView.findViewById(R.id.image_view);
            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }

        holder.mSubjectTextView.setText(subject.getName());

        holder.mFirstView.setImageDrawable(null);
        holder.mSecondView.setImageDrawable(null);
        holder.mThirdView.setImageDrawable(null);
        holder.mForthView.setImageDrawable(null);
        holder.mYearView.setImageDrawable(null);

        TextDrawable drawable;

        if(progress == null)
            return  customView;

        for (ProgressItem item: progress) {
            if(item.getPeriod().getName().equals(context.getString(R.string.first_period))) {
                drawable = TextDrawable.builder()
                        .beginConfig()
                        .bold()
                        .useFont(Typeface.DEFAULT)
                        .fontSize(30)
                        .endConfig()
                        .buildRoundRect(String.valueOf(item.getValue()), CommonFunctions.setMarkColor(context, item.getValue()), 8);
                holder.mFirstView.setImageDrawable(drawable);
            }

           if(item.getPeriod().getName().equals(context.getString(R.string.second_period))) {
                drawable = TextDrawable.builder()
                        .beginConfig()
                        .bold()
                        .useFont(Typeface.DEFAULT)
                        .fontSize(30)
                        .endConfig()
                        .buildRoundRect(String.valueOf(item.getValue()), CommonFunctions.setMarkColor(context, item.getValue()), 8);
                holder.mSecondView.setImageDrawable(drawable);
            }

            if(item.getPeriod().getName().equals(context.getString(R.string.third_period))) {
                drawable = TextDrawable.builder()
                        .beginConfig()
                        .bold()
                        .useFont(Typeface.DEFAULT)
                        .fontSize(30)
                        .endConfig()
                        .buildRoundRect(String.valueOf(item.getValue()), CommonFunctions.setMarkColor(context, item.getValue()), 8);
                holder.mThirdView.setImageDrawable(drawable);
            }

            if(item.getPeriod().getName().equals(context.getString(R.string.forth_period))) {
                drawable = TextDrawable.builder()
                        .beginConfig()
                        .bold()
                        .useFont(Typeface.DEFAULT)
                        .fontSize(30)
                        .endConfig()
                        .buildRoundRect(String.valueOf(item.getValue()), CommonFunctions.setMarkColor(context, item.getValue()), 8);
                holder.mForthView.setImageDrawable(drawable);
            }

            if(item.getPeriod().getName().equals(context.getString(R.string.year_period))) {
                drawable = TextDrawable.builder()
                        .beginConfig()
                        .bold()
                        .useFont(Typeface.DEFAULT)
                        .fontSize(30)
                        .endConfig()
                        .buildRoundRect(String.valueOf(item.getValue()), CommonFunctions.setMarkColor(context, item.getValue()), 8);
                holder.mYearView.setImageDrawable(drawable);
            }

        }

        return customView;
    }
}
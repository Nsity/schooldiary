package com.example.nsity.schooldiary.navigation.timetable;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;

import java.util.ArrayList;

/**
 * Created by nsity on 17.11.15.
 */
public class TimetableAdapter extends BaseAdapter {
    private ArrayList<Timetable> arrayList;
    private Context context;

    public TimetableAdapter(Context context, ArrayList<Timetable> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public static class ViewHolder {
        TextView mTimeTextView;
        TextView mSubjectTextView;
        TextView mRoomTextView;
        View mDividerView;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = convertView;
        final Timetable timetable = arrayList.get(position);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater li = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            customView = li.inflate(R.layout.item_timetable, null);
            holder.mTimeTextView = (TextView) customView.findViewById(R.id.time);
            holder.mSubjectTextView = (TextView) customView.findViewById(R.id.subject);
            holder.mRoomTextView = (TextView) customView.findViewById(R.id.room);
            holder.mDividerView = customView.findViewById(R.id.divider);
            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }

        holder.mSubjectTextView.setText(timetable.getSubject());
        holder.mRoomTextView.setText(timetable.getRoom());
        holder.mTimeTextView.setText(timetable.getTimeStart().substring(0, timetable.getTimeStart().length() - 3));

        int[] colors = context.getResources().getIntArray(R.array.colors);

        if(timetable.getColor() < colors.length) {
            holder.mDividerView.setBackgroundColor(colors[timetable.getColor()]);
        } else {
            holder.mDividerView.setBackgroundColor(colors[timetable.getColor() - colors.length]);
        }

        return customView;
    }
}
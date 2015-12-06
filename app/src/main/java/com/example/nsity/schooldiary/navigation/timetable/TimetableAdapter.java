package com.example.nsity.schooldiary.navigation.timetable;

import android.content.Context;
import android.text.Html;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonFunctions;

import java.util.ArrayList;

/**
 * Created by nsity on 17.11.15.
 */
public class TimetableAdapter extends BaseAdapter {
    private ArrayList<TimetableItem> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;

    public TimetableAdapter(Context context, ArrayList<TimetableItem> arrayList) {
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public static class ViewHolder {
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
        final TimetableItem timetable = arrayList.get(position);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            customView = layoutInflater.inflate(R.layout.item_timetable, parent, false);
            holder.mSubjectTextView = (TextView) customView.findViewById(R.id.subject);
            holder.mRoomTextView = (TextView) customView.findViewById(R.id.room);
            holder.mDividerView = customView.findViewById(R.id.divider);
            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }

        String s = "<b>" + timetable.getSubject().getName() + "</b>" + (timetable.getRoom().equals("") ? "" : " - " + "<i> каб. " + timetable.getRoom() + "</i>");
        holder.mSubjectTextView.setText(Html.fromHtml(s));

        String time = CommonFunctions.getTime(timetable.getTime().getTimeStart()) + " - " + CommonFunctions.getTime(timetable.getTime().getTimeEnd());
        holder.mRoomTextView.setText(time);

        holder.mDividerView.setBackgroundColor(CommonFunctions.setColor(context, timetable.getSubject().getColor()));

        return customView;
    }
}
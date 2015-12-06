package com.example.nsity.schooldiary.navigation.marks.subjects;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonFunctions;

import java.util.ArrayList;

/**
 * Created by nsity on 06.12.15.
 */
public class MarksAdapter extends BaseAdapter {

    private ArrayList<SubjectMark> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;

    public MarksAdapter(Context context, ArrayList<SubjectMark> arrayList) {
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public static class ViewHolder {
        TextView mTypeTextView;
        TextView mDateTextView;
        ImageView mMarkView;
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
        final SubjectMark mark = arrayList.get(position);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            customView = layoutInflater.inflate(R.layout.item_subject_mark, parent, false);

            holder.mMarkView = (ImageView) customView.findViewById(R.id.image_view);
            holder.mTypeTextView = (TextView) customView.findViewById(R.id.type);
            holder.mDateTextView = (TextView) customView.findViewById(R.id.date);
            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .bold()
                .useFont(Typeface.DEFAULT)
                .fontSize(30)
                .endConfig()
                .buildRoundRect(String.valueOf(mark.getValue()), CommonFunctions.setMarkColor(context, mark.getValue()), 8);

        holder.mMarkView.setImageDrawable(drawable);
        holder.mTypeTextView.setText(mark.getType());
        holder.mDateTextView.setText(CommonFunctions.getDate(mark.getDate(),
                CommonFunctions.FORMAT_YYYY_MM_DD, CommonFunctions.FORMAT_DD_MM_YYYY));

        return customView;
    }
}

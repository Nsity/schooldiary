package com.example.nsity.schooldiary.navigation.homework;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.nsity.schooldiary.R;

import java.util.ArrayList;

/**
 * Created by fedorova on 25.11.2015.
 */
public class HomeworkAdapter extends BaseAdapter {
    private ArrayList<Homework> arrayList;
    private LayoutInflater layoutInflater;
    private Context context;

    public HomeworkAdapter(Context context, ArrayList<Homework> arrayList) {
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class ViewHolder {
        TextView mTimeTextView;
        TextView mSubjectTextView;
        TextView mTaskTextView;
        ImageView mImageView;
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
        final Homework homework = arrayList.get(position);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            customView = layoutInflater.inflate(R.layout.item_homework, parent, false);
            holder.mTimeTextView = (TextView) customView.findViewById(R.id.homework_subject);
            holder.mSubjectTextView = (TextView) customView.findViewById(R.id.homework_subject);
            holder.mImageView = (ImageView) customView.findViewById(R.id.image_view);
            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }


        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .fontSize(30) /* size in px */
                .toUpperCase()
                .endConfig()
                .buildRound("A", Color.BLUE);

        holder.mImageView.setImageDrawable(drawable);

        holder.mSubjectTextView.setText(homework.getSubjectName());

        return customView;
        }
    }
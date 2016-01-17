package com.example.nsity.schooldiary.navigation.homework;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.lesson.Lesson;
import com.example.nsity.schooldiary.system.BaseEntity;
import com.example.nsity.schooldiary.system.CommonFunctions;

import java.util.ArrayList;

/**
 * Created by nsity on 17.01.16.
 */
public class HomeworkAdapter extends BaseAdapter {

    private ArrayList<BaseEntity> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;

    public HomeworkAdapter(Context context, ArrayList<BaseEntity> arrayList) {
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
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
        final BaseEntity item = arrayList.get(position);

        if(item instanceof Lesson) {
            customView = layoutInflater.inflate(R.layout.item_homework, parent, false);
            ((TextView) customView.findViewById(R.id.subject)).setText(((Lesson) item).getSubject().getName());
            ((TextView) customView.findViewById(R.id.homework)).setText(((Lesson) item).getHomework());

            ((GradientDrawable) (customView.findViewById(R.id.circle)).getBackground()).setColor(CommonFunctions.setColor(context, ((Lesson) item).getSubject().getColor()));
        }

        if(item instanceof SectionItem) {
            customView = layoutInflater.inflate(R.layout.item_section, parent, false);
            ((TextView) customView.findViewById(R.id.title)).setText(((SectionItem)item).getTitle());
        }

        return customView;
    }

}

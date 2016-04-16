package com.example.nsity.schooldiary.navigation.homework;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.lesson.Lesson;
import com.example.nsity.schooldiary.system.BaseEntity;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.tables.LessonsDBInterface;
import com.rey.material.widget.CheckBox;

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
       // this.layoutInflater = LayoutInflater.from(context);
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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.item_homework, parent, false);

        final BaseEntity item = arrayList.get(position);

        if(item instanceof Lesson) {


            ((TextView) customView.findViewById(R.id.subject)).setText(((Lesson) item).getSubject().getName());
            ((TextView) customView.findViewById(R.id.homework)).setText(((Lesson) item).getHomework());

            ((GradientDrawable) (customView.findViewById(R.id.circle)).getBackground()).setColor(CommonFunctions.setColor(context, ((Lesson) item).getSubject().getColor()));

            CheckBox mIsHomeworkCompleted = (CheckBox) customView.findViewById(R.id.is_homework_completed);

            mIsHomeworkCompleted.setChecked(((Lesson) item).isHomeworkCompleted());
            mIsHomeworkCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    new LessonsDBInterface(context).setLessonIsHomeworkCompleted(item.getId(), isChecked ? 1 : 0);
                    ((Lesson) item).setIsHomeworkCompleted(isChecked);
                }
            });
        }

        if(item instanceof SectionItem) {
            customView = inflater.inflate(R.layout.item_section, parent, false);
            ((TextView) customView.findViewById(R.id.title)).setText(((SectionItem)item).getTitle());
        }

        return customView;
    }

}

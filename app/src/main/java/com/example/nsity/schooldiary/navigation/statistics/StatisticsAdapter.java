package com.example.nsity.schooldiary.navigation.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.util.ArrayList;

/**
 * Created by nsity on 17.01.16.
 */
public class StatisticsAdapter extends BaseAdapter {

    private ArrayList<Score> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;

    public StatisticsAdapter(Context context, ArrayList<Score> arrayList) {
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public static class ViewHolder {
        TextView mSubjectTextView;
        BarChart mBarChart;
        TextView mNoDateTextView;
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
        final Score score = arrayList.get(position);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            customView = layoutInflater.inflate(R.layout.item_statistics, parent, false);
            holder.mSubjectTextView = (TextView) customView.findViewById(R.id.subject);
            holder.mBarChart = (BarChart)customView.findViewById(R.id.barchart);
            holder.mNoDateTextView = (TextView)customView.findViewById(R.id.no_data);
            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }

        holder.mSubjectTextView.setText(score.getSubject().getName());

        if(score.getMinAverageScore() == 0 && score.getAverageScore() == 0 && score.getMaxAverageScore() == 0) {
            holder.mNoDateTextView.setVisibility(View.VISIBLE);
            holder.mBarChart.setVisibility(View.GONE);
        } else {
            holder.mNoDateTextView.setVisibility(View.GONE);
            holder.mBarChart.setVisibility(View.VISIBLE);

            holder.mBarChart.clearChart();

            holder.mBarChart.addBar(new BarModel(context.getResources().getString(R.string.min_score),
                    (float)score.getMinAverageScore(), setColor(score.getMinAverageScore())));
            holder.mBarChart.addBar(new BarModel(context.getResources().getString(R.string.score),
                    (float)score.getAverageScore(), setColor(score.getAverageScore())));
            holder.mBarChart.addBar(new BarModel(context.getResources().getString(R.string.max_score),
                    (float) score.getMaxAverageScore(), setColor(score.getMaxAverageScore())));

            holder.mBarChart.startAnimation();
        }

        return customView;
    }


    private int setColor(double mark) {
        int[] colors = context.getResources().getIntArray(R.array.mark_colors);
        if (mark >= 4.5) {
            return colors[0];
        }
        if (mark < 4.5 && mark >= 3.5) {
            return colors[1];
        }
        if (mark < 3.5 && mark >= 2.5) {
            return colors[2];
        }
        if (mark < 2.5 && mark > 0) {
            return colors[3];
        }

        return context.getResources().getColor(R.color.blue_grey);
    }
}

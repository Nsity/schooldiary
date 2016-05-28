package com.example.nsity.schooldiary.navigation.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonFunctions;

import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by nsity on 28.05.16.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int VIEW_PROGRESS = 0;
    private final int VIEW_NEWS = 1;
    private Context context;
    private ArrayList<News> newsList;

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView theme, date, author, text;

        public NewsViewHolder(View view) {
            super(view);
            theme = (TextView) view.findViewById(R.id.theme);
            date = (TextView) view.findViewById(R.id.date);
            author = (TextView) view.findViewById(R.id.author);
            text = (TextView) view.findViewById(R.id.text);
        }
    }



    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public MaterialProgressBar progressBar;

        public ProgressViewHolder(View view) {
            super(view);
            progressBar = (MaterialProgressBar)view.findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    public NewsAdapter(Context context, ArrayList<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if(viewType == VIEW_PROGRESS) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            return new ProgressViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_row, parent, false);
            return new NewsViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (newsList.get(position) != null) {
            return VIEW_NEWS;
        } else {
            return VIEW_PROGRESS;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof NewsViewHolder) {
            News item = newsList.get(position);
            ((NewsViewHolder) holder).theme.setText(item.getTitle());

            String text = CommonFunctions.getDate(item.getDate(),
                    CommonFunctions.FORMAT_YYYY_MM_DD, CommonFunctions.FORMAT_DD_MM_YYYY);



            if(item.getTeacher() == null) {

                text += " | " + context.getString(R.string.admin);
               // ((NewsViewHolder) holder).author.setText(R.string.admin);
            } else {

                text += " | " +item.getTeacher().getName();
               // ((NewsViewHolder) holder).author.setText(item.getTeacher().getName());
            }


            ((NewsViewHolder) holder).date.setText(text);
            ((NewsViewHolder) holder).text.setText(item.getText());
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}

package com.example.nsity.schooldiary.navigation.homework.recent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.nsity.schooldiary.R;

/**
 * Created by nsity on 13.12.15.
 */

public class EndlessListView extends ListView {
    @SuppressWarnings("unused")
    private final static String TAG = "EndlessListView";

    public interface OnLoadMoreListener {
        boolean onLoadMore();
    }

    private boolean mIsLoading;
    private ProgressBar progressBar;
    private OnLoadMoreListener onLoadMoreListener;

    public EndlessListView(Context context) {
        super(context);
        init();
    }

    public EndlessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void loadMoreComplete() {
        mIsLoading = false;
        progressBar.setVisibility(View.GONE);
    }

    private void init() {
        mIsLoading = false;

        progressBar = new ProgressBar(getContext(), null,
                R.style.Widget_MaterialProgressBar_ProgressBar);

        progressBar = (ProgressBar) LayoutInflater.from(getContext()).inflate(R.layout.progressbar, null);
        LinearLayout.LayoutParams progressBarParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        progressBar.setLayoutParams(progressBarParams);
        progressBar.setPadding(16, 16, 16, 16);
        progressBar.setVisibility(View.GONE);

        LinearLayout footerLinearLayout = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        footerLinearLayout.setGravity(Gravity.CENTER);
        footerLinearLayout.setLayoutParams(layoutParams);
        footerLinearLayout.addView(progressBar);

        addFooterView(footerLinearLayout);

        super.setOnScrollListener(new ELScrollChangedListener());
    }

    private class ELScrollChangedListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

            boolean loadMore;
            loadMore = (0 != totalItemCount)
                    && ((firstVisibleItem + visibleItemCount) >= (totalItemCount));

            if (!mIsLoading && loadMore) {

                if (null != onLoadMoreListener) {
                    if (onLoadMoreListener.onLoadMore()) {
                        mIsLoading = true;
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
}

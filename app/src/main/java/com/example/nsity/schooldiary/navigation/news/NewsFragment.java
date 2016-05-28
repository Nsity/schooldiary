package com.example.nsity.schooldiary.navigation.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.messages.ChatRoomActivity;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.CommonManager;
import com.example.nsity.schooldiary.system.Utils;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nsity on 28.05.16.
 */
public class NewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private ArrayList<News> newsList;
    private View mProgressView;
    private SwipeRefreshLayout swipeRefreshLayout;


    private boolean mLoading = false;

    private int current_page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && isAdded()) {
            actionBar.setTitle(getString(R.string.nav_news));
        }

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        newsList = new ArrayList<>();
        adapter = new NewsAdapter(getActivity(), newsList);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        mProgressView = rootView.findViewById(R.id.progress);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItem = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if ((dy > 0) && !mLoading && lastVisibleItem == totalItem - 1 && !swipeRefreshLayout.isRefreshing()) {
                    mLoading = true;

                    current_page++;
                    newsList.add(null);
                    adapter.notifyItemInserted(newsList.size());
                    // Toast.makeText(getActivity(), String.valueOf(current_page), Toast.LENGTH_SHORT).show();

                    CommonManager.getNews(getActivity(), current_page, new CallBack<ArrayList<News>>() {
                        @Override
                        public void onSuccess(ArrayList<News> arrayList) {
                            if (isAdded() && getActivity() != null) {
                                newsList.remove(newsList.size() - 1);
                                newsList.addAll(arrayList);
                                adapter.notifyDataSetChanged();

                                mLoading = false;

                                if (arrayList.size() == 0) {
                                    current_page--;
                                }
                            }
                        }

                        @Override
                        public void onFail(String error) {
                            if (isAdded() && getActivity() != null) {
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                newsList.remove(newsList.size() - 1);
                                adapter.notifyDataSetChanged();


                                current_page--;
                                mLoading = false;
                            }
                        }
                    });
                }
            }
        });


        recyclerView.addOnItemTouchListener(new NewsAdapter.RecyclerTouchListener(getActivity(),
                recyclerView, new NewsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (mLoading && position == newsList.size() - 1) {
                    return;
                }
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra(Utils.NEWS, newsList.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        setView(current_page);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                current_page = 1;
                setView(current_page);
            }
        });


        /*   recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                newsList.add(null);
                adapter.notifyItemInserted(newsList.size());
                Toast.makeText(getActivity(), String.valueOf(current_page), Toast.LENGTH_SHORT).show();

                CommonManager.getNews(getActivity(), current_page, new CallBack<ArrayList<News>>() {
                    @Override
                    public void onSuccess(ArrayList<News> arrayList) {
                        if (isAdded() && getActivity() != null) {
                            newsList.remove(newsList.size() - 1);
                            newsList.addAll(arrayList);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFail(String error) {
                        if (isAdded() && getActivity() != null) {
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            newsList.remove(newsList.size() - 1);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });*/


        return rootView;
    }


    private void setView(int page) {
        CommonFunctions.showProgress(true, getActivity(), recyclerView, mProgressView);
        CommonManager.getNews(getActivity(), page, new CallBack<ArrayList<News>>() {
            @Override
            public void onSuccess(ArrayList<News> arrayList) {
                if (isAdded() && getActivity() != null) {
                    CommonFunctions.showProgress(false, getActivity(), recyclerView, mProgressView);
                    newsList.clear();
                    newsList.addAll(arrayList);
                    adapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFail(String error) {
                if (isAdded() && getActivity() != null) {
                    CommonFunctions.showProgress(false, getActivity(), recyclerView, mProgressView);
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Server.getHttpClient().cancelRequests(getActivity(), true);
    }

}

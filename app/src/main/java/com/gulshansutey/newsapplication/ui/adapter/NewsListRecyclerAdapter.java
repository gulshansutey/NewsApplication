package com.gulshansutey.newsapplication.ui.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.gulshansutey.newsapplication.model.News;
import com.gulshansutey.newsapplication.ui.adapter.viewholder.NewsAdapterViewHolder;

public class NewsListRecyclerAdapter extends ListAdapter<News, RecyclerView.ViewHolder> {


    private OnItemTouchEvenListener  onItemTouchEvenListener;
    public NewsListRecyclerAdapter() {
        super(News.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return NewsAdapterViewHolder.create(parent,onItemTouchEvenListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        News aNews = getItem(position);
        if (aNews != null) {
            ((NewsAdapterViewHolder)holder).bindViews(aNews);
        }
    }

    public interface OnItemTouchEvenListener{
        void onItemClick(int position);
        void onItemBookmark(int position,boolean check);
    }


}


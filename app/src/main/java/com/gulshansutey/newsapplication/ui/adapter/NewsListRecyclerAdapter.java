package com.gulshansutey.newsapplication.ui.adapter;

import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.gulshansutey.newsapplication.model.News;
import com.gulshansutey.newsapplication.ui.adapter.viewholder.NewsAdapterViewHolder;

import java.util.ArrayList;

public class NewsListRecyclerAdapter extends ListAdapter<News, RecyclerView.ViewHolder> implements Filterable {


    private OnItemTouchEvenListener onItemTouchEvenListener;

    public NewsListRecyclerAdapter() {
        super(News.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return NewsAdapterViewHolder.create(parent, onItemTouchEvenListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        News aNews = getItem(position);
        if (aNews != null) {
            try {
                ((NewsAdapterViewHolder) holder).bindViews(aNews);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnItemTouchEvenListener(OnItemTouchEvenListener onItemTouchEvenListener) {
        this.onItemTouchEvenListener = onItemTouchEvenListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<News> filteredNewsList = new ArrayList<>();
                for (News news : getCurrentList()) {
                    if(news.getAuthor().equalsIgnoreCase(constraint.toString()))filteredNewsList.add(news);
                }
                results.values = filteredNewsList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                submitList((ArrayList<News>) results.values);
            }
        };
    }

    public interface OnItemTouchEvenListener {
        void onItemClick(int position);

        void onItemBookmark(int position, boolean check);
    }


}


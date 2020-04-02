package com.gulshansutey.newsapplication.ui.adapter.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gulshansutey.newsapplication.R;
import com.gulshansutey.newsapplication.model.News;
import com.gulshansutey.newsapplication.ui.adapter.NewsListRecyclerAdapter;
import com.gulshansutey.newsapplication.utils.DateFormatUtils;

public class NewsAdapterViewHolder extends RecyclerView.ViewHolder {


    private AppCompatImageView iv_banner;
    private AppCompatCheckBox cb_bookmark;
    private AppCompatTextView tv_title;
    private AppCompatTextView tv_description;
    private AppCompatTextView tv_source;
    private AppCompatTextView tv_time;
    private Context context;
    private NewsListRecyclerAdapter.OnItemTouchEvenListener onItemTouchEvenListener;
    public NewsAdapterViewHolder(@NonNull View itemView, NewsListRecyclerAdapter.OnItemTouchEvenListener onItemTouchEvenListener) {
        super(itemView);
        this.onItemTouchEvenListener = onItemTouchEvenListener;
        initViews(itemView);
    }

    public static NewsAdapterViewHolder create(ViewGroup parent, NewsListRecyclerAdapter.OnItemTouchEvenListener onItemTouchEvenListener) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_news_list_recycler, parent, false);
        return new NewsAdapterViewHolder(view, onItemTouchEvenListener);
    }

    private void initViews(View v) {
        context = v.getContext();
        iv_banner = v.findViewById(R.id.iv_banner);
        cb_bookmark = v.findViewById(R.id.cb_bookmark);
        tv_title = v.findViewById(R.id.tv_title);
        tv_description = v.findViewById(R.id.tv_description);
        tv_source = v.findViewById(R.id.tv_source);
        tv_time = v.findViewById(R.id.tv_time);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemTouchEvenListener != null) {
                    onItemTouchEvenListener.onItemClick(getAdapterPosition());
                }
            }
        });

        cb_bookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onItemTouchEvenListener != null)
                    onItemTouchEvenListener.onItemBookmark(getAdapterPosition(), isChecked);
            }
        });
    }

    public void bindViews(final News news) throws Exception {
        cb_bookmark.setChecked(news.isBookmarked());
        tv_description.setText(news.getDescription());
        tv_title.setText(news.getTitle());
        tv_source.setText(news.getSource().getName());
        tv_title.setText(news.getTitle());
        tv_time.setText(String.format("• %s", DateFormatUtils.simplifyDate(news.getPublishedAt())));
        Glide.with(context)
                .load(news.getUrlToImage())
                .placeholder(R.drawable.ic_launcher_background).into(iv_banner);
    }
}

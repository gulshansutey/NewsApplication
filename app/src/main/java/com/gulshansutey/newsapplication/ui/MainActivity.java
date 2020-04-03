package com.gulshansutey.newsapplication.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gulshansutey.newsapplication.NewsRepository;
import com.gulshansutey.newsapplication.R;
import com.gulshansutey.newsapplication.model.News;
import com.gulshansutey.newsapplication.model.NewsResponse;
import com.gulshansutey.newsapplication.networking.RestClient;
import com.gulshansutey.newsapplication.ui.adapter.NewsListRecyclerAdapter;
import com.gulshansutey.newsapplication.utils.NewsFilterUtils;

import java.util.ArrayList;

import static com.gulshansutey.newsapplication.ui.actions.SortingActionProvider.SORT_BY_LATEST;
import static com.gulshansutey.newsapplication.ui.actions.SortingActionProvider.SORT_BY_OLDEST;

public class MainActivity extends AppCompatActivity implements NewsListRecyclerAdapter.OnItemTouchEvenListener {

    private final int NEWS_API_REQUEST_CODE = 10;
    private final String NEWS_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json";
    IntentFilter intentFilters;
    BroadcastReceiver connectivityReceiver;
    private NewsListRecyclerAdapter listRecyclerAdapter;
    private ProgressBar progressBar;
    private RecyclerView rv_news_list;
    private NewsRepository newsRepository;
    private ArrayList<News> newsArrayList = new ArrayList<>();
    private ArrayList<String> filterItemsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRepository = new NewsRepository();
        initViews();
        initConnectivityBroadcast();
        getNewsFromUrl();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setIcon(R.drawable.ic_language_white_36dp);
            bar.setDisplayShowHomeEnabled(true);
            bar.setTitle(" News");
        }

        listRecyclerAdapter = new NewsListRecyclerAdapter();
        listRecyclerAdapter.setOnItemTouchEvenListener(this);
        rv_news_list = findViewById(R.id.rv_news_list);
        rv_news_list.setLayoutManager(new LinearLayoutManager(this));
        rv_news_list.setAdapter(listRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getTitle().toString()) {
            case SORT_BY_LATEST:
                sortNewsList(true);
                break;
            case SORT_BY_OLDEST:
                sortNewsList(false);
                break;
        }

        if (!newsArrayList.isEmpty()) {
            if (item.getItemId() == R.id.menu_filter) {
                showFilterDialog();
            }
        } else {
            Toast.makeText(this, "There is nothing to filter", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private void makeFilterList() {
        for (News news : newsArrayList) {
            if (!TextUtils.isEmpty(news.getAuthor()) && !filterItemsList.contains(news.getAuthor())) filterItemsList.add(news.getAuthor());
        }
    }

    /**
     * Sort whatever is inside the adapter
     */
    private void sortNewsList(final boolean b) {
        ArrayList<News> tempList = (ArrayList<News>) listRecyclerAdapter.getCurrentList();
        listRecyclerAdapter.submitList(null);
        NewsFilterUtils.sortDataByDate(tempList, b);
        listRecyclerAdapter.submitList(tempList);
    }

    private void filterNewsList(String keyword) {

        if (keyword == null && listRecyclerAdapter.getCurrentList().size() == newsArrayList.size())
            return;

        listRecyclerAdapter.submitList(null);
        listRecyclerAdapter.submitList(newsArrayList);
        if (keyword != null) {
            listRecyclerAdapter.getFilter().filter(keyword);
        }
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(filterItemsList.toArray(new String[]{}), 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                filterNewsList(filterItemsList.get(which));
            }
        });
        builder.setNegativeButton("Clear Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filterNewsList(null);
            }
        });
        builder.create().show();
    }

    private void getNewsFromUrl() {

        RestClient restClient = new RestClient<NewsResponse>()
                .setMethod(RestClient.METHOD.GET)
                .setRequestCode(NEWS_API_REQUEST_CODE)
                .setUrl(NEWS_URL);

        restClient.execute(NewsResponse.class, new RestClient.Callback<NewsResponse>() {
            @Override
            public void onSuccess(NewsResponse o, int code) {
                if (code == NEWS_API_REQUEST_CODE && o != null)
                    newsArrayList = newsRepository.checkBookmarks(o.getArticles());
                makeFilterList();
                listRecyclerAdapter.submitList(newsArrayList);
            }

            @Override
            public void onProgressChange(int visibility) {
                progressBar.setVisibility(visibility);
            }

            @Override
            public void onFailure(Throwable throwable, int code) {

                /*If fails to load news from api then check and populate
                  if news database has something stored*/
                newsArrayList = newsRepository.checkBookmarks(newsArrayList);
                makeFilterList();
                listRecyclerAdapter.submitList(newsArrayList);

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intentNewsRead = new Intent(MainActivity.this, NewsReadActivity.class);
        intentNewsRead.putExtra(NewsReadActivity.URL, listRecyclerAdapter.getCurrentList().get(position).getUrl());
        startActivity(intentNewsRead);
    }

    @Override
    public void onItemBookmark(int position, boolean check) {
        listRecyclerAdapter.getCurrentList().get(position).setBookmarked(check);
        if (check) {
            newsRepository.bookmarkNews(newsArrayList.get(position));
        } else {
            newsRepository.removeBookmarkNews(newsArrayList.get(position).getTitle());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, intentFilters);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectivityReceiver);
    }

    /**
     * Broadcast Receiver to listen to network and connectivity changes
     */
    private void initConnectivityBroadcast() {
        intentFilters = new IntentFilter();
        intentFilters.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilters.addAction("android.net.wifi.STATE_CHANGE");
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //getNewsFromUrl();
            }
        };


    }

}

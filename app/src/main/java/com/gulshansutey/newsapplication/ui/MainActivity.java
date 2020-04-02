package com.gulshansutey.newsapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gulshansutey.newsapplication.R;
import com.gulshansutey.newsapplication.model.News;
import com.gulshansutey.newsapplication.model.NewsResponse;
import com.gulshansutey.newsapplication.networking.RestClient;
import com.gulshansutey.newsapplication.ui.adapter.NewsListRecyclerAdapter;
import com.gulshansutey.newsapplication.utils.NewsFilterUtils;

import java.util.ArrayList;
import java.util.List;

import static com.gulshansutey.newsapplication.ui.actions.SortingActionProvider.SORT_BY_LATEST;
import static com.gulshansutey.newsapplication.ui.actions.SortingActionProvider.SORT_BY_OLDEST;

public class MainActivity extends AppCompatActivity implements NewsListRecyclerAdapter.OnItemTouchEvenListener {

    private final int NEWS_API_REQUEST_CODE = 10;
    private final String NEWS_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json";
    private NewsListRecyclerAdapter listRecyclerAdapter;
    private ProgressBar progressBar;
    private RecyclerView rv_news_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        getNewsFromUrl();
    }

    private void initViews(){
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
                resetAdapterData(true);
                break;
            case SORT_BY_OLDEST:
                resetAdapterData(false);
                break;
        }

        return true;
    }

    private void resetAdapterData(final boolean b) {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<News> newsList = new ArrayList<>(listRecyclerAdapter.getCurrentList());
                listRecyclerAdapter.submitList(null);
                NewsFilterUtils.sortDataByDate(newsList, b);
                listRecyclerAdapter.submitList(newsList);
            }
        });
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
                    listRecyclerAdapter.submitList(o.getArticles());

            }

            @Override
            public void onProgressChange(int visibility) {
                progressBar.setVisibility(visibility);
            }

            @Override
            public void onFailure(Throwable throwable, int code) {
                Toast.makeText(MainActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Intent intentNewsRead = new Intent(MainActivity.this,NewsReadActivity.class);
        intentNewsRead.putExtra(NewsReadActivity.URL,listRecyclerAdapter.getCurrentList().get(position).getUrl());
        startActivity(intentNewsRead);
    }

    @Override
    public void onItemBookmark(int position, boolean check) {

    }
}

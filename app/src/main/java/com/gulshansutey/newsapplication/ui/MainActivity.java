package com.gulshansutey.newsapplication.ui;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gulshansutey.newsapplication.R;
import com.gulshansutey.newsapplication.model.NewsResponse;
import com.gulshansutey.newsapplication.networking.RestClient;
import com.gulshansutey.newsapplication.ui.adapter.NewsListRecyclerAdapter;

public class MainActivity extends AppCompatActivity {

    private NewsListRecyclerAdapter listRecyclerAdapter;
    private ProgressBar progressBar;
    private final int NEWS_API_REQUEST_CODE = 10;
    private final String NEWS_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        ActionBar bar = getSupportActionBar();
        if (bar!=null){
            bar.setIcon(R.drawable.ic_language_white_36dp);
            bar.setDisplayShowHomeEnabled(true);
            bar.setTitle(" News");
        }

        listRecyclerAdapter = new NewsListRecyclerAdapter();
        RecyclerView rv_news_list = findViewById(R.id.rv_news_list);
        rv_news_list.setLayoutManager(new LinearLayoutManager(this));
        rv_news_list.setAdapter(listRecyclerAdapter);
        getNewsFromUrl();
    }

    private void getNewsFromUrl(){

          RestClient restClient = new RestClient<NewsResponse>()
                 .setMethod(RestClient.METHOD.GET)
                 .setRequestCode(NEWS_API_REQUEST_CODE)
                 .setUrl(NEWS_URL);

        restClient.execute(NewsResponse.class, new RestClient.Callback<NewsResponse>() {
             @Override
             public void onSuccess(NewsResponse o, int code) {
                 if (o!=null)
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


}

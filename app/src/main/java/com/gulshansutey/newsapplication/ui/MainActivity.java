package com.gulshansutey.newsapplication.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gulshansutey.newsapplication.R;
import com.gulshansutey.newsapplication.model.NewsResponse;
import com.gulshansutey.newsapplication.networking.RestClient;

public class MainActivity extends AppCompatActivity {

    private final int NEWS_API_REQUEST_CODE = 10;
    private final String NEWS_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv_news_list = findViewById(R.id.rv_news_list);
        rv_news_list.setLayoutManager(new LinearLayoutManager(this));
        getNewsFromUrl();
    }

    private void getNewsFromUrl(){
         RestClient restClient = new RestClient()
                 .setMethod(RestClient.METHOD.GET)
                 .setRequestCode(NEWS_API_REQUEST_CODE)
                 .setUrl(NEWS_URL);
         restClient.execute(NewsResponse.class, new RestClient.Callback<NewsResponse>() {
             @Override
             public void onSuccess(NewsResponse o, int code) {
                 if (o!=null)
                 Toast.makeText(MainActivity.this, o.getStatus(), Toast.LENGTH_SHORT).show();
             }

             @Override
             public void onFailure(Throwable throwable, int code) {
                 Toast.makeText(MainActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
             }
         });
    }
}

package com.gulshansutey.newsapplication.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.gulshansutey.newsapplication.R;

public class NewsReadActivity extends AppCompatActivity {


    public static final String URL = "url";
    private WebView web_view;
    private ProgressBar progressBar;
    private TextView tv_warning_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_read);
        initViews();
        loadUrl(getIntent().getStringExtra(URL));
    }

    private void initViews(){

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null)actionBar.setDisplayHomeAsUpEnabled(true);
        web_view = findViewById(R.id.web_view);
         tv_warning_message = findViewById(R.id.tv_warning_message);
         progressBar = findViewById(R.id.progressBar);

    }

    private void loadUrl(String url){
        if (url==null)return;
        progressBar.setVisibility(View.VISIBLE);
        tv_warning_message.setVisibility(View.GONE);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.loadUrl(url);
        web_view.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                tv_warning_message.setVisibility(View.VISIBLE);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}

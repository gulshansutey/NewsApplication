package com.gulshansutey.newsapplication;

import android.app.Application;

import com.gulshansutey.newsapplication.database.NewsDatabase;

public class NewsApplication extends Application {

    private static final String DB_NAME = "MyNewsDataBase.db";
    private static final int DB_VERSION = 1;
    private static NewsDatabase database;

    public static NewsDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = new NewsDatabase(getApplicationContext(),DB_NAME,DB_VERSION);

    }

}

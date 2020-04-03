package com.gulshansutey.newsapplication.model;

import java.util.ArrayList;

public class NewsResponse {

    private String status;
    private ArrayList<News> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<News> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<News> articles) {
        this.articles = articles;
    }

}

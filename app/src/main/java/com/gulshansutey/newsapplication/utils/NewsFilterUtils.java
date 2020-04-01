package com.gulshansutey.newsapplication.utils;

import com.gulshansutey.newsapplication.model.News;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NewsFilterUtils {


    public static void sortDataByDate(List<News> news, final boolean toAscending) {
        Collections.sort(news, new Comparator<News>() {
            @Override
            public int compare(News lhs, News rhs) {
                try {
                    if (toAscending) {
                        return DateFormatUtils.dateToMilli(lhs.getPublishedAt()) > DateFormatUtils.dateToMilli(rhs.getPublishedAt())? -1 : (DateFormatUtils.dateToMilli(lhs.getPublishedAt())< DateFormatUtils.dateToMilli(rhs.getPublishedAt())) ? 1 : 0;
                    } else
                        return DateFormatUtils.dateToMilli(rhs.getPublishedAt()) > DateFormatUtils.dateToMilli(lhs.getPublishedAt())? -1 : (DateFormatUtils.dateToMilli(rhs.getPublishedAt())< DateFormatUtils.dateToMilli(lhs.getPublishedAt())) ? 1 : 0;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

}

package com.gulshansutey.newsapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.gulshansutey.newsapplication.database.tables.NewsTable;
import com.gulshansutey.newsapplication.model.News;

import java.util.ArrayList;


/**
 * All the News Database related operations of {@link NewsTable} table
 */
public class NewsRepository {

    private SQLiteDatabase database;

    /**
     * Initialize writable {@link SQLiteDatabase}
     */
    public NewsRepository() {
        database = NewsApplication.getDatabase().getWritableDatabase();
    }

    /**
     * Checks all records list if exist in news database
     * {@link NewsTable}
     *
     * @param list List {@link ArrayList<News>} that needs to be checked if exist in database
     * @return {@link ArrayList<News>} after setting its boolean bookmark value
     */
    public ArrayList<News> checkBookmarks(@Nullable ArrayList<News> list) {
        if (list == null || list.isEmpty()) {
            return getSavedNews();
        }
        ArrayList<News> newsArrayList = new ArrayList<>();
        database.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            News news = list.get(i);
            news.setBookmarked(isItemBookmarked(list.get(i).getTitle()));
            newsArrayList.add(news);
        }
        database.endTransaction();
        return newsArrayList;
    }

    /**
     * Checks a record in news database
     * {@link NewsTable}
     * {@link News}
     *
     * @param whereValue String record title that needs to be checked in news database if exist
     */
    private boolean isItemBookmarked(String whereValue) {
        Cursor cursor = null;
        String tableName = NewsTable.TABLE_NAME;
        String whereColumnName = NewsTable.COLUMN_TITLE;
        try {
            cursor = database.rawQuery("SELECT * FROM " + tableName + " WHERE " + whereColumnName + " = ?;", new String[]{whereValue});
            while (cursor.moveToNext()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();

        }

        return false;
    }


    /**
     * Save a record in news database
     * {@link NewsTable}
     * {@link News}
     *
     * @param news record to be saved
     */
    public void bookmarkNews(News news) {

        ContentValues values = new ContentValues();
        values.put(NewsTable.COLUMN_AUTHOR, news.getAuthor());
        values.put(NewsTable.COLUMN_DESCRIPTION, news.getDescription());
        values.put(NewsTable.COLUMN_PUBLISHED_AT_TIME, news.getPublishedAt());
        values.put(NewsTable.COLUMN_SOURCE_NAME, news.getSource().getName());
        values.put(NewsTable.COLUMN_TITLE, news.getTitle());
        values.put(NewsTable.COLUMN_URL_TO_IMAGE, news.getUrlToImage());
        values.put(NewsTable.COLUMN_URL_TO_NEWS, news.getUrl());

        try {
            database.beginTransaction();
            database.insertOrThrow(NewsTable.TABLE_NAME, null, values);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            values.clear();
            database.endTransaction();
        }

    }


    /**
     * Get saved records from news database
     * {@link NewsTable}
     *
     * @return newsArrayList
     */
    private ArrayList<News> getSavedNews() {
        Cursor cursor = null;
        ArrayList<News> newsArrayList = new ArrayList<>();
        try {
            database.beginTransaction();
            cursor = database.rawQuery("select * from " + NewsTable.TABLE_NAME, null);

            while (cursor.moveToNext()) {
                News news = new News();
                news.setBookmarked(true);
                news.setTitle(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_TITLE)));
                news.setAuthor(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_AUTHOR)));
                news.setPublishedAt(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_PUBLISHED_AT_TIME)));
                news.setDescription(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_DESCRIPTION)));
                News.Source source = new News.Source();
                source.setId(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_SOURCE_NAME)));
                source.setName(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_SOURCE_NAME)));
                news.setSource(source);
                news.setUrl(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_URL_TO_NEWS)));
                news.setUrlToImage(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_URL_TO_IMAGE)));
                newsArrayList.add(news);
            }

        } finally {
            if (cursor != null) cursor.close();
            database.endTransaction();
        }
        return newsArrayList;
    }

    /**
     * Removes record from the news table
     *
     * @param whereValue News title which needs to be removed
     */
    public void removeBookmarkNews(String whereValue) {
        try {
            database.beginTransaction();
            database.delete(NewsTable.TABLE_NAME, NewsTable.COLUMN_TITLE + " = ?", new String[]{whereValue});
        } finally {
            database.endTransaction();
        }
    }

}

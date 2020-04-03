package com.gulshansutey.newsapplication.database.tables;

import android.database.sqlite.SQLiteDatabase;

public class NewsTable {


    /**
     * <b>TABLE_NAME</b> - name of the table in database
     */
    public static String TABLE_NAME = "ApplicationProfile";

    /**
     * Columns for fields
     */
    public static String COLUMN_ID = "id";
    public static String COLUMN_AUTHOR = "author";
    public static String COLUMN_TITLE = "title";
    public static String COLUMN_SOURCE_NAME = "source_name";
    public static String COLUMN_DESCRIPTION = "description";
    public static String COLUMN_URL_TO_IMAGE = "image_url";
    public static String COLUMN_URL_TO_NEWS = "url";
    public static String COLUMN_PUBLISHED_AT_TIME = "published_at";
    public static String COLUMN_HTML_DATA = "news_html_data";


    /**
     * Query to create table @TABLE_NAME with columns
     */
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_AUTHOR + " TEXT ,"
            + COLUMN_TITLE + " TEXT ,"
            + COLUMN_SOURCE_NAME + " TEXT ,"
            + COLUMN_DESCRIPTION + " TEXT ,"
            + COLUMN_HTML_DATA + " TEXT ,"
            + COLUMN_URL_TO_IMAGE + " TEXT ,"
            + COLUMN_URL_TO_NEWS + " TEXT ,"
            + COLUMN_PUBLISHED_AT_TIME + " TEXT "
            + " );";

    /**
     * <b>public static void createTable(SQLiteDatabase db)</b>
     *
     * @param db instance of application's SQLiteDatabase
     */
    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    /**
     * <b>public static void dropTable(SQLiteDatabase db)</b>
     *
     * @param db instance of application's SQLiteDatabase
     */
    private static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }


    /**
     * <b>public static void reset(SQLiteDatabase db)</b>
     * Drop and recreate the table
     *
     * @param db instance of application's SQLiteDatabase
     */
    public static void reset(SQLiteDatabase db) {
        dropTable(db);
        createTable(db);
    }


}

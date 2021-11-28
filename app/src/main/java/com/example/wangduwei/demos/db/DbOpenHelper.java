package com.example.wangduwei.demos.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * Room框架虽好，也要会用原始API
 *
 * @auther:duwei
 * @date:2018/5/28
 */

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "book_provider.db";
    public static final String TABLE_NAME_BOOK = "book";
    public static final String TABLE_NAME_USER = "user";

    private static final int DB_VERSION = 1;

    private String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_BOOK +
            " ( _id INTEGER PRIMARY KEY , name TEXT )";
    private String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USER +
            " ( _id INTEGER PRIMARY KEY , name TEXT , sex INT )";

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

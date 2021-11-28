package com.example.wangduwei.demos.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Process;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.example.wangduwei.demos.db.DbOpenHelper;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/5/28
 */

public class BookProvider extends ContentProvider {
    private static final String AUTHORITY = "com.example.wangduwei.demos.provider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private SQLiteDatabase mDb;

    static {
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.TABLE_NAME_BOOK;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.TABLE_NAME_USER;
                break;
            default:
                break;
        }
        return tableName;

    }

    private void initDbData(Context context) {
        mDb = new DbOpenHelper(context).getWritableDatabase();
        mDb.execSQL("delete from " + DbOpenHelper.TABLE_NAME_BOOK);
        mDb.execSQL("delete from " + DbOpenHelper.TABLE_NAME_USER);
        mDb.execSQL("insert into book values(3,'Android');");
        mDb.execSQL("insert into book values(4,'ios');");
        mDb.execSQL("insert into book values(5,'html');");
        mDb.execSQL("insert into user values(1,'jake',1)");
        mDb.execSQL("insert into user values(2,'james',0)");
    }

    @Override//主线程
    public boolean onCreate() {
        Log.d("demo_provider", "onCreate::thread = " + Thread.currentThread().getName() + ",进程ID = " + Process.myPid());

        initDbData(getContext());
        return false;
    }

    @Nullable
    @Override//并发访问，Bind线程
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Log.d("demo_provider", "query::thread = " + Thread.currentThread().getName() + ",进程ID = " + Process.myPid());
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("");
        }
        return mDb.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d("demo_provider", "getType::thread = " + Thread.currentThread().getName() + ",进程ID = " + Process.myPid());
        return null;
    }

    @Nullable
    @Override//并发访问，Bind线程
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d("demo_provider", "insert::thread = " + Thread.currentThread().getName() + ",进程ID = " + Process.myPid());
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("");
        }
        mDb.insert(tableName, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override//并发访问，Bind线程
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d("demo_provider", "delete::thread = " + Thread.currentThread().getName() + ",进程ID = " + Process.myPid());
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("");
        }
        int count = mDb.delete(tableName, selection, selectionArgs);
        if (count > 0) {
           getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override//并发访问，Bind线程
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d("demo_provider", "update::thread = " + Thread.currentThread().getName() + ",进程ID = " + Process.myPid());
       String tableName = getTableName(uri);
       if (tableName == null){
           throw new IllegalArgumentException("");
       }
       int row = mDb.update(tableName,values,selection,selectionArgs);
       if (row >0){
           getContext().getContentResolver().notifyChange(uri,null);
       }

        return row;
    }
}

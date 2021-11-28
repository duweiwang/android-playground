package com.example.wangduwei.demos.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;

import com.example.wangduwei.demos.main.BaseSupportFragment;

/**
 * <p>ContentProvider的基本使用
 *
 * @author : duwei
 * @date : 2018/5/28
 */

public class ProviderFragment extends BaseSupportFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = Uri.parse("content://com.example.wangduwei.demos.provider/book");

        Cursor bookCursor = getActivity().getContentResolver().query(uri, new String[]{"_id", "name"},
                null, null, null);
        while (bookCursor.moveToNext()) {
            int id = bookCursor.getInt(0);
            String name = bookCursor.getString(1);
            Log.d("demo_provider", "id = " + id + ",name = " + name);
        }
        bookCursor.close();
        //-----------
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", 6);
        contentValues.put("name", "aaa");
        getActivity().getContentResolver().insert(uri, contentValues);
        //----------------


    }
}

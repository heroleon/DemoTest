package com.dcl.testprocess;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;

public class MainActivity extends Activity {
    String[] presidents = {
            "Dwight D. Eisenhower",
            "John F. Kennedy",
            "Lyndon B. Johnson",
            "Richard Nixon",
            "Gerald Ford",
            "Jimmy Carter",
            "Ronald Reagan",
            "George H. W. Bush",
            "Bill Clinton",
            "George W. Bush",
            "Barack Obama"
    };
    private String newId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            String value = savedInstanceState.getCharSequence("value").toString();
            Log.i("MYINFO", "恢复数据----" + value);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, presidents);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.txtCountries);
        textView.setThreshold(3);
        textView.setAdapter(adapter);
        // Glide.with(this).load("").into();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MYINFO", "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MYINFO", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MYINFO", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MYINFO", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MYINFO", "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MYINFO", "onDestroy()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("value", "存储数据测试");
        Log.i("MYINFO", "保存数据");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String value = savedInstanceState.getCharSequence("value").toString();
        Log.i("MYINFO", "恢复数据----" + value);
    }

    public void provider(View view) {
        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.READ_CONTACTS)
                /*以下为自定义提示语、按钮文字
                .setDeniedMessage()
                .setDeniedCloseBtn()
                .setDeniedSettingBtn()
                .setRationalMessage()
                .setRationalBtn()*/
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        Intent intent = new Intent(MainActivity.this, ProviderActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(MainActivity.this, permissions.toString() + "权限被拒绝", Toast.LENGTH_SHORT).show();
                        //makeText(permissions.toString() + "权限拒绝");
                    }
                });
    }

    public void addData(View view) {
        Uri uri = Uri.parse("content://com.dcl.contentprividertest.provider/book");
        ContentValues values = new ContentValues();
        values.put("name", "A Clash of Kings");
        values.put("author", "George Martin");
        values.put("pages", 1040);
        values.put("price", 22.85);
        Uri newUri = getContentResolver().insert(uri, values);
        newId = newUri.getPathSegments().get(1);
    }

    public void DelData(View view) {
        // 删除数据
        Uri uri = Uri.parse("content://com.dcl.contentprividertest.provider/book/" + newId);
        getContentResolver().delete(uri, null, null);
    }

    public void updData(View view) {
        // 更新数据
        Uri uri = Uri.parse("content://com.dcl.contentprividertest.provider/book/" + newId);
        ContentValues values = new ContentValues();
        values.put("name", "A Storm of Swords");
        values.put("pages", 1216);
        values.put("price", 24.05);
        getContentResolver().update(uri, values, null, null);
    }

    public void queryData(View view) {
        Uri uri = Uri.parse("content://com.dcl.contentprividertest.provider/book");
        Cursor cursor = getContentResolver().query(uri, null, null,
                null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.
                        getColumnIndex("name"));
                String author = cursor.getString(cursor.
                        getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex
                        ("pages"));
                double price = cursor.getDouble(cursor.
                        getColumnIndex("price"));
                Log.d("", "book name is " + name);
                Log.d("MainActivity", "book author is " + author);
                Log.d("MainActivity", "book pages is " + pages);
                Log.d("MainActivity", "book price is " + price);
            }
            cursor.close();
        }
    }
}

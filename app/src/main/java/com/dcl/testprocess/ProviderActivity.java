package com.dcl.testprocess;

import android.app.ListActivity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dcl on 2018/1/5.
 */

public class ProviderActivity extends ListActivity {
    private ArrayList<String> contacts = new ArrayList();
    private ArrayAdapter<String> listAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_provider);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        setListAdapter(listAdapter);
        Uri allContacts = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor c;
        CursorLoader cursorLoader = new CursorLoader(
                ProviderActivity.this,
                allContacts,
                null,
                null,
                null,
                null
        );
        c = cursorLoader.loadInBackground();
        try {
            while (c.moveToNext()) {
                String contactId = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                String contactName = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String num = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                int hasPhone = c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));
                if (1 == hasPhone) {
                    contacts.add(contactName + "\n" + num);
                    Log.i("Contentprovider", contactId + "," + contactName + "," + num);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
    }
}

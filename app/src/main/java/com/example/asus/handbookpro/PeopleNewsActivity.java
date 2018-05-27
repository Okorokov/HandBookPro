package com.example.asus.handbookpro;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.asus.handbookpro.sql.SQLiteAssets;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

public class PeopleNewsActivity extends AppCompatActivity {

    private PeopleAdapter adapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<People> testList=new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.search_name);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_news_recyclerView);
        assertNotNull(mRecyclerView);

        linearLayoutManager = new LinearLayoutManager(PeopleNewsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

/*
        Intent intent = getIntent();
        testList=intent.getParcelableArrayListExtra(MainActivity.EXTRA_RESULT_RATING);
*/
        Intent intent = getIntent();
        int flStart=intent.getIntExtra(MainActivity.PARAM_START_ACTNEWS,0);
       // if (flStart==1) {
            SQLiteDatabase db = MainActivity.sqLiteDatabase;
            Cursor cursor = db.rawQuery("select * from "+MainActivity.TABLE_NAME+" where rating LIKE '1'", null);
            //MainActivity.sqLiteHelper.getListByKeyword(db, "1", MainActivity.TABLE_NAME);
            if (cursor != null) {
                testList = new ArrayList<>();
                cursor.moveToFirst();
                do {
                    People people = new People();
                    people.setName(cursor.getString(0));
                    people.setEmail(cursor.getString(1));
                    people.setPhoneWork(cursor.getString(2));
                    people.setPhoneCell(cursor.getString(3));
                    people.setbDay(cursor.getString(4));
                    people.setJob(cursor.getString(5));
                    people.setDepartment(cursor.getString(6));
                    people.setCompany(cursor.getString(7));
                    people.setImage(BitmapUtil.resizeBitMap(BitmapUtil.decodeBitmapFromBytes(cursor.getBlob(8), 2), 150));
                    people.setRating(cursor.getString(9));
                    testList.add(people);
                } while (cursor.moveToNext());

            }
        //}
        adapter = new PeopleAdapter(this, testList, true);
        mRecyclerView.setAdapter(adapter);
    }
}

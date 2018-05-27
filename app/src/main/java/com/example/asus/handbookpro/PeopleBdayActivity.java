package com.example.asus.handbookpro;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

public class PeopleBdayActivity extends AppCompatActivity {


    private PeopleAdapterParcel adapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<PeopleParcelable> testList=new ArrayList<>();


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
        setContentView(R.layout.activity_people_bday);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.bday_name);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_bday_recyclerView);
        assertNotNull(mRecyclerView);

        linearLayoutManager = new LinearLayoutManager(PeopleBdayActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        testList=intent.getParcelableArrayListExtra(MainActivity.EXTRA_RESULT_BDAY);

        adapter = new PeopleAdapterParcel(this, testList);
        mRecyclerView.setAdapter(adapter);




        Log.d("XXXXXXXX", "PeopleBdayActivity-  " + testList.size());


    }
}

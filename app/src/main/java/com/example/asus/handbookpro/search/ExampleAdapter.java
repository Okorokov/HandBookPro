package com.example.asus.handbookpro.search;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.asus.handbookpro.People;
import com.example.asus.handbookpro.R;

import java.util.Date;
import java.util.List;

/**
 * Created by ASUS on 05.05.2018.
 */

public class ExampleAdapter extends CursorAdapter {

    private List<People> items;

    private TextView name;
    private TextView email;
    private TextView phoneWork;
    private TextView phoneCell;
    private TextView bDay;
    private TextView job;
    private TextView department;
    private TextView company;
    private Bitmap image;
    private Date bDayDate;

    public ExampleAdapter(Context context, Cursor cursor, List<People> items) {

        super(context, cursor, false);
        this.items=items;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        name.setText(items.get(cursor.getPosition()).getName());
       // email.setText(items.get(cursor.getPosition()).getEmail());
       /* phoneWork.setText(cursor.getString(2));
        phoneCell.setText(cursor.getString(3));
        bDay.setText(cursor.getString(4));
        job.setText(cursor.getString(5));
        department.setText(cursor.getString(6));
        company.setText(cursor.getString(7));*/
        //people.setImage(BitmapUtil.resizeBitMap(BitmapUtil.decodeBitmapFromBytes(cursor.getBlob(8), 2), 150));

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.search_item, parent, false);

        name = (TextView) view.findViewById(R.id.txtName);


        return view;

    }

}

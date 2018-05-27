package com.example.asus.handbookpro;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PeopleDataActivity extends AppCompatActivity {
    private TextView textViewname, textViewemail, textViewphoneWork, textViewphoneCell, textViewbDay, textViewjob, textViewdepartment, textViewcompany;
    private String stringUriphoneCell;
    private ImageView imageViewimage;
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
        setContentView(R.layout.activity_people_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.data_name);

        imageViewimage = (ImageView) findViewById(R.id.imageView_image);

        textViewname = (TextView) findViewById(R.id.textView_name);
        textViewemail = (TextView) findViewById(R.id.textView_email);
        textViewphoneWork = (TextView) findViewById(R.id.textView_phoneWork);
        textViewphoneCell = (TextView) findViewById(R.id.textView_phoneCell);
        textViewbDay = (TextView) findViewById(R.id.textView_bDay);
        textViewjob = (TextView) findViewById(R.id.textView_job);
        textViewdepartment = (TextView) findViewById(R.id.textView_department);
        textViewcompany = (TextView) findViewById(R.id.textView_company);

        Intent intent = getIntent();
        textViewname.setText(intent.getStringExtra(MainActivity.EXTRA_NAME));
        textViewemail.setText(intent.getStringExtra(MainActivity.EXTRA_EMAIL));
        textViewphoneWork.setText(intent.getStringExtra(MainActivity.EXTRA_PHONEWORK));
        textViewphoneCell.setText(intent.getStringExtra(MainActivity.EXTRA_PHONECELL));
        textViewbDay.setText(intent.getStringExtra(MainActivity.EXTRA_BDAY));
        textViewjob.setText(intent.getStringExtra(MainActivity.EXTRA_JOB));
        textViewdepartment.setText(intent.getStringExtra(MainActivity.EXTRA_DEPARTMENT));
        textViewcompany.setText(intent.getStringExtra(MainActivity.EXTRA_COMPANY));
        imageViewimage.setImageBitmap((Bitmap) getIntent().getParcelableExtra(MainActivity.EXTRA_IMAGE));

        stringUriphoneCell =intent.getStringExtra(MainActivity.EXTRA_PHONECELL);

        FloatingActionButton fabCall = (FloatingActionButton) findViewById(R.id.fab_call);
        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!stringUriphoneCell.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+stringUriphoneCell));
                    startActivity(intent);
                }else {
                    Snackbar.make(view, "Увы! Нету номера телефона", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        FloatingActionButton fabSms = (FloatingActionButton) findViewById(R.id.fab_sms);
        fabSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!stringUriphoneCell.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+stringUriphoneCell));
                    startActivity(intent);
                }else {
                    Snackbar.make(view, "Увы! Нету номера телефона", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        FloatingActionButton fabEmail = (FloatingActionButton) findViewById(R.id.fab_email);
        fabEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!textViewemail.getText().equals("")){
                     Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto",textViewemail.getText().toString(), null));
                    sendIntent.setAction(Intent.ACTION_SENDTO);
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, textViewemail.getText());
                    startActivity(Intent.createChooser(sendIntent, "Send email..."));
                }else {
                    Snackbar.make(view, "Увы! Нету email", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}

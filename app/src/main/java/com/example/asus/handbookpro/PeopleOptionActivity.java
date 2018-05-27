package com.example.asus.handbookpro;

import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.asus.handbookpro.service.MyService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PeopleOptionActivity extends AppCompatActivity {
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_TIME_ON = "timeHour";
    public final static String PARAM_DATE="date";
    public final static String STATE_SWITCH="state_swich";
    public final static String STATE_textViewTime="state_textViewTime";
    public final static String PARAM_hourOfDay="hourOfDay";
    public final static String PARAM_MINUT="MINUT";
    public final static String PARAM_STATE_hourOfDay="state_hourOfDay";
    public final static String PARAM_STATE_MINUT="state_MINUT";
    public final static String PARAM_STATE_MCITY="state_MCITY";
    public final static String PARAM_STATE_MCITYPOS="state_MCITYPOS";
    public final static String STATE_SPINNERVIS="state_SPINNERVIS";
    public final static String PARAM_CITY="param_SPINNERVIS";
    public final static int TASK1_CODE = 1;
    public final static int TIME_ON = 1;
    public final static int TIME_OFF = 2;

    private Switch mSwitch;
    private Intent intent;
    private PendingIntent pi;
    private SharedPreferences sPref;
    public TimePicker timePicker;
    public TextView textViewTime;
    public TimePickerDialog tpd;
    public TimePickerDialog.OnTimeSetListener myCallBack;
    public int mhourOfDay;
    public int mminute;
    String[] cities = {"Все", "ООО АСК Инжиниринг Саров", "ООО АСК Инжиниринг НН", "ООО АСК Инжиниринг Москва", "ООО АСК Инжиниринг Самара", "ООО АСК Инжиниринг СПБ","ОП ООО Синтек Саров","ООО Синтек НН","ОП ООО Синтек Москва","ОП ООО Синтек СПБ","ОП ООО Синтек Омск","ОП ООО Синтек Томск","ОП ООО Синтек Хабаровск","ООО Неотехника","ООО Проматик","ООО УК Альфа-Сервис","АО Атомик Софт Томск","ООО TПК Новатор","ООО Неоэнерго"};
    public Spinner spinner;
    public String mCity="Все";
    public int mCityPos=0;
    public int mtimeId;
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
        setContentView(R.layout.activity_people_option);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.option_name);

        mSwitch=(Switch)findViewById(R.id.switch1);
        textViewTime=(TextView)findViewById(R.id.textViewTime);
        spinner=(Spinner) findViewById(R.id.spinner);

        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PeopleOptionActivity.this, android.R.layout.simple_spinner_item, cities);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                //spinner.set
                mCity=item;
                mCityPos=parent.getSelectedItemPosition();
                Log.d("XXXXXXXX", "mCityPos " + mCityPos);
                SaveState();
                StartService();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        LoadState();





        myCallBack=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Toast.makeText(PeopleOptionActivity.this,"onTimeSet",Toast.LENGTH_SHORT).show();
                mhourOfDay=hourOfDay;
                mminute=minute;
                SaveState();
                SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");

                textViewTime.setText(sdf.format(new Date(0,0,0,mhourOfDay,mminute,0)));
                mtimeId=TIME_ON;
                StartService();

            }
        };
        tpd = new TimePickerDialog(PeopleOptionActivity.this, myCallBack, mhourOfDay, mminute, true);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(PeopleOptionActivity.this,"Start",Toast.LENGTH_SHORT).show();
                    mtimeId=TIME_ON;
                    StartService();
                    textViewTime.setEnabled(true);
                    spinner.setEnabled(true);
                    SaveState();

                }
                else {
                    Toast.makeText(PeopleOptionActivity.this,"Stop",Toast.LENGTH_SHORT).show();
                    mtimeId=TIME_OFF;
                    StartService();
                    textViewTime.setEnabled(false);
                    spinner.setEnabled(false);
                    SaveState();

                }
            }
        });
        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PeopleOptionActivity.this,"onClick",Toast.LENGTH_SHORT).show();
                tpd.show();
            }
        });
    }
    public void StartService(){
        intent = new Intent(PeopleOptionActivity.this, MyService.class);
        pi = createPendingResult(TASK1_CODE,intent,0);
        intent.putExtra(PARAM_TIME_ON, mtimeId);
        intent.putExtra(PARAM_hourOfDay, mhourOfDay);
        intent.putExtra(PARAM_MINUT, mminute);
        intent.putExtra(PARAM_CITY, mCity);
        intent.putExtra(PARAM_PINTENT, pi);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }else{
            startService(intent);
        }
    }
    public void SaveState(){
        sPref = getSharedPreferences("OPTION", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(STATE_SWITCH, mSwitch.isChecked());
        ed.putBoolean(STATE_textViewTime, textViewTime.isEnabled());
        ed.putInt(PARAM_STATE_hourOfDay, mhourOfDay);
        ed.putInt(PARAM_STATE_MINUT, mminute);
        ed.putString(PARAM_STATE_MCITY, mCity);
        ed.putInt(PARAM_STATE_MCITYPOS, mCityPos);
        ed.putBoolean(STATE_SPINNERVIS, spinner.isEnabled());

        ed.commit();
    }
    public void LoadState() {
        sPref = getSharedPreferences("OPTION", MODE_PRIVATE);
        mSwitch.setChecked(sPref.getBoolean(STATE_SWITCH, false));
        textViewTime.setEnabled(sPref.getBoolean(STATE_textViewTime, false));
        mhourOfDay = sPref.getInt(PARAM_STATE_hourOfDay, 0);
        mminute = sPref.getInt(PARAM_STATE_MINUT, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");

        textViewTime.setText(sdf.format(new Date(0,0,0,mhourOfDay,mminute,0)));

        mCity = sPref.getString(PARAM_STATE_MCITY, "Все");
        mCityPos = sPref.getInt(PARAM_STATE_MCITYPOS, 0);
        spinner.setSelection(mCityPos);
        spinner.setEnabled(sPref.getBoolean(STATE_SPINNERVIS, false));
        mtimeId=TIME_ON;
        StartService();
       /* intent = new Intent(PeopleOptionActivity.this, MyService.class);
        pi = createPendingResult(TASK1_CODE,intent,0);
        intent.putExtra(PARAM_TIME_ON, 2);
        intent.putExtra(PARAM_hourOfDay, mhourOfDay);
        intent.putExtra(PARAM_MINUT, mminute);
        intent.putExtra(PARAM_PINTENT, pi);
        if (mSwitch.isChecked()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        }*/
    }
}

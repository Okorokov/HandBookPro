package com.example.asus.handbookpro.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.asus.handbookpro.BitmapUtil;
import com.example.asus.handbookpro.MainActivity;
import com.example.asus.handbookpro.People;
import com.example.asus.handbookpro.PeopleOptionActivity;
import com.example.asus.handbookpro.PeopleParcelable;
import com.example.asus.handbookpro.R;
import com.example.asus.handbookpro.sql.SQLiteAssets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    public  Intent intentt;
    public int NOTIFY_ID = 1;
    public final static String name_base="contactsALL";
    public SQLiteAssets sqLiteHelper;
    public SQLiteDatabase sqLiteDatabase;
    public List<People> list=new ArrayList<>();
    public List<People> flist=new ArrayList<>();
    public Timer myTimer = new Timer();
    public Date dateNow;
    public Context context;
    public PendingIntent contentIntent;
    public Notification.Builder builder;
    public NotificationManager nm;
    public Boolean flag=false;
    public Boolean flagOne=false;
    public Intent notificationIntent;
    public int timeOn;
    public Date when;
    public int mhour;
    public int mminute;
    public Notification n;
    public String mCity;


    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("SERVICE","onCreate");
        dateNow=new Date();
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Handbook online")
                .setContentText("who has a birthday?...")
                .setContentIntent(pendingIntent).build();

        // startForeground(1037, notification);
        startForeground(1037, notification);
        Intent hideIntent = new Intent(this, HideNotification.class);
        startService(hideIntent);
        //OpenDateBase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SERVICE","onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SERVICE","onStartCommand");
        timeOn = intent.getIntExtra(PeopleOptionActivity.PARAM_TIME_ON, 2);
        mhour= intent.getIntExtra(PeopleOptionActivity.PARAM_hourOfDay, 7);
        mminute= intent.getIntExtra(PeopleOptionActivity.PARAM_MINUT, 45);

        mCity= intent.getStringExtra(PeopleOptionActivity.PARAM_CITY);
        when=new Date();
        when.setHours(mhour);
        when.setMinutes(mminute);
        context = getApplicationContext(); //инициатор - текущая активность
        notificationIntent = new Intent(context, MainActivity.class);
        contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        builder = new Notification.Builder(context);

        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                dateNow=new Date();

                Log.d("SERVICE","run");
                Log.d("SERVICE","flag "+flag);
                Log.d("SERVICE","timeOn "+timeOn);
                if(timeOn==1) {
                    Log.d("SERVICE", "timeOn " + timeOn);
                    Log.d("SERVICE", "dateNow.getHours() == when.getHours() " + dateNow.getHours() +"  "+when.getHours()+"dateNow.getMinutes() == when.getMinutes()"+dateNow.getMinutes() +" "+ when.getMinutes());
                    if ((dateNow.getHours() == when.getHours()) & (dateNow.getMinutes() == when.getMinutes())) {
                        Log.d("SERVICE","run ( "+dateNow.getMinutes()+":"+dateNow.getSeconds()+" )");
                        if (!flag) {
                            OpenDateBase();
                            flag = true;
                            Log.d("SERVICE", "flag " + flag);
                        }
                        FilterDateBase();
                        NOTIFY_ID = 1;
                        for (int i = 0; i <= flist.size() - 1; i++) {

                            //SendMessage(flist.get(i).getName(), flist.get(i).getbDay(), flist.get(i).getImage(), NOTIFY_ID);
                            SendMessage(flist.get(i).getName(), flist.get(i).getbDay(), flist.get(i).getImage(), NOTIFY_ID);
                            NOTIFY_ID = NOTIFY_ID + 1;
                        }
                        flagOne=true;
                        Log.d("SERVICE", "mn " );
                        SystemClock.sleep(60000);
                    }

                }
            }
        },0,60000);
        return Service.START_STICKY_COMPATIBILITY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void OpenDateBase(){
        Log.d("XXXXX","name = 1");
        sqLiteHelper=new SQLiteAssets(MyService.this);
        Log.d("XXXXX","name = 2");
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        Log.d("XXXXX","name = 3");
        Cursor cursor = sqLiteHelper.getInformation(sqLiteDatabase, name_base);
        cursor.moveToFirst();
        do {
            //bitmap=BitmapUtil.resizeBitMap(BitmapUtil.decodeBitmapFromBytes(cursor.getBlob(8), 2), 100);
            People people = new People();
            people.setName(cursor.getString(0));
            people.setEmail(cursor.getString(1));
            people.setPhoneWork(cursor.getString(2));
            people.setPhoneCell(cursor.getString(3));
            people.setbDay(cursor.getString(4));
            people.setJob(cursor.getString(5));
            people.setDepartment(cursor.getString(6));
            people.setCompany(cursor.getString(7));
            people.setImage(BitmapUtil.resizeBitMap(BitmapUtil.decodeBitmapFromBytes(cursor.getBlob(8), 2), 100));
            Log.d("XXXXX","name = "+cursor.getString(0));
            list.add(people);
        } while (cursor.moveToNext());
    }
public void FilterDateBase(){
    try {
        flist=new ArrayList<>();
        dateNow = new Date();
        for (int i=0; i<=list.size()-1;i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            //Date date = sdf.parse(list.get(i).getbDay());
            dateNow.setMinutes(0);
            dateNow.setSeconds(0);
            dateNow.setHours(0);
            //dateNow.setDate(23);
            String dateNowParse=sdf.format(dateNow);
            Date dateNowDate=sdf.parse(dateNowParse);
            Log.d("XXXXX","mCity.equals(list.get(i).getCompany() = "+mCity+" list "+list.get(i).getCompany());
            if (dateNowDate.equals(list.get(i).getBDayDateNow())&((mCity.equals(list.get(i).getCompany()))|(mCity.equals("Все")))) {
           // if (dateNowDate.equals(list.get(i).getBDayDateNow())) {
                flist.add(list.get(i));
            }
        }
    } catch (ParseException e) {
        e.printStackTrace();
    }
}
    public void SendMessage(CharSequence title,CharSequence mess, Bitmap bitmap, Integer NOTIFY_ID) {


        nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);


        builder.setContentIntent(contentIntent)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                //.setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setLargeIcon(bitmap)
                .setTicker(mess)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText("День рождение: "+mess); // Текст уведомления

        n = builder.getNotification();

        n.defaults = Notification.DEFAULT_SOUND |
                Notification.DEFAULT_VIBRATE;

        nm.notify(NOTIFY_ID, n);


    };
}

package com.example.asus.handbookpro;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.asus.handbookpro.search.ExampleAdapter;

import com.example.asus.handbookpro.service.MyService;
import com.example.asus.handbookpro.sql.SQLiteAssets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String SAVED_TEXT = "saved_nickName";

    public final static String TABLE_NAME = "contactsALL";
    public final static String EXTRA_NAME = "EXTRA_NAME";
    public final static String EXTRA_EMAIL = "EXTRA_EMAIL";
    public final static String EXTRA_PHONEWORK = "EXTRA_PHONEWORK";
    public final static String EXTRA_PHONECELL = "EXTRA_PHONECELL";
    public final static String EXTRA_BDAY = "EXTRA_BDAY";
    public final static String EXTRA_JOB = "EXTRA_JOB";
    public final static String EXTRA_DEPARTMENT = "EXTRA_DEPARTMENT";
    public final static String EXTRA_COMPANY = "EXTRA_COMPANY";
    public final static String EXTRA_IMAGE = "EXTRA_IMAGE";
    public final static String EXTRA_RESULT_BDAY = "EXTRA_RESULT_BDAY";
    public final static String EXTRA_RESULT_RATING = "EXTRA_RESULT_RATING";
    public final static String PARAM_START_ACTNEWS = "PARAM_START_ACTNEWS";

    private EditText userInput;
    private SharedPreferences sPref;
    private String nickName;
    private DatePicker datePicker;
    private Intent intent;
    private  RecyclerView mRecyclerView;
    public static SQLiteAssets sqLiteHelper;
    public static SQLiteDatabase sqLiteDatabase;
    private Handler handlerCalled;
    private Context context = this;
    private List<People> result = new ArrayList<>();
    private PeopleAdapter adapter;


    private LinearLayoutManager linearLayoutManager;
    private int mPreviousTotal = 0;
    /**
     * True if we are still waiting for the last set of data to load.
     */
    private boolean mLoading = true;
    private Cursor cursor;

    private ProgressBar progressBar;
    private Menu menu;
    private ImageView imageViewAva;
    private TextView textnameheader;
    private  int mndayOfMonth, mnmonth, mnyear;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.INVISIBLE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.d("XXXXXXXX", "***********************************START********************************- ");
        progressBar=findViewById(R.id.progressBar);
        // Наполняем шапку элементами

        //View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        View headerLayout = navigationView.getHeaderView(0);
        textnameheader = headerLayout.findViewById(R.id.text_name_header);
        imageViewAva=headerLayout.findViewById(R.id.imageViewAva);


        mRecyclerView = (RecyclerView) findViewById(R.id.list_recyclerView);
        assertNotNull(mRecyclerView);

        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        sqLiteHelper = new SQLiteAssets(this);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        handlerCalled = new Handler();

        result = new ArrayList<>();
        cursor = sqLiteHelper.getInformation(sqLiteDatabase, TABLE_NAME);

        adapter = new PeopleAdapter(context, result);
        mRecyclerView.setAdapter(adapter);

        //мини авторизация
        if (!loadNickName()) {
            showDialog();
        }
       new AddDataTask().execute();


 /*       mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
           @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                Log.d("XXXXXXXX", "********************************************************************************");
                Log.d("XXXXXXXX", "mLoading- " + mLoading);
                if (mLoading) {
                    Log.d("XXXXXXXX", "totalItemCount > mPreviousTotal- " + totalItemCount+" , "+mPreviousTotal);
                    if (totalItemCount > mPreviousTotal) {
                        mLoading = false;
                        mPreviousTotal = totalItemCount;
                    }
                }
                int visibleThreshold = 8;

                Log.d("XXXXXXXX", "totalItemCount - visibleItemCount " + totalItemCount+" , "+visibleItemCount +": <=firstVisibleItem + visibleThreshold "+firstVisibleItem+" , "+visibleThreshold);
                Log.d("XXXXXXXX", "result" + result.size());
                if (!mLoading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached

                    Log.d("XXXXXXXX", "mLoading-11111 " + mLoading);

                    //result = new ArrayList<>();
                    int i=1;
                    //int delta =count/5;
                    //cursor.moveToPosition(1);
                    //cursor.moveToFirst();
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
                        //people.setImage(BitmapUtil.resizeBitMap(BitmapUtil.decodeBitmapFromBytes(cursor.getBlob(8), 2), 200));
                        Log.d("XXXXXXXX", "getName- " + people.getName());
                        result.add(people);
                        //cursor.moveToNext();
                        Log.d("XXXXXXXX", "i- scrool" + i);
                        i=i+1;
                    } while ((i<8)&(cursor.moveToNext()));
                    Log.d("XXXXXXXX", "cursor.moveToNext()" + cursor.moveToNext());
                    // } while (j<(j+1)*delta);

                    //adapter = new PeopleAdapter(MainActivity.this, result);
                    adapter.notifyDataSetChanged();

                    Log.d("XXXXXXXX", "adapter.notifyDataSetChanged()" + adapter.getItemCount());

                    //mRecyclerView.setAdapter(adapter);
                    mLoading = true;
                }
            }

        });
*/
    }

    class AddDataTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
          progressBar.setVisibility(View.VISIBLE);
           progressBar.setMax(cursor.getCount());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
           // imageViewAva.setImageBitmap(result.get(1).getImage());
                    /*Запускаем службу*/
            intent = new Intent(context, MyService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            }else{
                startService(intent);
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            int i=0;
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
                people.setImage(BitmapUtil.resizeBitMap(BitmapUtil.decodeBitmapFromBytes(cursor.getBlob(8), 1), 100));
                people.setRating(cursor.getString(9));
                Log.d("XXXXXXXX", "getName- " + people.getName());
                result.add(people);
                Log.d("XXXXXXXX", "i- do " + i);
                publishProgress(i);
                i=i+1;
           // } while (i<800);
            } while (cursor.moveToNext());
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setSecondaryProgress(values[0]);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(final String newText) {

                    final List<People> items;
                    String[] columns = new String[]{"_id", "text"};
                    MatrixCursor cursorsearchmc = new MatrixCursor(columns);
                    items = new ArrayList<>();
                    int j = 1;
                    for(int i=0; i<=result.size()-1; i++) {
                        Log.d("XXXXXXXX", "result.get(i).getName().toLowerCase() " + result.get(i).getName().toLowerCase()+" ? newText");
                        if(result.get(i).getName().toLowerCase().contains(newText)){
                            if(j<5) {
                                items.add(result.get(i));
                                cursorsearchmc.addRow(new String[]{String.valueOf(j), result.get(i).getName()});
                                j = j + 1;
                            }
                        }

                    }

                        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

                        final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();

                        search.setSuggestionsAdapter(new ExampleAdapter(MainActivity.this, cursorsearchmc, items));


                    search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                        @Override
                        public boolean onSuggestionSelect(int position) {
                            return false;
                        }

                        @Override
                        public boolean onSuggestionClick(int position) {
                            //search.setQuery(items.get(position).getName(), false);

                            Intent intent= new Intent(MainActivity.this, PeopleDataActivity.class);
                            intent.putExtra(MainActivity.EXTRA_NAME,items.get(position).getName());
                            intent.putExtra(MainActivity.EXTRA_EMAIL,items.get(position).getEmail());
                            intent.putExtra(MainActivity.EXTRA_PHONEWORK,items.get(position).getPhoneWork());
                            intent.putExtra(MainActivity.EXTRA_PHONECELL,items.get(position).getPhoneCell());
                            intent.putExtra(MainActivity.EXTRA_BDAY,items.get(position).getbDay());
                            intent.putExtra(MainActivity.EXTRA_JOB,items.get(position).getJob());
                            intent.putExtra(MainActivity.EXTRA_DEPARTMENT,items.get(position).getDepartment());
                            intent.putExtra(MainActivity.EXTRA_COMPANY,items.get(position).getCompany());
                            intent.putExtra(MainActivity.EXTRA_IMAGE,items.get(position).getImage());
                            startActivity(intent);
                            return true;
                        }
                    });
               // }
                  /*  new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Cursor cursor = sqLiteHelper.getListByKeyword(sqLiteDatabase, newText, MainActivity.TABLE_NAME);
                            if (cursor != null) {
                                result = new ArrayList<>();
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
                                    Log.d("XXXXXXXX", "getName- " + people.getName());
                                    result.add(people);
                                } while (cursor.moveToNext());
                                adapter = new PeopleAdapter(context, result);
                                mRecyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //adapter = new PeopleAdapter(context, result);
                                        mRecyclerView.setAdapter(adapter);
                                        //adapter.notifyDataSetChanged();
                                    }
                                    //recyclerView.setAdapter(adapter);
                                });

                            }
                        }
                    }).start();


                    return false;

                }
            });*/
                    return false;
                }
    });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     /*   if (id == R.id.action_settings) {
            intent= new Intent(MainActivity.this, PeopleOptionActivity.class);
            startActivity(intent);
            return true;
        }
/*
        if (id == R.id.search) {
            intent= new Intent(MainActivity.this, PeopleNewsActivity.class);
            startActivity(intent);
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_news) {
            intent= new Intent(MainActivity.this, PeopleNewsActivity.class);
            //PeopleParcelable peopleParcelable=new PeopleParcelable();
/*
            ArrayList<PeopleParcelable> ratingList=new ArrayList<PeopleParcelable>();
                 for (int i=0; i<=result.size()-1;i++) {

                     result.get(i).setRating("1");

                     if (result.get(i).getRating()) {
                         Log.d("XXXXXXXX", "rresult.get(0) = dateNow " + i);
                         ratingList.add(new PeopleParcelable(result.get(i)));
                     }
                 }
            intent.putParcelableArrayListExtra(MainActivity.EXTRA_RESULT_RATING,ratingList);*/
            //Log.d("XXXXXXXX","rresult.get(0) = "+result.get(0).getName());
            intent.putExtra(PARAM_START_ACTNEWS,1);
            startActivity(intent);
          /*  if (!ratingList.isEmpty()){
                startActivity(intent);
            }else {
                Toast.makeText(MainActivity.this, "Нет избранных контактов", Toast.LENGTH_SHORT).show();
            }
*/
            //Toast.makeText(MainActivity.this, "Увы:( пока в разработке", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_bday) {
            intent= new Intent(MainActivity.this, PeopleBdayActivity.class);
            //PeopleParcelable peopleParcelable=new PeopleParcelable();

            ArrayList<PeopleParcelable> testList=new ArrayList<PeopleParcelable>();
                try {
                    Date dateNow = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

                    for (int i=0; i<=result.size()-1;i++) {

                        //Date date = sdf.parse(result.get(i).getbDay());
                        dateNow.setMinutes(0);
                        dateNow.setSeconds(0);
                        dateNow.setHours(0);
                        //dateNow.setDate(23);
                        String dateNowParse=sdf.format(dateNow);
                        Date dateNowDate=sdf.parse(dateNowParse);
                        //Log.d("XXXXXXXX", "dateNowDate = dateNow " + i);
                        Log.d("XXXXXXXX", i+") dateNowDate =" + dateNowDate+" ; result.get(i).getBDayDateNow() = ");
                        if (dateNowDate.equals(result.get(i).getBDayDateNow())) {
                            Log.d("XXXXXXXX", "rresult.get(0) = dateNow " + i);
                            testList.add(new PeopleParcelable(result.get(i)));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            intent.putParcelableArrayListExtra(MainActivity.EXTRA_RESULT_BDAY,testList);
            //Log.d("XXXXXXXX","rresult.get(0) = "+result.get(0).getName());
            if (!testList.isEmpty()){
                startActivity(intent);
            }else {
                Toast.makeText(MainActivity.this, "Нет день рождений", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_option) {
           intent= new Intent(MainActivity.this, PeopleOptionActivity.class);
            startActivity(intent);
            //Toast.makeText(MainActivity.this, "Увы:( пока в разработке", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_exit) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public  void showDialog(){
        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.dialog_signin, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        userInput = (EditText) promptsView.findViewById(R.id.input_text);
        //datePicker=(DatePicker) promptsView.findViewById(R.id.datePicker);
        //userInput.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        final TextView textViewDate=(TextView)promptsView.findViewById(R.id.textViewDate);
        DatePickerDialog.OnDateSetListener myCallBack=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");
                Date date=new Date();
                //view.getYear()
                date.setYear(view.getYear());
                textViewDate.setText(dayOfMonth+"."+String.valueOf(month+1)+"."+year);

                mndayOfMonth=dayOfMonth;
                mnmonth=month+1;
                mnyear=year;
                //textViewDate.setText(date.toString());
            }

        };

        final DatePickerDialog tpd = new DatePickerDialog(MainActivity.this, myCallBack, 1980, 1, 1);
        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tpd.show();
            }
        });
        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                //Вводим текст и отображаем в строке ввода на основном экране:
                                boolean fl=false;
                                for(int i=0; i<=result.size()-1; i++) {

                                    if(result.get(i).getName().toLowerCase().contains(userInput.getText().toString().toLowerCase())){
                                        Log.d("XXXXXXXX","result.size() = "+i);

                                        try {
                                            Log.d("XXXXXXXX","result.size() = "+i);
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                                            Date date=sdf.parse(result.get(i).getbDay());
                                            //Date dateNow=sdf.parse(datePicker.getDayOfMonth()+"."+String.valueOf(datePicker.getMonth()+1)+"."+datePicker.getYear());
                                            Date dateNow=sdf.parse(mndayOfMonth+"."+mnmonth+"."+mnyear);
                                            if(date.equals(dateNow)){
                                                fl=true;
                                                Log.d("XXXXXXXX", "result.get(i).getName() "+result.get(i).getName());
                                                imageViewAva.setImageBitmap(result.get(i).getImage());
                                                textnameheader.setText(result.get(i).getName());
                                                saveINFO(result.get(i).getName(),result.get(i).getImage());
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }
                                if (fl){
                                   Toast.makeText(MainActivity.this,"Добро пожаловать "+userInput.getText(), Toast.LENGTH_SHORT).show();
                                }else {
                                    finish();
                                    dialog.cancel();
                                }



                                loadNickName();
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                finish();
                                dialog.cancel();

                            }
                        });

        //Создаем AlertDialog:
        AlertDialog alertDialog = mDialogBuilder.create();

        //и отображаем его:
        alertDialog.show();

    }
    private void saveINFO(String nickName, Bitmap bm) throws FileNotFoundException {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, nickName);
        ed.commit();
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES),"savedBitmap.jpg");
        FileOutputStream fos = null;
        fos = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        //Toast.makeText(MainActivity.this, "nickName saved", Toast.LENGTH_SHORT).show();
    }
    private boolean loadNickName() {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        String savedText = sPref.getString(SAVED_TEXT, "");
        textnameheader.setText(savedText);
        nickName=savedText;
        boolean auth=false;
        //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"savedBitmap.jpg");
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES),"savedBitmap.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        imageViewAva.setImageBitmap(bitmap);
        if (!savedText.equals("")){
            auth=true;
            //Toast.makeText(MainActivity.this, "Text loaded ="+savedText, Toast.LENGTH_SHORT).show();
        }
        return auth;
    }
}

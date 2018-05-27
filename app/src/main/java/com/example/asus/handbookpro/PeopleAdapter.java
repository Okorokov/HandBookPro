package com.example.asus.handbookpro;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;


public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {
    private LayoutInflater inflater;
    private List<People> peoples;
    private Boolean fl=false;


    public PeopleAdapter(Context context, List<People> peoples) {
         this.peoples = peoples;
        this.inflater = LayoutInflater.from(context);
     }
    public PeopleAdapter(Context context, List<People> peoples, Boolean fl) {
        this.peoples = peoples;
        this.inflater = LayoutInflater.from(context);
        this.fl=fl;
    }


    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new PeopleViewHolder(view);
    }

    @Override
    public void onViewDetachedFromWindow(PeopleViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
      /*  Animation animation = holder.imageView.getAnimation();
        if (animation != null) {
            animation.cancel();
        }
        holder.imageView.setImageDrawable(null);
        holder.textFio.setText(null);

        holder.textOrg.setText(null);
        holder.textDepartmet.setText(null);
        holder.textTitle.setText(null);*/
    }

    @Override
    public void onBindViewHolder(final PeopleViewHolder holder,final int position) {
        People people = peoples.get(position);
        holder.imageView.setImageBitmap(people.getImage());
        holder.imageView.setAlpha(0f);
        holder.imageView.animate().setDuration(500).alpha(1f).start();
        holder.textFio.setText(people.getName());
        holder.textOrg.setText(people.getCompany());
        holder.textDepartmet.setText(people.getDepartment());
        holder.textTitle.setText(people.getJob());
        holder.checkBox.setChecked(people.getRating());
        if (fl){
            holder.checkBox.setVisibility(View.GONE);
        }else {
            holder.checkBox.setVisibility(View.VISIBLE);
        }
       holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                People people = peoples.get(holder.getAdapterPosition());
                ContentValues cv = new ContentValues();
                if (isChecked){
                    people.setRating("1");
                    cv.put("rating","1");
                }else {
                    people.setRating("0");
                    cv.put("rating","0");
                }
                SQLiteDatabase db=MainActivity.sqLiteDatabase;
                db.update(MainActivity.TABLE_NAME,cv,"name = ?",new String[] { people.getName() });
            }
        });
        View mCurrentView = holder.itemView;
        mCurrentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("XXXXXXXX","contacts- "+holder.getAdapterPosition());

                People people = peoples.get(holder.getAdapterPosition());
                Log.d("XXXXXXXX","people.getBDayDate()- "+people.getBDayDate());

                Context context=v.getContext();
                Intent intent= new Intent(context, PeopleDataActivity.class);
                intent.putExtra(MainActivity.EXTRA_NAME,people.getName());
                intent.putExtra(MainActivity.EXTRA_EMAIL,people.getEmail());
                intent.putExtra(MainActivity.EXTRA_PHONEWORK,people.getPhoneWork());
                intent.putExtra(MainActivity.EXTRA_PHONECELL,people.getPhoneCell());
                intent.putExtra(MainActivity.EXTRA_BDAY,people.getbDay());
                intent.putExtra(MainActivity.EXTRA_JOB,people.getJob());
                intent.putExtra(MainActivity.EXTRA_DEPARTMENT,people.getDepartment());
                intent.putExtra(MainActivity.EXTRA_COMPANY,people.getCompany());
                intent.putExtra(MainActivity.EXTRA_IMAGE,people.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return peoples.size();
    }


    class PeopleViewHolder  extends RecyclerView.ViewHolder{
          ImageView imageView;
          TextView textFio,textOrg,textDepartmet,textTitle;
          CheckBox checkBox;
        public PeopleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_View);
            textFio = itemView.findViewById(R.id.text_Fio);
            textOrg = itemView.findViewById(R.id.text_Org);
            textDepartmet = itemView.findViewById(R.id.text_Departmet);
            textTitle = itemView.findViewById(R.id.text_Title);
            checkBox=itemView.findViewById(R.id.checkBox);

        }
    }
    public void addPeoples(List<People> people){
        for(People pe: people){
            peoples.add(pe);
        }
        notifyDataSetChanged();
    }
}

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class PeopleAdapterParcel extends RecyclerView.Adapter<PeopleAdapterParcel.PeopleViewHolder> {
    private LayoutInflater inflater;

    private List<PeopleParcelable> peoples;

    public PeopleAdapterParcel(Context context, List<PeopleParcelable> peoples) {
         this.peoples = peoples;
        this.inflater = LayoutInflater.from(context);
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
        PeopleParcelable people = peoples.get(position);
        holder.imageView.setImageBitmap(people.people.getImage());
        holder.imageView.setAlpha(0f);
        holder.imageView.animate().setDuration(500).alpha(1f).start();
        holder.textFio.setText(people.people.getName());
        holder.textOrg.setText(people.people.getCompany());
        holder.textDepartmet.setText(people.people.getDepartment());
        holder.textTitle.setText(people.people.getJob());

        holder.checkBox.setEnabled(false);
        holder.checkBox.setVisibility(View.GONE);
        View mCurrentView = holder.itemView;
        mCurrentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("XXXXXXXX","contacts- "+holder.getAdapterPosition());

                PeopleParcelable people = peoples.get(holder.getAdapterPosition());

                Log.d("XXXXXXXX","people.getBDayDate()- "+people.people.getBDayDate());

                Context context=v.getContext();
                Intent intent= new Intent(context, PeopleDataActivity.class);
                intent.putExtra(MainActivity.EXTRA_NAME,people.people.getName());
                intent.putExtra(MainActivity.EXTRA_EMAIL,people.people.getEmail());
                intent.putExtra(MainActivity.EXTRA_PHONEWORK,people.people.getPhoneWork());
                intent.putExtra(MainActivity.EXTRA_PHONECELL,people.people.getPhoneCell());
                intent.putExtra(MainActivity.EXTRA_BDAY,people.people.getbDay());
                intent.putExtra(MainActivity.EXTRA_JOB,people.people.getJob());
                intent.putExtra(MainActivity.EXTRA_DEPARTMENT,people.people.getDepartment());
                intent.putExtra(MainActivity.EXTRA_COMPANY,people.people.getCompany());
                intent.putExtra(MainActivity.EXTRA_IMAGE,people.people.getImage());
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

}

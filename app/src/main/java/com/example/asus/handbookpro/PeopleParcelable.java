package com.example.asus.handbookpro;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ASUS on 11.05.2018.
 */

public class PeopleParcelable  implements Parcelable {
    public People people=new People();


    public PeopleParcelable( People people) {
        this.people=people;

    }
    public PeopleParcelable(Parcel in) {
        this.people.setName(in.readString());
        people.setEmail(in.readString());
        people.setPhoneWork(in.readString());
        people.setPhoneCell(in.readString());
        people.setbDay(in.readString());
        people.setJob(in.readString());
        people.setDepartment(in.readString());
        people.setCompany(in.readString());
        Bitmap bitmap=in.readParcelable(null);
        people.setImage(bitmap);
        people.setRating(in.readString());

        }

        public static final Creator<PeopleParcelable> CREATOR = new Creator<PeopleParcelable>() {
            @Override
            public PeopleParcelable createFromParcel(Parcel in) {
                return new PeopleParcelable(in);
            }

            @Override
            public PeopleParcelable[] newArray(int size) {
                return new PeopleParcelable[size];
            }
        };

        @Override
        public int describeContents () {
            return 0;
        }

        @Override
        public void writeToParcel (Parcel dest,int flags){

        dest.writeString(people.getName());
        dest.writeString(people.getEmail());
        dest.writeString(people.getPhoneWork());
        dest.writeString(people.getPhoneCell());
        dest.writeString(people.getbDay());
        dest.writeString(people.getJob());
        dest.writeString(people.getDepartment());
        dest.writeString(people.getCompany());
         dest.writeParcelable(people.getImage(), flags);
            dest.writeString(people.getRatingSt());
        }
    }

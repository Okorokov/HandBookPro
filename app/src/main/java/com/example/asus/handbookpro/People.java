package com.example.asus.handbookpro;

import android.graphics.Bitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ASUS on 02.04.2018.
 */

public class People {
    private String name;
    private String email;
    private String phoneWork;
    private String phoneCell;
    private String bDay;
    private String job;
    private String department;
    private String company;
    private Bitmap image;
    private Date bDayDate;
    private Date bDayDateNow;
    private Date date=new Date();
    private Boolean rating;
    private String ratingSt;

    public People(){

    }
    public People(String name, String email, String phoneWork, String phoneCell, String bDay, String job, String department, String company, Bitmap image, Date bDayDate, Date bDayDateNow,Boolean rating) {
        this.name = name;
        this.email = email;
        this.phoneWork = phoneWork;
        this.phoneCell = phoneCell;
        this.bDay = bDay;
        this.job = job;
        this.department = department;
        this.company = company;
        this.image = image;
        this.bDayDate=bDayDate;
        this.bDayDateNow=bDayDateNow;
        this.rating=rating;

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneWork() {
        return phoneWork;
    }

    public String getPhoneCell() {
        return phoneCell;
    }

    public String getbDay() {
        return bDay;
    }

    public String getJob() {
        return job;
    }

    public String getDepartment() {
        return department;
    }

    public String getCompany() {
        return company;
    }

    public Bitmap getImage() {

        return image;
    }
    public Date getBDayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            bDayDate=sdf.parse(this.bDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return bDayDate;
    }
    public Date getBDayDateNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            if(!bDay.isEmpty()) {
                Date datebday = sdf.parse(bDay);
                datebday.setYear(date.getYear());

                //bDayDateNow=sdf.parse(datebday.getDay()+"."+String.valueOf(datebday.getMonth()+1)+"."+date.getYear());
                bDayDateNow = datebday;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return bDayDateNow;
    }
    public Boolean getRating() {
        return rating;
    }
    public String getRatingSt() {

        return String.valueOf(rating);
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneWork(String phoneWork) {
        this.phoneWork = phoneWork;
    }

    public void setPhoneCell(String phoneCell) {
        this.phoneCell = phoneCell;
    }

    public void setbDay(String bDay) {
        this.bDay = bDay;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
    public void setRating(String rating) {
        boolean mrating;
        if (rating.equals("0")){
            mrating = false;
        }else{
            mrating = true;
        }
        this.rating = mrating;
    }

}

package com.codeaamirkalimi.mealplanner.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Menu implements Parcelable {

    private String id;
    private String categoryID;
    private String imageUrl;
    private String title;
    private ArrayList<MealDay> mealDays;

    public Menu() { }

    public Menu(String id, String categoryID, String imageUrl, String title, ArrayList<MealDay> mealDays ) {
        this.id = id;
        this.categoryID = categoryID;
        this.imageUrl = imageUrl;
        this.title = title;
        this.mealDays = mealDays;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<MealDay> getMealDays() {
        return mealDays;
    }

    public void setMealDays(ArrayList<MealDay> mealDays) {
        this.mealDays = mealDays;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.categoryID);
        dest.writeString(this.imageUrl);
        dest.writeString(this.title);
        dest.writeTypedList(this.mealDays);
    }

    protected Menu(Parcel in) {
        this.id = in.readString();
        this.categoryID = in.readString();
        this.imageUrl = in.readString();
        this.title = in.readString();
        this.mealDays = in.createTypedArrayList(MealDay.CREATOR);
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel source) {
            return new Menu(source);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

}


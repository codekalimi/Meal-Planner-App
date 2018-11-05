package com.codeaamirkalimi.mealplanner.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuCategory implements Parcelable {

    private String id;
    private String category;

    public MenuCategory() { }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.category);
    }

    public MenuCategory(String id, String category) {
        this.id = id;
        this.category = category;
    }

    protected MenuCategory(Parcel in) {
        this.id = in.readString();
        this.category = in.readString();
    }

    public static final Creator<MenuCategory> CREATOR = new Creator<MenuCategory>() {
        @Override
        public MenuCategory createFromParcel(Parcel source) {
            return new MenuCategory(source);
        }

        @Override
        public MenuCategory[] newArray(int size) {
            return new MenuCategory[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

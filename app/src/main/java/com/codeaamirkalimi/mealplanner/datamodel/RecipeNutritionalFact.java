package com.codeaamirkalimi.mealplanner.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeNutritionalFact implements Parcelable {

    private String title;
    private String units;
    private double quantity;

    public RecipeNutritionalFact() { }

    public RecipeNutritionalFact(String title, String units, double quantity) {
        this.title = title;
        this.units = units;
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.units);
        dest.writeDouble(this.quantity);
    }

    protected RecipeNutritionalFact(Parcel in) {
        this.title = in.readString();
        this.units = in.readString();
        this.quantity = in.readDouble();
    }

    public static final Creator<RecipeNutritionalFact> CREATOR = new Creator<RecipeNutritionalFact>() {
        @Override
        public RecipeNutritionalFact createFromParcel(Parcel source) {
            return new RecipeNutritionalFact(source);
        }

        @Override
        public RecipeNutritionalFact[] newArray(int size) {
            return new RecipeNutritionalFact[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

}

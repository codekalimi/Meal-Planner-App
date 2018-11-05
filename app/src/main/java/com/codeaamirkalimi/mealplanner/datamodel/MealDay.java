package com.codeaamirkalimi.mealplanner.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

public class MealDay implements Parcelable {

    private long id;
    private String recipeBreakfastId;
    private String recipeBrunchId;
    private String recipeLunchId;
    private String recipeDinnerId;

    public MealDay() { }

    public MealDay(long id, String recipeBreakfastId, String recipeBrunchId, String recipeLunchId,
                   String recipeDinnerId) {
        this.id = id;
        this.recipeBreakfastId = recipeBreakfastId;
        this.recipeBrunchId = recipeBrunchId;
        this.recipeLunchId = recipeLunchId;
        this.recipeDinnerId = recipeDinnerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecipeBreakfastId() {
        return recipeBreakfastId;
    }

    public void setRecipeBreakfastId(String recipeBreakfastId) {
        this.recipeBreakfastId = recipeBreakfastId;
    }

    public String getRecipeBrunchId() {
        return recipeBrunchId;
    }

    public void setRecipeBrunchId(String recipeBrunchId) {
        this.recipeBrunchId = recipeBrunchId;
    }

    public String getRecipeLunchId() {
        return recipeLunchId;
    }

    public void setRecipeLunchId(String recipeLunchId) {
        this.recipeLunchId = recipeLunchId;
    }

    public String getRecipeDinnerId() {
        return recipeDinnerId;
    }

    public void setRecipeDinnerId(String recipeDinnerId) {
        this.recipeDinnerId = recipeDinnerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.recipeBreakfastId);
        dest.writeString(this.recipeBrunchId);
        dest.writeString(this.recipeLunchId);
        dest.writeString(this.recipeDinnerId);
    }

    protected MealDay(Parcel in) {
        this.id = in.readLong();
        this.recipeBreakfastId = in.readString();
        this.recipeBrunchId = in.readString();
        this.recipeLunchId = in.readString();
        this.recipeDinnerId = in.readString();
    }

    public static final Creator<MealDay> CREATOR = new Creator<MealDay>() {
        @Override
        public MealDay createFromParcel(Parcel source) {
            return new MealDay(source);
        }

        @Override
        public MealDay[] newArray(int size) {
            return new MealDay[size];
        }
    };
}

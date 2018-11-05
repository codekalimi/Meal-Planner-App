package com.codeaamirkalimi.mealplanner.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

public class ShoppingIngredient implements Parcelable {

    private String name;
    private String quantity;
    private String units;
    private boolean wasBought;

    public ShoppingIngredient(String name, String quantity, String units, boolean wasBought) {
        this.name = name;
        this.quantity = quantity;
        this.units = units;
        this.wasBought = wasBought;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.quantity);
        dest.writeString(this.units);
        dest.writeByte(this.wasBought ? (byte) 1 : (byte) 0);
    }

    protected ShoppingIngredient(Parcel in) {
        this.name = in.readString();
        this.quantity = in.readString();
        this.units = in.readString();
        this.wasBought = in.readByte() != 0;
    }

    public static final Creator<ShoppingIngredient> CREATOR = new Creator<ShoppingIngredient>() {
        @Override
        public ShoppingIngredient createFromParcel(Parcel source) {
            return new ShoppingIngredient(source);
        }

        @Override
        public ShoppingIngredient[] newArray(int size) {
            return new ShoppingIngredient[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public boolean isWasBought() {
        return wasBought;
    }

    public void setWasBought(boolean wasBought) {
        this.wasBought = wasBought;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}

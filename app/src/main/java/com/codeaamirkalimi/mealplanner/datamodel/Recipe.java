package com.codeaamirkalimi.mealplanner.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable {

    private String id;
    private String category;
    private String imageUrl;
    private String title;
    private int servings;
    private String author;
    private String cookingTime;
    private ArrayList<RecipeIngredient> ingredients;
    private ArrayList<RecipeNutritionalFact> nutritionalFacts;
    private ArrayList<RecipeStep> steps;

    public Recipe() { }

    public Recipe(String id, String category, String imageUrl, String title, int servings,
                  String author, String cookingTime, ArrayList<RecipeIngredient> ingredients,
                  ArrayList<RecipeNutritionalFact> nutritionalFacts, ArrayList<RecipeStep> steps) {
        this.id = id;
        this.category = category;
        this.imageUrl = imageUrl;
        this.title = title;
        this.servings = servings;
        this.author = author;
        this.cookingTime = cookingTime;
        this.ingredients = ingredients;
        this.nutritionalFacts = nutritionalFacts;
        this.steps = steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.category);
        dest.writeString(this.imageUrl);
        dest.writeString(this.title);
        dest.writeInt(this.servings);
        dest.writeString(this.author);
        dest.writeString(this.cookingTime);
        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.nutritionalFacts);
        dest.writeTypedList(this.steps);
    }

    protected Recipe(Parcel in) {
        this.id = in.readString();
        this.category = in.readString();
        this.imageUrl = in.readString();
        this.title = in.readString();
        this.servings = in.readInt();
        this.author = in.readString();
        this.cookingTime = in.readString();
        this.ingredients = in.createTypedArrayList(RecipeIngredient.CREATOR);
        this.nutritionalFacts = in.createTypedArrayList(RecipeNutritionalFact.CREATOR);
        this.steps = in.createTypedArrayList(RecipeStep.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public ArrayList<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<RecipeNutritionalFact> getNutritionalFacts() {
        return nutritionalFacts;
    }

    public void setNutritionalFacts(ArrayList<RecipeNutritionalFact> nutritionalFacts) {
        this.nutritionalFacts = nutritionalFacts;
    }

    public ArrayList<RecipeStep> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<RecipeStep> steps) {
        this.steps = steps;
    }
}


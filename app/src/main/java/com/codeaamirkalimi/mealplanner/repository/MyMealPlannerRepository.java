package com.codeaamirkalimi.mealplanner.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.codeaamirkalimi.mealplanner.application.MyMealPlannerExecutors;
import com.codeaamirkalimi.mealplanner.datamodel.MealDay;
import com.codeaamirkalimi.mealplanner.datamodel.Menu;
import com.codeaamirkalimi.mealplanner.datamodel.MenuCategory;
import com.codeaamirkalimi.mealplanner.datamodel.Recipe;
import com.codeaamirkalimi.mealplanner.datamodel.RecipeIngredient;
import com.codeaamirkalimi.mealplanner.datamodel.RecipeNutritionalFact;
import com.codeaamirkalimi.mealplanner.datamodel.RecipeStep;
import com.codeaamirkalimi.mealplanner.remotedatabase.MyMealPlannerRTDBContract;

import java.util.ArrayList;
import java.util.List;

public class MyMealPlannerRepository {

    //--------------------------------------------------------------------------------|
    //                                Constants                                       |
    //--------------------------------------------------------------------------------|

    private static final String TAG = MyMealPlannerRepository.class.getName();


    //--------------------------------------------------------------------------------|
    //                                  Params                                        |
    //--------------------------------------------------------------------------------|

    private static MyMealPlannerRepository INSTANCE;

    private FirebaseDatabase mMyMealPlannerFirebaseDatabase;

    private final MyMealPlannerExecutors mExecutors;

    //--------------------------------------------------------------------------------|
    //                  Constructor (Singleton Pattern)                               |
    //--------------------------------------------------------------------------------|

    private MyMealPlannerRepository(final MyMealPlannerExecutors executors) {
        mExecutors = executors;
        mMyMealPlannerFirebaseDatabase = FirebaseDatabase.getInstance();
        mMyMealPlannerFirebaseDatabase.setPersistenceEnabled(true);
    }

    public static MyMealPlannerRepository getInstance(
                                               final MyMealPlannerExecutors executors) {
        if (INSTANCE == null) {
            synchronized (MyMealPlannerRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MyMealPlannerRepository(executors);
                }
            }
        }
        return INSTANCE;
    }


    //--------------------------------------------------------------------------------|
    //                               Local DB Ops                                     |
    //--------------------------------------------------------------------------------|


    //--------------------------------------------------------------------------------|
    //                               Network Requests                                 |
    //--------------------------------------------------------------------------------|

    public LiveData<List<Menu>> getMenus() {
        final MutableLiveData<List<Menu>> menus = new MutableLiveData<>();
        mExecutors.mainThread().execute(new Runnable() {
           @Override
           public void run() {
               mMyMealPlannerFirebaseDatabase.getReference(
                       MyMealPlannerRTDBContract.RT_MENUS_TABLE_NAME)
                       .addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       List<Menu> data = new ArrayList<>();
                       for(DataSnapshot menu : dataSnapshot.getChildren()){
                           data.add(menu.getValue(Menu.class));
                       }
                       menus.setValue(data);
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {
                       Log.w(TAG, "Failed to read value.", databaseError.toException());
                       menus.setValue(null);
                   }
               });
           }
       });
        return menus;
    }

    public LiveData<List<MenuCategory>> getMenuCategories() {
        final MutableLiveData<List<MenuCategory>> menuCategories = new MutableLiveData<>();
        mExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                mMyMealPlannerFirebaseDatabase.getReference(
                        MyMealPlannerRTDBContract.RT_MENU_CATEGORIES_TABLE_NAME)
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<MenuCategory> data = new ArrayList<>();
                        for(DataSnapshot menuCategory : dataSnapshot.getChildren()){
                            data.add(menuCategory.getValue(MenuCategory.class));
                        }
                        menuCategories.setValue(data);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "Failed to read value.", databaseError.toException());
                        menuCategories.setValue(null);
                    }
                });
            }
        });
        return menuCategories;
    }

    public LiveData<List<Recipe>> getRecipes() {
        final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();
        mExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                mMyMealPlannerFirebaseDatabase.getReference(
                        MyMealPlannerRTDBContract.RT_MENU_RECIPES_TABLE_NAME)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<Recipe> data = new ArrayList<>();
                                for(DataSnapshot recipe : dataSnapshot.getChildren()){
                                    data.add(recipe.getValue(Recipe.class));
                                }
                                recipes.setValue(data);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.w(TAG, "Failed to read value.", databaseError.toException());
                                recipes.setValue(null);
                            }
                        });
            }
        });
        return recipes;
    }

    /**
     * TODO Remove it before release
     */
    public void setRecipes() {
        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();
        ingredients.add(new RecipeIngredient("Large sweet potato"," ", 1));
        ingredients.add(new RecipeIngredient("Cooked spinach","g", 100));
        ingredients.add(new RecipeIngredient("Egg"," ", 1));
        ingredients.add(new RecipeIngredient("Chives","g", 15));
        ingredients.add(new RecipeIngredient("Pepper"," ", 0.0));
        ingredients.add(new RecipeIngredient("Salt"," ", 0.0));
        ArrayList<RecipeNutritionalFact> nutritionalFacts = new ArrayList<>();
        nutritionalFacts.add(new RecipeNutritionalFact("calories", "kcal", 265.5));
        nutritionalFacts.add(new RecipeNutritionalFact("carbs", "g", 41.1));
        nutritionalFacts.add(new RecipeNutritionalFact("cholesterol", "mg", 211.0));
        nutritionalFacts.add(new RecipeNutritionalFact("fat", "g", 5.5));
        nutritionalFacts.add(new RecipeNutritionalFact("protein", "g", 12.6));
        nutritionalFacts.add(new RecipeNutritionalFact("sodium", "mg", 274.8));
        ArrayList<RecipeStep> steps = new ArrayList<>();
        steps.add(new RecipeStep(0,"Preheat oven", "Preheat oven to 250°C.", " ", " "));
        steps.add(new RecipeStep(1,"Cut and toast the potato", "Cut potato in half (longitudinally) and place on a baking sheet. Roast it 12-15 minutes to 200°C.", " ", " "));
        steps.add(new RecipeStep(2,"Poached or fried an egg", "Put water on a stewpan, heat it and boil an egg for 4-5 min (poached egg); or put olive oil in a pan, heat it and fried an egg, stirring in salt and pepper as your taste (fried egg).", " ", " "));
        steps.add(new RecipeStep(3,"Top ingredients and serve", "Top the potato with spinach, the egg and chives and stir in salt and pepper as your taste. Serve it.", " ", " "));
        mMyMealPlannerFirebaseDatabase.getReference(
                MyMealPlannerRTDBContract.RT_MENU_RECIPES_TABLE_NAME).push().setValue(
                        new Recipe(
                                " ",
                                " ",
                                " ",
                                "Spinach & Egg Sweet Potato Toast",
                                1,
                                "EatingWell Magazine",
                                "00:30:00",
                                ingredients,
                                nutritionalFacts,
                                steps));
    }




    /**
     * TODO Remove it before release
     */
    public void setMenus() {
        ArrayList<MealDay> meals = new ArrayList<MealDay>();
        meals.add(new MealDay(0, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(1, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(2, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(3, "aaa", "bbb", "ccc", "ddd"));
        mMyMealPlannerFirebaseDatabase.getReference("menus").push().setValue(new Menu(
                " ",
                " ",
                " ",
                "Veggies Menu 1",
                meals));
        meals = new ArrayList<MealDay>();
        meals.add(new MealDay(0, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(1, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(2, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(3, "aaa", "bbb", "ccc", "ddd"));
        mMyMealPlannerFirebaseDatabase.getReference("menus").push().setValue(new Menu(
                " ",
                " ",
                " ",
                "Veggies Menu 2",
                meals));
        meals = new ArrayList<MealDay>();
        meals.add(new MealDay(0, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(1, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(2, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(3, "aaa", "bbb", "ccc", "ddd"));
        mMyMealPlannerFirebaseDatabase.getReference("menus").push().setValue(new Menu(
                " ",
                " ",
                " ",
                "Veggies Menu 3",
                meals));
        meals = new ArrayList<MealDay>();
        meals.add(new MealDay(0, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(1, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(2, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(3, "aaa", "bbb", "ccc", "ddd"));
        mMyMealPlannerFirebaseDatabase.getReference("menus").push().setValue(new Menu(
                " ",
                " ",
                " ",
                "Veggies Menu 4",
                meals));
        meals = new ArrayList<MealDay>();
        meals.add(new MealDay(0, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(1, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(2, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(3, "aaa", "bbb", "ccc", "ddd"));
        mMyMealPlannerFirebaseDatabase.getReference("menus").push().setValue(new Menu(
                " ",
                " ",
                " ",
                "Veggies Menu 5",
                meals));
        meals = new ArrayList<MealDay>();
        meals.add(new MealDay(0, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(1, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(2, "aaa", "bbb", "ccc", "ddd"));
        meals.add(new MealDay(3, "aaa", "bbb", "ccc", "ddd"));
        mMyMealPlannerFirebaseDatabase.getReference("menus").push().setValue(new Menu(
                " ",
                " ",
                " ",
                "Veggies Menu 6",
                meals));
    }

}

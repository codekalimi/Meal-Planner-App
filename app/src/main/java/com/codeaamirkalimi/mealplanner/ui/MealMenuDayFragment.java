package com.codeaamirkalimi.mealplanner.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.MealDay;
import com.codeaamirkalimi.mealplanner.datamodel.Menu;
import com.codeaamirkalimi.mealplanner.datamodel.Recipe;
import com.codeaamirkalimi.mealplanner.global.MyMealPlannerGlobals;
import com.codeaamirkalimi.mealplanner.ui.adapters.MealMenuDayMealtimeAdapter;
import com.codeaamirkalimi.mealplanner.viewmodel.RecipeViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealMenuDayFragment extends Fragment implements
        MealMenuDayMealtimeAdapter.MealMenuDayMealtimeClickListener {

    public static final String POSITION_KEY = "meal-day-fragment-position";

    public static final String MEAL_DAY_KEY = "meal-day";

    public static final String RECIPE_KEY = "recipe";

    /* Key for storing the list state in savedInstanceState */
    private static final String MEAL_MENU_DAY_RECIPE_LIST_STATE_KEY = "meal-day-recipe-list-state";

    /**
     * Key for storing the meal-day recipes to buy in savedInstanceState
     */
    private static final String MEAL_MENU_DAY_RECIPE_LIST_KEY = "meal-day-recipe-list";

    Unbinder unbinder;
    Context mContext;
    ActionBar actionBar;

    private MealDay mMealDay;

    /**  ViewModel instance */
    private RecipeViewModel mRecipeViewModel;

    @BindView(R.id.rv_meal_menu_day_data)
    RecyclerView rvMealMenuDayData;
    @BindView(R.id.fab_meal_menu_day_schedule)
    FloatingActionButton fabMealMenuDaySchedule;

    /**
     * RecyclerView LayoutManager instance
     */
    private RecyclerView.LayoutManager mMealPlannerMealtimeLayoutManager;
    /**
     * Meal-day Recipes Custom ArrayAdapter
     */
    private MealMenuDayMealtimeAdapter mMealPlannerMealtimeAdapter;
    /**
     * List state stored in savedInstanceState
     */
    private Parcelable mListStateMealPlannerMealtime;


    public MealMenuDayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_meal_menu_day, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mContext = getActivity();

        // Load & set GridLayout
        rvMealMenuDayData.setHasFixedSize(true);
        setRecyclerViewLayoutManager(rootView);

        if (savedInstanceState != null) {

            List<Recipe> mealDayRecipes = savedInstanceState.
                    getParcelableArrayList(MEAL_MENU_DAY_RECIPE_LIST_KEY);
            mMealPlannerMealtimeAdapter = new MealMenuDayMealtimeAdapter(mealDayRecipes, this);
            rvMealMenuDayData.setAdapter(mMealPlannerMealtimeAdapter);
            mMealPlannerMealtimeAdapter.notifyDataSetChanged();

        } else {

            mMealDay = getArguments().getParcelable(MEAL_DAY_KEY);

            // Subscribe MealMenuDayFragment to receive notifications from RecipeViewModel
            registerToRecipeViewModel();

            // Get recipes
            getRecipes();

        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fab_meal_menu_day_schedule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_meal_menu_day_schedule:
                break;
        }
    }

    private void setRecyclerViewLayoutManager(View rootView) {
        switch (this.getResources().getConfiguration().orientation) {
            case MyMealPlannerGlobals.LANDSCAPE_VIEW: // Landscape Mode
                mMealPlannerMealtimeLayoutManager = new GridLayoutManager(rootView.getContext(),
                        MyMealPlannerGlobals.MEAL_MENU_DAY_RECIPE_GV_LAND_COLUMN_NUMB);
                break;
            case MyMealPlannerGlobals.PORTRAIT_VIEW: // Portrait Mode
            default:
                mMealPlannerMealtimeLayoutManager = new GridLayoutManager(rootView.getContext(),
                        MyMealPlannerGlobals.MEAL_MENU_DAY_RECIPE_GV_PORT_COLUMN_NUMB);
                break;
        }
        rvMealMenuDayData.setLayoutManager(mMealPlannerMealtimeLayoutManager);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mListStateMealPlannerMealtime = mMealPlannerMealtimeLayoutManager.onSaveInstanceState();
        outState.putParcelable(MEAL_MENU_DAY_RECIPE_LIST_KEY, mListStateMealPlannerMealtime);
        outState.putParcelableArrayList(MEAL_MENU_DAY_RECIPE_LIST_KEY, (ArrayList<Recipe>)
                mMealPlannerMealtimeAdapter.getmMealDayRecipeList());

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mListStateMealPlannerMealtime = savedInstanceState.getParcelable(MEAL_MENU_DAY_RECIPE_LIST_STATE_KEY);
        }
    }

    @Override
    public void onMealMenuDayMealtimeClick(Recipe recipe) {
        Intent intent = new Intent(mContext, RecipeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_KEY, recipe);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Registers the View into Recipe ViewModel to receive updated recipes (Observer pattern)
     */
    private void registerToRecipeViewModel() {
        // Create RecipeViewModel Factory for param injection
        RecipeViewModel.Factory recipeFactory = new RecipeViewModel.Factory(
                getActivity().getApplication());
        // Get instance of RecipeViewModel
        mRecipeViewModel = ViewModelProviders.of(this, recipeFactory)
                .get(RecipeViewModel.class);
    }

    private void setRecipes() {
        mRecipeViewModel.setRecipes();
    }

    private void getRecipes() {
        mRecipeViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes == null || recipes.isEmpty()) { return; }
                else {
                    populateMealMenuDayMealtimeAdapter(recipes);
                }
            }
        });
    }

    /**
     * Populate ArrayAdapters's list of recipes
     *
     * @param   recipes       Retrieved list of recipes from Realtime Database
     */
    private void populateMealMenuDayMealtimeAdapter(List<Recipe> recipes) {
        Recipe[] mealDayRecipes = new Recipe[4];

        for(Recipe recipe : recipes) {
            if(mMealDay.getRecipeBreakfastId().equals(recipe.getId())) {
                mealDayRecipes[0] = recipe;
            } else if(mMealDay.getRecipeBrunchId().equals(recipe.getId())) {
                mealDayRecipes[1] = recipe;
            } else if(mMealDay.getRecipeLunchId().equals(recipe.getId())) {
                mealDayRecipes[2] = recipe;
            } else if(mMealDay.getRecipeDinnerId().equals(recipe.getId())) {
                mealDayRecipes[3] = recipe;
            } else {}
        }

        // Set Adapter and notifyDataSetChanged
        mMealPlannerMealtimeAdapter = new MealMenuDayMealtimeAdapter(
                new ArrayList<>(Arrays.asList(mealDayRecipes)), this);
        rvMealMenuDayData.setAdapter(mMealPlannerMealtimeAdapter);
        mMealPlannerMealtimeAdapter.notifyDataSetChanged();

    }

}

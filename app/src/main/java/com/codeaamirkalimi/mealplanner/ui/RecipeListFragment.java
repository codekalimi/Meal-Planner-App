package com.codeaamirkalimi.mealplanner.ui;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.Recipe;
import com.codeaamirkalimi.mealplanner.global.MyMealPlannerGlobals;
import com.codeaamirkalimi.mealplanner.ui.adapters.RecipeListAdapter;
import com.codeaamirkalimi.mealplanner.viewmodel.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment
        implements RecipeListAdapter.RecipeListClickListener {

    /* Key for storing the list state in savedInstanceState */
    private static final String RECIPE_LIST_STATE_KEY = "recipe-list-state";

    /** Key for storing the ingredients to buy in savedInstanceState */
    private static final String RECIPE_LIST_KEY = "recipe-list";

    public static final String RECIPE_KEY = "recipe";


    @BindView(R.id.rv_recipe_list)
    RecyclerView rvRecipeList;

    private Activity mContext;
    ActionBar actionBar;
    Unbinder unbinder;

    /**
     * RecyclerView LayoutManager instance
     */
    private RecyclerView.LayoutManager mRecipeListLayoutManager;
    /**
     * Shopping Ingredients Custom ArrayAdapter
     */
    private RecipeListAdapter mRecipeListAdapter;
    /**
     * List state stored in savedInstanceState
     */
    private Parcelable mListStateRecipes;

    /**  ViewModel instance */
    private RecipeViewModel mRecipeViewModel;

    public RecipeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Set ActionBar Title
        setActionBarTitle(getString(R.string.recipe_list_screen_title));

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        mContext = getActivity();
        unbinder = ButterKnife.bind(this, rootView);

        // Load & set GridLayout
        rvRecipeList.setHasFixedSize(true);
        setRecyclerViewLayoutManager(rootView);

        if (savedInstanceState != null) {

            List<Recipe> recipes = savedInstanceState.
                    getParcelableArrayList(RECIPE_LIST_KEY);
            mRecipeListAdapter = new RecipeListAdapter(recipes, this);
            rvRecipeList.setAdapter(mRecipeListAdapter);
            mRecipeListAdapter.notifyDataSetChanged();

        } else {

            // Subscribe RecipeListFragment to receive notifications from RecipeViewModel
            registerToRecipeViewModel();

            // Get recipes
            getRecipes();

        }

        return rootView;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mListStateRecipes = mRecipeListLayoutManager.onSaveInstanceState();

        outState.putParcelable(RECIPE_LIST_STATE_KEY, mListStateRecipes);
        outState.putParcelableArrayList(RECIPE_LIST_KEY, (ArrayList<Recipe>)
                mRecipeListAdapter.getmRecipeList());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mListStateRecipes = savedInstanceState.getParcelable(RECIPE_LIST_STATE_KEY);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //--------------------------------------------------------------------------------|
    //                               DataModel Methods                                |
    //--------------------------------------------------------------------------------|

    /**
     * Registers the View into Recipe ViewModel to receive updated recipes (Observer pattern)
     */
    private void registerToRecipeViewModel() {
        // Create RecipeViewModel Factory for param injection
        RecipeViewModel.Factory menuFactory = new RecipeViewModel.Factory(
                getActivity().getApplication());
        // Get instance of RecipeViewModel
        mRecipeViewModel = ViewModelProviders.of(this, menuFactory)
                .get(RecipeViewModel.class);
    }

    /**
     * Retrieve list of Menus from MenuViewModel (Observer pattern)
     */
    private void getRecipes(){
        mRecipeViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes == null || recipes.isEmpty()) { return; }
                else {
                    populateRecipeListAdapter(recipes);
                }
            }
        });
    }


    //--------------------------------------------------------------------------------|
    //                               Private Methods                                  |
    //--------------------------------------------------------------------------------|

    private void populateRecipeListAdapter(List<Recipe> recipes) {
        mRecipeListAdapter = new RecipeListAdapter(recipes, this);
        rvRecipeList.setAdapter(mRecipeListAdapter);
        mRecipeListAdapter.notifyDataSetChanged();
    }

    private void setRecyclerViewLayoutManager(View rootView) {

        switch (this.getResources().getConfiguration().orientation) {
            case MyMealPlannerGlobals.LANDSCAPE_VIEW: // Landscape Mode
                mRecipeListLayoutManager = new GridLayoutManager(rootView.getContext(),
                        MyMealPlannerGlobals.RECIPE_GV_LAND_COLUMN_NUMB);
                break;
            case MyMealPlannerGlobals.PORTRAIT_VIEW: // Portrait Mode
            default:
                mRecipeListLayoutManager = new GridLayoutManager(rootView.getContext(),
                        MyMealPlannerGlobals.RECIPE_GV_PORT_COLUMN_NUMB);
                break;
        }
        rvRecipeList.setLayoutManager(mRecipeListLayoutManager);
    }

    private void setActionBarTitle(String title) {
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(title);
            if(!actionBar.isShowing()) {
                actionBar.show();
            }
        }
    }

    @Override
    public void onRecipeClickListenerClick(Recipe recipe) {
        Intent intent = new Intent(mContext, RecipeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_KEY, recipe);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}


package com.codeaamirkalimi.mealplanner.ui;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.Recipe;
import com.codeaamirkalimi.mealplanner.ui.adapters.MealPlannerMealtimeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealPlannerFragment extends Fragment
        implements MealPlannerMealtimeAdapter.MealPlannerMealtimeClickListener {

    /* Key for storing the list state in savedInstanceState */
    private static final String MEALTIME_RECIPE_LIST_STATE_KEY = "mealtime-recipe-list-state";

    /** Key for storing the meal-day recipes to buy in savedInstanceState */
    private static final String MEALTIME_RECIPE_LIST_KEY = "mealtime-recipe-list";

    @BindView(R.id.cv_meal_planner_calendar)
    CalendarView cvMealPlannerCalendarView;
    @BindView(R.id.tv_meal_planner_date)
    TextView tvMealPlannerDate;
    @BindView(R.id.rv_cv_meal_planner_mealtime_data)
    RecyclerView rvCvMealPlannerMealtimeData;

    Unbinder unbinder;
    Context mContext;
    ActionBar actionBar;

    /**
     * RecyclerView LayoutManager instance
     */
    private RecyclerView.LayoutManager mMealPlannerMealtimeLayoutManager;
    /**
     * Meal-day Recipes Custom ArrayAdapter
     */
    private MealPlannerMealtimeAdapter mMealPlannerMealtimeAdapter;
    /**
     * List state stored in savedInstanceState
     */
    private Parcelable mListStateMealPlannerMealtime;

    public MealPlannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();

        // Set ActionBar Title
        setActionBarTitle(getString(R.string.meal_planner_screen_title));

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_meal_planner, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        // Set Calendar
        setCalendar(rootView);

        // Load & set GridLayout
        rvCvMealPlannerMealtimeData.setHasFixedSize(true);
        setRecyclerViewLayoutManager(rootView);

        if (savedInstanceState != null) {

            List<Recipe> mealDayRecipes = savedInstanceState.
                    getParcelableArrayList(MEALTIME_RECIPE_LIST_KEY);
            mMealPlannerMealtimeAdapter = new MealPlannerMealtimeAdapter(mealDayRecipes, this);
            rvCvMealPlannerMealtimeData.setAdapter(mMealPlannerMealtimeAdapter);
            mMealPlannerMealtimeAdapter.notifyDataSetChanged();

        } else {

            ArrayList<Recipe> mealDayRecipes = new ArrayList<>();
            // TODO Fill in
            mealDayRecipes.add(null);
            mealDayRecipes.add(null);
            mealDayRecipes.add(null);
            mealDayRecipes.add(null);
            mMealPlannerMealtimeAdapter = new MealPlannerMealtimeAdapter(mealDayRecipes, this);
            // Set Adapter and notifyDataSetChanged
            rvCvMealPlannerMealtimeData.setAdapter(mMealPlannerMealtimeAdapter);
            mMealPlannerMealtimeAdapter.notifyDataSetChanged();

        }

        // Return fragment view
        return rootView;
    }

    private void setActionBarTitle(String title) {
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            if (!actionBar.isShowing()) {
                actionBar.show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    // TODO Implement Calendar
    private void setCalendar(View view) {

    }

    private void setRecyclerViewLayoutManager(View rootView) {
        mMealPlannerMealtimeLayoutManager = new GridLayoutManager(rootView.getContext(),
                1, GridLayoutManager.VERTICAL, false);
        rvCvMealPlannerMealtimeData.setLayoutManager(mMealPlannerMealtimeLayoutManager);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mListStateMealPlannerMealtime = mMealPlannerMealtimeLayoutManager.onSaveInstanceState();
        outState.putParcelable(MEALTIME_RECIPE_LIST_STATE_KEY, mListStateMealPlannerMealtime);
        outState.putParcelableArrayList(MEALTIME_RECIPE_LIST_KEY,
                (ArrayList<Recipe>) mMealPlannerMealtimeAdapter.getmMealDayRecipeList());

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mListStateMealPlannerMealtime = savedInstanceState
                    .getParcelable(MEALTIME_RECIPE_LIST_STATE_KEY);
        }
    }

    @Override
    public void onMealPlannerMealtimeClick(int position) {

    }

}

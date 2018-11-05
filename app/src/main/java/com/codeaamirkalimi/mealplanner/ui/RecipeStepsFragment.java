package com.codeaamirkalimi.mealplanner.ui;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.RecipeStep;
import com.codeaamirkalimi.mealplanner.ui.adapters.RecipeStepsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepsFragment extends Fragment {

    /**
     * Key for storing the list state in savedInstanceState
     */
    private static final String RECIPE_STEP_LIST_STATE_KEY = "recipe-step-list-state";

    /**
     * Key for storing the recipe steps in savedInstanceState
     */
    private static final String RECIPE_STEP_LIST_KEY = "recipe-step-list";

    @BindView(R.id.rv_recipe_steps) RecyclerView rvRecipeSteps;

    Unbinder unbinder;

    /**
     * RecyclerView instance
     */
    private RecyclerView rvRecipeStepsList;
    /**
     * RecyclerView LayoutManager instance
     */
    private RecyclerView.LayoutManager mRecipeStepsLayoutManager;
    /**
     * Recipe Steps Custom ArrayAdapter
     */
    private RecipeStepsAdapter mRecipeStepsAdapter;
    /**
     * List state stored in savedInstanceState
     */
    private Parcelable mListStateRecipeSteps;

    private List<RecipeStep> mRecipeSteps;

    public RecipeStepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        //
        rvRecipeStepsList = (RecyclerView) rootView.findViewById(R.id.rv_recipe_steps);

        // Load & set GridLayout
        rvRecipeStepsList.setHasFixedSize(true);
        setRecyclerViewLayoutManager(rootView);

        if (savedInstanceState != null) {

            mRecipeSteps = savedInstanceState.
                    getParcelableArrayList(RECIPE_STEP_LIST_KEY);
            if (mRecipeSteps != null) {
                mRecipeStepsAdapter = new RecipeStepsAdapter(mRecipeSteps);
                rvRecipeStepsList.setAdapter(mRecipeStepsAdapter);
                mRecipeStepsAdapter.notifyDataSetChanged();
            }

        } else {

            mRecipeSteps = getArguments().getParcelableArrayList(RECIPE_STEP_LIST_KEY);
            if (mRecipeSteps != null) {
                mRecipeStepsAdapter = new RecipeStepsAdapter(mRecipeSteps);
                // Set Adapter and notifyDataSetChanged
                rvRecipeStepsList.setAdapter(mRecipeStepsAdapter);
                mRecipeStepsAdapter.notifyDataSetChanged();
            }

        }

        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void setRecyclerViewLayoutManager(View rootView) {
        mRecipeStepsLayoutManager = new GridLayoutManager(rootView.getContext(),
                1, GridLayoutManager.VERTICAL, false);
        rvRecipeStepsList.setLayoutManager(mRecipeStepsLayoutManager);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mListStateRecipeSteps = mRecipeStepsLayoutManager.onSaveInstanceState();
        outState.putParcelable(RECIPE_STEP_LIST_STATE_KEY, mListStateRecipeSteps);
        outState.putParcelableArrayList(RECIPE_STEP_LIST_KEY,
                (ArrayList<RecipeStep>) mRecipeStepsAdapter.getmRecipeSteps());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mListStateRecipeSteps = savedInstanceState.getParcelable(RECIPE_STEP_LIST_STATE_KEY);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

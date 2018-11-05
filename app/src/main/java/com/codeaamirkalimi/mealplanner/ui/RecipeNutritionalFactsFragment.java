package com.codeaamirkalimi.mealplanner.ui;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codeaamirkalimi.mealplanner.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeNutritionalFactsFragment extends Fragment {


    @BindView(R.id.v_recipe_screen_nutritional_facts_calories_daily_bar)
    View vRecipeScreenNutritionalFactsCaloriesDailyBar;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_calories_value)
    TextView tvRecipeScreenNutritionalFactsCaloriesValue;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_calories_units)
    TextView tvRecipeScreenNutritionalFactsCaloriesUnits;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_calories_pro_day)
    TextView tvRecipeScreenNutritionalFactsCaloriesProDay;
    @BindView(R.id.cl_recipe_screen_nutritional_facts_calories)
    ConstraintLayout clRecipeScreenNutritionalFactsCalories;
    @BindView(R.id.v_recipe_screen_nutritional_facts_fat_daily_bar)
    View vRecipeScreenNutritionalFactsFatDailyBar;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_fat_value)
    TextView tvRecipeScreenNutritionalFactsFatValue;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_fat_units)
    TextView tvRecipeScreenNutritionalFactsFatUnits;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_fat_pro_day)
    TextView tvRecipeScreenNutritionalFactsFatProDay;
    @BindView(R.id.cl_recipe_screen_nutritional_facts_fat)
    ConstraintLayout clRecipeScreenNutritionalFactsFat;
    @BindView(R.id.v_recipe_screen_nutritional_facts_carbs_daily_bar)
    View vRecipeScreenNutritionalFactsCarbsDailyBar;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_carbs_value)
    TextView tvRecipeScreenNutritionalFactsCarbsValue;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_carbs_units)
    TextView tvRecipeScreenNutritionalFactsCarbsUnits;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_carbs_pro_day)
    TextView tvRecipeScreenNutritionalFactsCarbsProDay;
    @BindView(R.id.cl_recipe_screen_nutritional_facts_carbs)
    ConstraintLayout clRecipeScreenNutritionalFactsCarbs;
    @BindView(R.id.v_recipe_screen_nutritional_facts_fiber_daily_bar)
    View vRecipeScreenNutritionalFactsFiberDailyBar;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_fiber_value)
    TextView tvRecipeScreenNutritionalFactsFiberValue;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_fiber_units)
    TextView tvRecipeScreenNutritionalFactsFiberUnits;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_fiber_pro_day)
    TextView tvRecipeScreenNutritionalFactsFiberProDay;
    @BindView(R.id.cl_recipe_screen_nutritional_facts_fiber)
    ConstraintLayout clRecipeScreenNutritionalFactsFiber;
    @BindView(R.id.v_recipe_screen_nutritional_facts_protein_daily_bar)
    View vRecipeScreenNutritionalFactsProteinDailyBar;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_protein_value)
    TextView tvRecipeScreenNutritionalFactsProteinValue;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_protein_units)
    TextView tvRecipeScreenNutritionalFactsProteinUnits;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_protein_pro_day)
    TextView tvRecipeScreenNutritionalFactsProteinProDay;
    @BindView(R.id.cl_recipe_screen_nutritional_facts_protein)
    ConstraintLayout clRecipeScreenNutritionalFactsProtein;
    @BindView(R.id.v_recipe_screen_nutritional_facts_sugars_daily_bar)
    View vRecipeScreenNutritionalFactsSugarsDailyBar;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_sugars_value)
    TextView tvRecipeScreenNutritionalFactsSugarsValue;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_sugars_units)
    TextView tvRecipeScreenNutritionalFactsSugarsUnits;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_sugars_pro_day)
    TextView tvRecipeScreenNutritionalFactsSugarsProDay;
    @BindView(R.id.cl_recipe_screen_nutritional_facts_sugars)
    ConstraintLayout clRecipeScreenNutritionalFactsSugars;
    @BindView(R.id.v_recipe_screen_nutritional_facts_sodium_daily_bar)
    View vRecipeScreenNutritionalFactsSodiumDailyBar;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_sodium_value)
    TextView tvRecipeScreenNutritionalFactsSodiumValue;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_sodium_units)
    TextView tvRecipeScreenNutritionalFactsSodiumUnits;
    @BindView(R.id.tv_recipe_screen_nutritional_facts_sodium_pro_day)
    TextView tvRecipeScreenNutritionalFactsSodiumProDay;
    @BindView(R.id.cl_recipe_screen_nutritional_facts_sodium)
    ConstraintLayout clRecipeScreenNutritionalFactsSodium;
    Unbinder unbinder;

    public RecipeNutritionalFactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_nutritional_facts, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

package com.codeaamirkalimi.mealplanner.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.RecipeIngredient;
import com.codeaamirkalimi.mealplanner.ui.adapters.RecipeIngredientsAlreadyBoughtAdapter;
import com.codeaamirkalimi.mealplanner.ui.adapters.RecipeIngredientsToBuyAdapter;
import com.codeaamirkalimi.mealplanner.ui.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeIngredientsFragment extends Fragment implements
        RecipeIngredientsToBuyAdapter.RecipeIngredientsToBuyClickListener,
        RecipeIngredientsAlreadyBoughtAdapter.RecipeIngredientsAlreadyBoughtClickListener {

    /**
     * Key for retrieving the recipe ingredients from Arguments
     */
    private static final String RECIPE_INGREDIENTS_LIST_KEY = "recipe-ingredient-list";
    /**
     * Key for retrieving the recipe servings from Arguments
     */
    private static final String RECIPE_SERVINGS_KEY = "recipe-servings";
    /**
     * Key for storing the recipe ingredients to buy in savedInstanceState
     */
    private static final String RECIPE_INGREDIENTS_TO_BUY_LIST_KEY = "recipe-ingredient-to-buy-list";
    /**
     * Key for storing the recipe ingredients already bought in savedInstanceState
     */
    private static final String RECIPE_INGREDIENTS_ALREADY_BOUGHT_LIST_KEY = "recipe-ingredient-already-bought-list";

    /**
     * Key for storing the list state in savedInstanceState
     */
    private static final String RECIPE_INGREDIENTS_TO_BUY_LIST_STATE_KEY = "recipe-ingredients-to-buy-list-state";
    /**
     * Key for storing the list state in savedInstanceState
     */
    private static final String RECIPE_INGREDIENTS_ALREADY_BOUGHT_LIST_STATE_KEY = "recipe-ingredients-already-bought-list-state";

    /**
     * Key for storing the expand/collapse status of the to buy ingredient list in savedInstanceState
     */
    private static final String RECIPE_INGREDIENTS_TO_BUY_EXPANDED_KEY = "recipe-ingredients-to-buy-expanded";
    /**
     * Key for storing the expand/collapse status of the to buy ingredient list in savedInstanceState
     */
    private static final String RECIPE_INGREDIENTS_ALREADY_BOUGHT_EXPANDED_KEY = "recipe-ingredients-already-bought-expanded";

    private static final int MAX_NUMBER_OF_SERVINGS = 40;
    private static final int MIN_NUMBER_OF_SERVINGS = 1;

    @BindView(R.id.tv_recipe_screen_ingredients_servings_number)
    TextView tvRecipeScreenIngredientsServingsNumber;
    @BindView(R.id.iv_recipe_screen_ingredients_increase_servings)
    ImageView ivRecipeScreenIngredientsIncreaseServings;
    @BindView(R.id.iv_recipe_screen_ingredients_decrease_servings)
    ImageView ivRecipeScreenIngredientsDecreaseServings;
    @BindView(R.id.iv_recipe_screen_ingredients_to_buy_title_collapse)
    ImageView ivRecipeScreenIngredientsToBuyTitleCollapse;
    @BindView(R.id.iv_recipe_screen_ingredients_to_buy_header_check_all)
    ImageView ivRecipeScreenIngredientsToBuyHeaderCheckAll;
    @BindView(R.id.iv_recipe_screen_ingredients_already_bought_title_collapse)
    ImageView ivRecipeScreenIngredientsAlreadyBoughtTitleCollapse;
    @BindView(R.id.iv_recipe_screen_ingredients_already_bought_header_remove_all)
    ImageView ivRecipeScreenIngredientsAlreadyBoughtHeaderRemoveAll;
    @BindView(R.id.iv_recipe_screen_ingredients_reset_ingredients)
    ImageView ivRecipeScreenIngredientsResetIngredients;

    private ArrayList<RecipeIngredient> mRecipeIngredients;
    private int mServings;

    /**
     * RecyclerView instance
     */
    private RecyclerView rvRecipeScreenIngredientsToBuy;
    /**
     * RecyclerView LayoutManager instance
     */
    private RecyclerView.LayoutManager mRecipeScreenIngredientsToBuyLayoutManager;
    /**
     * Recipe Steps Custom ArrayAdapter
     */
    private RecipeIngredientsToBuyAdapter mRecipeIngredientsToBuyAdapter;
    /**
     * List state stored in savedInstanceState
     */
    private Parcelable mListStateRecipeIngredientsToBuy;

    /**
     * RecyclerView instance
     */
    private RecyclerView rvRecipeScreenIngredientsAlreadyBought;
    /**
     * RecyclerView LayoutManager instance
     */
    private RecyclerView.LayoutManager mRecipeScreenIngredientsAlreadyBoughtLayoutManager;
    /**
     * Recipe Steps Custom ArrayAdapter
     */
    private RecipeIngredientsAlreadyBoughtAdapter mRecipeIngredientsAlreadyBoughtAdapter;
    /**
     * List state stored in savedInstanceState
     */
    private Parcelable mListStateRecipeIngredientsAlreadyBought;

    /**
     * Flag for storing if RecipeIngredientsToBuyRecyclerView is expanded
     */
    private boolean isRecipeIngredientsToBuyRecyclerViewExpanded;
    /**
     * Flag for storing if RecipeIngredientsAlreadyBoughtRecyclerView is expanded
     */
    private boolean isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded;

    private Context mContext;
    Unbinder unbinder;

    public RecipeIngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        mContext = getActivity();
        unbinder = ButterKnife.bind(this, rootView);

        rvRecipeScreenIngredientsToBuy =
                (RecyclerView) rootView.findViewById(R.id.rv_recipe_screen_ingredients_to_buy_body_table);
        rvRecipeScreenIngredientsAlreadyBought =
                (RecyclerView) rootView.findViewById(R.id.rv_recipe_screen_ingredients_already_bought_body_table);

        // Load & set GridLayout
        rvRecipeScreenIngredientsToBuy.setHasFixedSize(true);
        rvRecipeScreenIngredientsAlreadyBought.setHasFixedSize(true);
        setRecyclerViewLayoutManager(rootView);

        if (savedInstanceState != null) {

            mServings = savedInstanceState.getInt(RECIPE_SERVINGS_KEY);
            mRecipeIngredients = savedInstanceState.
                    getParcelableArrayList(RECIPE_INGREDIENTS_LIST_KEY);
            ArrayList<RecipeIngredient> mRecipeIngredientsToBuy = savedInstanceState.
                    getParcelableArrayList(RECIPE_INGREDIENTS_TO_BUY_LIST_KEY);
            ArrayList<RecipeIngredient> mRecipeIngredientsAlreadyBought = savedInstanceState.
                    getParcelableArrayList(RECIPE_INGREDIENTS_ALREADY_BOUGHT_LIST_KEY);

            // Update expand/collapse status
            isRecipeIngredientsToBuyRecyclerViewExpanded = savedInstanceState.
                    getBoolean(RECIPE_INGREDIENTS_TO_BUY_EXPANDED_KEY);
            isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded = savedInstanceState.
                    getBoolean(RECIPE_INGREDIENTS_ALREADY_BOUGHT_EXPANDED_KEY);
            setRecyclerViewVisibility(isRecipeIngredientsToBuyRecyclerViewExpanded,
                    rvRecipeScreenIngredientsToBuy);
            setRecyclerViewVisibility(isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded,
                    rvRecipeScreenIngredientsAlreadyBought);

            // Update current recipe servings
            tvRecipeScreenIngredientsServingsNumber.setText(String.valueOf(mServings));

            // Populate adapters
            mRecipeIngredientsToBuyAdapter =
                    new RecipeIngredientsToBuyAdapter(mRecipeIngredientsToBuy, this);
            mRecipeIngredientsAlreadyBoughtAdapter =
                    new RecipeIngredientsAlreadyBoughtAdapter(mRecipeIngredientsAlreadyBought, this);

            // Notify data changes
            rvRecipeScreenIngredientsToBuy.setAdapter(mRecipeIngredientsToBuyAdapter);
            mRecipeIngredientsToBuyAdapter.notifyDataSetChanged();
            rvRecipeScreenIngredientsAlreadyBought.setAdapter(mRecipeIngredientsAlreadyBoughtAdapter);
            mRecipeIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();

        } else {

            // Retrieve original recipe servings
            mServings = getArguments().getInt(RECIPE_SERVINGS_KEY);
            tvRecipeScreenIngredientsServingsNumber.setText(String.valueOf(mServings));

            mRecipeIngredients = getArguments().getParcelableArrayList(RECIPE_INGREDIENTS_LIST_KEY);

            if (mRecipeIngredients != null) {

                // Mark all ingredients as "to buy" at first time
                mRecipeIngredientsToBuyAdapter =
                        new RecipeIngredientsToBuyAdapter(new ArrayList<RecipeIngredient>(mRecipeIngredients), this);
                // Mark null ingredients as "already bought" at first time
                mRecipeIngredientsAlreadyBoughtAdapter = new
                        RecipeIngredientsAlreadyBoughtAdapter(new ArrayList<RecipeIngredient>(), this);

                // Update expand/collapse status
                if (mRecipeIngredientsToBuyAdapter.getItemCount() > 0) {
                    isRecipeIngredientsToBuyRecyclerViewExpanded = true;
                } else {
                    isRecipeIngredientsToBuyRecyclerViewExpanded = false;
                }
                if (mRecipeIngredientsAlreadyBoughtAdapter.getItemCount() > 0) {
                    isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded = true;
                } else {
                    isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded = false;
                }
                setRecyclerViewVisibility(isRecipeIngredientsToBuyRecyclerViewExpanded,
                        rvRecipeScreenIngredientsToBuy);
                setRecyclerViewVisibility(isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded,
                        rvRecipeScreenIngredientsAlreadyBought);

                // Notify data changes
                rvRecipeScreenIngredientsToBuy.setAdapter(mRecipeIngredientsToBuyAdapter);
                mRecipeIngredientsToBuyAdapter.notifyDataSetChanged();
                rvRecipeScreenIngredientsAlreadyBought.setAdapter(mRecipeIngredientsAlreadyBoughtAdapter);
                mRecipeIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();

            }

        }

        // Set OnClickListeners
        setOnClickListeners();

        // Update Expand/Collapse UI status
        updateStatusCollapseView(isRecipeIngredientsToBuyRecyclerViewExpanded,
                ivRecipeScreenIngredientsToBuyTitleCollapse);
        updateStatusCollapseView(isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded,
                ivRecipeScreenIngredientsAlreadyBoughtTitleCollapse);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mListStateRecipeIngredientsToBuy =
                mRecipeScreenIngredientsToBuyLayoutManager.onSaveInstanceState();
        mListStateRecipeIngredientsAlreadyBought =
                mRecipeScreenIngredientsAlreadyBoughtLayoutManager.onSaveInstanceState();

        outState.putParcelable(RECIPE_INGREDIENTS_TO_BUY_LIST_STATE_KEY,
                mListStateRecipeIngredientsToBuy);
        outState.putParcelable(RECIPE_INGREDIENTS_ALREADY_BOUGHT_LIST_STATE_KEY,
                mListStateRecipeIngredientsAlreadyBought);

        outState.putInt(RECIPE_SERVINGS_KEY, mServings);
        outState.putParcelableArrayList(RECIPE_INGREDIENTS_LIST_KEY, mRecipeIngredients);
        outState.putParcelableArrayList(RECIPE_INGREDIENTS_TO_BUY_LIST_KEY,
                (ArrayList<RecipeIngredient>) mRecipeIngredientsToBuyAdapter.getmRecipeIngredientList());
        outState.putParcelableArrayList(RECIPE_INGREDIENTS_ALREADY_BOUGHT_LIST_KEY,
                (ArrayList<RecipeIngredient>) mRecipeIngredientsAlreadyBoughtAdapter.getmRecipeIngredientList());

        outState.putBoolean(RECIPE_INGREDIENTS_TO_BUY_EXPANDED_KEY,
                isRecipeIngredientsToBuyRecyclerViewExpanded);
        outState.putBoolean(RECIPE_INGREDIENTS_ALREADY_BOUGHT_EXPANDED_KEY,
                isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mListStateRecipeIngredientsToBuy =
                    savedInstanceState.getParcelable(RECIPE_INGREDIENTS_TO_BUY_LIST_STATE_KEY);
            mListStateRecipeIngredientsAlreadyBought =
                    savedInstanceState.getParcelable(RECIPE_INGREDIENTS_ALREADY_BOUGHT_LIST_STATE_KEY);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mListStateRecipeIngredientsToBuy != null) {
            mRecipeScreenIngredientsToBuyLayoutManager.
                    onRestoreInstanceState(mListStateRecipeIngredientsToBuy);
        }
        if (mListStateRecipeIngredientsAlreadyBought != null) {
            mRecipeScreenIngredientsAlreadyBoughtLayoutManager.
                    onRestoreInstanceState(mListStateRecipeIngredientsAlreadyBought);
        }
    }

    @OnClick({R.id.iv_recipe_screen_ingredients_increase_servings,
            R.id.iv_recipe_screen_ingredients_decrease_servings,
            R.id.iv_recipe_screen_ingredients_to_buy_title_collapse,
            R.id.iv_recipe_screen_ingredients_to_buy_header_check_all,
            R.id.iv_recipe_screen_ingredients_already_bought_title_collapse,
            R.id.iv_recipe_screen_ingredients_already_bought_header_remove_all,
            R.id.iv_recipe_screen_ingredients_reset_ingredients})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_recipe_screen_ingredients_increase_servings:
                increaseServings();
                break;
            case R.id.iv_recipe_screen_ingredients_decrease_servings:
                decreaseServings();
                break;
            case R.id.iv_recipe_screen_ingredients_to_buy_title_collapse:
                break;
            case R.id.iv_recipe_screen_ingredients_to_buy_header_check_all:
                break;
            case R.id.iv_recipe_screen_ingredients_already_bought_title_collapse:
                break;
            case R.id.iv_recipe_screen_ingredients_already_bought_header_remove_all:
                break;
            case R.id.iv_recipe_screen_ingredients_reset_ingredients:
                resetIngredientList();
                break;
        }
    }

    @Override
    public void onRecipeIngredientsAlreadyBoughtClick(int position) {
        if (mRecipeIngredientsAlreadyBoughtAdapter.getItemCount() == 0) {
            enableAlreadyBoughtRemoveAllButton();
        }
        if (mRecipeIngredientsAlreadyBoughtAdapter.getItemCount() == 0) {
            isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded = true;
            setRecyclerViewVisibility(isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded,
                    rvRecipeScreenIngredientsAlreadyBought);
            updateStatusCollapseView(isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded,
                    ivRecipeScreenIngredientsAlreadyBoughtTitleCollapse);
        }
        mRecipeIngredientsAlreadyBoughtAdapter.addItem(mRecipeIngredientsToBuyAdapter.getItem(position));
        mRecipeIngredientsToBuyAdapter.removeItem(position);
        if (mRecipeIngredientsToBuyAdapter.getItemCount() == 0) {
            isRecipeIngredientsToBuyRecyclerViewExpanded = false;
            setRecyclerViewVisibility(isRecipeIngredientsToBuyRecyclerViewExpanded,
                    rvRecipeScreenIngredientsToBuy);
            updateStatusCollapseView(isRecipeIngredientsToBuyRecyclerViewExpanded,
                    ivRecipeScreenIngredientsToBuyTitleCollapse);
        }
        mRecipeIngredientsToBuyAdapter.notifyDataSetChanged();
        mRecipeIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();
        if (mRecipeIngredientsToBuyAdapter.getItemCount() == 0) {
            disableToBuyAllBoughtButton();
        }
    }

    @Override
    public void onRecipeIngredientsBackToBuyClick(int position) {
        if (mRecipeIngredientsToBuyAdapter.getItemCount() == 0) {
            enableToBuyAllBoughtButton();
        }
        if (mRecipeIngredientsToBuyAdapter.getItemCount() == 0) {
            isRecipeIngredientsToBuyRecyclerViewExpanded = true;
            setRecyclerViewVisibility(isRecipeIngredientsToBuyRecyclerViewExpanded,
                    rvRecipeScreenIngredientsToBuy);
            updateStatusCollapseView(isRecipeIngredientsToBuyRecyclerViewExpanded,
                    ivRecipeScreenIngredientsToBuyTitleCollapse);
        }
        mRecipeIngredientsToBuyAdapter.addItem(mRecipeIngredientsAlreadyBoughtAdapter.getItem(position));
        mRecipeIngredientsAlreadyBoughtAdapter.removeItem(position);
        if (mRecipeIngredientsAlreadyBoughtAdapter.getItemCount() == 0) {
            isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded = false;
            setRecyclerViewVisibility(isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded,
                    rvRecipeScreenIngredientsAlreadyBought);
            updateStatusCollapseView(isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded,
                    ivRecipeScreenIngredientsAlreadyBoughtTitleCollapse);
        }
        mRecipeIngredientsToBuyAdapter.notifyDataSetChanged();
        mRecipeIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();
        if (mRecipeIngredientsAlreadyBoughtAdapter.getItemCount() == 0) {
            disableAlreadyBoughtRemoveAllButton();
        }
    }

    /**
     * Set OnClickListeners for:
     * - Expand/Collapse to buy list button
     * - Expand/Collapse already bought list button
     * - Mark all ingredients as bought button
     * - Remove all bought ingredients button
     */
    private void setOnClickListeners() {

        /**************************************************/
        /*      Expand/Collapse to buy list button        */
        /*   Expand/Collapse already bought list button   */
        /**************************************************/
        setCollapseViewClickListeners();

        /**************************************************/
        /*      Mark all ingredients as bought button     */
        /**************************************************/
        // 1. Check list of Ingredients to buy is not empty
        if (mRecipeIngredientsToBuyAdapter.getmRecipeIngredientList() != null
                && mRecipeIngredientsToBuyAdapter.getItemCount() > 0) {
            // 2 Set OnClickListener (mark all ingredients as bought button)
            enableToBuyAllBoughtButton();
        } else {
            // 2.1 No ingredients to buy - Disable button
            disableToBuyAllBoughtButton();

        }

        /**************************************************/
        /*      Remove all bought ingredients button      */
        /**************************************************/
        // 1. Check list bought ingredients is not empty
        if (mRecipeIngredientsAlreadyBoughtAdapter.getmRecipeIngredientList() != null
                && mRecipeIngredientsAlreadyBoughtAdapter.getItemCount() > 0) {
            // 2 Set OnClickListener (remove all bought ingredients button)
            enableAlreadyBoughtRemoveAllButton();
        } else {
            // 2.1 No bought ingredients - Remove button
            disableAlreadyBoughtRemoveAllButton();
        }

    }

    private void setRecyclerViewLayoutManager(View rootView) {
        mRecipeScreenIngredientsToBuyLayoutManager = new GridLayoutManager(rootView.getContext(),
                1, GridLayoutManager.VERTICAL, false);
        rvRecipeScreenIngredientsToBuy.setLayoutManager(mRecipeScreenIngredientsToBuyLayoutManager);
        mRecipeScreenIngredientsAlreadyBoughtLayoutManager = new GridLayoutManager(rootView.getContext(),
                1, GridLayoutManager.VERTICAL, false);
        rvRecipeScreenIngredientsAlreadyBought.
                setLayoutManager(mRecipeScreenIngredientsAlreadyBoughtLayoutManager);
    }

    /**
     * Expand / Collapse RecyclerView
     * TODO Improve performance of expand transition
     *
     * @param isExpanded RecyclerView visible state
     * @param view       RecyclerView
     */
    private void setRecyclerViewVisibility(boolean isExpanded, View view) {
        if (isExpanded) {
            ViewUtils.expandView(view);
        } else {
            ViewUtils.collapseView(view);
        }
    }

    /**
     * Update UI status of Expand/Collapse GridView Button
     *
     * @param isExpanded RecyclerView visible state
     * @param view       Expand/Collapse View button
     */
    private void updateStatusCollapseView(boolean isExpanded, ImageView view) {
        // Handle expansion / collapse of RecyclerViews
        if (isExpanded) {
            view.setImageResource(R.mipmap.ic_collapse);
        } else {
            view.setImageResource(R.mipmap.ic_expand);
        }
    }

    private void increaseServings() {
        int servings = Integer.parseInt(tvRecipeScreenIngredientsServingsNumber.getText().toString());
        if (servings < MAX_NUMBER_OF_SERVINGS) {

            // Calculate ingredients ratio
            double ratio = (double)((mServings+1)/Double.valueOf(mServings));
            mServings += 1;
            tvRecipeScreenIngredientsServingsNumber.setText(String.valueOf(mServings));

            // Apply ratio to ingredient quantities
            List<RecipeIngredient> recipeIngredientsToBuy =
                    mRecipeIngredientsToBuyAdapter.getmRecipeIngredientList();
            for(RecipeIngredient recipeIngredient : recipeIngredientsToBuy) {
                recipeIngredient.setQuantity(recipeIngredient.getQuantity() * ratio);
            }

            List<RecipeIngredient> recipeIngredientsAlreadyBought =
                    mRecipeIngredientsAlreadyBoughtAdapter.getmRecipeIngredientList();
            for(RecipeIngredient recipeIngredient : recipeIngredientsAlreadyBought) {
                recipeIngredient.setQuantity(recipeIngredient.getQuantity() * ratio);
            }

            // Notify data changes
            mRecipeIngredientsToBuyAdapter.notifyDataSetChanged();
            mRecipeIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();
        }
    }

    private void decreaseServings() {
        int servings = Integer.parseInt(tvRecipeScreenIngredientsServingsNumber.getText().toString());
        if (servings > MIN_NUMBER_OF_SERVINGS) {
            // Calculate ingredients ratio
            double ratio = (double)((mServings-1)/Double.valueOf(mServings));
            mServings -= 1;
            tvRecipeScreenIngredientsServingsNumber.setText(String.valueOf(mServings));

            // Apply ratio to ingredient quantities
            List<RecipeIngredient> recipeIngredientsToBuy =
                    mRecipeIngredientsToBuyAdapter.getmRecipeIngredientList();
            for(RecipeIngredient recipeIngredient : recipeIngredientsToBuy) {
                recipeIngredient.setQuantity(recipeIngredient.getQuantity() * ratio);
            }

            List<RecipeIngredient> recipeIngredientsAlreadyBought =
                    mRecipeIngredientsAlreadyBoughtAdapter.getmRecipeIngredientList();
            for(RecipeIngredient recipeIngredient : recipeIngredientsAlreadyBought) {
                recipeIngredient.setQuantity(recipeIngredient.getQuantity() * ratio);
            }

            // Notify data changes
            mRecipeIngredientsToBuyAdapter.notifyDataSetChanged();
            mRecipeIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();
        }
    }

    private void resetIngredientList() {

        // Mark all ingredients as "to buy"
        mRecipeIngredientsToBuyAdapter.setmRecipeIngredientList(
                new ArrayList<RecipeIngredient>(mRecipeIngredients));
        isRecipeIngredientsToBuyRecyclerViewExpanded = true;
        // Mark null ingredients as "already bought"
        mRecipeIngredientsAlreadyBoughtAdapter.setmRecipeIngredientList(
                new ArrayList<RecipeIngredient>());
        isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded = false;

        // Update expand/collapse status
        setRecyclerViewVisibility(isRecipeIngredientsToBuyRecyclerViewExpanded,
                rvRecipeScreenIngredientsToBuy);
        setRecyclerViewVisibility(isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded,
                rvRecipeScreenIngredientsAlreadyBought);
        updateStatusCollapseView(isRecipeIngredientsToBuyRecyclerViewExpanded,
                ivRecipeScreenIngredientsToBuyTitleCollapse);
        updateStatusCollapseView(isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded,
                ivRecipeScreenIngredientsAlreadyBoughtTitleCollapse);

        // Notify data changes
        rvRecipeScreenIngredientsToBuy.setAdapter(mRecipeIngredientsToBuyAdapter);
        mRecipeIngredientsToBuyAdapter.notifyDataSetChanged();
        rvRecipeScreenIngredientsAlreadyBought.setAdapter(mRecipeIngredientsAlreadyBoughtAdapter);
        mRecipeIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();

    }

    private void markAllIngredientsAsBought() {

        // Move all ingredients to already bought list
        List<RecipeIngredient> ingredientsToBuy = mRecipeIngredientsToBuyAdapter.getmRecipeIngredientList();
        if (ingredientsToBuy != null) {
            if (ingredientsToBuy.size() > 0) {
                for (int i = ingredientsToBuy.size() - 1; i >= 0; i--) {
                    mRecipeIngredientsAlreadyBoughtAdapter.addItem(ingredientsToBuy.get(i));
                    mRecipeIngredientsToBuyAdapter.removeItem(i);
                }
                mRecipeIngredientsToBuyAdapter.notifyDataSetChanged();
                mRecipeIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();
                enableAlreadyBoughtRemoveAllButton();
            }
        }

        // Update icon and disable button
        disableToBuyAllBoughtButton();

    }

    private void removeAllIngredientsBought() {

        // Remove all bought ingredients
        List<RecipeIngredient> ingredientsBought = mRecipeIngredientsAlreadyBoughtAdapter.getmRecipeIngredientList();
        if (ingredientsBought != null) {
            for (int i = ingredientsBought.size() - 1; i >= 0; i--) {
                mRecipeIngredientsAlreadyBoughtAdapter.removeItem(i);
            }
            mRecipeIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();
        }

        // Update icon and disable button
        disableAlreadyBoughtRemoveAllButton();

    }

    private void setCollapseViewClickListeners() {

        // Set Expand/Collapse OnClick action for To Buy Ingredient List RecyclerView
        ivRecipeScreenIngredientsToBuyTitleCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecipeIngredientsToBuyRecyclerViewExpanded = !isRecipeIngredientsToBuyRecyclerViewExpanded;
                setRecyclerViewVisibility(isRecipeIngredientsToBuyRecyclerViewExpanded, rvRecipeScreenIngredientsToBuy);
                updateStatusCollapseView(isRecipeIngredientsToBuyRecyclerViewExpanded, ivRecipeScreenIngredientsToBuyTitleCollapse);
            }
        });

        // Set Expand/Collapse OnClick action for Already Bought Ingredient List RecyclerView
        ivRecipeScreenIngredientsAlreadyBoughtTitleCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded = !isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded;
                setRecyclerViewVisibility(isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded, rvRecipeScreenIngredientsAlreadyBought);
                updateStatusCollapseView(isRecipeIngredientsAlreadyBoughtRecyclerViewExpanded, ivRecipeScreenIngredientsAlreadyBoughtTitleCollapse);
            }
        });

    }

    private void enableAlreadyBoughtRemoveAllButton() {
        ivRecipeScreenIngredientsAlreadyBoughtHeaderRemoveAll.setClickable(true);
        ivRecipeScreenIngredientsAlreadyBoughtHeaderRemoveAll.setImageResource(R.mipmap.ic_remove_all);
        ivRecipeScreenIngredientsAlreadyBoughtHeaderRemoveAll.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        ivRecipeScreenIngredientsAlreadyBoughtHeaderRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAllIngredientsBought();
            }
        });
    }

    private void disableAlreadyBoughtRemoveAllButton() {
        ivRecipeScreenIngredientsAlreadyBoughtHeaderRemoveAll.setImageResource(R.mipmap.ic_check);
        ivRecipeScreenIngredientsAlreadyBoughtHeaderRemoveAll.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        // ivShoppingListToBuyHeaderCheckAll.setElevation((float)0.0);
        ivRecipeScreenIngredientsAlreadyBoughtHeaderRemoveAll.setClickable(false);
    }

    private void enableToBuyAllBoughtButton() {
        ivRecipeScreenIngredientsToBuyHeaderCheckAll.setClickable(true);
        ivRecipeScreenIngredientsToBuyHeaderCheckAll.setColorFilter(ContextCompat.getColor(mContext, R.color.cardBackground), PorterDuff.Mode.MULTIPLY);
        ivRecipeScreenIngredientsToBuyHeaderCheckAll.setImageResource(R.drawable.card_shape_rectangle);
        ivRecipeScreenIngredientsToBuyHeaderCheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAllIngredientsAsBought();
            }
        });
    }

    private void disableToBuyAllBoughtButton() {
        ivRecipeScreenIngredientsToBuyHeaderCheckAll.setImageResource(R.mipmap.ic_check);
        ivRecipeScreenIngredientsToBuyHeaderCheckAll.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        // ivShoppingListToBuyHeaderCheckAll.setElevation((float)0.0);
        ivRecipeScreenIngredientsToBuyHeaderCheckAll.setClickable(false);
    }

}

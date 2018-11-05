package com.codeaamirkalimi.mealplanner.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.ShoppingIngredient;
import com.codeaamirkalimi.mealplanner.ui.adapters.ShoppingIngredientAlreadyBoughtAdapter;
import com.codeaamirkalimi.mealplanner.ui.adapters.ShoppingIngredientToBuyAdapter;
import com.codeaamirkalimi.mealplanner.ui.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment implements
        ShoppingIngredientAlreadyBoughtAdapter.ShoppingIngredientAlreadyBoughtClickListener,
        ShoppingIngredientToBuyAdapter.ShoppingIngredientToBuyClickListener {

    /** Key for storing the list state in savedInstanceState */
    private static final String SHOPPING_INGREDIENTS_TO_BUY_LIST_STATE_KEY =
            "shopping-ingredients-to-buy-list-state";

    /** Key for storing the list state in savedInstanceState */
    private static final String SHOPPING_INGREDIENTS_ALREADY_BOUGHT_LIST_STATE_KEY =
            "shopping-ingredients-already-bought-list-state";

    /** Key for storing the ingredients to buy in savedInstanceState */
    private static final String SHOPPING_INGREDIENTS_TO_BUY_KEY = "shopping-ingredients-to-buy";

    /** Key for storing the ingredients already bought in savedInstanceState */
    private static final String SHOPPING_INGREDIENTS_ALREADY_BOUGHT_KEY =
            "shopping-ingredients-already-bought";

    /** Key for storing the expand/collapse status of the to buy ingredient list in savedInstanceState */
    private static final String SHOPPING_INGREDIENTS_TO_BUY_EXPANDED_KEY =
            "shopping-ingredients-to-buy-expanded";

    /** Key for storing the expand/collapse status of the to buy ingredient list in savedInstanceState */
    private static final String SHOPPING_INGREDIENTS_ALREADY_BOUGHT_EXPANDED_KEY =
            "shopping-ingredients-already-bought-expanded";

    @BindView(R.id.iv_shopping_list_to_buy_title_collapse)
    ImageView ivShoppingListToBuyTitleCollapse;
    @BindView(R.id.iv_shopping_list_to_buy_header_check_all)
    ImageView ivShoppingListToBuyHeaderCheckAll;
    @BindView(R.id.rv_shopping_list_to_buy_body_table)
    RecyclerView rvShoppingListToBuyBodyTable;
    @BindView(R.id.iv_shopping_list_already_bought_title_collapse)
    ImageView ivShoppingListAlreadyBoughtTitleCollapse;
    @BindView(R.id.iv_shopping_list_already_bought_header_remove_all)
    ImageView ivShoppingListAlreadyBoughtHeaderRemoveAll;
    @BindView(R.id.rv_shopping_list_already_bought_body_table)
    RecyclerView rvShoppingListAlreadyBoughtBodyTable;

    private Context mContext;
    Unbinder unbinder;
    ActionBar actionBar;

    /** List state stored in savedInstanceState */
    private Parcelable mListStateIngredientsToBuy;
    private Parcelable mListStateIngredientsAlreadyBought;

    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager mShoppingIngredientsToBuyLayoutManager;
    /** Shopping Ingredients Custom ArrayAdapter */
    private ShoppingIngredientToBuyAdapter mShoppingIngredientsToBuyAdapter;
    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager mShoppingIngredientsAlreadyBoughtLayoutManager;
    /** Shopping Ingredients Custom ArrayAdapter */
    private ShoppingIngredientAlreadyBoughtAdapter mShoppingIngredientsAlreadyBoughtAdapter;

    /** Flag for storing if ShoppingIngredientsToBuyRecyclerView is expanded */
    private boolean isShoppingIngredientsToBuyRecyclerViewExpanded;
    /** Flag for storing if ShoppingIngredientsAlreadyBoughtRecyclerView is expanded */
    private boolean isShoppingIngredientsAlreadyBoughtRecyclerViewExpanded;

    public ShoppingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Set ActionBar Title
        setActionBarTitle(getString(R.string.shopping_list_screen_title));

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();

        // Load & set GridLayout
        rvShoppingListToBuyBodyTable.setHasFixedSize(true);
        rvShoppingListAlreadyBoughtBodyTable.setHasFixedSize(true);
        setRecyclerViewLayoutManager(view);

        if (savedInstanceState != null) {

            List<ShoppingIngredient> ingredientsToBuy = savedInstanceState.
                    getParcelableArrayList(SHOPPING_INGREDIENTS_TO_BUY_KEY);
            List<ShoppingIngredient> ingredientsAlreadyBought = savedInstanceState.
                    getParcelableArrayList(SHOPPING_INGREDIENTS_ALREADY_BOUGHT_KEY);

            isShoppingIngredientsToBuyRecyclerViewExpanded = savedInstanceState.
                    getBoolean(SHOPPING_INGREDIENTS_TO_BUY_EXPANDED_KEY);
            isShoppingIngredientsAlreadyBoughtRecyclerViewExpanded = savedInstanceState.
                    getBoolean(SHOPPING_INGREDIENTS_ALREADY_BOUGHT_EXPANDED_KEY);
            setRecyclerViewVisibility(
                    isShoppingIngredientsToBuyRecyclerViewExpanded, rvShoppingListToBuyBodyTable);
            setRecyclerViewVisibility(
                    isShoppingIngredientsAlreadyBoughtRecyclerViewExpanded,
                    rvShoppingListAlreadyBoughtBodyTable);

            mShoppingIngredientsToBuyAdapter =
                    new ShoppingIngredientToBuyAdapter(ingredientsToBuy, this);
            mShoppingIngredientsAlreadyBoughtAdapter =
                    new ShoppingIngredientAlreadyBoughtAdapter(ingredientsAlreadyBought, this);

            rvShoppingListToBuyBodyTable.setAdapter(mShoppingIngredientsToBuyAdapter);
            mShoppingIngredientsToBuyAdapter.notifyDataSetChanged();
            rvShoppingListAlreadyBoughtBodyTable
                    .setAdapter(mShoppingIngredientsAlreadyBoughtAdapter);
            mShoppingIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();

            //
            setOnClickListeners();

        } else {

            // TODO Only used for testing - Dynamic injection
            ArrayList<ShoppingIngredient> ingredientsToBuy = new ArrayList<>();
            ingredientsToBuy.add(new ShoppingIngredient("Spaguetti Noodles", "250", "g", false));
            ingredientsToBuy.add(new ShoppingIngredient("Bacon", "100", "g", false));
            ingredientsToBuy.add(new ShoppingIngredient("Milk", "200", "ml", false));
            mShoppingIngredientsToBuyAdapter =
                    new ShoppingIngredientToBuyAdapter(ingredientsToBuy, this);

            // TODO Only used for testing - Dynamic injection
            ArrayList<ShoppingIngredient> ingredientsAlreadyBought = new ArrayList<>();
            ingredientsAlreadyBought.add(new ShoppingIngredient("Salt", "", "tspc", false));
            ingredientsAlreadyBought.add(new ShoppingIngredient("Pepper", "", "tspc", false));
            mShoppingIngredientsAlreadyBoughtAdapter =
                    new ShoppingIngredientAlreadyBoughtAdapter(ingredientsAlreadyBought, this);

            // RecyclerViews expanded by default
            isShoppingIngredientsToBuyRecyclerViewExpanded = true;
            isShoppingIngredientsAlreadyBoughtRecyclerViewExpanded = true;
            setRecyclerViewVisibility(
                    isShoppingIngredientsToBuyRecyclerViewExpanded, rvShoppingListToBuyBodyTable);
            setRecyclerViewVisibility(
                    isShoppingIngredientsAlreadyBoughtRecyclerViewExpanded,
                    rvShoppingListAlreadyBoughtBodyTable);

            // Set Adapter and notifyDataSetChanged
            rvShoppingListToBuyBodyTable.setAdapter(mShoppingIngredientsToBuyAdapter);
            mShoppingIngredientsToBuyAdapter.notifyDataSetChanged();
            rvShoppingListAlreadyBoughtBodyTable.setAdapter(mShoppingIngredientsAlreadyBoughtAdapter);
            mShoppingIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();

        }

        // Set OnClickListeners
        setOnClickListeners();

        // Update Expand/Collapse UI status
        updateStatusCollapseView(isShoppingIngredientsToBuyRecyclerViewExpanded,
                ivShoppingListToBuyTitleCollapse);
        updateStatusCollapseView(isShoppingIngredientsAlreadyBoughtRecyclerViewExpanded,
                ivShoppingListAlreadyBoughtTitleCollapse);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mListStateIngredientsToBuy =
                mShoppingIngredientsToBuyLayoutManager.onSaveInstanceState();
        mListStateIngredientsAlreadyBought =
                mShoppingIngredientsAlreadyBoughtLayoutManager.onSaveInstanceState();

        outState.putParcelable(SHOPPING_INGREDIENTS_TO_BUY_LIST_STATE_KEY,
                mListStateIngredientsToBuy);
        outState.putParcelable(SHOPPING_INGREDIENTS_ALREADY_BOUGHT_LIST_STATE_KEY,
                mListStateIngredientsAlreadyBought);

        outState.putParcelableArrayList(SHOPPING_INGREDIENTS_TO_BUY_KEY,
                (ArrayList<ShoppingIngredient>) mShoppingIngredientsToBuyAdapter
                        .getmShoppingIngredientList());
        outState.putParcelableArrayList(SHOPPING_INGREDIENTS_ALREADY_BOUGHT_KEY,
                (ArrayList<ShoppingIngredient>) mShoppingIngredientsAlreadyBoughtAdapter
                        .getmShoppingIngredientList());

        outState.putBoolean(SHOPPING_INGREDIENTS_TO_BUY_EXPANDED_KEY,
                isShoppingIngredientsToBuyRecyclerViewExpanded);
        outState.putBoolean(SHOPPING_INGREDIENTS_ALREADY_BOUGHT_EXPANDED_KEY,
                isShoppingIngredientsAlreadyBoughtRecyclerViewExpanded);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            mListStateIngredientsToBuy = savedInstanceState
                    .getParcelable(SHOPPING_INGREDIENTS_TO_BUY_LIST_STATE_KEY);
            mListStateIngredientsAlreadyBought = savedInstanceState
                    .getParcelable(SHOPPING_INGREDIENTS_ALREADY_BOUGHT_LIST_STATE_KEY);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mListStateIngredientsToBuy != null) {
            mShoppingIngredientsToBuyLayoutManager
                    .onRestoreInstanceState(mListStateIngredientsToBuy);
        }
        if (mListStateIngredientsAlreadyBought != null) {
            mShoppingIngredientsAlreadyBoughtLayoutManager
                    .onRestoreInstanceState(mListStateIngredientsAlreadyBought);
        }
    }

    @Override
    public void onShoppingIngredientBackToBuyListClick(int position) {
        if(mShoppingIngredientsToBuyAdapter.getItemCount() == 0) {
            enableToBuyAllBoughtButton();
        }
        mShoppingIngredientsToBuyAdapter.addItem(
                mShoppingIngredientsAlreadyBoughtAdapter.getItem(position));
        mShoppingIngredientsAlreadyBoughtAdapter.removeItem(position);
        mShoppingIngredientsToBuyAdapter.notifyDataSetChanged();
        mShoppingIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();
        if(mShoppingIngredientsAlreadyBoughtAdapter.getItemCount() == 0) {
            disableAlreadyBoughtRemoveAllButton();
        }
    }

    @Override
    public void onShoppingIngredientAlreadyBoughtClick(int position) {
        if(mShoppingIngredientsAlreadyBoughtAdapter.getItemCount() == 0) {
            enableAlreadyBoughtRemoveAllButton();
        }
        mShoppingIngredientsAlreadyBoughtAdapter.addItem(
                mShoppingIngredientsToBuyAdapter.getItem(position));
        mShoppingIngredientsToBuyAdapter.removeItem(position);
        mShoppingIngredientsToBuyAdapter.notifyDataSetChanged();
        mShoppingIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();
        if(mShoppingIngredientsToBuyAdapter.getItemCount() == 0) {
            disableToBuyAllBoughtButton();
        }
    }


    //--------------------------------------------------------------------------------|
    //                                  UI Methods                                    |
    //--------------------------------------------------------------------------------|

    private void setActionBarTitle(String title) {
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            if (!actionBar.isShowing()) {
                actionBar.show();
            }
        }
    }

    /**
     * Set RecyclerView's LayoutManager.
     * Different layouts for portrait and landscape mode
     *
     * @param rootView Activity view
     */
    private void setRecyclerViewLayoutManager(View rootView) {
        mShoppingIngredientsToBuyLayoutManager =
                new GridLayoutManager(rootView.getContext(), 1);
        rvShoppingListToBuyBodyTable.setLayoutManager(mShoppingIngredientsToBuyLayoutManager);
        mShoppingIngredientsAlreadyBoughtLayoutManager =
                new GridLayoutManager(rootView.getContext(), 1);
        rvShoppingListAlreadyBoughtBodyTable
                .setLayoutManager(mShoppingIngredientsAlreadyBoughtLayoutManager);
    }

    /**
     * Expand / Collapse RecyclerView
     * TODO Improve performance of expand transition
     *
     * @param isExpanded    RecyclerView visible state
     * @param view          RecyclerView
     */
    private void setRecyclerViewVisibility(boolean isExpanded, View view) {
        if(isExpanded) {
            ViewUtils.expandView(view);
        } else {
            ViewUtils.collapseView(view);
        }
    }

    /**
     * Update UI status of Expand/Collapse GridView Button
     *
     * @param isExpanded    RecyclerView visible state
     * @param view          Expand/Collapse View button
     */
    private void updateStatusCollapseView(boolean isExpanded, ImageView view) {
        // Handle expansion / collapse of RecyclerViews
        if(isExpanded) {
            view.setImageResource(R.mipmap.ic_collapse);
        } else {
            view.setImageResource(R.mipmap.ic_expand);
        }
    }

    /**
     * Set OnClickListeners for:
     *  - Expand/Collapse to buy list button
     *  - Expand/Collapse already bought list button
     *  - Mark all ingredients as bought button
     *  - Remove all bought ingredients button
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
        if(mShoppingIngredientsToBuyAdapter.getmShoppingIngredientList() != null &&
                mShoppingIngredientsToBuyAdapter.getItemCount() > 0) {
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
        if(mShoppingIngredientsAlreadyBoughtAdapter.getmShoppingIngredientList() != null &&
                mShoppingIngredientsAlreadyBoughtAdapter.getItemCount() > 0) {
            // 2 Set OnClickListener (remove all bought ingredients button)
            enableAlreadyBoughtRemoveAllButton();
        } else {
            // 2.1 No bought ingredients - Remove button
            disableAlreadyBoughtRemoveAllButton();
        }

    }

    private void markAllIngredientsAsBought() {

        // Move all ingredients to already bought list
        List<ShoppingIngredient> ingredientsToBuy =
                mShoppingIngredientsToBuyAdapter.getmShoppingIngredientList();
        if(ingredientsToBuy != null) {
            if(ingredientsToBuy.size() > 0) {
                for (int i = ingredientsToBuy.size() - 1; i >= 0; i--) {
                    mShoppingIngredientsAlreadyBoughtAdapter.addItem(ingredientsToBuy.get(i));
                    mShoppingIngredientsToBuyAdapter.removeItem(i);
                }
                mShoppingIngredientsToBuyAdapter.notifyDataSetChanged();
                mShoppingIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();
                enableAlreadyBoughtRemoveAllButton();
            }
        }

        // Update icon and disable button
        disableToBuyAllBoughtButton();

    }

    private void removeAllIngredientsBought() {

        // Remove all bought ingredients
        List<ShoppingIngredient> ingredientsBought =
                mShoppingIngredientsAlreadyBoughtAdapter.getmShoppingIngredientList();
        if(ingredientsBought != null) {
            for(int i = ingredientsBought.size() - 1; i >= 0; i--) {
                mShoppingIngredientsAlreadyBoughtAdapter.removeItem(i);
            }
            mShoppingIngredientsAlreadyBoughtAdapter.notifyDataSetChanged();
        }

        // Update icon and disable button
        disableAlreadyBoughtRemoveAllButton();

    }

    private void setCollapseViewClickListeners() {

        // Set Expand/Collapse OnClick action for To Buy Ingredient List RecyclerView
        ivShoppingListToBuyTitleCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShoppingIngredientsToBuyRecyclerViewExpanded =
                        !isShoppingIngredientsToBuyRecyclerViewExpanded;
                setRecyclerViewVisibility(isShoppingIngredientsToBuyRecyclerViewExpanded,
                        rvShoppingListToBuyBodyTable);
                updateStatusCollapseView(isShoppingIngredientsToBuyRecyclerViewExpanded,
                        ivShoppingListToBuyTitleCollapse);
            }
        });

        // Set Expand/Collapse OnClick action for Already Bought Ingredient List RecyclerView
        ivShoppingListAlreadyBoughtTitleCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShoppingIngredientsAlreadyBoughtRecyclerViewExpanded =
                        !isShoppingIngredientsAlreadyBoughtRecyclerViewExpanded;
                setRecyclerViewVisibility(isShoppingIngredientsAlreadyBoughtRecyclerViewExpanded,
                        rvShoppingListAlreadyBoughtBodyTable);
                updateStatusCollapseView(isShoppingIngredientsAlreadyBoughtRecyclerViewExpanded,
                        ivShoppingListAlreadyBoughtTitleCollapse);
            }
        });

    }

    private void enableAlreadyBoughtRemoveAllButton() {
        ivShoppingListAlreadyBoughtHeaderRemoveAll.setClickable(true);
        ivShoppingListAlreadyBoughtHeaderRemoveAll.setImageResource(R.mipmap.ic_remove_all);
        ivShoppingListAlreadyBoughtHeaderRemoveAll.setColorFilter(ContextCompat.getColor(mContext,
                R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_ATOP);
        ivShoppingListAlreadyBoughtHeaderRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAllIngredientsBought();
            }
        });
    }

    private void disableAlreadyBoughtRemoveAllButton() {
        ivShoppingListAlreadyBoughtHeaderRemoveAll.setImageResource(R.mipmap.ic_check);
        ivShoppingListAlreadyBoughtHeaderRemoveAll.setColorFilter(ContextCompat.getColor(mContext,
                R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_ATOP);
        // ivShoppingListToBuyHeaderCheckAll.setElevation((float)0.0);
        ivShoppingListAlreadyBoughtHeaderRemoveAll.setClickable(false);
    }

    private void enableToBuyAllBoughtButton() {
        ivShoppingListToBuyHeaderCheckAll.setClickable(true);
        ivShoppingListToBuyHeaderCheckAll.setColorFilter(ContextCompat.getColor(mContext,
                R.color.cardBackground), PorterDuff.Mode.MULTIPLY);
        ivShoppingListToBuyHeaderCheckAll.setImageResource(R.drawable.card_shape_rectangle);
        ivShoppingListToBuyHeaderCheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAllIngredientsAsBought();
            }
        });
    }

    private void disableToBuyAllBoughtButton() {
        ivShoppingListToBuyHeaderCheckAll.setImageResource(R.mipmap.ic_check);
        ivShoppingListToBuyHeaderCheckAll.setColorFilter(ContextCompat.getColor(mContext,
                R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_ATOP);
        // ivShoppingListToBuyHeaderCheckAll.setElevation((float)0.0);
        ivShoppingListToBuyHeaderCheckAll.setClickable(false);
    }

}

package com.codeaamirkalimi.mealplanner.ui;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Button;

import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.Menu;
import com.codeaamirkalimi.mealplanner.datamodel.MenuCategory;
import com.codeaamirkalimi.mealplanner.remotedatabase.MyMealPlannerRTDBContract;
import com.codeaamirkalimi.mealplanner.ui.adapters.MealPlansPreviewAdapter;
import com.codeaamirkalimi.mealplanner.viewmodel.MenuCategoryViewModel;
import com.codeaamirkalimi.mealplanner.viewmodel.MenuViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MealPlansFragment extends Fragment
        implements MealPlansPreviewAdapter.MealPlansPreviewClickListener {

    /**
     * Key for storing the list state in savedInstanceState
     */
    private static final String FAVOURITE_MEAL_PLANS_LIST_STATE_KEY = "favourite-meal-plan-list-state";
    /**
     * Key for storing the list state in savedInstanceState
     */
    private static final String DIET_MEAL_PLANS_LIST_STATE_KEY = "diet-meal-plan-list-state";
    /**
     * Key for storing the list state in savedInstanceState
     */
    private static final String VEGGIE_MEAL_PLANS_LIST_STATE_KEY = "veggie-meal-plan-list-state";

    /**
     * Key for storing the favourite meal plans in savedInstanceState
     */
    private static final String FAVOURITE_MEAL_PLANS_LIST_KEY = "favourite-meal-plan-list";
    /**
     * Key for storing the diet meal plans in savedInstanceState
     */
    private static final String DIET_MEAL_PLANS_LIST_KEY = "diet-meal-plan-list";
    /**
     * Key for storing the veggies meal plans in savedInstanceState
     */
    private static final String VEGGIE_MEAL_PLANS_LIST_KEY = "veggie-meal-plan-list";

    @BindView(R.id.bt_show_more_favourite_meals)
    Button btShowMoreFavouriteMeals;
    @BindView(R.id.bt_show_more_diet_meals)
    Button btShowMoreDietMeals;
    @BindView(R.id.bt_show_more_veggie_meals)
    Button btShowMoreVeggieMeals;

    /**  ViewModel instance */
    private MenuViewModel mMenuViewModel;

    /**  ViewModel instance */
    private MenuCategoryViewModel mMenuCategoryViewModel;

    RecyclerView rvFavouriteMealCards;
    RecyclerView rvDietMealCards;
    RecyclerView rvVeggieMealCards;
    Unbinder unbinder;
    ActionBar actionBar;
    private Activity mContext;

    private HashMap<String, String> mMenuCategories = new HashMap<String, String>();

    private OnMealPlansFragmentInteractionListener mListener;

    /**
     * RecyclerView LayoutManager instance
     */
    private RecyclerView.LayoutManager mFavouriteMealPlanLayoutManager;
    /**
     * Favourite Meal Plans Custom ArrayAdapter
     */
    private MealPlansPreviewAdapter mFavouriteMealPlanListAdapter;
    /**
     * List state stored in savedInstanceState
     */
    private Parcelable mListStateFavouriteMealPlan;
    /**
     * RecyclerView LayoutManager instance
     */
    private RecyclerView.LayoutManager mDietMealPlanLayoutManager;
    /**
     * Diet Meal Plans Custom ArrayAdapter
     */
    private MealPlansPreviewAdapter mDietMealPlanListAdapter;
    /**
     * List state stored in savedInstanceState
     */
    private Parcelable mListStateDietMealPlan;
    /**
     * RecyclerView LayoutManager instance
     */
    private RecyclerView.LayoutManager mVeggieMealPlanLayoutManager;
    /**
     * Veggie Meal Plans Custom ArrayAdapter
     */
    private MealPlansPreviewAdapter mVeggieMealPlanListAdapter;
    /**
     * List state stored in savedInstanceState
     */
    private Parcelable mListStateVeggieMealPlan;

    public MealPlansFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMealPlansFragmentInteractionListener) {
            mListener = (OnMealPlansFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMealPlansFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface OnMealPlansFragmentInteractionListener {
        void onMealPlanPreviewClick(Menu menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Set ActionBar Title
        setActionBarTitle(getString(R.string.meal_plans_screen_title));

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_meal_plans, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mContext = getActivity();

        // Inflate RecyclerViews
        rvFavouriteMealCards = (RecyclerView) rootView.findViewById(R.id.rv_favourite_meal_cards);
        rvDietMealCards = (RecyclerView) rootView.findViewById(R.id.rv_diet_meal_cards);
        rvVeggieMealCards = (RecyclerView) rootView.findViewById(R.id.rv_veggie_meal_cards);

        // Load & set GridLayout
        rvFavouriteMealCards.setHasFixedSize(true);
        rvDietMealCards.setHasFixedSize(true);
        rvVeggieMealCards.setHasFixedSize(true);
        setRecyclerViewLayoutManager(rootView);

        if (savedInstanceState != null) {

            List<Menu> favouriteMenus = savedInstanceState.
                    getParcelableArrayList(FAVOURITE_MEAL_PLANS_LIST_KEY);
            mFavouriteMealPlanListAdapter = new MealPlansPreviewAdapter(favouriteMenus, this);
            rvFavouriteMealCards.setAdapter(mFavouriteMealPlanListAdapter);
            mFavouriteMealPlanListAdapter.notifyDataSetChanged();

            List<Menu> dietMealPlans = savedInstanceState.
                    getParcelableArrayList(DIET_MEAL_PLANS_LIST_KEY);
            mDietMealPlanListAdapter = new MealPlansPreviewAdapter(dietMealPlans, this);
            rvDietMealCards.setAdapter(mDietMealPlanListAdapter);
            mDietMealPlanListAdapter.notifyDataSetChanged();

            List<Menu> veggieMealPlans = savedInstanceState.
                    getParcelableArrayList(VEGGIE_MEAL_PLANS_LIST_KEY);
            mVeggieMealPlanListAdapter = new MealPlansPreviewAdapter(veggieMealPlans, this);
            rvVeggieMealCards.setAdapter(mVeggieMealPlanListAdapter);
            mVeggieMealPlanListAdapter.notifyDataSetChanged();

        } else {

            // Subscribe MealPlansFragment to receive notifications from MenuViewModel
            registerToMenuViewModel();

            // Subscribe MealPlansFragment to receive notifications from MenuCategoryViewModel
            registerToMenuCategoryViewModel();

            // Get menu categories
            getMenuCategories();

            // Get menus (delayed action because categories are previously needed)
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 1 second
                    getMenus();
                }
            }, 1000);

        }

        return rootView;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mListStateFavouriteMealPlan = mFavouriteMealPlanLayoutManager.onSaveInstanceState();
        outState.putParcelable(FAVOURITE_MEAL_PLANS_LIST_STATE_KEY, mListStateFavouriteMealPlan);
        outState.putParcelableArrayList(FAVOURITE_MEAL_PLANS_LIST_KEY,
                (ArrayList<Menu>) mFavouriteMealPlanListAdapter.getmMenuList());

        mListStateDietMealPlan = mDietMealPlanLayoutManager.onSaveInstanceState();
        outState.putParcelable(DIET_MEAL_PLANS_LIST_STATE_KEY, mListStateDietMealPlan);
        outState.putParcelableArrayList(DIET_MEAL_PLANS_LIST_KEY,
                (ArrayList<Menu>) mDietMealPlanListAdapter.getmMenuList());

        mListStateVeggieMealPlan = mVeggieMealPlanLayoutManager.onSaveInstanceState();
        outState.putParcelable(VEGGIE_MEAL_PLANS_LIST_STATE_KEY, mListStateVeggieMealPlan);
        outState.putParcelableArrayList(VEGGIE_MEAL_PLANS_LIST_KEY,
                (ArrayList<Menu>) mVeggieMealPlanListAdapter.getmMenuList());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mListStateFavouriteMealPlan = savedInstanceState
                    .getParcelable(FAVOURITE_MEAL_PLANS_LIST_STATE_KEY);
            mListStateDietMealPlan = savedInstanceState
                    .getParcelable(DIET_MEAL_PLANS_LIST_STATE_KEY);
            mListStateVeggieMealPlan = savedInstanceState
                    .getParcelable(VEGGIE_MEAL_PLANS_LIST_STATE_KEY);
        }
    }


    //--------------------------------------------------------------------------------|
    //                        Fragment --> Activity Comm Methods                      |
    //--------------------------------------------------------------------------------|

    @Override
    public void onMealPlansPreviewClickListenerClick(Menu menu) {
        if (mListener != null) {
            mListener.onMealPlanPreviewClick(menu);
        }
    }

    //--------------------------------------------------------------------------------|
    //                                   UI Methods                                   |
    //--------------------------------------------------------------------------------|

    /**
     * Set screen name as ActionBar Title
     *
     * @param   title   Screen name
     */
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
     * Set RecyclerViews" layout managers
     *
     * @param   rootView    parent view
     */
    private void setRecyclerViewLayoutManager(View rootView) {
        mFavouriteMealPlanLayoutManager = new GridLayoutManager(rootView.getContext(),
                1, GridLayoutManager.HORIZONTAL, false);
        rvFavouriteMealCards.setLayoutManager(mFavouriteMealPlanLayoutManager);
        mDietMealPlanLayoutManager = new GridLayoutManager(rootView.getContext(),
                1, GridLayoutManager.HORIZONTAL, false);
        rvDietMealCards.setLayoutManager(mDietMealPlanLayoutManager);
        mVeggieMealPlanLayoutManager = new GridLayoutManager(rootView.getContext(),
                1, GridLayoutManager.HORIZONTAL, false);
        rvVeggieMealCards.setLayoutManager(mVeggieMealPlanLayoutManager);
    }

    /**
     * Populate ArrayAdapters's list of menus classified by menu category
     *
     * @param   menus       Retrieved list of menus from Realtime Database
     */
    private void populateMenusAdapters(List<Menu> menus) {

        // TODO Load available menu categories from xml?
        List<Menu> favouriteMenus = new ArrayList<>();
        List<Menu> losingWeightMenus = new ArrayList<>();
        List<Menu> veggiesMenus = new ArrayList<>();

        for(Menu menu : menus) {
            if(mMenuCategories.containsKey(menu.getCategoryID())) {
                switch(mMenuCategories.get(menu.getCategoryID())) {
                    case(MyMealPlannerRTDBContract.RT_MENU_CATEGORY_FAVOURITES_COLUMN_VALUE):
                        favouriteMenus.add(menu);
                        break;
                    case(MyMealPlannerRTDBContract.RT_MENU_CATEGORY_LOSING_WEIGHT_COLUMN_VALUE):
                        losingWeightMenus.add(menu);
                        break;
                    case(MyMealPlannerRTDBContract.RT_MENU_CATEGORY_VEGGIES_COLUMN_VALUE):
                        veggiesMenus.add(menu);
                        break;
                    default:
                        break;
                }
            }
        }

        mFavouriteMealPlanListAdapter = new MealPlansPreviewAdapter(favouriteMenus, this);
        rvFavouriteMealCards.setAdapter(mFavouriteMealPlanListAdapter);
        mFavouriteMealPlanListAdapter.notifyDataSetChanged();

        mDietMealPlanListAdapter = new MealPlansPreviewAdapter(losingWeightMenus, this);
        rvDietMealCards.setAdapter(mDietMealPlanListAdapter);
        mDietMealPlanListAdapter.notifyDataSetChanged();

        mVeggieMealPlanListAdapter = new MealPlansPreviewAdapter(veggiesMenus, this);
        rvVeggieMealCards.setAdapter(mVeggieMealPlanListAdapter);
        mVeggieMealPlanListAdapter.notifyDataSetChanged();

    }

    //--------------------------------------------------------------------------------|
    //                               DataModel Methods                                |
    //--------------------------------------------------------------------------------|

    /**
     * Registers the View into Recipe ViewModel to receive updated recipes (Observer pattern)
     */
    private void registerToMenuViewModel() {
        // Create RecipeViewModel Factory for param injection
        MenuViewModel.Factory menuFactory = new MenuViewModel.Factory(
                getActivity().getApplication());
        // Get instance of RecipeViewModel
        mMenuViewModel = ViewModelProviders.of(this, menuFactory)
                .get(MenuViewModel.class);
    }

    /**
     * Registers the View into Recipe ViewModel to receive updated recipes (Observer pattern)
     */
    private void registerToMenuCategoryViewModel() {
        // Create RecipeViewModel Factory for param injection
        MenuCategoryViewModel.Factory menuCategoryFactory = new MenuCategoryViewModel.Factory(
                getActivity().getApplication());
        // Get instance of RecipeViewModel
        mMenuCategoryViewModel = ViewModelProviders.of(this, menuCategoryFactory)
                .get(MenuCategoryViewModel.class);
    }

    /**
     * Retrieve list of Menus from MenuViewModel (Observer pattern)
     */
    private void getMenus(){
        mMenuViewModel.getMenus().observe(this, new Observer<List<Menu>>() {
            @Override
            public void onChanged(@Nullable List<Menu> menus) {
                if(menus == null || menus.isEmpty()) { return; }
                else {
                    populateMenusAdapters(menus);
                }
            }
        });
    }

    /**
     * Retrieve list of MenuCategories from MenuCategoryViewModel (Observer pattern)
     */
    private void getMenuCategories() {
        mMenuCategoryViewModel.getMenuCategories().observe(this, new Observer<List<MenuCategory>>() {
            @Override
            public void onChanged(@Nullable List<MenuCategory> menuCategories) {
                if(menuCategories == null || menuCategories.isEmpty()) { return; }
                else {
                    populateMenuCategories(menuCategories);
                }
            }
        });
    }


    //--------------------------------------------------------------------------------|
    //                               Private Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Populate menu categories HashMap
     *
     * @param   menuCategories      Retrieved list of menu categories from Realtime Database
     */
    private void populateMenuCategories(List<MenuCategory> menuCategories) {
        for(MenuCategory menuCategory : menuCategories) {
            mMenuCategories.put(menuCategory.getId(), menuCategory.getCategory());
        }
    }

}


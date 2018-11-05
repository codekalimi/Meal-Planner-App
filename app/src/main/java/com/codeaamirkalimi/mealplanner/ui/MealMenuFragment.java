package com.codeaamirkalimi.mealplanner.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.Menu;
import com.codeaamirkalimi.mealplanner.ui.adapters.MealMenuFragmentPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealMenuFragment extends Fragment {

    private static final String MEAL_MENU_SAVE_INSTANCE_KEY = "meal-menu";

    private Menu mMenu;
    ActionBar actionBar;
    private Context mContext;

    public MealMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get Activity Context
        mContext = getActivity();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_meal_menu, container, false);

        // Handle screen rotation and/or screen creation
        if (savedInstanceState == null) {
            mMenu = getArguments().getParcelable(MEAL_MENU_SAVE_INSTANCE_KEY);
            setActionBarTitle(mMenu.getTitle());
        } else {
            mMenu = savedInstanceState.getParcelable(MEAL_MENU_SAVE_INSTANCE_KEY);
        }

        // Return rootView
        return rootView;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MEAL_MENU_SAVE_INSTANCE_KEY, mMenu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ViewPager mViewPager = (ViewPager) view.findViewById(R.id.vp_meal_menu_weekly_pager);
        mViewPager.setAdapter(new MealMenuFragmentPagerAdapter(getChildFragmentManager(), getActivity(), mMenu));

        // Attach the page change listener inside the activity
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                // Code goes here
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
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

}

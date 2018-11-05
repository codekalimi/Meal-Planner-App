package com.codeaamirkalimi.mealplanner.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codeaamirkalimi.mealplanner.datamodel.Menu;
import com.codeaamirkalimi.mealplanner.ui.MealMenuDayFragment;

public class MealMenuFragmentPagerAdapter extends FragmentPagerAdapter {
    private Menu mMenu;
    private int mNumDays;
    private Context mContext;

    public MealMenuFragmentPagerAdapter(FragmentManager fm, Context context, Menu menu) {
        super(fm);
        mContext = context;
        mMenu = menu;
        mNumDays = menu.getMealDays().size();
    }

    @Override
    public int getCount() {
        return mNumDays;
    }

    @Override
    public Fragment getItem(int position) {
        MealMenuDayFragment mealMenuDayFragment = new MealMenuDayFragment();
        Bundle args = new Bundle();
        args.putInt(MealMenuDayFragment.POSITION_KEY, position + 1);
        args.putParcelable(MealMenuDayFragment.MEAL_DAY_KEY, mMenu.getMealDays().get(position));
        mealMenuDayFragment.setArguments(args);
        return mealMenuDayFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Day " + (position + 1);
    }
}

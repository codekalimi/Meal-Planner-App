package com.codeaamirkalimi.mealplanner.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.application.MyMealPlanner;
import com.codeaamirkalimi.mealplanner.datamodel.GoogleAccountData;
import com.codeaamirkalimi.mealplanner.datamodel.Menu;
import com.codeaamirkalimi.mealplanner.global.MyMealPlannerGlobals;
import com.codeaamirkalimi.mealplanner.ui.utils.BottomNavigationViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MealPlansFragment.OnMealPlansFragmentInteractionListener,
        MyAccountFragment.OnMyAccountFragmentInteractionListener {

    private static final String MEAL_MENU_SAVE_INSTANCE_KEY = "meal-menu";

    private static final String GOOGLE_ACCOUNT_DATA_KEY = "google-account-data";

    private static final String FRAGMENT_NAME_SAVE_INSTANCE_KEY = "fragment-name";

    @BindView(R.id.fl_meal_screen_fragment_container)
    FrameLayout flMealScreenFragmentContainer;
    @BindView(R.id.bnv_app_navigation)
    BottomNavigationView bnvAppNavigation;

    private String sFragmentName;
    private GoogleAccountData mGoogleAccountData;
    private SharedPreferences mSharedPreferences;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_meal_plans:
                    sFragmentName = getString(R.string.meal_plans_screen_title);
                    getSupportFragmentManager().
                            beginTransaction()
                            .replace(R.id.fl_meal_screen_fragment_container, new MealPlansFragment(),
                                    sFragmentName)
                            .commit();
                    return true;
                case R.id.nav_meal_planner:
                    sFragmentName = getString(R.string.meal_planner_screen_title);
                    getSupportFragmentManager().
                            beginTransaction()
                            .replace(R.id.fl_meal_screen_fragment_container, new MealPlannerFragment(),
                                    sFragmentName)
                            .commit();
                    return true;
                case R.id.nav_shopping_list:
                    sFragmentName = getString(R.string.shopping_list_screen_title);
                    getSupportFragmentManager().
                            beginTransaction()
                            .replace(R.id.fl_meal_screen_fragment_container, new ShoppingListFragment(),
                                    sFragmentName)
                            .commit();
                    return true;
                case R.id.nav_recipe_list:
                    sFragmentName = getString(R.string.recipe_list_screen_title);
                    getSupportFragmentManager().
                            beginTransaction()
                            .replace(R.id.fl_meal_screen_fragment_container, new RecipeListFragment(),
                                    sFragmentName)
                            .commit();
                    return true;
                case R.id.nav_user_profile:
                    sFragmentName = getString(R.string.my_account_screen_title);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(GOOGLE_ACCOUNT_DATA_KEY, mGoogleAccountData);
                    MyAccountFragment myAccountFragment = new MyAccountFragment();
                    myAccountFragment.setArguments(bundle);
                    getSupportFragmentManager().
                            beginTransaction()
                            .replace(R.id.fl_meal_screen_fragment_container, myAccountFragment,
                                    sFragmentName)
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Build Bottom App Navigation Bar
        bnvAppNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(bnvAppNavigation);

        if (savedInstanceState == null) {

            // Get Google Account Data
            Intent intent = getIntent();
            if(intent.hasExtra(GOOGLE_ACCOUNT_DATA_KEY)) {
                mGoogleAccountData = intent.getParcelableExtra(GOOGLE_ACCOUNT_DATA_KEY);
            }

            // Load MealPlansFragment by default
            sFragmentName = getString(R.string.meal_plans_screen_title);
            getSupportFragmentManager().
                    beginTransaction()
                    .replace(R.id.fl_meal_screen_fragment_container, new MealPlansFragment(),
                            sFragmentName)
                    .commit();

        } else {

            sFragmentName = savedInstanceState.getString(FRAGMENT_NAME_SAVE_INSTANCE_KEY);
            getSupportFragmentManager().findFragmentByTag(sFragmentName);

        }

        // Get SharedPreferences
        mSharedPreferences = getSharedPreferences(
                getString(R.string.app_shared_preferences), Context.MODE_PRIVATE);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FRAGMENT_NAME_SAVE_INSTANCE_KEY, sFragmentName);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMealPlanPreviewClick(Menu menu) {
        Bundle bundle = new Bundle();
        MealMenuFragment fragment = new MealMenuFragment();
        bundle.putParcelable(MEAL_MENU_SAVE_INSTANCE_KEY, menu);
        fragment.setArguments(bundle);
        getSupportFragmentManager().
                beginTransaction()
                .add(R.id.fl_meal_screen_fragment_container, fragment, menu.getTitle())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSignOut() {

        String loginType = mSharedPreferences.getString(getString(R.string.login_type),
                MyMealPlannerGlobals.NOT_LOGGED);
        switch(loginType) {
            case MyMealPlannerGlobals.MAIL_LOGGED:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null) {
                        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
                break;
            case MyMealPlannerGlobals.GOOGLE_LOGGED:
                GoogleSignInClient client = ((MyMealPlanner) this.getApplication()).getmGoogleSignInClient();
                if(client != null) {
                    client.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });
                }
                break;
            default:
            case MyMealPlannerGlobals.NOT_LOGGED:
                break;

        }
    }

}

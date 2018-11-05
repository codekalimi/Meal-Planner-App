package com.codeaamirkalimi.mealplanner.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.Recipe;
import com.codeaamirkalimi.mealplanner.ui.utils.UrlUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeActivity extends AppCompatActivity {

    private static final String FRAGMENT_NAME_SAVE_INSTANCE_KEY = "fragment-name";

    private static final String RECIPE_KEY = "recipe";

    @BindView(R.id.iv_recipe_photo)
    ImageView ivRecipePhoto;
    @BindView(R.id.tv_recipe_name)
    TextView tvRecipeName;
    @BindView(R.id.tv_recipe_author)
    TextView tvRecipeAuthor;
    @BindView(R.id.tv_recipe_data_timer_time)
    TextView tvRecipeDataTimerTime;
    @BindView(R.id.iv_recipe_icon_bookmark)
    ImageView ivRecipeIconBookmark;
    @BindView(R.id.iv_recipe_icon_plan)
    ImageView ivRecipeIconPlan;
    @BindView(R.id.app_bar)
    Toolbar appBar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.abl_appbar_layout)
    AppBarLayout ablAppbarLayout;

    private String sFragmentName;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {

            if(getIntent().hasExtra(RECIPE_KEY)) {
                mRecipe = getIntent().getParcelableExtra(RECIPE_KEY);
            }

            // Load MealPlansFragment by default
            Fragment recipeFragment = new RecipeDataFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(RECIPE_KEY, mRecipe);
            recipeFragment.setArguments(bundle);
            sFragmentName = getString(R.string.recipe_screen_title);
            getSupportFragmentManager().
                    beginTransaction().replace(
                            R.id.fl_recipe_screen_fragment_container,
                            recipeFragment,
                            sFragmentName).commit();
        } else {
            sFragmentName = savedInstanceState.getString(FRAGMENT_NAME_SAVE_INSTANCE_KEY);
            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
            getSupportFragmentManager().findFragmentByTag(sFragmentName);
        }

        updateUI();

        setActionBarChangedListener();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FRAGMENT_NAME_SAVE_INSTANCE_KEY, sFragmentName);
        outState.putParcelable(RECIPE_KEY, mRecipe);
        super.onSaveInstanceState(outState);
    }

    @OnClick({R.id.iv_recipe_icon_bookmark, R.id.iv_recipe_icon_plan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_recipe_icon_bookmark:
                break;
            case R.id.iv_recipe_icon_plan:
                break;
        }
    }

    private void setActionBarChangedListener() {
        ablAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(mRecipe.getTitle());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void updateUI() {
        tvRecipeName.setText(mRecipe.getTitle());
        tvRecipeAuthor.setText(mRecipe.getAuthor());
        // TODO Format String time
        tvRecipeDataTimerTime.setText(mRecipe.getCookingTime());
        String imageUrl = mRecipe.getImageUrl();
        if(imageUrl != null && !imageUrl.isEmpty() && UrlUtils.isValid(imageUrl)) {
            Picasso.get()
                    .load(imageUrl)
                    .error(ContextCompat.getDrawable(this, R.drawable.im_recipe))
                    .fit()
                    .into(ivRecipePhoto);
        }
    }

}

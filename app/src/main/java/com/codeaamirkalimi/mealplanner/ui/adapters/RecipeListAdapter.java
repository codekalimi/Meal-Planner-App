package com.codeaamirkalimi.mealplanner.ui.adapters;


import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.Recipe;
import com.codeaamirkalimi.mealplanner.global.MyMealPlannerGlobals;
import com.codeaamirkalimi.mealplanner.ui.utils.UrlUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeListAdapter
        extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    /** Aspect Ratio of Recipe Image (PosterWidth / PosterHeight) */
    private static final double RECIPE_IMAGE_ASPECT_RATIO = 1;

    List<Recipe> mRecipeList;
    private Context mContext;
    private RecipeListClickListener mRecipeListClickListener;

    /**ScreenWidth (in px) - Runtime resize of GridView elements */
    private final int mScreenWidth;


    //--------------------------------------------------------------------------------|
    //                                 Constructors                                   |
    //--------------------------------------------------------------------------------|

    public RecipeListAdapter(List<Recipe> recipes, RecipeListClickListener listener) {
        this.mRecipeList = recipes;
        this.mRecipeListClickListener = listener;
        mScreenWidth = getScreenWidth();
    }


    //--------------------------------------------------------------------------------|
    //                              Override Methods                                  |
    //--------------------------------------------------------------------------------|

    @NonNull
    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.card_recipe, parent, false);
        return new RecipeListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.ViewHolder holder, int position) {

        // Retrieve i-ingredient from shopping ingredient list
        Recipe recipe = mRecipeList.get(position);

        // Set position-tag
        holder.recipeViewLayout.setTag(position);

        // Populate UI elements
        populateUIView(holder, recipe);

        // Set OnViewClickListener
        setOnViewClickListener(holder);

    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }


    //--------------------------------------------------------------------------------|
    //                             Getters / Setters                                  |
    //--------------------------------------------------------------------------------|

    public List<Recipe> getmRecipeList() {
        return mRecipeList;
    }

    //--------------------------------------------------------------------------------|
    //                              Support Classes                                   |
    //--------------------------------------------------------------------------------|

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_card_recipe_name)
        TextView recipeName;
        @BindView(R.id.iv_card_recipe_photo)
        ImageView recipePhoto;
        private final View recipeViewLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeViewLayout = itemView;
            ButterKnife.bind(this, itemView);

            // Resize film poster size
            resizeRecipeCard();
        }

        private void resizeRecipeCard() {
            int width;
            int height;
            switch (mContext.getResources().getConfiguration().orientation) {
                case MyMealPlannerGlobals.LANDSCAPE_VIEW: // Landscape Mode
                    width = ((mScreenWidth - 16) / MyMealPlannerGlobals.RECIPE_GV_LAND_COLUMN_NUMB);
                    height = (int) (width / RECIPE_IMAGE_ASPECT_RATIO);
                    recipeViewLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    break;
                case MyMealPlannerGlobals.PORTRAIT_VIEW: // Portrait Mode
                default:
                    width = ((mScreenWidth - 16) / MyMealPlannerGlobals.RECIPE_GV_PORT_COLUMN_NUMB);
                    height = (int) (width / RECIPE_IMAGE_ASPECT_RATIO);
                    recipeViewLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    break;
            }
        }

    }


    //--------------------------------------------------------------------------------|
    //                                 UI Methods                                     |
    //--------------------------------------------------------------------------------|

    /**
     * Populate UI view elements
     *
     * @param holder     ViewHolder (View container)
     * @param recipe     Shopping Ingredient object
     */
    private void populateUIView(RecipeListAdapter.ViewHolder holder, Recipe recipe) {
        holder.recipeName.setText(recipe.getTitle());
        String imageUrl = recipe.getImageUrl();
        if(imageUrl != null && !imageUrl.isEmpty() && UrlUtils.isValid(imageUrl)) {
            Picasso.get()
                    .load(imageUrl)
                    .error(ContextCompat.getDrawable(mContext, R.drawable.im_recipe))
                    .fit()
                    .into(holder.recipePhoto);
        }
    }


    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    //--------------------------------------------------------------------------------|
    //                          Fragment--> Activity Comm                             |
    //--------------------------------------------------------------------------------|

    public interface RecipeListClickListener {
        public void onRecipeClickListenerClick(Recipe recipe);
    }


    //--------------------------------------------------------------------------------|
    //                              Support Methods                                   |
    //--------------------------------------------------------------------------------|

    /**
     * Set a film click-listener on the film-view
     *
     * @param    holder    ViewHolder (View container)
     */
    private void setOnViewClickListener(final ViewHolder holder) {
        holder.recipeViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecipeListClickListener != null) {
                    mRecipeListClickListener
                            .onRecipeClickListenerClick(mRecipeList.get(
                                    (int)holder.recipeViewLayout.getTag()));
                }
            }
        });
    }

}


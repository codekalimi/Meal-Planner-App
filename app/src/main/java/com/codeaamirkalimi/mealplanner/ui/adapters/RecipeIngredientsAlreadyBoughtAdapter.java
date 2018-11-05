package com.codeaamirkalimi.mealplanner.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.RecipeIngredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientsAlreadyBoughtAdapter
        extends RecyclerView.Adapter<RecipeIngredientsAlreadyBoughtAdapter.ViewHolder> {

    private List<RecipeIngredient> mRecipeIngredientList;
    private RecipeIngredientsAlreadyBoughtClickListener mRecipeIngredientsToBuyClickListener;
    private Context mContext;

    //--------------------------------------------------------------------------------|
    //                                 Constructors                                   |
    //--------------------------------------------------------------------------------|

    public RecipeIngredientsAlreadyBoughtAdapter(List<RecipeIngredient> recipeIngredients,
                                                 RecipeIngredientsAlreadyBoughtClickListener listener) {
        this.mRecipeIngredientList = recipeIngredients;
        this.mRecipeIngredientsToBuyClickListener = listener;
    }


    //--------------------------------------------------------------------------------|
    //                              Override Methods                                  |
    //--------------------------------------------------------------------------------|

    @NonNull
    @Override
    public RecipeIngredientsAlreadyBoughtAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_recipe_ingredient_already_bought, parent,
                false);
        return new RecipeIngredientsAlreadyBoughtAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientsAlreadyBoughtAdapter.ViewHolder holder,
                                 int position) {

        // Retrieve i-ingredient from shopping ingredient list
        RecipeIngredient ingredient = mRecipeIngredientList.get(position);

        // Set position-tag
        holder.recipeIngredientViewLayout.setTag(position);

        // Populate UI elements
        populateUIView(holder, ingredient);

        // Set OnClickListener
        setOnViewClickListener(holder);

    }

    @Override
    public int getItemCount() {
        if(mRecipeIngredientList != null) {
            return mRecipeIngredientList.size();
        } else { return 0; }
    }


    //--------------------------------------------------------------------------------|
    //                               Public methods                                   |
    //--------------------------------------------------------------------------------|

    public void removeItem(int i) {
        mRecipeIngredientList.remove(i);
    }

    public void addItem(RecipeIngredient ingredient) {
        mRecipeIngredientList.add(ingredient);
    }

    public RecipeIngredient getItem(int i) { return mRecipeIngredientList.get(i); }


    //--------------------------------------------------------------------------------|
    //                             Getters / Setters                                  |
    //--------------------------------------------------------------------------------|

    public List<RecipeIngredient> getmRecipeIngredientList() {
        return mRecipeIngredientList;
    }

    public void setmRecipeIngredientList(List<RecipeIngredient> mRecipeIngredientList) {
        this.mRecipeIngredientList = mRecipeIngredientList;
    }


    //--------------------------------------------------------------------------------|
    //                              Support Classes                                   |
    //--------------------------------------------------------------------------------|

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recipe_ingredient_name)
        TextView ingredientName;
        @BindView(R.id.tv_recipe_ingredient_quantity)
        TextView ingredientQuantity;
        @BindView(R.id.tv_recipe_ingredient_units)
        TextView ingredientUnits;
        @BindView(R.id.iv_recipe_ingredient_remove)
        ImageView ingredientBackToBuy;
        private final View recipeIngredientViewLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeIngredientViewLayout = itemView;
            ButterKnife.bind(this, itemView);
        }

    }


    //--------------------------------------------------------------------------------|
    //                          Fragment--> Activity Comm                             |
    //--------------------------------------------------------------------------------|

    public interface RecipeIngredientsAlreadyBoughtClickListener {
        public void onRecipeIngredientsBackToBuyClick(int position);
    }


    //--------------------------------------------------------------------------------|
    //                                 UI Methods                                     |
    //--------------------------------------------------------------------------------|

    /**
     * Populate UI view elements
     *
     * @param holder     ViewHolder (View container)
     * @param ingredient Recipe Ingredient object
     */
    private void populateUIView(RecipeIngredientsAlreadyBoughtAdapter.ViewHolder holder,
                                RecipeIngredient ingredient) {
        holder.ingredientName.setText(ingredient.getIngredient());
        holder.ingredientQuantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.ingredientUnits.setText(ingredient.getMeasure());
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
        holder.ingredientBackToBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecipeIngredientsToBuyClickListener != null) {
                    // TODO Add visual delay for displaying the tick
                    mRecipeIngredientsToBuyClickListener.
                            onRecipeIngredientsBackToBuyClick(
                                    (int)holder.recipeIngredientViewLayout.getTag());
                }
            }
        });
    }

}

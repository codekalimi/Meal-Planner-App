package com.codeaamirkalimi.mealplanner.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.RecipeStep;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    /** List of Recipe Steps*/
    List<RecipeStep> mRecipeSteps;
    /** Activity Context */
    private Context mContext;


    //--------------------------------------------------------------------------------|
    //                                 Constructors                                   |
    //--------------------------------------------------------------------------------|

    public RecipeStepsAdapter(List<RecipeStep> recipeSteps) {
        this.mRecipeSteps = recipeSteps;
    }


    //--------------------------------------------------------------------------------|
    //                              Override Methods                                  |
    //--------------------------------------------------------------------------------|

    @NonNull
    @Override
    public RecipeStepsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_recipe_step, parent, false);
        return new RecipeStepsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsAdapter.ViewHolder holder,
                                 int position) {

        // Retrieve i-recipe-step from recipe step list
        RecipeStep recipeStep = mRecipeSteps.get(position);

        // Set position-tag
        holder.recipeStepViewLayout.setTag(position);

        // Populate UI elements
        populateUIView(holder, recipeStep);

    }

    @Override
    public int getItemCount() {
        return mRecipeSteps.size();
    }


    //--------------------------------------------------------------------------------|
    //                             Getters / Setters                                  |
    //--------------------------------------------------------------------------------|

    public List<RecipeStep> getmRecipeSteps() {
        return mRecipeSteps;
    }


    //--------------------------------------------------------------------------------|
    //                              Support Classes                                   |
    //--------------------------------------------------------------------------------|

    class ViewHolder extends RecyclerView.ViewHolder {

        private boolean recipeStepChecked = false;

        @BindView(R.id.iv_recipe_step_done_checkbox)
        ImageView recipeStepDone;
        @BindView(R.id.tv_recipe_step_number)
        TextView recipeStepNumber;
        @BindView(R.id.tv_recipe_step_short_description)
        TextView recipeStepShortDescription;
        @BindView(R.id.tv_recipe_step_description)
        TextView recipeStepDescription;
        private final View recipeStepViewLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeStepViewLayout = itemView;
            ButterKnife.bind(this, itemView);

            // Set OnClickListeners
            setOnClickListeners();
        }

        private void setOnClickListeners() {
            recipeStepDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recipeStepChecked) {
                        recipeStepDone.setImageDrawable(ContextCompat.getDrawable(mContext,
                                R.drawable.card_shape_rectangle));
                        recipeStepNumber.setPaintFlags(recipeStepNumber.getPaintFlags()
                                & ~Paint.STRIKE_THRU_TEXT_FLAG);
                        recipeStepShortDescription.setPaintFlags(recipeStepShortDescription.getPaintFlags()
                                & ~Paint.STRIKE_THRU_TEXT_FLAG);
                        recipeStepDescription.setPaintFlags(recipeStepDescription.getPaintFlags()
                                & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_check);
                        drawable = DrawableCompat.wrap(drawable);
                        DrawableCompat.setTint(drawable, ContextCompat.getColor(mContext,
                                R.color.colorPrimary));
                        recipeStepDone.setImageDrawable(drawable);
                        recipeStepNumber.setPaintFlags(recipeStepNumber.getPaintFlags()
                                | Paint.STRIKE_THRU_TEXT_FLAG);
                        recipeStepShortDescription.setPaintFlags(recipeStepShortDescription.getPaintFlags()
                                | Paint.STRIKE_THRU_TEXT_FLAG);
                        recipeStepDescription.setPaintFlags(recipeStepDescription.getPaintFlags()
                                | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    recipeStepChecked = !recipeStepChecked;
                }
            });
        }

    }

    //--------------------------------------------------------------------------------|
    //                                 UI Methods                                     |
    //--------------------------------------------------------------------------------|

    /**
     * Populate UI view elements
     *
     * @param holder        ViewHolder (View container)
     * @param recipeStep    RecipeStep object
     */
    private void populateUIView(RecipeStepsAdapter.ViewHolder holder, RecipeStep recipeStep) {
        holder.recipeStepNumber.setText(
                Integer.toString((int)recipeStep.getId() + 1) + ".");
        holder.recipeStepShortDescription.setText(recipeStep.getShortDescription());
        holder.recipeStepDescription.setText(recipeStep.getDescription());
    }

}

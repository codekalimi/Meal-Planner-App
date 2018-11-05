package com.codeaamirkalimi.mealplanner.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.Recipe;

import java.util.List;

public class MealPlannerMealtimeAdapter extends RecyclerView.Adapter<MealPlannerMealtimeAdapter.ViewHolder> {

    List<Recipe> mMealDayRecipeList;
    private Context mContext;
    private MealPlannerMealtimeClickListener mMealPlannerMealtimeClickListener;


    //--------------------------------------------------------------------------------|
    //                                 Constructors                                   |
    //--------------------------------------------------------------------------------|

    public MealPlannerMealtimeAdapter(List<Recipe> recipes, MealPlannerMealtimeClickListener listener) {
        this.mMealDayRecipeList = recipes;
        this.mMealPlannerMealtimeClickListener = listener;
    }


    //--------------------------------------------------------------------------------|
    //                              Override Methods                                  |
    //--------------------------------------------------------------------------------|

    @NonNull
    @Override
    public MealPlannerMealtimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_mealtime_item, parent, false);
        return new MealPlannerMealtimeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlannerMealtimeAdapter.ViewHolder holder,
                                 int position) {

        // Retrieve i-ingredient from shopping ingredient list
        Recipe recipe = mMealDayRecipeList.get(position);

        // Set position-tag
        holder.mealtimeViewLayout.setTag(position);

        // Populate UI elements
        populateUIView(holder, recipe);

    }

    @Override
    public int getItemCount() {
        return mMealDayRecipeList.size();
    }


    //--------------------------------------------------------------------------------|
    //                             Getters / Setters                                  |
    //--------------------------------------------------------------------------------|

    public List<Recipe> getmMealDayRecipeList() {
        return mMealDayRecipeList;
    }


    //--------------------------------------------------------------------------------|
    //                              Support Classes                                   |
    //--------------------------------------------------------------------------------|

    class ViewHolder extends RecyclerView.ViewHolder {

        private final View mealtimeViewLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mealtimeViewLayout = itemView;
        }

    }

    //--------------------------------------------------------------------------------|
    //                          Fragment--> Activity Comm                             |
    //--------------------------------------------------------------------------------|

    public interface MealPlannerMealtimeClickListener {
        public void onMealPlannerMealtimeClick(int position);
    }


    //--------------------------------------------------------------------------------|
    //                                 UI Methods                                     |
    //--------------------------------------------------------------------------------|

    /**
     * Populate UI view elements
     *
     * @param holder        ViewHolder (View container)
     * @param recipe        Recipe object
     */
    private void populateUIView(MealPlannerMealtimeAdapter.ViewHolder holder, Recipe recipe) {
        // TODO fill-in
    }

    //--------------------------------------------------------------------------------|
    //                              Support Methods                                   |
    //--------------------------------------------------------------------------------|

    /**
     * Set a film click-listener on the film-view
     *
     * @param    holder    ViewHolder (View container)
     */
    private void setOnViewClickListener(final MealPlannerMealtimeAdapter.ViewHolder holder) {
        holder.mealtimeViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMealPlannerMealtimeClickListener != null) {
                    mMealPlannerMealtimeClickListener.onMealPlannerMealtimeClick(
                            (int)holder.mealtimeViewLayout.getTag());
                }
            }
        });
    }
}

package com.codeaamirkalimi.mealplanner.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.Menu;
import com.codeaamirkalimi.mealplanner.ui.utils.UrlUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MealPlansPreviewAdapter
        extends RecyclerView.Adapter<MealPlansPreviewAdapter.ViewHolder> {

    List<Menu> mMenuList;
    private Context mContext;
    private MealPlansPreviewClickListener mMealPlansPreviewClickListener;


    //--------------------------------------------------------------------------------|
    //                                 Constructors                                   |
    //--------------------------------------------------------------------------------|

    public MealPlansPreviewAdapter(List<Menu> menus, MealPlansPreviewClickListener listener) {
        this.mMenuList = menus;
        this.mMealPlansPreviewClickListener = listener;
    }


    //--------------------------------------------------------------------------------|
    //                              Override Methods                                  |
    //--------------------------------------------------------------------------------|

    @NonNull
    @Override
    public MealPlansPreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.card_meal_plan_preview, parent, false);
        return new MealPlansPreviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlansPreviewAdapter.ViewHolder holder,
                                 int position) {

        // Retrieve i-ingredient from shopping ingredient list
        Menu menu = mMenuList.get(position);

        // Set position-tag
        holder.mealPlanViewLayout.setTag(position);

        // Populate UI elements
        populateUIView(holder, menu);

        // Set OnClickListener
        setOnViewClickListener(holder);

    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }


    //--------------------------------------------------------------------------------|
    //                             Getters / Setters                                  |
    //--------------------------------------------------------------------------------|

    public List<Menu> getmMenuList() {
        return mMenuList;
    }

    public void setmMenuList(List<Menu> mMenuList) {
        this.mMenuList = mMenuList;
    }

    //--------------------------------------------------------------------------------|
    //                              Support Classes                                   |
    //--------------------------------------------------------------------------------|

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_card_meal_plan_name)
        TextView mealPlanTitle;
        @BindView(R.id.iv_card_meal_plan_photo)
        ImageView mealPlanPhoto;
        private final View mealPlanViewLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mealPlanViewLayout = itemView;
            ButterKnife.bind(this, itemView);
        }

    }

    //--------------------------------------------------------------------------------|
    //                          Fragment--> Activity Comm                             |
    //--------------------------------------------------------------------------------|

    public interface MealPlansPreviewClickListener {
        public void onMealPlansPreviewClickListenerClick(Menu menu);
    }


    //--------------------------------------------------------------------------------|
    //                                 UI Methods                                     |
    //--------------------------------------------------------------------------------|

    /**
     * Populate UI view elements
     *
     * @param holder        ViewHolder (View container)
     * @param menu          Meal Plan object
     */
    private void populateUIView(MealPlansPreviewAdapter.ViewHolder holder, Menu menu) {
        holder.mealPlanTitle.setText(menu.getTitle());
        String imageUrl = menu.getImageUrl();
        if(imageUrl != null && !imageUrl.isEmpty() && UrlUtils.isValid(imageUrl)) {
            Picasso.get()
                    .load(imageUrl)
                    .error(ContextCompat.getDrawable(mContext, R.drawable.im_recipe))
                    .fit()
                    .into(holder.mealPlanPhoto);
        }
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
        holder.mealPlanViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMealPlansPreviewClickListener != null) {
                    mMealPlansPreviewClickListener
                            .onMealPlansPreviewClickListenerClick(mMenuList.get(
                                    (int)holder.mealPlanViewLayout.getTag()));
                }
            }
        });
    }

}

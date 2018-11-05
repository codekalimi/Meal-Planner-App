package com.codeaamirkalimi.mealplanner.ui;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.GoogleAccountData;
import com.codeaamirkalimi.mealplanner.global.MyMealPlannerGlobals;
import com.codeaamirkalimi.mealplanner.ui.utils.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {

    /** Class name - Log TAG */
    private static final String TAG = MyAccountFragment.class.getName();
    /** Key for retrieving GoogleAccountData parcelable object from Intent */
    private static final String GOOGLE_ACCOUNT_DATA_EXTRA = "google-account-data";
    /** Key for storing GoogleAccountData data across screen rotations */
    private static final String GOOGLE_ACCOUNT_DATA_KEY = "google-account-data";

    Unbinder unbinder;
    ActionBar actionBar;

    @BindView(R.id.iv_my_account_user_photo)
    RoundedImageView ivMyAccountUserPhoto;
    @BindView(R.id.tv_my_account_user_name)
    TextView tvMyAccountUserName;
    @BindView(R.id.tb_my_account_app_bar)
    Toolbar tbMyAccountAppBar;
    @BindView(R.id.tv_my_account_favourite_recipes_number)
    TextView tvMyAccountFavouriteRecipesNumber;
    @BindView(R.id.tv_my_account_my_meals_number)
    TextView tvMyAccountMyMealsNumber;
    @BindView(R.id.tv_my_account_planned_meals_number)
    TextView tvMyAccountPlannedMealsNumber;
    @BindView(R.id.iv_my_account_statistics)
    ImageView ivMyAccountStatistics;
    @BindView(R.id.iv_my_account_statistics_description)
    TextView ivMyAccountStatisticsDescription;
    @BindView(R.id.bt_my_account_send_feedback)
    Button btMyAccountSendFeedback;
    @BindView(R.id.bt_my_account_about_me)
    Button btMyAccountAboutMe;
    @BindView(R.id.bt_my_account_logout)
    Button btMyAccountLogout;
    @BindView(R.id.abl_my_account_layout)
    AppBarLayout ablMyAccountLayout;
    @BindView(R.id.tv_my_account_my_meals_title)
    TextView tvMyAccountMyMealsTitle;
    @BindView(R.id.tv_my_account_planned_meals_title)
    TextView tvMyAccountPlannedMealsTitle;
    @BindView(R.id.cl_my_account_resume_data)
    ConstraintLayout clMyAccountResumeData;
    @BindView(R.id.tv_my_account_send_feedback_title)
    TextView tvMyAccountSendFeedbackTitle;
    @BindView(R.id.tv_my_account_about_me_title)
    TextView tvMyAccountAboutMeTitle;
    @BindView(R.id.tv_my_account_logout_title)
    TextView tvMyAccountLogoutTitle;
    @BindView(R.id.ctl_my_account_toolbar_layout)
    CollapsingToolbarLayout ctlMyAccountToolbarLayout;

    private OnMyAccountFragmentInteractionListener mListener;
    private GoogleAccountData mGoogleAccountData;
    private SharedPreferences mSharedPreferences;
    private Activity mContext;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyAccountFragmentInteractionListener) {
            mListener = (OnMyAccountFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMyAccountFragmentInteractionListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Hide ActionBar
        hideActionBar();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_account, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mContext = getActivity();

        // Get SharedPreferences
        mSharedPreferences = mContext.getSharedPreferences(
                getString(R.string.app_shared_preferences), Context.MODE_PRIVATE);

        // Retrieve Data sent from Activity
        if(savedInstanceState != null) {
            // Reload film backdrop image
            mGoogleAccountData = savedInstanceState.getParcelable(GOOGLE_ACCOUNT_DATA_KEY);
        } else {
            mGoogleAccountData = getArguments().getParcelable(GOOGLE_ACCOUNT_DATA_EXTRA);
        }

        // Update UI
        updateUI();

        // Return rootView
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(GOOGLE_ACCOUNT_DATA_KEY, mGoogleAccountData);
    }

    private void updateUI() {

        // Set Toolbar title (Collapsed Toolbar)
        ablMyAccountLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    ctlMyAccountToolbarLayout.setTitle(mGoogleAccountData.getDisplayName());
                    isShow = true;
                } else if (isShow) {
                    ctlMyAccountToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        // Display Account info
        String loginType = mSharedPreferences.getString(getString(R.string.login_type),
                getString(R.string.default_login_type));
        switch (loginType) {
            case MyMealPlannerGlobals.GOOGLE_LOGGED:
                tvMyAccountUserName.setText(mGoogleAccountData.getDisplayName());
                Picasso.get().load(mGoogleAccountData.getPhotoUrl()).fit().into(ivMyAccountUserPhoto);
                break;
            case MyMealPlannerGlobals.MAIL_LOGGED:
                break;
            default:
            case MyMealPlannerGlobals.NOT_LOGGED:
                break;
        }
    }

    private void hideActionBar() {
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && actionBar.isShowing()) {
            actionBar.hide();
        }
    }

    @OnClick({R.id.bt_my_account_send_feedback, R.id.bt_my_account_about_me, R.id.bt_my_account_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_my_account_send_feedback:
                break;
            case R.id.bt_my_account_about_me:
                break;
            case R.id.bt_my_account_logout:
                if (mListener != null) {
                    mListener.onSignOut();
                }
                break;
        }
    }

    public interface OnMyAccountFragmentInteractionListener {
        void onSignOut();
    }

}

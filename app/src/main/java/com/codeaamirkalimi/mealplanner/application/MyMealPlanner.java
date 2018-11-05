package com.codeaamirkalimi.mealplanner.application;

import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.codeaamirkalimi.mealplanner.repository.MyMealPlannerRepository;

public class MyMealPlanner extends Application {

    private static MyMealPlanner mInstance;
    private MyMealPlannerExecutors mMyMealPlannerExecutors;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mMyMealPlannerExecutors = new MyMealPlannerExecutors();
        mInstance = this;
    }

    public static synchronized MyMealPlanner getInstance() {
        return mInstance;
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public void setmGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
    }

    public MyMealPlannerRepository getRepository() {
        return MyMealPlannerRepository.getInstance(mMyMealPlannerExecutors);
    }

}

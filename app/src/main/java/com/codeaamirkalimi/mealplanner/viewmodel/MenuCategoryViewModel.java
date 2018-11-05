package com.codeaamirkalimi.mealplanner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.codeaamirkalimi.mealplanner.application.MyMealPlanner;
import com.codeaamirkalimi.mealplanner.datamodel.Menu;
import com.codeaamirkalimi.mealplanner.datamodel.MenuCategory;
import com.codeaamirkalimi.mealplanner.repository.MyMealPlannerRepository;

import java.util.List;

public class MenuCategoryViewModel extends AndroidViewModel {

    private final MyMealPlannerRepository myMealPlannerRepository;

    public MenuCategoryViewModel(Application application, MyMealPlannerRepository repository) {
        super(application);
        this.myMealPlannerRepository = repository;
    }

    public LiveData<List<MenuCategory>> getMenuCategories() {
        return myMealPlannerRepository.getMenuCategories();
    }

    /**
     * A creator is used to inject the product ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the product ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final MyMealPlannerRepository mRepository;

        public Factory(@NonNull Application application) {
            mApplication = application;
            mRepository = ((MyMealPlanner) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MenuCategoryViewModel(mApplication, mRepository);
        }
    }

}

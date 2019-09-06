package com.example.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.bakingapp.Fragments.RecipeDescriptionFragment;
import com.example.bakingapp.Fragments.RecipeDetailFragment;
import com.example.bakingapp.Fragments.RecipeNextFragment;
import com.example.bakingapp.Fragments.RecipeVideoFragment;
import com.example.bakingapp.Fragments.RecipesListFragment;
import com.example.bakingapp.Model.Ingredient;

public class MainActivity extends AppCompatActivity {
    private BakingViewModel mBakingViewModel;

    FragmentManager fragmentManager;
    RecipesListFragment recipesListFragment;
    RecipeDetailFragment recipeDetailFragment;
    RecipeNextFragment recipeNextFragment;
    RecipeDescriptionFragment recipeDescriptionFragment;
    RecipeVideoFragment recipeVideoFragment;
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String RECIPE_LIST_FRAGMENT_TAG = "RECIPE_LIST";
    public static final String RECIPE_DETAIL_FRAGMENT_TAG = "RECIPE_DETAIL";
    public static final String RECIPE_PLAYER_FRAGMENT_TAG = "RECIPE_PLAYER";
    public static final String RECIPE_DESCRIPTION_FRAGMENT_TAG = "RECIPE_DESCRIPTION";
    public static final String RECIPE_NEXT_FRAGMENT_TAG = "RECIPE_NEXT";
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean tablentView = false;
    private boolean landscapeOrientation = false;
    private int heightToUseIfLandScape = 0;
    ///////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTablentView(findViewById(R.id.container_1_tablet) != null);
        setLandscapeOrientation(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        if (isLandscapeOrientation()) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            setHeightToUseIfLandScape(Resources.getSystem().getDisplayMetrics().heightPixels);
        }
        basicSetup();
        ////////////////////////////////////////////////////////////////////////////////////////////
        mBakingViewModel.getmSelectedRecipe().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("dddddd", "onChangedFragmnet SelectedRecipe");
                switch (mBakingViewModel.getCurrentState()) {
                    case BakingViewModel.RECIPE_LIST_FRAGMENT: {
                        if (!isTablentView())
                            replaceRecipeListFragmentMobileView();
                        else {
                            replaceRecipeListFragmentTabletView();
                        }
                        break;
                    }
                    case BakingViewModel.RECIPE_DETAIL_FRAGMENT: {
                        if (!isTablentView())
                            replaceRecipeDetailFragmentMobileView();
                        else {
                            replaceRecipeDetailVideoDescriptionNextFragmentTabletView();
                        }
                        break;
                    }
                }
            }
        });
        mBakingViewModel.getmSelectedRecipeDetail().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("dddddd", "onChangedFragmnet Selected DETAIL");
                    switch (mBakingViewModel.getCurrentState()) {
                        case BakingViewModel.RECIPE_NEXT_FRAGMENT: {
                            if (!isTablentView()) {
                                //check if landscape and mobile view and player layout is actually visible(not visible if integer==0 "because its not part of steps" and there is a video or thumbnail url)
                                if (isLandscapeOrientation() && !isTablentView() && integer != 0 && ((!mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getSteps().get(integer - 1).getVideoURL().isEmpty()) || (!mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getSteps().get(integer - 1).getThumbnailURL().isEmpty()))) {
                                    Log.d("dddddd", "LANDSCAPE");
                                    findViewById(R.id.container_2).setVisibility(View.GONE);
                                    findViewById(R.id.container_3).setVisibility(View.GONE);
                                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                                    getSupportActionBar().hide();
                                }
                                replaceRecipeVideoDescriptionNextFragmentMobileView();
                                Log.d("dddddd", "case BakingViewModel.RECIPE_NEXT_FRAGMENT");
                            } else {
                                Log.d("dddddd", "else RECIPE_NEXT_FRAGMENT tablet ");
                                replaceRecipeDetailVideoDescriptionNextFragmentTabletView();
                            }
                            break;
                        }
                    }
            }
        });
    }

    @Override
    //update widget with ingredients of latest opened recipe ingredients
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = getSharedPreferences(NewAppWidget.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        int savedId=sharedPref.getInt(NewAppWidget.SHARED_PREF_INT_KEY,-1);
        //only udpate it if the latest recipe ingredient is different from the stored one
        if( mBakingViewModel.getmRecipes().getValue()!=null && savedId != mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getId()){
            //store it in shared prev
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(NewAppWidget.SHARED_PREF_INT_KEY, mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getId());
            StringBuilder Ingredients = new StringBuilder();
            for (Ingredient ing : mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getIngredients()
            ) {
                Ingredients.append(ing.getIngredient() + " " + ing.getQuantity() + ing.getMeasure() + "\n");
            }
            editor.putString(NewAppWidget.SHARED_PREF_STRING_KEY, Ingredients.toString());
            editor.apply();
            ////trigger widget to update
            Intent intent = new Intent(this, NewAppWidget.class);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
            sendBroadcast(intent);
        }
    }
    public void replaceRecipeListFragmentMobileView() {
        if (fragmentManager.findFragmentByTag(RECIPE_LIST_FRAGMENT_TAG) == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_1, recipesListFragment, RECIPE_LIST_FRAGMENT_TAG);
            fragmentTransaction.commit();
            Log.d("dddddd", "null LIST");
        } else {
            Log.d("dddddd", "else LIST");
        }
    }

    public void replaceRecipeDetailFragmentMobileView() {
        if (fragmentManager.findFragmentByTag(RECIPE_DETAIL_FRAGMENT_TAG) == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_1, recipeDetailFragment, RECIPE_DETAIL_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            Log.d("dddddd", "null DETAIL");
        } else {
            Log.d("dddddd", "else DETAIL");
        }
    }

    public void replaceRecipeVideoDescriptionNextFragmentMobileView() {
        if (fragmentManager.findFragmentByTag(RECIPE_NEXT_FRAGMENT_TAG) == null && fragmentManager.findFragmentByTag(RECIPE_DESCRIPTION_FRAGMENT_TAG) == null && fragmentManager.findFragmentByTag(RECIPE_PLAYER_FRAGMENT_TAG) == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_1, recipeVideoFragment, RECIPE_PLAYER_FRAGMENT_TAG);
            fragmentTransaction.replace(R.id.container_2, recipeDescriptionFragment, RECIPE_DESCRIPTION_FRAGMENT_TAG);
            fragmentTransaction.replace(R.id.container_3, recipeNextFragment, RECIPE_NEXT_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            Log.d("dddddd", "null NEXT DESCRIPTION PLAYER");
        } else {
            Log.d("dddddd", "else NEXT DESCRIPTION PLAYER");
        }
    }

    public void replaceRecipeListFragmentTabletView() {
        if (fragmentManager.findFragmentByTag(RECIPE_LIST_FRAGMENT_TAG) == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_1_tablet, recipesListFragment, RECIPE_LIST_FRAGMENT_TAG);
            fragmentTransaction.commit();
            Log.d("dddddd", "null LIST");
        } else {
            Log.d("dddddd", "else LIST");
        }
    }

    public void replaceRecipeDetailVideoDescriptionNextFragmentTabletView() {
        if (fragmentManager.findFragmentByTag(RECIPE_DETAIL_FRAGMENT_TAG) == null && fragmentManager.findFragmentByTag(RECIPE_NEXT_FRAGMENT_TAG) == null && fragmentManager.findFragmentByTag(RECIPE_DESCRIPTION_FRAGMENT_TAG) == null && fragmentManager.findFragmentByTag(RECIPE_PLAYER_FRAGMENT_TAG) == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_1_tablet, recipeDetailFragment, RECIPE_DETAIL_FRAGMENT_TAG);
            fragmentTransaction.replace(R.id.container_2_tablet, recipeNextFragment, RECIPE_NEXT_FRAGMENT_TAG);
            fragmentTransaction.replace(R.id.container_3_tablet, recipeVideoFragment, RECIPE_PLAYER_FRAGMENT_TAG);
            fragmentTransaction.replace(R.id.container_4_tablet, recipeDescriptionFragment, RECIPE_DESCRIPTION_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            Log.d("dddddd", "null DETAIL..... Tablet");
        } else {
            Log.d("dddddd", "else DETAIL..... Tablet");
        }


    }

    public void basicSetup() {
        mBakingViewModel = ViewModelProviders.of(this).get(BakingViewModel.class);
        fragmentManager = getSupportFragmentManager();
        recipesListFragment = new RecipesListFragment();
        recipeDetailFragment = new RecipeDetailFragment();
        recipeNextFragment = new RecipeNextFragment();
        recipeDescriptionFragment = new RecipeDescriptionFragment();
        recipeVideoFragment = new RecipeVideoFragment();
    }

    public boolean isTablentView() {
        return tablentView;
    }

    public void setTablentView(boolean tablentView) {
        this.tablentView = tablentView;
    }

    public boolean isLandscapeOrientation() {
        return landscapeOrientation;
    }

    public void setLandscapeOrientation(boolean landscapeOrientation) {
        this.landscapeOrientation = landscapeOrientation;
    }

    public int getHeightToUseIfLandScape() {
        return heightToUseIfLandScape;
    }

    public void setHeightToUseIfLandScape(int heightToUseIfLandScape) {
        this.heightToUseIfLandScape = heightToUseIfLandScape;
    }
}


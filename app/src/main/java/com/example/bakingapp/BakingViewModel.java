package com.example.bakingapp;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bakingapp.Model.Ingredient;
import com.example.bakingapp.Model.Recipe;
import com.example.bakingapp.Model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BakingViewModel extends AndroidViewModel {
    private final String mUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private Application mApplication;
    private MutableLiveData<ArrayList<Recipe>> mRecipes = new MutableLiveData<>();
    private MutableLiveData<Integer> mSelectedRecipe = new MutableLiveData<>();
    private MutableLiveData<Integer> mSelectedRecipeDetail = new MutableLiveData<>();
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static final int RECIPE_LIST_FRAGMENT=1;
    public static final int RECIPE_DETAIL_FRAGMENT=2;
    public static final int RECIPE_NEXT_FRAGMENT=3;
    private int currentState;

    public BakingViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        privateUpdatemRecipes();
        //for triggering on change for the first time
        //mSelectedRecipe.setValue(-1);
        //initialize the first time
        currentState =RECIPE_LIST_FRAGMENT;;
    }

    public MutableLiveData<Integer> getmSelectedRecipe() {
        return mSelectedRecipe;
    }

    public MutableLiveData<Integer> getmSelectedRecipeDetail() {
        return mSelectedRecipeDetail;
    }

    public LiveData<ArrayList<Recipe>> getmRecipes() {
        return mRecipes;
    }

    private void privateUpdatemRecipes() {
        RequestQueue queue = Volley.newRequestQueue(mApplication);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //for triggering on change for the first time
                        mSelectedRecipe.setValue(0);
                        mRecipes.setValue(parseResponse(response));
                        Toast.makeText(mApplication, "recipes loaded", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mApplication, "recipes not loaded", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private ArrayList<Recipe> parseResponse(String response) {
        JSONArray recipesJSONArray;
        ArrayList<Recipe> Recipes = new ArrayList<Recipe>();
        try {
            recipesJSONArray = new JSONArray(response);
            for (int i = 0; i < recipesJSONArray.length(); i++) {
                Recipe recipe = new Recipe();
                JSONObject recipeJSONObject = recipesJSONArray.getJSONObject(i);
                recipe.setId(recipeJSONObject.getInt("id"));
                recipe.setName(recipeJSONObject.getString("name"));
                ArrayList<Ingredient> Ingredients = new ArrayList<Ingredient>();
                JSONArray ingredientsJSONArray = recipeJSONObject.getJSONArray("ingredients");
                for (int j = 0; j < ingredientsJSONArray.length(); j++) {
                    JSONObject IngredientJSONObject;
                    IngredientJSONObject = ingredientsJSONArray.getJSONObject(j);
                    Ingredient ingredient = new Ingredient();
                    ingredient.setQuantity(IngredientJSONObject.getInt("quantity"));
                    ingredient.setMeasure(IngredientJSONObject.getString("measure"));
                    ingredient.setIngredient(IngredientJSONObject.getString("ingredient"));
                    Ingredients.add(ingredient);
                }
                recipe.setIngredients(Ingredients);
                ArrayList<Step> steps = new ArrayList<Step>();
                JSONArray stepsJSONArray = recipeJSONObject.getJSONArray("steps");
                for (int k = 0; k < stepsJSONArray.length(); k++) {
                    JSONObject stepJSONObject;
                    stepJSONObject = stepsJSONArray.getJSONObject(k);
                    Step step = new Step();
                    step.setId(stepJSONObject.getInt("id"));
                    step.setShortDescription(stepJSONObject.getString("shortDescription"));
                    step.setDescription(stepJSONObject.getString("description"));
                    step.setVideoURL(stepJSONObject.optString("videoURL", ""));
                    step.setThumbnailURL(stepJSONObject.optString("thumbnailURL", ""));
                    steps.add(step);
                }
                recipe.setSteps(steps);
                recipe.setServings(recipeJSONObject.optInt("servings", -1));
                recipe.setImage(recipeJSONObject.optString("image", ""));
                Recipes.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Recipes;
    }
    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }
}

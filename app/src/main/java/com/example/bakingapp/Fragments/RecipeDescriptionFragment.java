package com.example.bakingapp.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.Model.Ingredient;
import com.example.bakingapp.BakingViewModel;
import com.example.bakingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDescriptionFragment extends Fragment {
    BakingViewModel mBakingViewModel;
    TextView fragmentDescriptionTv;

    public RecipeDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("dddddd", "onCreateView DESCRIPTION");
        mBakingViewModel = ViewModelProviders.of(getActivity()).get(BakingViewModel.class);
        View v = inflater.inflate(R.layout.fragment_recipe_description, container, false);
        fragmentDescriptionTv = v.findViewById(R.id.fragment_recipe_description_tv);
        mBakingViewModel.getmSelectedRecipeDetail().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    StringBuilder Ingredients=new StringBuilder();
                    for (Ingredient ing : mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getIngredients()
                    ) {
                        Ingredients.append(ing.getIngredient()+"    "+ing.getQuantity()+" "+ing.getMeasure()+"\n");
                    }
                    fragmentDescriptionTv.setText(Ingredients);
                } else {
                    fragmentDescriptionTv.setText(mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getSteps().get(integer - 1).getDescription());
                }
            }
        });

        return v;
    }

}

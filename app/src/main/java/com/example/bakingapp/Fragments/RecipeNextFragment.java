package com.example.bakingapp.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bakingapp.BakingViewModel;
import com.example.bakingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeNextFragment extends Fragment {

    BakingViewModel mBakingViewModel;
    Button nextBtn;
    Button prevBtn;
    private int maxRecipeDetailIndex;

    public RecipeNextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBakingViewModel = ViewModelProviders.of(getActivity()).get(BakingViewModel.class);
        mBakingViewModel.setCurrentState(BakingViewModel.RECIPE_NEXT_FRAGMENT);
        Log.d("dddddd", "onCreateView Next");
        View v = inflater.inflate(R.layout.fragment_recipe_next, container, false);
        nextBtn = v.findViewById(R.id.button_next);
        prevBtn = v.findViewById(R.id.button_previous);
        maxRecipeDetailIndex = mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getSteps().size();
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        mBakingViewModel.getmSelectedRecipeDetail().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == maxRecipeDetailIndex) {
                    nextBtn.setEnabled(false);
                } else {
                    nextBtn.setEnabled(true);
                }
                if (integer == 0) {
                    prevBtn.setEnabled(false);
                } else {
                    prevBtn.setEnabled(true);
                }

                if (integer != maxRecipeDetailIndex) {
                    nextBtn.setText(mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getSteps().get(integer).getShortDescription());
                } else {
                    nextBtn.setText("Next");
                }
                if (integer != 0) {
                    if (integer == 1) {
                        prevBtn.setText("Ingredients");
                    } else {
                        prevBtn.setText(mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getSteps().get(integer - 2).getShortDescription());
                    }
                } else {
                    prevBtn.setText("Previous");
                }
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBakingViewModel.getmSelectedRecipeDetail().getValue() < (maxRecipeDetailIndex)) {
                    mBakingViewModel.getmSelectedRecipeDetail().setValue(mBakingViewModel.getmSelectedRecipeDetail().getValue() + 1);
                }
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBakingViewModel.getmSelectedRecipeDetail().getValue() > 0) {
                    mBakingViewModel.getmSelectedRecipeDetail().setValue(mBakingViewModel.getmSelectedRecipeDetail().getValue() - 1);
                }
            }
        });
        return v;
    }

}

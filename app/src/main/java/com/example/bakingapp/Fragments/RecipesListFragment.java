package com.example.bakingapp.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bakingapp.Adapters.RecipesListFragmentAdapter;
import com.example.bakingapp.BakingViewModel;
import com.example.bakingapp.MainActivity;
import com.example.bakingapp.Model.Recipe;
import com.example.bakingapp.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesListFragment extends Fragment {
    RecyclerView rv;
    RecipesListFragmentAdapter adapter;
    BakingViewModel mBakingViewModel;

    public RecipesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("dddddd", "onCreateView LIST");
        mBakingViewModel = ViewModelProviders.of(getActivity()).get(BakingViewModel.class);
        mBakingViewModel.setCurrentState(BakingViewModel.RECIPE_LIST_FRAGMENT);
        adapter = new RecipesListFragmentAdapter();
        adapter.setmListener(createOnRecipeSelectedRvListener());
        mBakingViewModel.getmRecipes().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                adapter.setmRecipes(recipes);
                adapter.notifyDataSetChanged();
                mBakingViewModel.getmRecipes().removeObserver(this);
            }
        });
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        rv = v.findViewById(R.id.fragment_recipe_list_rv);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int gridspan;
        if (((MainActivity) getActivity()).isLandscapeOrientation() && !((MainActivity) getActivity()).isTablentView()) {
            gridspan = 2;
        } else if (!((MainActivity) getActivity()).isLandscapeOrientation() && ((MainActivity) getActivity()).isTablentView()) {
            gridspan = 2;
        } else if (((MainActivity) getActivity()).isLandscapeOrientation() && ((MainActivity) getActivity()).isTablentView()) {
            gridspan = 3;
        } else {
            gridspan = 1;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), gridspan);
        rv.setLayoutManager(gridLayoutManager);

    }

    private RecipesListFragmentAdapter.OnRecipeSelectedRvListener createOnRecipeSelectedRvListener() {
        return new RecipesListFragmentAdapter.OnRecipeSelectedRvListener() {
            @Override
            public void onRecipeSelected(int position) {
                mBakingViewModel.setCurrentState(BakingViewModel.RECIPE_DETAIL_FRAGMENT);
                mBakingViewModel.getmSelectedRecipe().setValue(position);
                //default value"ingredients"
                mBakingViewModel.getmSelectedRecipeDetail().setValue(0);
                Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
            }
        };
    }

}

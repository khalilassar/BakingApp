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

import com.example.bakingapp.Adapters.RecipeDetailFragmentAdapter;
import com.example.bakingapp.BakingViewModel;
import com.example.bakingapp.MainActivity;
import com.example.bakingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment {
    RecyclerView rv;
    RecipeDetailFragmentAdapter adapter;
    BakingViewModel mBakingViewModel;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("dddddd", "onCreateView DETAIL");
        mBakingViewModel = ViewModelProviders.of(getActivity()).get(BakingViewModel.class);
        mBakingViewModel.setCurrentState(BakingViewModel.RECIPE_DETAIL_FRAGMENT);
        adapter = new RecipeDetailFragmentAdapter();
        adapter.setmListener(createOnRecipeDetailSelectedRvListener());
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        rv = v.findViewById(R.id.fragment_recipe_detail_rv);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        mBakingViewModel.getmSelectedRecipe().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                adapter.setmRecipe(mBakingViewModel.getmRecipes().getValue().get(integer));
                adapter.notifyDataSetChanged();
                mBakingViewModel.getmSelectedRecipe().removeObserver(this);
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int gridspan=1;
        if (((MainActivity) getActivity()).isLandscapeOrientation() && !((MainActivity) getActivity()).isTablentView()) {
            gridspan = 2;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), gridspan);
        rv.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private RecipeDetailFragmentAdapter.OnRecipeDetailSelectedRvListener createOnRecipeDetailSelectedRvListener() {
        return new RecipeDetailFragmentAdapter.OnRecipeDetailSelectedRvListener() {
            @Override
            public void onRecipeDetailSelected(int position) {
                mBakingViewModel.setCurrentState(BakingViewModel.RECIPE_NEXT_FRAGMENT);
                mBakingViewModel.getmSelectedRecipeDetail().setValue(position);
                Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
            }
        };
    }
}

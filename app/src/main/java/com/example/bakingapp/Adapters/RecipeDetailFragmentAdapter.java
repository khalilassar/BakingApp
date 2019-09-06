package com.example.bakingapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.Model.Recipe;
import com.example.bakingapp.R;
import com.squareup.picasso.Picasso;

public class RecipeDetailFragmentAdapter extends RecyclerView.Adapter<RecipeDetailFragmentAdapter.RecipeDetailHolder> {
    Recipe mRecipe;
    OnRecipeDetailSelectedRvListener mListener;

    @NonNull
    @Override
    public RecipeDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_row, null, false);
        RecipeDetailHolder recipeDetailHolder = new RecipeDetailHolder(v);
        return recipeDetailHolder;
    }

    public void onBindViewHolder(@NonNull RecipeDetailHolder holder, int position) {
        if (position == 0) {
            holder.recipeDetailTv.setText("Ingredients");
            Picasso.get().load(R.drawable.recipe_ingredients).fit().into(holder.recipeDetailImg);
          //  holder.recipeDetailImg.setImageResource(R.drawable.recipe_ingredients);
        } else {
            Picasso.get().load(R.drawable.recipe_steps).fit().into(holder.recipeDetailImg);
            holder.recipeDetailTv.setText(mRecipe.getSteps().get(position - 1).getShortDescription());
          //  holder.recipeDetailImg.setImageResource(R.drawable.recipe_steps);
        }
    }

    public int getItemCount() {
        if (mRecipe == null) {
            return 0;
        } else {
            return mRecipe.getSteps().size()+1;
        }
    }

    protected class RecipeDetailHolder extends RecyclerView.ViewHolder {
        TextView recipeDetailTv;
        ImageView recipeDetailImg;

        public RecipeDetailHolder(@NonNull View itemView) {
            super(itemView);/////////////////////////////////////////////
            recipeDetailTv = itemView.findViewById(R.id.detail_tv);
            recipeDetailImg=itemView.findViewById(R.id.detail_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onRecipeDetailSelected(getAdapterPosition());
                }
            });
        }
    }

    public interface OnRecipeDetailSelectedRvListener {
        public void onRecipeDetailSelected(int position);
    }

    public void setmRecipe(Recipe mRecipe) {
        this.mRecipe = mRecipe;
    }

    public void setmListener(RecipeDetailFragmentAdapter.OnRecipeDetailSelectedRvListener mListener) {
        this.mListener = mListener;
    }
}

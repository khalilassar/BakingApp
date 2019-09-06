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

import java.util.List;

public class RecipesListFragmentAdapter extends RecyclerView.Adapter<RecipesListFragmentAdapter.RecipeHolder> {
    List<Recipe> mRecipes;
    OnRecipeSelectedRvListener mListener;

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_row, null, false);
        RecipeHolder recipeHolder = new RecipeHolder(v);
        return recipeHolder;
    }

    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        String mImage = recipe.getImage();
        //holder.recipeImg.setImageResource(R.drawable.recipe_img);
        Picasso.get().load(R.drawable.recipe_img).fit().into(holder.recipeImg);
        holder.recipeTv.setText(recipe.getName());

    }

    public int getItemCount() {
        if (mRecipes == null) {
            Log.d("ddddd", "return 0;");
            return 0;
        } else {
            Log.d("ddddd", "mRecipes.size();");
            return mRecipes.size();
        }
    }

    protected class RecipeHolder extends RecyclerView.ViewHolder {
        ImageView recipeImg;
        TextView recipeTv;

        public RecipeHolder(@NonNull View itemView) {
            super(itemView);
            recipeImg = itemView.findViewById(R.id.detail_img);
            recipeTv = itemView.findViewById(R.id.detail_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onRecipeSelected(getAdapterPosition());
                }
            });
        }
    }

    public interface OnRecipeSelectedRvListener {
        public void onRecipeSelected(int position);
    }

    public void setmRecipes(List<Recipe> mRecipes) {
        this.mRecipes = mRecipes;
    }

    public void setmListener(OnRecipeSelectedRvListener mListener) {
        this.mListener = mListener;
    }

}

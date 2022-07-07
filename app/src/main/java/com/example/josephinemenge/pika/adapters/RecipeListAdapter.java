package com.example.josephinemenge.pika.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.josephinemenge.pika.R;
import com.example.josephinemenge.pika.Recipe;
import com.example.josephinemenge.pika.ui.RecipeDetailActivity;
import com.squareup.picasso.Picasso;


import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Josephine Menge on 05/06/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
private ArrayList<Recipe>mRecipes = new ArrayList<>();
    private Context mContext;


    public RecipeListAdapter(Context context,ArrayList<Recipe> recipes) {
        mContext = context;
        mRecipes = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item,parent,false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bindRecipe(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       @Bind(R.id.recipeImageView) ImageView mImageView;
        @Bind(R.id.recipeNameTextView) TextView mNameTextView;
        @Bind(R.id.recipeSource)TextView mRecipeSourceView;
        @Bind(R.id.yieldTextView) TextView mYieldTextView;


        private  Context mContext;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }
        public void bindRecipe(Recipe recipe) {
            Picasso.with(mContext).load(recipe.getImageUrl()).into(mImageView);
            mNameTextView.setText(recipe.getLabel());
            mYieldTextView.setText("Feeds: " + recipe.getYield() +" people");
            mRecipeSourceView.setText(recipe.getSource());

        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, RecipeDetailActivity.class);
            intent.putExtra("position",itemPosition);
            intent.putExtra("recipes", Parcels.wrap(mRecipes));
            mContext.startActivity(intent);
        }
    }
}


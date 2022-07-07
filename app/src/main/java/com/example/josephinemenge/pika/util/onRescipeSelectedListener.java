package com.example.josephinemenge.pika.util;

import android.content.Intent;

import com.example.josephinemenge.pika.Recipe;

import java.util.ArrayList;

/**
 * Created by bubbles on 6/16/17.
 */

public interface onRescipeSelectedListener {
    public void onRecipeSelected(Intent position, ArrayList<Recipe> recipes, String source);
}

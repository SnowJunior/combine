package com.example.josephinemenge.pika.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.josephinemenge.pika.Constants;
import com.example.josephinemenge.pika.R;
import com.example.josephinemenge.pika.Recipe;
import com.example.josephinemenge.pika.adapters.RecipeListAdapter;
import com.example.josephinemenge.pika.service.EdmamService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecipeListActivity extends AppCompatActivity {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private RecipeListAdapter mAdapter;
    public static final String TAG = RecipeListActivity.class.getSimpleName();
    public ArrayList<Recipe> mRecipes = new ArrayList<>();
    private String mRecentSearch;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String foodType = intent.getStringExtra("foodType");
        String health = intent.getStringExtra("health");
        getRecipes(foodType,health);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentSearch = mSharedPreferences.getString(Constants.PREFERENCES_FOOD_TYPE,null);
        if (mRecentSearch != null) {
            getRecipes(mRecentSearch,health);
        }

    }

    private void getRecipes(final String foodType, String health) {
        final EdmamService edmamService = new EdmamService();
        EdmamService.findRecipes(foodType, health, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("Results Activity", "Failed to make api call");

            }

            @Override
            public void onResponse(Call call, Response response) {
                mRecipes = edmamService.processResults(response);
                RecipeListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecipes.isEmpty()) {
                            Toast.makeText(RecipeListActivity.this,"No results found for "+ foodType +" try another food type",Toast.LENGTH_LONG).show();
                        } else {
                        mAdapter = new RecipeListAdapter(getApplicationContext(), mRecipes);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecipeListActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                        }
                    }
                });

            }
        });
    }
    private void  addToSharedPreferences(String foodType) {
        mEditor.putString(Constants.PREFERENCES_FOOD_TYPE,foodType).apply();
    }

}

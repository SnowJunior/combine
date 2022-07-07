
package com.example.josephinemenge.pika;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.josephinemenge.pika.ui.LoginActivity;
import com.example.josephinemenge.pika.ui.RecipeListActivity;
import com.example.josephinemenge.pika.ui.SavedRecipeListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//    private SharedPreferences mSharedPreferences;
//    private SharedPreferences.Editor mEditor;
    private DatabaseReference mSearchedRecipeReference;
    private DatabaseReference mSearchedHealthReference;
    private ValueEventListener mSearchedRecipeReferenceListener;


    @Bind(R.id.FRecipeButton) Button mFRecipeButton;
    @Bind(R.id.fType) EditText mfType;
    @Bind(R.id.health)
    EditText mHealth;
    @Bind(R.id.SavedRecipes) Button mSavedRecipeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFRecipeButton.setOnClickListener(this);
        Log.d("Find recipe clicked", "Main activity");
//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        mEditor = mSharedPreferences.edit();
        mSearchedRecipeReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_CHILD_SEARCHED_RECIPE);
        mSearchedHealthReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_CHILD_SEARCHED_HEALTH);
        mSavedRecipeButton.setOnClickListener(this);
        mSearchedRecipeReferenceListener = mSearchedRecipeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    String recipe = recipeSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            Log.v("FAILEDtolisten","MainActivity");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mFRecipeButton) {
            String health = mHealth.getText().toString();
            String foodType = mfType.getText().toString();
//            if (!(foodType).equals("")){
//                addToSharedPreferences(foodType);
//            }
            String Health = mHealth.getText().toString();
            Intent intent = new Intent(MainActivity.this, RecipeListActivity.class);
            intent.putExtra("foodType", foodType);
            intent.putExtra("Health", Health);
            saveRecipeToFireBase(foodType);
            saveHealthToFireBase(health);
            if (foodType.equals("")) {
                mfType.setError("Please enter a food type :/");
                return;
            } else {
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Finding Recipe", Toast.LENGTH_LONG).show();
            }
        }
        if (v == mSavedRecipeButton ) {
            Intent intent = new Intent(MainActivity.this, SavedRecipeListActivity.class);
            startActivity(intent);
        }
    }
    private void saveRecipeToFireBase(String foodType) {
        mSearchedRecipeReference.push().setValue(foodType);
    }
    private void saveHealthToFireBase(String Health) {
        mSearchedHealthReference.push().setValue(Health);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchedRecipeReference.removeEventListener(mSearchedRecipeReferenceListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return  super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }

//    private void addToSharedPreferences(String foodType) {
//        mEditor.putString(Constants.PREFERENCES_FOOD_TYPE,foodType).apply();
//    }
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}


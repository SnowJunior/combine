
package com.example.josephinemenge.pika.service;

import android.util.Log;
import android.widget.Toast;

import com.example.josephinemenge.pika.Constants;
import com.example.josephinemenge.pika.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Josephine Menge on 02/06/2017.
 */

public class EdmamService {
    public static void findRecipes(String fType ,String health,Callback callback) {
       OkHttpClient client = new OkHttpClient.Builder().build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.QUERY_PARAMETER,fType);
        urlBuilder.addQueryParameter(Constants.HEALTH_QUERY_PARAMETER,health);
        urlBuilder.addQueryParameter("from","0");
        urlBuilder.addQueryParameter("to","90");
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public ArrayList<Recipe> processResults(Response response) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        try {
            String jsonData = response.body().string();
            if (response.isSuccessful()) {
                JSONObject edmamJSON = new JSONObject(jsonData);
                JSONArray hitsJSON = edmamJSON.getJSONArray("hits");

                if (hitsJSON.length() == 0) {
                Log.d("Service","No results found");
                } else {
                    for (int i = 0; i < hitsJSON.length(); i++) {
                        JSONObject recipeJSON = hitsJSON.getJSONObject(i).getJSONObject("recipe");
                        String name = recipeJSON.getString("label");
                        String imageUrl = recipeJSON.getString("image");
                        int yield = recipeJSON.getInt("yield");
                        String source = recipeJSON.getString("source");
                        ArrayList<String> ingredientLines = new ArrayList<>();
                        JSONArray ingredientJSON = recipeJSON.getJSONArray("ingredients");
                        for (int y = 0; y < ingredientJSON.length(); y++) {
                            ingredientLines.add(ingredientJSON.getJSONObject(y).getString("text"));
                        }
                        String website = recipeJSON.getString("url");
                        Recipe recipe = new Recipe(name, website, source, yield, ingredientLines, imageUrl);
                        recipes.add(recipe);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }
}



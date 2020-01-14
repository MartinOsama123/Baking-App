package com.example.martinosama.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    @BindView(R.id.rvNumbers)  RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    private ArrayList<String> name,ID,serving,ingredients,images;
    private ArrayList<ArrayList<String>> shortDescription,description,videoURL,thumbnailURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if(findViewById(R.id.tab_main) != null){
            recyclerView.setLayoutManager(new GridLayoutManager(this,calculateNoOfColumns(this)));
        }else
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new RecipeQueryTask().execute(NetworkUtils.buildUrl());
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString("INGREDIENTS", ingredients.get(position));
        extras.putStringArrayList("SHORT_DESCRIPTION", shortDescription.get(position));
        extras.putStringArrayList("DESCRIPTION", description.get(position));
        extras.putStringArrayList("VIDEO_URL", videoURL.get(position));
        extras.putStringArrayList("THUMBNAIL_URL", thumbnailURL.get(position));
        intent.putExtras(extras);
        startActivity(intent);
    }

    public class RecipeQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String recipeResults = null;
            try {
                recipeResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return recipeResults;
        }

        @Override
        protected void onPostExecute(String recipeResults) {
            serving = new ArrayList<String>();
            name = new ArrayList<String>();
            ID = new ArrayList<String>();
            ingredients = new ArrayList<String>();
            images = new ArrayList<>();
            shortDescription = new ArrayList<>();
            description = new ArrayList<>();
            videoURL = new ArrayList<>();
            thumbnailURL = new ArrayList<>();
            if (recipeResults != null && !recipeResults.equals("")) {
                try {
                    JSONArray reader = new JSONArray(recipeResults);
                    for(int i = 0;i<reader.length();i++){
                        ID.add(reader.optJSONObject(i).optString("id"));
                        name.add(reader.optJSONObject(i).optString("name"));
                        serving.add(reader.optJSONObject(i).optString("servings"));
                        images.add(reader.optJSONObject(i).optString("image"));
                        String ing = "";
                        for(int j = 0;j<reader.optJSONObject(i).optJSONArray("ingredients").length();j++){
                            ing += reader.optJSONObject(i).optJSONArray("ingredients").optJSONObject(j).optString("quantity")+" ";
                            ing += reader.optJSONObject(i).optJSONArray("ingredients").optJSONObject(j).optString("measure")+" of ";
                            ing += reader.optJSONObject(i).optJSONArray("ingredients").optJSONObject(j).optString("ingredient");
                            ing += "\n";
                        }
                        ingredients.add(ing);
                        ArrayList<String> temp1 = new ArrayList<String>();
                        ArrayList<String> temp2 = new ArrayList<String>();
                        ArrayList<String> temp3 = new ArrayList<String>();
                        ArrayList<String> temp4 = new ArrayList<String>();
                        for(int j = 0;j<reader.optJSONObject(i).optJSONArray("steps").length();j++){
                            temp1.add(reader.optJSONObject(i).optJSONArray("steps").optJSONObject(j).optString("shortDescription"));
                            temp2.add(reader.optJSONObject(i).optJSONArray("steps").optJSONObject(j).optString("description"));
                            temp3.add(reader.optJSONObject(i).optJSONArray("steps").optJSONObject(j).optString("videoURL"));
                            temp4.add(reader.optJSONObject(i).optJSONArray("steps").optJSONObject(j).optString("thumbnailURL"));
                        }
                        shortDescription.add(temp1);
                        description.add(temp2);
                        videoURL.add(temp3);
                        thumbnailURL.add(temp4);
                    }
                    adapter = new MyRecyclerViewAdapter(MainActivity.this,name,serving,images);
                    adapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

}

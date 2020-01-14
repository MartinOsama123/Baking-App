package com.example.martinosama.bakingapp;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


public class DetailActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle extras = getIntent().getExtras();
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(extras);
       FragmentManager fragmentManager = getSupportFragmentManager();
       if(savedInstanceState != null){
           detailFragment = (DetailFragment)fragmentManager.findFragmentByTag("details");
       }else
        fragmentManager.beginTransaction().add(R.id.detailed_recipe,detailFragment,"details").commit();
       }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}

package com.example.martinosama.bakingapp;

import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Bundle extras = getIntent().getExtras();
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(extras);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            stepDetailFragment = (StepDetailFragment)fragmentManager.findFragmentByTag("step");
        } else {
            fragmentManager.beginTransaction().add(R.id.detailed_step, stepDetailFragment,"step").commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}

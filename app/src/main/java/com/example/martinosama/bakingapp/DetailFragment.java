package com.example.martinosama.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment implements MyRecyclerViewAdapter2.ItemClickListener {
    @BindView(R.id.ingred) TextView ing;
    @BindView(R.id.recycle_description) RecyclerView recyclerView;
    MyRecyclerViewAdapter2 adapter;
    Bundle extras;
    View rootView = null;
   public static String temp;
    public DetailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_detail,container,false);
        ButterKnife.bind(this,rootView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        extras = getArguments();
          temp = extras.getString("INGREDIENTS");
        ing.setText(temp);
        adapter = new MyRecyclerViewAdapter2(getContext(), extras.getStringArrayList("SHORT_DESCRIPTION"));
           adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onItemClick(View view, int position) {
        extras.putInt("POSITION",position);
        if(rootView != null && (getActivity().findViewById(R.id.tab_detail) != null)) {
            StepDetailFragment stepDetailFragment= new StepDetailFragment();
            stepDetailFragment.setArguments(extras);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.detailed_step, stepDetailFragment).commit();
        }
        else{
            Intent intent = new Intent(getContext(), StepDetailActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }
}

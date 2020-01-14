package com.example.martinosama.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<String> name,serving,imageList;
    public ItemClickListener mClickListener;

    MyRecyclerViewAdapter(Context context,List<String> name,List<String> serving,List<String> imageList) {
        this.mInflater = LayoutInflater.from(context);
        this.name = name;
        this.serving = serving;
        this.imageList = imageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.recipeTxt.setText(name.get(i));
        holder.servingTxt.setText(serving.get(i));
        if(imageList.get(i).equals("")){
            holder.imageView.setVisibility(View.GONE);
        }else{
            Picasso.get().load(Uri.parse(imageList.get(i))).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return name.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_text_view) TextView recipeTxt;
        @BindView(R.id.serving_text_view) TextView servingTxt;
        @BindView(R.id.recipe_background) CardView cardView;
        @BindView(R.id.imageView) ImageView imageView;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ButterKnife.bind(this,view);
            mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    String getItem(int id) {
        return name.get(id);
    }

    void setClickListener(final ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
package com.example.martinosama.bakingapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyRecyclerViewAdapter2 extends RecyclerView.Adapter<MyRecyclerViewAdapter2.ViewHolder> {

    private LayoutInflater mInflater;
    private List<String> shortDescription;
    public ItemClickListener mClickListener;

    MyRecyclerViewAdapter2(Context context, List<String> shortDescription) {
        this.mInflater = LayoutInflater.from(context);
        this.shortDescription = shortDescription;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.shortText.setText(shortDescription.get(i));
    }

    @Override
    public int getItemCount() {
        return shortDescription.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       @BindView(R.id.short_description_text) TextView shortText;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    String getItem(int id) {
        return shortDescription.get(id);
    }

    void setClickListener(final ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
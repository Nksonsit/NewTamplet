package com.drkeironbrown.lifecoach.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.model.Slideshow;

import java.util.List;

public class SlideshowAdapter extends RecyclerView.Adapter<SlideshowAdapter.SlideshowViewHolder> {
    private List<Slideshow> list;
    private Context context;

    public SlideshowAdapter(Context context, List<Slideshow> list){
        this.context=context;
        this.list=list;
    }


    @NonNull
    @Override
    public SlideshowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slideshow,parent,false);
        return new SlideshowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideshowViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class SlideshowViewHolder extends RecyclerView.ViewHolder {
        public SlideshowViewHolder(View itemView) {
            super(itemView);
        }
    }
}

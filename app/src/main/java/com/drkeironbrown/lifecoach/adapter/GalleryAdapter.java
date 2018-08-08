package com.drkeironbrown.lifecoach.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.model.Gallery;
import com.drkeironbrown.lifecoach.model.Slideshow;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private List<Gallery> list;
    private Context context;

    public GalleryAdapter(Context context, List<Gallery> list){
        this.context=context;
        this.list=list;
    }


    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gallery,parent,false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder {
        public GalleryViewHolder(View itemView) {
            super(itemView);
        }
    }
}

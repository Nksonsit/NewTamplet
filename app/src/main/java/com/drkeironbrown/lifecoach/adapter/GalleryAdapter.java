package com.drkeironbrown.lifecoach.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Gallery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private OnClickItem onClickItem;
    private List<Gallery> list;
    private Context context;

    public GalleryAdapter(Context context, List<Gallery> list, OnClickItem onClickItem) {
        this.context = context;
        this.list = list;
        this.onClickItem = onClickItem;
    }


    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, final int position) {
        holder.txtGalleryName.setText(list.get(position).getGalleryName() + " ( Total image : " + list.get(position).getTotalImage() + " )");
        Functions.loadImage(context, new File(list.get(position).getImages().get(0).getImagePath()), holder.img1, null);
        Functions.loadImage(context, new File(list.get(position).getImages().get(1).getImagePath()), holder.img2, null);
        Functions.loadImage(context, new File(list.get(position).getImages().get(2).getImagePath()), holder.img3, null);

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onDeleteClick(position);
            }
        });
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onEditClick(position);
            }
        });
        holder.imgSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onSeeClick(position);
            }
        });
    }

    public void setDataList(List<Gallery> list) {
        this.list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder {
        private TfTextView txtGalleryName;
        private ImageView img3;
        private ImageView img2;
        private ImageView img1;
        private ImageView imgEdit;
        private ImageView imgDelete;
        private ImageView imgSee;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);
            img1 = (ImageView) itemView.findViewById(R.id.img1);
            img2 = (ImageView) itemView.findViewById(R.id.img2);
            img3 = (ImageView) itemView.findViewById(R.id.img3);
            imgSee = (ImageView) itemView.findViewById(R.id.imgSee);
            txtGalleryName = (TfTextView) itemView.findViewById(R.id.txtGalleryName);
        }
    }

    public interface OnClickItem {
        void onDeleteClick(int position);

        void onEditClick(int position);

        void onSeeClick(int position);
    }
}

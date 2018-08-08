package com.drkeironbrown.lifecoach.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.helper.AppConstant;
import com.drkeironbrown.lifecoach.model.Image;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean isGallery;
    private List<Image> list;
    private Context context;

    public ImageAdapter(Context context, List<Image> list, boolean isGallery) {
        this.context = context;
        this.list = list;
        this.isGallery = isGallery;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_image, viewGroup, false);
            return new ImageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_add_image, viewGroup, false);
            return new AddImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemViewType(int position) {
        if (isGallery) {
            if (list.size() < AppConstant.MAX_GALLERY_IMAGE) {
                if (position == list.size()) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            if (list.size() < AppConstant.MAX_SLIDESHOW_IMAGE) {
                Log.e(""+position,""+list.size());
                if (position == list.size()) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (isGallery) {
            if (list.size() == AppConstant.MAX_GALLERY_IMAGE) {
                return list.size();
            } else {
                return list.size() + 1;
            }
        } else {
            if (list.size() == AppConstant.MAX_SLIDESHOW_IMAGE) {
                return list.size();
            } else {
                return list.size() + 1;
            }
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class AddImageViewHolder extends RecyclerView.ViewHolder {
        public AddImageViewHolder(View itemView) {
            super(itemView);
        }
    }
}

package com.drkeironbrown.lifecoach.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.helper.AppConstant;
import com.drkeironbrown.lifecoach.model.Image;

import java.util.List;

public class ImageAdapterOld extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int width;
    private boolean is4x4;
    private boolean isGallery;
    private List<Image> list;
    private Context context;

    public ImageAdapterOld(Context context, List<Image> list, boolean isGallery, boolean is4x4) {
        this.context = context;
        this.list = list;
        this.isGallery = isGallery;
        this.is4x4 = is4x4;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }
    public void setIs4x4(boolean is4x4){
        this.is4x4 =is4x4;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == 0) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;
            if (is4x4) {
                imageViewHolder.rlItem.setLayoutParams(new RelativeLayout.LayoutParams((width / 4) - 20, (width / 4) - 20));
            }else {
                imageViewHolder.rlItem.setLayoutParams(new RelativeLayout.LayoutParams((width / 5) - 20, (width / 5) - 20));
            }
        }else {
            AddImageViewHolder addImageViewHolder = (AddImageViewHolder) viewHolder;
            if (is4x4) {
                addImageViewHolder.rlAddImage.setLayoutParams(new RelativeLayout.LayoutParams((width / 4) - 20, (width / 4) - 20));
            }else {
                addImageViewHolder.rlAddImage.setLayoutParams(new RelativeLayout.LayoutParams((width / 5) - 20, (width / 5) - 20));
            }
        }
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
                Log.e("" + position, "" + list.size());
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
        private RelativeLayout rlItem;

        public ImageViewHolder(View itemView) {
            super(itemView);
            rlItem = (RelativeLayout) itemView.findViewById(R.id.rlItem);
        }
    }

    public class AddImageViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlAddImage;
        public AddImageViewHolder(View itemView) {
            super(itemView);
            rlAddImage = (RelativeLayout) itemView.findViewById(R.id.rlAddImage);
        }
    }
}

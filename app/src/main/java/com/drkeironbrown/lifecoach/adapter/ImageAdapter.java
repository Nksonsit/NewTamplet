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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.helper.AppConstant;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int width;
    private OnClickItem onClickItem;
    private boolean isGallery;
    private List<Image> list;
    private Context context;

    public ImageAdapter(Context context, List<Image> list, boolean isGallery, OnClickItem onClickItem) {
        this.context = context;
        this.list = list;
        this.onClickItem = onClickItem;
        this.isGallery = isGallery;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == 0) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;
            imageViewHolder.rlItem.setLayoutParams(new RelativeLayout.LayoutParams((width / 3) - 20, (width / 3) - 20));
            Functions.loadImage(context, list.get(position).getImagePath(), imageViewHolder.img, null);
            imageViewHolder.imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyItemRemoved(position);
                }
            });
        } else {
            AddImageViewHolder addImageViewHolder = (AddImageViewHolder) viewHolder;
            addImageViewHolder.rlAddImage.setLayoutParams(new RelativeLayout.LayoutParams((width / 3) - 20, (width / 3) - 20));
            addImageViewHolder.rlAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItem.onAddImage();
                }
            });
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

    public void setDataList(List<Image> list) {
        this.list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgRemove;
        private ImageView img;
        private RelativeLayout rlItem;

        public ImageViewHolder(View itemView) {
            super(itemView);
            rlItem = (RelativeLayout) itemView.findViewById(R.id.rlItem);
            img = (ImageView) itemView.findViewById(R.id.img);
            imgRemove = (ImageView) itemView.findViewById(R.id.imgRemove);
        }
    }

    public class AddImageViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlAddImage;

        public AddImageViewHolder(View itemView) {
            super(itemView);
            rlAddImage = (RelativeLayout) itemView.findViewById(R.id.rlAddImage);
        }
    }

    public interface OnClickItem {
        void onAddImage();
    }
}

package com.drkeironbrown.lifecoach.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Gallery;
import com.drkeironbrown.lifecoach.model.Image;
import com.drkeironbrown.lifecoach.ui.ZoomablImageActivity;

import java.io.File;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ImageVH> {

    private final int width;
    private OnClick onClick;
    private boolean isEditMode;
    private List<Image> list;
    private Context context;
    private boolean is4x4;

    public GridAdapter(Context context, List<Image> list, boolean isEditMode, boolean is4x4, OnClick onClick) {
        this.context = context;
        this.list = list;
        this.is4x4 = is4x4;
        this.isEditMode = isEditMode;
        this.onClick = onClick;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }

    public void setIs4x4(boolean is4x4) {
        this.is4x4 = is4x4;
    }

    @NonNull
    @Override
    public ImageVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, viewGroup, false);
        return new ImageVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageVH imageVH, final int i) {
        if (isEditMode) {
            imageVH.imgRemove.setVisibility(View.VISIBLE);
        } else {
            imageVH.imgRemove.setVisibility(View.GONE);
        }
        Functions.loadImage(context, new File(list.get(i).getImagePath()), imageVH.img, null);
        imageVH.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onRemoveClick(i);
            }
        });
        if (is4x4) {
            imageVH.rlItem.setLayoutParams(new RelativeLayout.LayoutParams((width / 4) - 20, (width / 4) - 20));
        } else {
            imageVH.rlItem.setLayoutParams(new RelativeLayout.LayoutParams((width / 5) - 20, (width / 5) - 20));
        }
        imageVH.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ZoomablImageActivity.class);
                intent.putExtra("image", list.get(i).getImagePath());
                Functions.fireIntent((Activity) context, intent, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageVH extends RecyclerView.ViewHolder {
        private ImageView imgRemove;
        private ImageView img;
        private RelativeLayout rlItem;

        public ImageVH(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            imgRemove = (ImageView) itemView.findViewById(R.id.imgRemove);
            rlItem = (RelativeLayout) itemView.findViewById(R.id.rlItem);
        }
    }

    public interface OnClick {
        void onRemoveClick(int position);
    }
}

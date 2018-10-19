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
import com.drkeironbrown.lifecoach.model.Slideshow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SlideshowAdapter extends RecyclerView.Adapter<SlideshowAdapter.SlideshowViewHolder> {
    private OnClickItem onClickItem;
    private List<Slideshow> list;
    private Context context;

    public SlideshowAdapter(Context context, List<Slideshow> list, OnClickItem onClickItem) {
        this.context = context;
        this.list = list;
        this.onClickItem = onClickItem;
    }


    @NonNull
    @Override
    public SlideshowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slideshow, parent, false);
        return new SlideshowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideshowViewHolder holder, final int position) {

        holder.txtSlideshowName.setText(list.get(position).getSlideshowName() + " ( Total image : " + list.get(position).getTotalImage() + " )");
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
        holder.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onPlayClick(position);
            }
        });
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> imageList = new ArrayList<>();
                for (int i = 0; i < list.get(position).getImages().size(); i++) {
                    imageList.add(list.get(position).getImages().get(i).getImagePath());
                }
                Functions.shareImages(context, list.get(position).getSlideshowName(), imageList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setDataList(List<Slideshow> list) {
        this.list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    public class SlideshowViewHolder extends RecyclerView.ViewHolder {
        private TfTextView txtSlideshowName;
        private ImageView img3;
        private ImageView img2;
        private ImageView img1;
        private ImageView imgEdit;
        private ImageView imgDelete;
        private ImageView imgPlay;
        private ImageView imgShare;

        public SlideshowViewHolder(View itemView) {
            super(itemView);
            imgPlay = (ImageView) itemView.findViewById(R.id.imgPlay);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);
            img1 = (ImageView) itemView.findViewById(R.id.img1);
            img2 = (ImageView) itemView.findViewById(R.id.img2);
            img3 = (ImageView) itemView.findViewById(R.id.img3);
            imgShare = (ImageView) itemView.findViewById(R.id.imgShare);
            txtSlideshowName = (TfTextView) itemView.findViewById(R.id.txtSlideshowName);
        }
    }

    public interface OnClickItem {
        void onDeleteClick(int position);

        void onEditClick(int position);

        void onPlayClick(int position);
    }
}

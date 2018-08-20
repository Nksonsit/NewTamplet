package com.drkeironbrown.lifecoach.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.model.Shop;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopVH> {

    private List<Shop> list;
    private Context context;

    public ShopAdapter(Context context, List<Shop> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ShopVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false);
        return new ShopVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopVH holder, final int position) {
        Glide.with(context).load(list.get(position).getBookImg()).into(holder.imgBook);
        holder.txtBookName.setText(list.get(position).getBookName());
        holder.txtBookPrice.setText(list.get(position).getBookPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getBookUrl()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ShopVH extends RecyclerView.ViewHolder {
        private ImageView imgBook;
        private TfTextView txtBookName;
        private TfTextView txtBookPrice;

        public ShopVH(View itemView) {
            super(itemView);
            imgBook = (ImageView) itemView.findViewById(R.id.imgBook);
            txtBookName = (TfTextView) itemView.findViewById(R.id.txtBookName);
            txtBookPrice = (TfTextView) itemView.findViewById(R.id.txtBookPrice);
        }
    }
}

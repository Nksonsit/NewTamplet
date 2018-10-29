package com.drkeironbrown.lifecoach.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.model.Shop;

import java.util.ArrayList;
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
        Glide.with(context).load("http://drkeironbrown.com/alc/admin/" + list.get(position).getShopImg()).into(holder.imgBook);
        holder.txtBookName.setText(Html.fromHtml(list.get(position).getShopName()));
        holder.txtBookPrice.setText("$" + list.get(position).getShopPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getShopUrl()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setDataList(List<Shop> list) {
        this.list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
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

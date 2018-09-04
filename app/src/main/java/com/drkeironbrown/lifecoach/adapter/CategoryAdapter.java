package com.drkeironbrown.lifecoach.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Category;
import com.drkeironbrown.lifecoach.ui.CategoryDetailActivity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryVH> {

    private List<Category> list;
    private Context context;

    public CategoryAdapter(Context context, List<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, viewGroup, false);
        return new CategoryVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH categoryVH, final int i) {
        categoryVH.txtCategoy.setText(list.get(i).getCategoryName());
        categoryVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryDetailActivity.class);
                intent.putExtra("category", list.get(i));
                Functions.fireIntent(context, intent, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryVH extends RecyclerView.ViewHolder {
        private TfTextView txtCategoy;

        public CategoryVH(View itemView) {
            super(itemView);
            txtCategoy = (TfTextView) itemView.findViewById(R.id.txtCategory);
        }
    }
}

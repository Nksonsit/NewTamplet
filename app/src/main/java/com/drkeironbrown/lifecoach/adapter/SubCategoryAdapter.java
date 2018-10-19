package com.drkeironbrown.lifecoach.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Category;
import com.drkeironbrown.lifecoach.ui.CategoryDetailActivity;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.CategoryVH> {

    private OnSubLinksClick onSubLinksClick;
    private List<String> list;
    private Context context;

    public SubCategoryAdapter(Context context, List<String> list, OnSubLinksClick onSubLinksClick) {
        this.context = context;
        this.list = list;
        this.onSubLinksClick = onSubLinksClick;
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoryVH(LayoutInflater.from(context).inflate(R.layout.item_category, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH categoryVH, final int i) {
        categoryVH.txtCategoy.setText(list.get(i));
        categoryVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Functions.isConnected(context)) {
                    Functions.showToast(context, context.getString(R.string.check_internet), MDToast.TYPE_ERROR);
                    return;
                }
                onSubLinksClick.onSubLinksClick(i);
            }
        });
        categoryVH.txtNote.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryVH extends RecyclerView.ViewHolder {
        private TfTextView txtCategoy, txtNote;

        public CategoryVH(View itemView) {
            super(itemView);
            txtCategoy = (TfTextView) itemView.findViewById(R.id.txtCategory);
            txtNote = (TfTextView) itemView.findViewById(R.id.txtNote);
        }
    }

    public interface OnSubLinksClick {
        void onSubLinksClick(int i);
    }
}

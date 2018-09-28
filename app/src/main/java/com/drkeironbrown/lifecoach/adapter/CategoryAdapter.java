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

import java.util.ArrayList;
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
                if (!Functions.isConnected(context)) {
                    Functions.showToast(context, context.getString(R.string.check_internet), MDToast.TYPE_ERROR);
                    return;
                }
                if (list.get(i).getCategoryPrice() != null && list.get(i).getCategoryPrice().trim().length() > 0 && Integer.parseInt(list.get(i).getCategoryPrice()) > 0) {
                    Functions.showAlertDialogWithTwoOption(context, "Pay", "Cancel", "You need to pay $" + list.get(i).getCategoryPrice() + " to continue.", new Functions.DialogOptionsSelectedListener() {
                        @Override
                        public void onSelect(boolean isYes) {
                            if (isYes) {

                            }
                        }
                    });
                } else {
                    Intent intent = new Intent(context, CategoryDetailActivity.class);
                    intent.putExtra("category", list.get(i));
                    Functions.fireIntent(context, intent, true);
                }
            }
        });
        if (list.get(i).getCategoryPrice() != null && list.get(i).getCategoryPrice().trim().length() > 0 && Integer.parseInt(list.get(i).getCategoryPrice()) > 0) {
            categoryVH.txtNote.setText("$" + list.get(i).getCategoryPrice());
            categoryVH.txtNote.setVisibility(View.VISIBLE);
        } else {
            categoryVH.txtNote.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setDataList(List<Category> data) {
        list = new ArrayList<>();
        list = data;
        notifyDataSetChanged();
    }

    public class CategoryVH extends RecyclerView.ViewHolder {
        private TfTextView txtCategoy, txtNote;

        public CategoryVH(View itemView) {
            super(itemView);
            txtCategoy = (TfTextView) itemView.findViewById(R.id.txtCategory);
            txtNote = (TfTextView) itemView.findViewById(R.id.txtNote);
        }
    }
}
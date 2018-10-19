package com.drkeironbrown.lifecoach.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.AdvancedSpannableString;
import com.drkeironbrown.lifecoach.helper.Functions;

import java.util.List;

public class SubLinksAdapter extends RecyclerView.Adapter<SubLinksAdapter.SubLinksVH> {

    private OnSubLinksClick onSubLinksClick;
    private List<String> list;
    private Context context;

    public SubLinksAdapter(Context context, List<String> list, OnSubLinksClick onSubLinksClick) {
        this.context = context;
        this.list = list;
        this.onSubLinksClick = onSubLinksClick;
    }

    @NonNull
    @Override
    public SubLinksVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SubLinksVH(LayoutInflater.from(context).inflate(R.layout.item_sub_links, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubLinksVH subLinksVH, final int i) {
        AdvancedSpannableString spannableString = new AdvancedSpannableString(list.get(i));
        spannableString.setUnderLine(list.get(i));
        subLinksVH.txtSubLinks.setText(spannableString);
        subLinksVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Functions.isConnected(context)) {
                    Functions.showToast(context, context.getString(R.string.check_internet), MDToast.TYPE_ERROR);
                    return;
                }
                onSubLinksClick.onSubLinksClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SubLinksVH extends RecyclerView.ViewHolder {
        private TfTextView txtSubLinks;

        public SubLinksVH(View itemView) {
            super(itemView);
            txtSubLinks = itemView.findViewById(R.id.txtSubLinks);
        }
    }

    public interface OnSubLinksClick {
        void onSubLinksClick(int i);
    }
}

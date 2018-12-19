package com.drkeironbrown.lifecoach.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.PopupDialog;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Journal;
import com.drkeironbrown.lifecoach.ui.AddJournalActivity;

import java.util.ArrayList;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnDeleteClick onDeleteClick;
    private List<Journal> list;
    private Context context;

    public JournalAdapter(Context context, List<Journal> list, OnDeleteClick onDeleteClick) {
        this.context = context;
        this.list = list;
        this.onDeleteClick = onDeleteClick;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_add_journal, viewGroup, false);
            return new AddJournalViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_journal, viewGroup, false);
            return new JournalViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (getItemViewType(i) == 0) {
            AddJournalViewHolder addJournalViewHolder = (AddJournalViewHolder) viewHolder;
            addJournalViewHolder.txtAddJournal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Functions.fireIntent((Activity) context, AddJournalActivity.class, true);
                }
            });
        } else {
            JournalViewHolder journalViewHolder = (JournalViewHolder) viewHolder;
            journalViewHolder.txtJournal.setText(list.get(i - 1).getJournal());
            journalViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Functions.hideKeyPad(context, v);
                    Functions.showAlertDialogWithTwoOption(context, "YES", "NO", "Are you sure want to delete ?", new PopupDialog.OnPopupClick() {
                        @Override
                        public void onOkClick() {
                            Log.e("list", list.size() + "    " + i);
                            DBOpenHelper.deleteJournal(list.get(i - 1).getJournalId());
                            list.remove(i - 1);
                            notifyItemRemoved(i);
                            new CountDownTimer(1000, 500) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    notifyDataSetChanged();
                                }
                            }.start();
                            Log.e("list", list.size() + "");
                            onDeleteClick.onDeleteClick();
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });
                }
            });

            journalViewHolder.imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Functions.shareSimpleText(context,list.get(i-1).getJournal());
                }
            });

            journalViewHolder.txtJournal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddJournalActivity.class);
                    intent.putExtra("journal", list.get(i - 1));
                    Functions.fireIntent((Activity) context, intent, true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public void setDataList(List<Journal> list) {
        this.list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    public class AddJournalViewHolder extends RecyclerView.ViewHolder {
        private TfTextView txtAddJournal;

        public AddJournalViewHolder(View itemView) {
            super(itemView);
            txtAddJournal = (TfTextView) itemView.findViewById(R.id.txtAddJournal);
        }
    }

    public class JournalViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgDelete;
        private ImageView imgShare;
        private TfTextView txtJournal;

        public JournalViewHolder(View itemView) {
            super(itemView);
            txtJournal = (TfTextView) itemView.findViewById(R.id.txtJournal);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            imgShare = (ImageView) itemView.findViewById(R.id.imgShare);
        }
    }

    public interface OnDeleteClick {
        void onDeleteClick();
    }
}

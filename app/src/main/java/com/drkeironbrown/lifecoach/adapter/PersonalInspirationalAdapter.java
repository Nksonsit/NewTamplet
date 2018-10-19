package com.drkeironbrown.lifecoach.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.TfEditText;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.PersonalInspiration;

import java.util.ArrayList;
import java.util.List;

public class PersonalInspirationalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClick mOnClick;
    private boolean isAddInMode;
    private List<PersonalInspiration> list;
    private Context context;

    public PersonalInspirationalAdapter(Context context, List<PersonalInspiration> list, boolean isAddInMode, OnClick onClick) {
        this.context = context;
        this.list = list;
        this.isAddInMode = isAddInMode;
        mOnClick = onClick;
    }

    public void setAddInMode(boolean isAddInMode) {
        this.isAddInMode = isAddInMode;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isAddInMode && position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_add_personal_inspirational, viewGroup, false);
            return new AddPersonalInspirationVH(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_personal_inspirational, viewGroup, false);
            return new PersonalInspirationVH(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == 0) {
            final AddPersonalInspirationVH addPersonalInspirationVH = (AddPersonalInspirationVH) viewHolder;
            addPersonalInspirationVH.imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Functions.hideKeyPad(context, v);
                    isAddInMode = false;
                    notifyDataSetChanged();
                    mOnClick.onRemoveClick();
                }
            });
            addPersonalInspirationVH.imgSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Functions.hideKeyPad(context, v);
                    if (addPersonalInspirationVH.edtPInspirational.getText().toString().trim().length() == 0) {
                        Functions.showToast(context, "Please add message", MDToast.TYPE_INFO);
                        return;
                    }
                    mOnClick.onSaveClick(addPersonalInspirationVH.edtPInspirational.getText().toString().trim());
                    addPersonalInspirationVH.edtPInspirational.setText("");
                }
            });
        } else {
            int position = 0;
            if (isAddInMode) {
                position = i - 1;
            } else {
                position = i;
            }
            PersonalInspirationVH personalInspirationVH = (PersonalInspirationVH) viewHolder;
            final int finalPosition = position;
            personalInspirationVH.txtPInspirational.setText(list.get(position).getPInspirational());
            personalInspirationVH.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Functions.hideKeyPad(context, v);
                    Functions.showAlertDialogWithTwoOption(context, "YES", "NO", "Are you sure want to delete ?", new Functions.DialogOptionsSelectedListener() {
                        @Override
                        public void onSelect(boolean isYes) {
                            if (isYes) {
                                DBOpenHelper.deletePInspirational(list.get(finalPosition).getPInspirationalId());
                                list.remove(finalPosition);
                                notifyItemRemoved(finalPosition);
                                new CountDownTimer(1000, 500) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        notifyDataSetChanged();
                                    }
                                }.start();
                            }
                        }
                    });

                }
            });
            final int finalPosition1 = position;
            personalInspirationVH.imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Functions.shareSimpleText(context,list.get(finalPosition1).getPInspirational());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (isAddInMode) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    public void setDataList(List<PersonalInspiration> mList) {
        list = new ArrayList<>();
        list = mList;
        notifyDataSetChanged();
    }

    public class AddPersonalInspirationVH extends RecyclerView.ViewHolder {
        private TfEditText edtPInspirational;
        private ImageView imgSave;
        private ImageView imgRemove;

        public AddPersonalInspirationVH(View itemView) {
            super(itemView);
            imgRemove = (ImageView) itemView.findViewById(R.id.imgRemove);
            imgSave = (ImageView) itemView.findViewById(R.id.imgSave);
            edtPInspirational = (TfEditText) itemView.findViewById(R.id.edtPInspirational);
        }
    }

    public class PersonalInspirationVH extends RecyclerView.ViewHolder {
        private TfTextView txtPInspirational;
        private ImageView imgShare;
        private ImageView imgDelete;

        public PersonalInspirationVH(View itemView) {
            super(itemView);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            imgShare = (ImageView) itemView.findViewById(R.id.imgShare);
            txtPInspirational = (TfTextView) itemView.findViewById(R.id.txtPInspirational);
        }
    }

    public interface OnClick {
        void onRemoveClick();

        void onSaveClick(String trim);
    }
}

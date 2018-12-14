package com.drkeironbrown.lifecoach.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PopupDialog extends Dialog {

    private RelativeLayout rlOut;
    private OnPopupClick onPopupClick;
    private CardView msgView;
    private TfButton btnOk;
    private TfButton btnCancel;
    private TfTextView txtMsg;
    private Animation scalIn;
    private Animation scalOut;
    private View view;

    public PopupDialog(@NonNull final Context context, final String msg, String positive, String negative, final OnPopupClick onPopupClick) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_popup, null);
        setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        this.onPopupClick = onPopupClick;

        this.setCanceledOnTouchOutside(false);
        this.setCancelable(true);


        scalIn = AnimationUtils.loadAnimation(context, R.anim.scale_in_dialog);
        scalOut = AnimationUtils.loadAnimation(context, R.anim.scale_out_dialog);

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                view.startAnimation(scalOut);
            }
        });
        scalOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rlOut = (RelativeLayout) view.findViewById(R.id.rlOut);

        rlOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupClick.onCancelClick();
                onBackPressed();
            }
        });

        msgView = (CardView) view.findViewById(R.id.msgView);
        List<Integer> colorList = new ArrayList<>();
        colorList.add(R.color.intro_blue);
        colorList.add(R.color.intro_cyan);
        colorList.add(R.color.intro_green);
        colorList.add(R.color.intro_grey);
        colorList.add(R.color.intro_orange);
        colorList.add(R.color.intro_red);

        Random random = new Random();
        int index = random.nextInt(6);

        msgView.setCardBackgroundColor(ContextCompat.getColor(context, colorList.get(index)));
        btnOk = (TfButton) view.findViewById(R.id.btnOk);
        btnCancel = (TfButton) view.findViewById(R.id.btnCancel);
        txtMsg = (TfTextView) view.findViewById(R.id.txtMsg);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupClick.onOkClick();
                onBackPressed();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupClick.onCancelClick();
                onBackPressed();
            }
        });

        btnOk.setText(positive);
        if (negative != null && negative.trim().length()>0) {
            btnCancel.setText(negative);
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            btnCancel.setVisibility(View.GONE);
        }


        txtMsg.setText(msg);

        show();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            view.startAnimation(scalOut);
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void onBackPressed() {
        view.startAnimation(scalOut);
    }

    public interface OnPopupClick {
        void onOkClick();

        void onCancelClick();
    }

}

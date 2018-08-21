package com.drkeironbrown.lifecoach.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.drkeironbrown.lifecoach.R;

public class MessageDialog extends Dialog {

    private TfTextView txtMsg;
    private ImageView imgClose;
    private Animation scalIn;
    private Animation scalOut;
    private View view;

    public MessageDialog(@NonNull Context context, String msg) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_msg, null);
        setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

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

        imgClose = (ImageView)view.findViewById(R.id.imgClose);
        txtMsg = (TfTextView)view.findViewById(R.id.txtMsg);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
    public void onWindowFocusChanged(boolean hasFocus) {
        if (view != null && scalIn != null) {
            view.startAnimation(scalIn);
        }
    }

    @Override
    public void onBackPressed() {
        view.startAnimation(scalOut);
    }

}

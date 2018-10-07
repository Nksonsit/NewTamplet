package com.drkeironbrown.lifecoach.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.ImageView;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.helper.Functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdDialog extends Dialog {

    private Context mContext;
    private ImageView imgClose;
    private ImageView imgAd;
    private Animation scalIn;
    private Animation scalOut;
    private View view;

    public AdDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_ad, null);
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

        mContext = context;

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

        List<Integer> adImages = new ArrayList<>();
        adImages.add(R.drawable.ad1);
        adImages.add(R.drawable.ad2);
        adImages.add(R.drawable.ad3);

        final List<String> adUrl = new ArrayList<>();
        adUrl.add("http://dontdateapsycho.com");
        adUrl.add("http://dontdateapsycho.com");
        adUrl.add("http://ddapu.dontdateapsycho.com");

        imgAd = (ImageView) view.findViewById(R.id.imgAd);
        imgClose = (ImageView) view.findViewById(R.id.imgClose);

        Random random = new Random();
        final int index = random.nextInt(3);

        imgAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(adUrl.get(index)));
                mContext.startActivity(browserIntent);
                onBackPressed();
            }
        });

        Functions.loadImage(mContext,adUrl.get(index),imgAd,null);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

}

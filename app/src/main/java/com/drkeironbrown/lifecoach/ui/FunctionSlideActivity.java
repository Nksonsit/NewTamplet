package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.fragment.FifthFragment;
import com.drkeironbrown.lifecoach.fragment.FirstFragment;
import com.drkeironbrown.lifecoach.fragment.ForthFragment;
import com.drkeironbrown.lifecoach.fragment.SecondFragment;
import com.drkeironbrown.lifecoach.fragment.SevenFragment;
import com.drkeironbrown.lifecoach.fragment.SixthFragment;
import com.drkeironbrown.lifecoach.fragment.ThirdFragment;
import com.drkeironbrown.lifecoach.helper.Functions;

public class FunctionSlideActivity extends AppCompatActivity {

    private android.widget.LinearLayout container;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtNext;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtSkip;
    private FragmentManager fm;
    private int count = 1;
    private View divider;
    private View bottomSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_slide);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        txtSkip = (TfTextView) findViewById(R.id.txtSkip);
        txtNext = (TfTextView) findViewById(R.id.txtNext);
        bottomSpace = findViewById(R.id.bottomSpace);
        divider = findViewById(R.id.divider);
        container = (LinearLayout) findViewById(R.id.container);

        fm = getSupportFragmentManager();
        pushFragment(new FirstFragment());
        txtSkip.setVisibility(View.GONE);
        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count == 2) {
                    txtSkip.setVisibility(View.VISIBLE);
                    pushFragment(new SecondFragment());
                } else if (count == 3) {
                    pushFragment(new ThirdFragment());
                } else if (count == 4) {
                    pushFragment(new ForthFragment());
                } else if (count == 5) {
                    pushFragment(new FifthFragment());
                } else if (count == 6) {
                    pushFragment(new SixthFragment());
                } else if (count == 7) {
                    txtNext.setText("Done");
                    pushFragment(new SevenFragment());
                } else {
                    Functions.fireIntent(FunctionSlideActivity.this, PayActivity.class, true);
                    finish();
                }
            }
        });
        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(FunctionSlideActivity.this, PayActivity.class, true);
                finish();
            }
        });

        if (Functions.hasSoftKeys(this, getWindowManager())) {
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,102);
            bottomSpace.setLayoutParams(param);
        }

    }

    private void pushFragment(Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        int rightIn = R.anim.slide_in_right;
        int rightOut = R.anim.slide_out_right;
        int leftIn = R.anim.slide_in_left;
        int leftOut = R.anim.slide_out_left;

        ft.setCustomAnimations(rightIn, leftOut);
        ft.replace(R.id.container, fragment);
        ft.commit();
    }
}

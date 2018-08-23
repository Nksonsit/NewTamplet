package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.LinedEditText;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.TfButton;
import com.drkeironbrown.lifecoach.helper.Functions;

public class SecondThoughtActivity extends AppCompatActivity {

    private LinedEditText edtSecondThought;
    private TfButton btnLetItGo;
    //    private ExplosionField mExplosionField;
    private RelativeLayout rrMainView;
    private LinedEditText edtSecondThough2t;
    private Animation fadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_thought);
//        mExplosionField = ExplosionField.attach2Window(this);
        rrMainView = (RelativeLayout) findViewById(R.id.rrMainView);
        edtSecondThought = (LinedEditText) findViewById(R.id.edtSecondThought);
        edtSecondThough2t = (LinedEditText) findViewById(R.id.edtSecondThought2);
        btnLetItGo = (TfButton) findViewById(R.id.btnLetItGo);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        btnLetItGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(SecondThoughtActivity.this, v);
                if (edtSecondThought.getText().toString().trim().length() == 0) {
                    Functions.showToast(SecondThoughtActivity.this, "Please enter your second thought", MDToast.TYPE_INFO);
                    return;
                }

                edtSecondThought.startAnimation(fadeOut);
                edtSecondThough2t.setVisibility(View.VISIBLE);

//                mExplosionField.explode(rrMainView);



                /*new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        onBackPressed();
                    }
                }.start();*/
            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                edtSecondThough2t.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                edtSecondThought.setText("");
                edtSecondThought.setVisibility(View.VISIBLE);
                edtSecondThough2t.setVisibility(View.GONE);
                //onBackPressed();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}

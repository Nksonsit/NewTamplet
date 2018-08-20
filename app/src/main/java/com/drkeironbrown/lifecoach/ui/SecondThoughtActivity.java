package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.LinedEditText;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.TfButton;
import com.drkeironbrown.lifecoach.helper.Functions;

import tyrantgit.explosionfield.ExplosionField;

public class SecondThoughtActivity extends AppCompatActivity {

    private LinedEditText edtSecondThought;
    private TfButton btnLetItGo;
    private ExplosionField mExplosionField;
    private RelativeLayout rrMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_thought);
        mExplosionField = ExplosionField.attach2Window(this);
        rrMainView = (RelativeLayout) findViewById(R.id.rrMainView);
        edtSecondThought = (LinedEditText) findViewById(R.id.edtSecondThought);
        btnLetItGo = (TfButton) findViewById(R.id.btnLetItGo);

        btnLetItGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(SecondThoughtActivity.this, v);
                if (edtSecondThought.getText().toString().trim().length() == 0) {
                    Functions.showToast(SecondThoughtActivity.this, "Please enter your second thought", MDToast.TYPE_INFO);
                    return;
                }
                mExplosionField.explode(rrMainView);

                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        onBackPressed();
                    }
                }.start();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}

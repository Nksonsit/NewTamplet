package com.drkeironbrown.lifecoach.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.LinedEditText;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.TfButton;
import com.drkeironbrown.lifecoach.helper.Functions;

public class SecondThoughtActivity extends AppCompatActivity {

    private LinedEditText edtSecondThought;
    private TfButton btnLetItGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_thought);
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

                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}

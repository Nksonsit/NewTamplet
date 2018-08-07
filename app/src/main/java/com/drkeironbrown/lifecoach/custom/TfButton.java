package com.drkeironbrown.lifecoach.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.drkeironbrown.lifecoach.helper.Functions;


public class TfButton extends AppCompatButton {

    private Context _ctx;

    public TfButton(Context context) {
        super(context);
        if (!isInEditMode()) {
            this._ctx = context;
            init();
        }
    }

    public TfButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            this._ctx = context;
            init();
        }
    }

    private void init() {
        try {
            setTypeface(Functions.getBoldFont(_ctx));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.drkeironbrown.lifecoach.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.drkeironbrown.lifecoach.helper.Functions;


public class TfEditText extends AppCompatEditText {

    private Context _ctx;

    public TfEditText(Context context) {
        super(context);
        if (!isInEditMode()) {
            this._ctx = context;
            init();
        }
    }

    public TfEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            this._ctx = context;
            init();
        }
    }

    private void init() {
        try {
//            setFloatingLabel(FLOATING_LABEL_HIGHLIGHT);
//            setHintTextColor(ContextCompat.getColor(_ctx, R.color.yellow));
//            setPrimaryColor(ContextCompat.getColor(_ctx, R.color.yellow));
//            setAccentTypeface(Functions.getRegularFont(_ctx));
            setTypeface(Functions.getRegularFont(_ctx));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

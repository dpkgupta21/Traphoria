package com.app.traphoria.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by deepak on 16/11/15.
 */

public class MyTextView14 extends TextView {

    public MyTextView14(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextView14(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView14(Context context) {
        super(context);
        init();
    }

    private void init() {
//        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/arcon_regular.otf");
        setTypeface(tf);
        setTextSize(14);
    }

}
package com.app.traphoria.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class MyButtonViewSemi extends Button {

    public MyButtonViewSemi(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyButtonViewSemi(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButtonViewSemi(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/arcon_regular.otf");
        setTypeface(tf);

    }

}
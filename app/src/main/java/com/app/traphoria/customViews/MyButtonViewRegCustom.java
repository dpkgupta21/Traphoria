package com.app.traphoria.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class MyButtonViewRegCustom extends Button {

    public MyButtonViewRegCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyButtonViewRegCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButtonViewRegCustom(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Regular.ttf");
        setTypeface(tf);
       
    }

}
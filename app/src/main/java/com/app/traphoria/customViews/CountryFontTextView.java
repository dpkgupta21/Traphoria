package com.app.traphoria.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class CountryFontTextView extends TextView {

    public CountryFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CountryFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountryFontTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/country_font.ttf");
        setTypeface(tf);
    }

}



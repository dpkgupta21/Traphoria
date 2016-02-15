
package com.app.traphoria.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.app.traphoria.utility.OnSizeChangedListener;

public class ExpandingLayout extends RelativeLayout {


    private OnSizeChangedListener mSizeChangedListener;
    private int mExpandedHeight = -1;

    public ExpandingLayout(Context context) {
        super(context);
    }

    public ExpandingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        if (mExpandedHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mExpandedHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        mExpandedHeight = h;
        //Notifies the list data object corresponding to this layout that its size has changed.
        mSizeChangedListener.onSizeChange(h);
    }

    public int getExpandedHeight() {
        return mExpandedHeight;
    }

    public void setExpandedHeight(int expandedHeight) {
        mExpandedHeight = expandedHeight;
    }

    public void setSizeChangedListener(OnSizeChangedListener listener) {
        mSizeChangedListener = listener;
    }
}

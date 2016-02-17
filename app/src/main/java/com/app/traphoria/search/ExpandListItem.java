package com.app.traphoria.search;

import com.app.traphoria.utility.OnSizeChangedListener;

public class ExpandListItem implements OnSizeChangedListener {

    private String mTitle;
    private String mText;
    private boolean mIsExpand;
    //  private int mCollapsedHeight;
    private int mExpandedHeight;

    public ExpandListItem(String title, int collapsedHeight, String text) {
        mText = text;
        mTitle = title;
        // mCollapsedHeight = collapsedHeight;
        mExpandedHeight = -1;
        mIsExpand = false;
    }

    public boolean isExpand() {
        return mIsExpand;
    }

    public void setExpand(boolean isExpand) {
        mIsExpand = isExpand;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getExpandedHeight() {
        return mExpandedHeight;
    }

    public void setExpandedHeight(int expandedHeight) {
        mExpandedHeight = expandedHeight;
    }

    @Override
    public void onSizeChange(int newHeight) {
        setExpandedHeight(newHeight);
    }

//    public int getCollapsedHeight() {
//        return mCollapsedHeight;
//    }
//
//    public void setCollapsedHeight(int mCollapsedHeight) {
//        this.mCollapsedHeight = mCollapsedHeight;
//    }

}

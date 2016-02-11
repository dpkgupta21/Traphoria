package com.app.traphoria.utility;

import android.view.View;

public interface MyOnClickListener {
    public void onRecyclerClick(View view, int position);

    public void onRecyclerLongClick(View view, int position);

    public void onItemClick(View view, int position);
}

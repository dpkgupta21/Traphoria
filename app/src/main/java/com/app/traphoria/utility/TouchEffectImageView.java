package com.app.traphoria.utility;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class TouchEffectImageView implements OnTouchListener {

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView iv = (ImageView) v;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			iv.setAlpha(150);
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			iv.setAlpha(255);
		}
		return false;
	}

}

package com.app.traphoria.customViews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.traphoria.R;
import com.app.traphoria.search.ExpandListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ExpandViewListView extends ListView {

    private boolean mRemoveObserver = false;
    private List<View> mViewsToDraw = new ArrayList<>();
    private int[] mTranslate;

    public ExpandViewListView(Context context) {
        super(context);
        init();
    }

    public ExpandViewListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpandViewListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnItemClickListener(mOnItemClickListener);
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView
            .OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ExpandListItem viewObject = (ExpandListItem) getItemAtPosition(getPositionForView
                    (view));
            if (!viewObject.isExpand()) {
                expandView(view);
            } else {
                collapseView(view);
            }
        }
    };
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void collapseView(final View view) {
        final ExpandListItem viewObject = (ExpandListItem) getItemAtPosition
                (getPositionForView(view));

        /* Store the original top and bottom bounds of all the cells.*/
        final int oldTop = view.getTop();
        final int oldBottom = view.getBottom();

        final HashMap<View, int[]> oldCoordinates = new HashMap<View, int[]>();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            v.setHasTransientState(true);
            oldCoordinates.put(v, new int[]{v.getTop(), v.getBottom()});
        }

        /* Update the layout so the extra content becomes invisible.*/
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

         /* Add an onPreDraw listener. */
        final ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onPreDraw() {

                if (!mRemoveObserver) {
                    /*Same as for expandingView, the parameters for setSelectionFromTop must
                    * be determined such that the necessary cells of the ListView are rendered
                    * and added to it.*/
                    mRemoveObserver = true;

                    int newTop = view.getTop();
                    int newBottom = view.getBottom();

                    int newHeight = newBottom - newTop;
                    int oldHeight = oldBottom - oldTop;
                    int deltaHeight = oldHeight - newHeight;

                    mTranslate = getTopAndBottomTranslations(oldTop, oldBottom, deltaHeight, false);

                    int currentTop = view.getTop();
                    int futureTop = oldTop + mTranslate[0];

                    int firstChildStartTop = getChildAt(0).getTop();
                    int firstVisiblePosition = getFirstVisiblePosition();
                    int deltaTop = currentTop - futureTop;

                    int i;
                    int childCount = getChildCount();
                    for (i = 0; i < childCount; i++) {
                        View v = getChildAt(i);
                        int height = v.getBottom() - Math.max(0, v.getTop());
                        if (deltaTop - height > 0) {
                            firstVisiblePosition++;
                            deltaTop -= height;
                        } else {
                            break;
                        }
                    }

                    if (i > 0) {
                        firstChildStartTop = 0;
                    }

                    setSelectionFromTop(firstVisiblePosition, firstChildStartTop - deltaTop);

                    requestLayout();

                    return false;
                }

                mRemoveObserver = false;
                observer.removeOnPreDrawListener(this);

                int yTranslateTop = mTranslate[0];
                int yTranslateBottom = mTranslate[1];

                int index = indexOfChild(view);
                int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View v = getChildAt(i);
                    int[] old = oldCoordinates.get(v);
                    if (old != null) {
                        /* If the cell was present in the ListView before the collapse and
                        * after the collapse then the bounds are reset to their old values.*/
                        v.setTop(old[0]);
                        v.setBottom(old[1]);
                        v.setHasTransientState(false);
                    } else {
                        /* If the cell is present in the ListView after the collapse but
                         * not before the collapse then the bounds are calculated using
                         * the bottom and top translation of the collapsing cell.*/
                        int delta = i > index ? yTranslateBottom : -yTranslateTop;
                        v.setTop(v.getTop() + delta);
                        v.setBottom(v.getBottom() + delta);
                    }
                }

                final View expandingLayout = view.findViewById(R.id.expanding_layout);

                /* Animates all the cells present on the screen after the collapse. */
                ArrayList<Animator> animations = new ArrayList<Animator>();
                for (int i = 0; i < childCount; i++) {
                    View v = getChildAt(i);
                    if (v != view) {
                        float diff = i > index ? -yTranslateBottom : yTranslateTop;
                        animations.add(getAnimation(v, diff, diff));
                    }
                }


                /* Adds animation for collapsing the cell that was clicked. */
                animations.add(getAnimation(view, yTranslateTop, -yTranslateBottom));

                /* Adds an animation for fading out the extra content. */
                animations.add(ObjectAnimator.ofFloat(expandingLayout, View.ALPHA, 1, 0));

                /* Disabled the ListView for the duration of the animation.*/
                setEnabled(false);
                setClickable(false);

                /* Play all the animations created above together at the same time. */
                AnimatorSet s = new AnimatorSet();
                s.playTogether(animations);
                s.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        expandingLayout.setVisibility(View.GONE);
                        view.setLayoutParams(new LayoutParams(
                                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        viewObject.setExpand(false);
                        setEnabled(true);
                        setClickable(true);
                        /* Note that alpha must be set back to 1 in case this view is reused
                        * by a cell that was expanded, but not yet collapsed, so its state
                        * should persist in an expanded state with the extra content visible.*/
                        expandingLayout.setAlpha(1);
                    }
                });
                s.start();

                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void expandView(final View view) {
        final ExpandListItem viewObject = (ExpandListItem) getItemAtPosition(getPositionForView
                (view));

    /* Store the original top and bottom bounds of all the cells.*/
        final int oldTop = view.getTop();
        final int oldBottom = view.getBottom();

        final HashMap<View, int[]> oldCoordinates = new HashMap<View, int[]>();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            v.setHasTransientState(true);
            oldCoordinates.put(v, new int[]{v.getTop(), v.getBottom()});
        }

    /* Update the layout so the extra content becomes visible.*/
        final View expandingLayout = view.findViewById(R.id.expanding_layout);
        expandingLayout.setVisibility(View.VISIBLE);

    /* Add an onPreDraw Listener to the listview. onPreDraw will get invoked after onLayout
    * and onMeasure have run but before anything has been drawn. This
    * means that the final post layout properties for all the items have already been
    * determined, but still have not been rendered onto the screen.*/
        final ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                /* Determine if this is the first or second pass.*/
                if (!mRemoveObserver) {
                    mRemoveObserver = true;

                    /* Calculate what the parameters should be for setSelectionFromTop.
                    * The ListView must be offset in a way, such that after the animation
                    * takes place, all the cells that remain visible are rendered completely
                    * by the ListView.*/
                    int newTop = view.getTop();
                    int newBottom = view.getBottom();

                    int newHeight = newBottom - newTop;
                    int oldHeight = oldBottom - oldTop;
                    int delta = newHeight - oldHeight;

                    mTranslate = getTopAndBottomTranslations(oldTop, oldBottom, delta, true);

                    int currentTop = view.getTop();
                    int futureTop = oldTop - mTranslate[0];

                    int firstChildStartTop = getChildAt(0).getTop();
                    int firstVisiblePosition = getFirstVisiblePosition();
                    int deltaTop = currentTop - futureTop;

                    int i;
                    int childCount = getChildCount();
                    for (i = 0; i < childCount; i++) {
                        View v = getChildAt(i);
                        int height = v.getBottom() - Math.max(0, v.getTop());
                        if (deltaTop - height > 0) {
                            firstVisiblePosition++;
                            deltaTop -= height;
                        } else {
                            break;
                        }
                    }

                    if (i > 0) {
                        firstChildStartTop = 0;
                    }

                    setSelectionFromTop(firstVisiblePosition, firstChildStartTop - deltaTop);

                    /* Request another layout to update the layout parameters of the cells.*/
                    requestLayout();

                    /* Return false such that the ListView does not redraw its contents on
                     * this layout but only updates all the parameters associated with its
                     * children.*/
                    return false;
                }

                /* Remove the predraw listener so this method does not keep getting called. */
                mRemoveObserver = false;
                observer.removeOnPreDrawListener(this);

                int yTranslateTop = mTranslate[0];
                int yTranslateBottom = mTranslate[1];

                ArrayList<Animator> animations = new ArrayList<Animator>();

                int index = indexOfChild(view);

                for (View v : oldCoordinates.keySet()) {
                    int[] old = oldCoordinates.get(v);
                    v.setTop(old[0]);
                    v.setBottom(old[1]);
                    if (v.getParent() == null) {
                        mViewsToDraw.add(v);
                        int delta = old[0] < oldTop ? -yTranslateTop : yTranslateBottom;
                        animations.add(getAnimation(v, delta, delta));
                    } else {
                        int i = indexOfChild(v);
                        if (v != view) {
                            int delta = i > index ? yTranslateBottom : -yTranslateTop;
                            animations.add(getAnimation(v, delta, delta));
                        }
                        v.setHasTransientState(false);
                    }
                }

                /* Adds animation for expanding the cell that was clicked. */
                animations.add(getAnimation(view, -yTranslateTop, yTranslateBottom));

                /* Adds an animation for fading in the extra content. */
                animations.add(ObjectAnimator.ofFloat(view.findViewById(R.id.expanding_layout),
                        View.ALPHA, 0, 1));

                /* Disabled the ListView for the duration of the animation.*/
                setEnabled(false);
                setClickable(false);

            /* Play all the animations created above together at the same time. */
                AnimatorSet s = new AnimatorSet();
                s.playTogether(animations);
                s.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewObject.setExpand(true);
                        setEnabled(true);
                        setClickable(true);
                        if (mViewsToDraw.size() > 0) {
                            for (View v : mViewsToDraw) {
                                v.setHasTransientState(false);
                            }
                        }
                        mViewsToDraw.clear();
                    }
                });
                s.start();
                return true;
            }
        });
    }

    private int[] getTopAndBottomTranslations(int top, int bottom, int yDelta,
                                              boolean isExpanding) {
        int yTranslateTop = 0;
        int yTranslateBottom = yDelta;

        int height = bottom - top;

        if (isExpanding) {
            boolean isOverTop = top < 0;
            boolean isBelowBottom = (top + height + yDelta) > getHeight();
            if (isOverTop) {
                yTranslateTop = top;
                yTranslateBottom = yDelta - yTranslateTop;
            } else if (isBelowBottom) {
                int deltaBelow = top + height + yDelta - getHeight();
                yTranslateTop = top - deltaBelow < 0 ? top : deltaBelow;
                yTranslateBottom = yDelta - yTranslateTop;
            }
        } else {
            int offset = computeVerticalScrollOffset();
            int range = computeVerticalScrollRange();
            int extent = computeVerticalScrollExtent();
            int leftoverExtent = range - offset - extent;

            boolean isCollapsingBelowBottom = (yTranslateBottom > leftoverExtent);
            boolean isCellCompletelyDisappearing = bottom - yTranslateBottom < 0;

            if (isCollapsingBelowBottom) {
                yTranslateTop = yTranslateBottom - leftoverExtent;
                yTranslateBottom = yDelta - yTranslateTop;
            } else if (isCellCompletelyDisappearing) {
                yTranslateBottom = bottom;
                yTranslateTop = yDelta - yTranslateBottom;
            }
        }

        return new int[]{yTranslateTop, yTranslateBottom};
    }

    private Animator getAnimation(final View view, float translateTop, float translateBottom) {

        int top = view.getTop();
        int bottom = view.getBottom();

        int endTop = (int) (top + translateTop);
        int endBottom = (int) (bottom + translateBottom);

        PropertyValuesHolder translationTop = PropertyValuesHolder.ofInt("top", top, endTop);
        PropertyValuesHolder translationBottom = PropertyValuesHolder.ofInt("bottom", bottom,
                endBottom);

        return ObjectAnimator.ofPropertyValuesHolder(view, translationTop, translationBottom);
    }
}

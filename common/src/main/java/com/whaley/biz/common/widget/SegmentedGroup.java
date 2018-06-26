package com.whaley.biz.common.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.whaley.biz.common.R;

import java.util.HashMap;

public class SegmentedGroup extends RadioGroup {

    private Resources resources;
    private int mTintColor;
    private int mUnCheckedTintColor;
    private int mCheckedTextColor;
    private int mUnCheckedTextColor;
    private LayoutSelector mLayoutSelector;
    private Float mCornerRadius;
    private OnCheckedChangeListener mCheckedChangeListener;
    private HashMap<Integer, TransitionDrawable> mDrawableMap;
    private int mLastCheckId;

    public SegmentedGroup(Context context) {
        this(context, null);
    }

    public SegmentedGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        resources = getResources();
        init();
        mLayoutSelector = new LayoutSelector(mCornerRadius);
    }

    private void init(){
        mTintColor = resources.getColor(R.color.color1);
        mUnCheckedTintColor = resources.getColor(R.color.color9);
        mCheckedTextColor =  getResources().getColor(R.color.color12);
        mUnCheckedTextColor = getResources().getColor(R.color.color12);
        mCornerRadius = getResources().getDimension(R.dimen.size_0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        updateBackground();
    }

    public void setTextColor(int color){
        mCheckedTextColor = color;
    }

    public void setUnCheckedTextColor(int color){
        mUnCheckedTextColor = color;
    }

    public void setRadius(float radius){
        mCornerRadius=radius;
        mLayoutSelector.setRadius(mCornerRadius);
    }

    public void setTintColor(int color) {
        mTintColor = color;
    }

    public void setUnCheckedTintColor(int color) {
        mUnCheckedTintColor = color;
    }

    public void updateBackground() {
        mDrawableMap = new HashMap<>();
        int count = super.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            updateBackground(child);
            if (i == count - 1) break;
            LayoutParams initParams = (LayoutParams) child.getLayoutParams();
            LayoutParams params = new LayoutParams(initParams.width, initParams.height, initParams.weight);
            child.setLayoutParams(params);
        }
    }

    private void updateBackground(View view) {
        int checked = mLayoutSelector.getSelected();
        int unchecked = mLayoutSelector.getUnselected();
        ColorStateList colorStateList = new ColorStateList(new int[][]{
                {-android.R.attr.state_checked},
                {android.R.attr.state_checked}},
                new int[]{mUnCheckedTextColor, mCheckedTextColor});
        ((Button) view).setTextColor(colorStateList);
        Drawable checkedDrawable = resources.getDrawable(checked).mutate();
        Drawable uncheckedDrawable = resources.getDrawable(unchecked).mutate();
        ((GradientDrawable) checkedDrawable).setColor(mTintColor);
//        ((GradientDrawable) checkedDrawable).setStroke(mMarginDp, mTintColor);
//        ((GradientDrawable) uncheckedDrawable).setStroke(mMarginDp, mTintColor);
        ((GradientDrawable) uncheckedDrawable).setColor(mUnCheckedTintColor);
        ((GradientDrawable) checkedDrawable).setCornerRadii(mLayoutSelector.getChildRadii(view));
        ((GradientDrawable) uncheckedDrawable).setCornerRadii(mLayoutSelector.getChildRadii(view));
//        GradientDrawable maskDrawable = (GradientDrawable) resources.getDrawable(unchecked).mutate();
//        maskDrawable.setStroke(mMarginDp, mTintColor);
//        maskDrawable.setColor(mUnCheckedTintColor);
//        maskDrawable.setCornerRadii(mLayoutSelector.getChildRadii(view));
//        int maskColor = Color.argb(50, Color.red(mTintColor), Color.green(mTintColor), Color.blue(mTintColor));
//        maskDrawable.setColor(maskColor);
//        LayerDrawable pressedDrawable = new LayerDrawable(new Drawable[] {uncheckedDrawable, maskDrawable});
        Drawable[] drawables = {uncheckedDrawable, checkedDrawable};
        TransitionDrawable transitionDrawable = new TransitionDrawable(drawables);
        if (((RadioButton) view).isChecked()) {
            transitionDrawable.reverseTransition(0);
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
//        stateListDrawable.addState(new int[] {-android.R.attr.state_checked, android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(StateSet.WILD_CARD, transitionDrawable);
        mDrawableMap.put(view.getId(), transitionDrawable);
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(stateListDrawable);
        } else {
            view.setBackgroundDrawable(stateListDrawable);
        }
        super.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                TransitionDrawable current = mDrawableMap.get(checkedId);
                current.reverseTransition(0);
                if (mLastCheckId != 0) {
                    TransitionDrawable last = mDrawableMap.get(mLastCheckId);
                    if (last != null) last.reverseTransition(0);
                }
                mLastCheckId = checkedId;

                if (mCheckedChangeListener != null) {
                    mCheckedChangeListener.onCheckedChanged(group, checkedId);
                }
            }
        });
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        mDrawableMap.remove(child.getId());
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mCheckedChangeListener = listener;
    }

    private class LayoutSelector {

        private int children;
        private int child;
        private final int SELECTED_LAYOUT = R.drawable.radio_checked;
        private final int UNSELECTED_LAYOUT = R.drawable.radio_unchecked;

        private float r;
        private final float r1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                , 0.1f, getResources().getDisplayMetrics());
        private float[] rLeft;
        private float[] rRight;
        private float[] rMiddle;
        private float[] rDefault;
        private float[] rTop;
        private float[] rBot;
        private float[] radii;

        public LayoutSelector(float cornerRadius) {
            children = -1;
            child = -1;
            setRadius(cornerRadius);
        }

        public void setRadius(float radius){
            r = radius;
            rLeft = new float[]{r, r, r1, r1, r1, r1, r, r};
            rRight = new float[]{r1, r1, r, r, r, r, r1, r1};
            rMiddle = new float[]{r1, r1, r1, r1, r1, r1, r1, r1};
            rDefault = new float[]{r, r, r, r, r, r, r, r};
            rTop = new float[]{r, r, r, r, r1, r1, r1, r1};
            rBot = new float[]{r1, r1, r1, r1, r, r, r, r};
        }

        private int getChildren() {
            return SegmentedGroup.this.getChildCount();
        }

        private int getChildIndex(View view) {
            return SegmentedGroup.this.indexOfChild(view);
        }

        private void setChildRadii(int newChildren, int newChild) {

            if (children == newChildren && child == newChild)
                return;

            children = newChildren;
            child = newChild;

            if (children == 1) {
                radii = rDefault;
            } else if (child == 0) {
                radii = (getOrientation() == LinearLayout.HORIZONTAL) ? rLeft : rTop;
            } else if (child == children - 1) {
                radii = (getOrientation() == LinearLayout.HORIZONTAL) ? rRight : rBot;
            } else {
                radii = rMiddle;
            }
        }

        public int getSelected() {
            return SELECTED_LAYOUT;
        }

        public int getUnselected() {
            return UNSELECTED_LAYOUT;
        }

        public float[] getChildRadii(View view) {
            int newChildren = getChildren();
            int newChild = getChildIndex(view);
            setChildRadii(newChildren, newChild);
            return radii;
        }
    }
}
package com.whaley.biz.livegift;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.viewpager.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: qxw
 * Date:2017/10/29
 * Introduction:
 */

public class ViewPagerIndicator implements ViewPager.OnPageChangeListener {
    private Context context;
    private ViewPager viewPager;
    private LinearLayout dotLayout;
    private int size;
    private int img1 = R.drawable.bg_page_indicator_focused_shape, img2 = R.drawable.bg_page_indicator_unfocused_shape;
    private int imgSize = DisplayUtil.convertDIP2PX(4);
    private int margin = DisplayUtil.convertDIP2PX(5);
    private List<ImageView> dotViewLists = new ArrayList<>();

    public ViewPagerIndicator(Context context, ViewPager viewPager, LinearLayout dotLayout, int size) {
        this.context = context;
        this.viewPager = viewPager;
        this.dotLayout = dotLayout;
        this.size = size;
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //为小圆点左右添加间距
            params.leftMargin = margin;
            params.rightMargin = margin;
            //手动给小圆点一个大小
            params.height = imgSize;
            params.width = imgSize;
            if (i == 0) {
                imageView.setBackgroundResource(img1);
            } else {
                imageView.setBackgroundResource(img2);
            }
            //为LinearLayout添加ImageView
            dotLayout.addView(imageView, params);
            dotViewLists.add(imageView);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < size; i++) {
            //选中的页面改变小圆点为选中状态，反之为未选中
            if ((position % size) == i) {
                ((View) dotViewLists.get(i)).setBackgroundResource(img1);
            } else {
                ((View) dotViewLists.get(i)).setBackgroundResource(img2);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}

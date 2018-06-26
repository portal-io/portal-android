package com.whaley.biz.livegift.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.livegift.R;

/**
 * Author: qxw
 * Date:2017/10/24
 * Introduction:
 */

public class GiftViewLayout extends FrameLayout {
    ImageView ivAvatar;
    TextView tvName;
    TextView tvText;
    RelativeLayout viewGift;
    ImageView ivGiftImg;
    RelativeLayout viewGiftTemp;
    ImageView ivMultiply;
    RedoubleTextView viewMultiple;

    public GiftViewLayout(@NonNull Context context) {
        this(context, null);
    }

    public GiftViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
//        if (attrs != null) {
//            TypedArray a = context.obtainStyledAttributes(attrs,
//                    R.styleable.GiftViewLayout);
//
//        }
        LayoutInflater.from(context).inflate(R.layout.layout_view_gift, this, true);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvText = (TextView) findViewById(R.id.tv_text);
        viewGift = (RelativeLayout) findViewById(R.id.view_gift);
        ivGiftImg = (ImageView) findViewById(R.id.iv_gift_img);
        viewGiftTemp = (RelativeLayout) findViewById(R.id.view_gift_temp);
        ivMultiply = (ImageView) findViewById(R.id.iv_multiply);
        viewMultiple = (RedoubleTextView) findViewById(R.id.view_multiple);
    }


    public ImageView getIvAvatar() {
        return ivAvatar;
    }

    public ImageView getIvGift() {
        return ivGiftImg;
    }

    public void setText(String text) {
        tvText.setText(text);
    }

    public void setName(String text) {
        tvName.setText(text);
    }

    public void showRedouble(String duplicate) {
        ivMultiply.setVisibility(VISIBLE);
        viewMultiple.setText(duplicate);
    }

    public void hideRedouble() {
        ivMultiply.setVisibility(INVISIBLE);
        viewMultiple.setText("");
    }
}

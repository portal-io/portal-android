package com.whaleyvr.biz.unity.vrone;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snailvr.vrone.R;

/**
 * Created by dell on 2017/12/8.
 */

public class LiveSplitLayout extends SplitLayout {

    ImageButton btnClose;
    View divider;
    TextView tvCount;

    public LiveSplitLayout(Context context) {
        super(context);
    }

    public LiveSplitLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveSplitLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected int getLayoutId(){
        return R.layout.layout_live_split;
    }

    @Override
    protected boolean isLive() {
        return true;
    }

    @Override
    protected void onViewCreated(View view) {
        btnBack = (ImageButton)view.findViewById(R.id.btn_back);
        btnClose = (ImageButton)view.findViewById(R.id.btn_close);
        tvTitle = (TextView)view.findViewById(R.id.tv_title);
        tvToast = (TextView)view.findViewById(R.id.tv_toast);
        btnDefinition = (Button)view.findViewById(R.id.btn_definition);
        btnSplit = (ImageButton)view.findViewById(R.id.btn_split);
        layoutTouch = (FrameLayout)view.findViewById(R.id.layout_touch);
        layoutControl = (FrameLayout)view.findViewById(R.id.layout_control);
        hotSpot = (FrameLayout)view.findViewById(R.id.hot_spot);
        layoutTitle = (RelativeLayout)view.findViewById(R.id.layout_title);
        layoutBottomControls = (RelativeLayout)view.findViewById(R.id.layout_bottom_controls);
        divider = view.findViewById(R.id.divider);
        tvCount = (TextView)view.findViewById(R.id.tv_count);
        line = view.findViewById(R.id.line);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        if(btnClose != null){
            btnClose.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(splitController != null) {
                        splitController.onExitClick();
                    }
                }
            });
        }
    }

    @Override
    public void showToast(String text, String color) {
        super.showToast(text, color);
        divider.setVisibility(GONE);
        tvCount.setVisibility(GONE);
    }

    @Override
    public void clearToast() {
        super.clearToast();
        divider.setVisibility(VISIBLE);
        tvCount.setVisibility(VISIBLE);
    }

    @Override
    public void setPlayCount(String playCount) {
        tvCount.setText(playCount);
    }

    @Override
    public void updateCurrentTimeText(String text) {

    }

    @Override
    public void updateTotalTimeText(String text) {

    }

    @Override
    public void changeSeekMax(int duration) {

    }

    @Override
    public void changeSeekProgress(int duration) {

    }

    @Override
    public void changeWidgetClickable(boolean clickable) {
        setClickable(clickable);
    }

    @Override
    public void changeStartPauseBtn(boolean isStarted) {

    }

    @Override
    public void changeSeekSecondProgress(int secondProgress) {

    }

    @Override
    public void onCompleted() {

    }

}

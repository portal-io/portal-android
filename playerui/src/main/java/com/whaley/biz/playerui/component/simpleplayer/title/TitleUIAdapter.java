package com.whaley.biz.playerui.component.simpleplayer.title;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.whaley.biz.playerui.R;
import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;

/**
 * Created by YangZhi on 2017/8/3 19:45.
 */

public class TitleUIAdapter<T extends TitleController> extends ControlUIAdapter<T> {

    protected TextView tvTitle;
    protected ImageButton btnBack;

    @Override
    public void show(boolean anim) {
        startAnim(0,0,1f);
        tvTitle.setText(tvTitle.getText());
    }

    @Override
    public void hide(boolean anim) {
        startAnim(0,-getRootView().getMeasuredHeight()-((ViewGroup.MarginLayoutParams)getRootView().getLayoutParams()).topMargin,0f);
    }

    public void updateTitleText(String title){
        tvTitle.setText(title);
    }

    @Override
    protected View initView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_controls_title,parent,false);
        return view;
    }

    @Override
    protected void onViewCreated(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btnBack = (ImageButton) view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onBackClick();
            }
        });
        hideTitleText();
    }

    public void showTitleText(){
        tvTitle.setVisibility(View.VISIBLE);
    }

    public void hideTitleText(){
        tvTitle.setVisibility(View.GONE);
    }

}

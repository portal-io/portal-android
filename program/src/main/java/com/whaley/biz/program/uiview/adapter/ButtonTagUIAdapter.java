package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.viewmodel.ButtonTagUIViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date: 2017/3/20
 */

public class ButtonTagUIAdapter extends BaseUIAdapter<ButtonTagUIAdapter.ButtonTagViewHolder, ButtonTagUIViewModel> {
    @Override
    public ButtonTagViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new ButtonTagViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_button_tag, parent, false));
    }
    @Override
    public void onBindViewHolder(ButtonTagViewHolder UIViewHolder, ButtonTagUIViewModel uiViewModel, int position) {
        UIViewHolder.tvLabel.setText(uiViewModel.getName());
    }


    static class ButtonTagViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.tv_label)
        TextView tvLabel;

        ButtonTagViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

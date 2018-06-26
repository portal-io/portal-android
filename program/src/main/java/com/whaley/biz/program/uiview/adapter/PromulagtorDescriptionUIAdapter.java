package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.viewmodel.PromulagtorDescriptionUIViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date: 2017/3/20
 */

public class PromulagtorDescriptionUIAdapter extends BaseUIAdapter<PromulagtorDescriptionUIAdapter.PromulagtorDescriptionViewHolder, PromulagtorDescriptionUIViewModel> {


    @Override
    public PromulagtorDescriptionViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new PromulagtorDescriptionViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_promulagtor_description, parent, false));
    }

    @Override
    public void onBindViewHolder(PromulagtorDescriptionViewHolder UIViewHolder, PromulagtorDescriptionUIViewModel uiViewModel, int position) {
        if (uiViewModel != null) {
            UIViewHolder.tvName.setText(uiViewModel.getName());
            UIViewHolder.tvFollowNum.setText(uiViewModel.getFollowNum());
            UIViewHolder.tvIntroduction.setText(uiViewModel.getIntroduction());
        }
    }

    static class PromulagtorDescriptionViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.tv_name)
        TextView tvName;
        @BindView(R2.id.tv_follow_num)
        TextView tvFollowNum;
        @BindView(R2.id.tv_introduction)
        TextView tvIntroduction;

        public PromulagtorDescriptionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

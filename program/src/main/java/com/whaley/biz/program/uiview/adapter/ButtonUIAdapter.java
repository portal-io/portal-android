package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.viewmodel.ButtonViewUIViewModel;
import com.whaley.core.appcontext.AppContextProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date: 2017/3/17
 */

public class ButtonUIAdapter
        extends BaseUIAdapter<ButtonUIAdapter.ButtonViewHolder, ButtonViewUIViewModel> {

    @Override
    public ButtonViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new ButtonViewHolder(getLayoutInfalterFormParent(parent)
                .inflate(R.layout.item_button, parent, false));
    }

    @Override
    public void onBindViewHolder(ButtonViewHolder viewHolder,
                                 ButtonViewUIViewModel uiViewModel, int position) {
        switch (uiViewModel.getButtonType()) {
            case ButtonViewUIViewModel.TYPE_ENTER:
                viewHolder.tvButton.setText("进入频道");
                viewHolder.tvButton.setTextColor(
                        AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color3));
                viewHolder.ivButton.setImageResource(R.drawable.bg_btn_button_view_selector);
                viewHolder.ivButton.setSelected(true);
                break;
            case ButtonViewUIViewModel.TYPE_BATCH:
                viewHolder.tvButton.setText("换一批");
                viewHolder.tvButton.setTextColor(
                        AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color3));
                viewHolder.ivButton.setImageResource(R.drawable.bg_btn_button_view_selector);
                viewHolder.ivButton.setSelected(false);
                break;
            case ButtonViewUIViewModel.TYPE_EXPAND:
                viewHolder.tvButton.setText("展开更多");
                viewHolder.tvButton.setTextColor(
                        AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color1));
                viewHolder.ivButton.setImageResource(R.drawable.bg_btn_button_view_selector2);
                viewHolder.ivButton.setSelected(true);
                break;
            case ButtonViewUIViewModel.TYPE_PUT_AWAY:
                viewHolder.tvButton.setText("收起");
                viewHolder.tvButton.setTextColor(
                        AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color1));
                viewHolder.ivButton.setImageResource(R.drawable.bg_btn_button_view_selector2);
                viewHolder.ivButton.setSelected(false);
                break;
            default:
                break;
        }
    }

    static class ButtonViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.tv_button)
        TextView tvButton;
        @BindView(R2.id.iv_button)
        ImageView ivButton;

        ButtonViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

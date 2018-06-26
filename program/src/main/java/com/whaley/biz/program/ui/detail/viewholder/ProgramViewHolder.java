package com.whaley.biz.program.ui.detail.viewholder;

import android.view.View;
import android.widget.LinearLayout;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class ProgramViewHolder<T> extends ViewHolder {
    @BindView(R2.id.layout_detail)
    View layoutDetail;
    @BindView(R2.id.layout_downlod)
    LinearLayout layoutDownlod;
    @BindView(R2.id.layout_collect)
    LinearLayout layoutCollect;
    @BindView(R2.id.layout_share)
    LinearLayout layoutShare;
    @BindView(R2.id.layout_buttons)
    LinearLayout layoutButtons;


    ButtonsClickListener buttonsClickListener;

    T data;

    public ProgramViewHolder(View view) {
        super(view);
    }

    public LinearLayout getLayoutCollect() {
        return layoutCollect;
    }

    public void show() {
        layoutDetail.setVisibility(View.VISIBLE);
    }

    public void hide() {
        layoutDetail.setVisibility(View.GONE);
    }

    @OnClick(R2.id.layout_downlod)
    public void onDownloadClick() {
        if (buttonsClickListener != null)
            buttonsClickListener.onDownloadClick();
    }

    @OnClick(R2.id.layout_share)
    public void onShareClick() {
        if (buttonsClickListener != null)
            buttonsClickListener.onShareClick();
    }

    @OnClick(R2.id.layout_collect)
    public void onCollectClick() {
        if (buttonsClickListener != null)
            buttonsClickListener.onCollectionClick();
    }


    public void setButtonsClickListener(ButtonsClickListener buttonsClickListener) {
        this.buttonsClickListener = buttonsClickListener;
    }

    public void bindData(T data) {
        this.data = data;
        onBindData(data);
    }

    public T getData() {
        return data;
    }

    protected abstract void onBindData(T data);

    public interface ButtonsClickListener {
        void onCollectionClick();

        void onDownloadClick();

        void onShareClick();
    }

}
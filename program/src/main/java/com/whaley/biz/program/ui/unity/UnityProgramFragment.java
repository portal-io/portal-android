package com.whaley.biz.program.ui.unity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.biz.program.ui.unity.presenter.UnityProgramPresenter;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.StrUtil;

import butterknife.BindView;

/**
 * Created by dell on 2017/8/30.
 */

@Route(path = ProgramRouterPath.UNITY_PROGRAM)
public class UnityProgramFragment extends BaseMVPFragment<UnityProgramPresenter> implements UnityProgramView {

    @BindView(R2.id.tv_program_name)
    TextView tvProgramName;@BindView(R2.id.tv_play_count)
    TextView tvPlayCount;
    @BindView(R2.id.iv_poster_img)
    ImageView ivPosterImg;
    @BindView(R2.id.tv_poster_name)
    TextView tvPosterName;
    @BindView(R2.id.tv_poster_fans)
    TextView tvPosterFans;
    @BindView(R2.id.iv_pic)
    ImageView ivPic;
    @BindView(R2.id.btn_pay)
    TextView btnPay;

    ImageRequest.RequestManager requestManager;

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        requestManager = ImageLoader.with(this);
        super.setViews(view, savedInstanceState);
        ProgramDetailModel programDetailModel = getPresenter().getRepository().getProgramDetailModel();
        if (!StrUtil.isEmpty(getPresenter().getRepository().getCouponPackModel().price)) {
            btnPay.setText(getPresenter().getRepository().getCouponPackModel().price);
        }
        btnPay.setVisibility(View.VISIBLE);
        requestManager.load(programDetailModel.getBigPic()).medium().placeholder(R.mipmap.default_6).centerCrop().into(ivPic);
        tvProgramName.setText(programDetailModel.getDisplayName());
        tvPosterName.setText(programDetailModel.getName());
        tvPosterFans.setText(String.valueOf(programDetailModel.getFansCount()) + "粉丝");
        tvPlayCount.setText(getPresenter().getRepository().getAmountStr());
        requestManager.load(programDetailModel.getHeadPic()).centerCrop().circle().into(ivPosterImg);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_unity_pay_program;
    }

}

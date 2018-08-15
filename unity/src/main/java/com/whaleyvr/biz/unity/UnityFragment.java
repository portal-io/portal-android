package com.whaleyvr.biz.unity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.snailvr.vrone.R;
import com.snailvr.vrone.R2;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.core.widget.imageview.FrameAnimImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/16.
 */

@Route(path = "/unity/ui/unity")
public class UnityFragment extends BaseMVPFragment<UnityPresenter> implements UnityView {

    public static final int[] LOGO_ANIM_RES_ARR = new int[]{
            R.mipmap.cardboard_00000, R.mipmap.cardboard_00002, R.mipmap.cardboard_00004,
            R.mipmap.cardboard_00006, R.mipmap.cardboard_00008, R.mipmap.cardboard_00010,
            R.mipmap.cardboard_00012, R.mipmap.cardboard_00014, R.mipmap.cardboard_00016,
            R.mipmap.cardboard_00018, R.mipmap.cardboard_00020, R.mipmap.cardboard_00022,
            R.mipmap.cardboard_00024, R.mipmap.cardboard_00026, R.mipmap.cardboard_00028,
            R.mipmap.cardboard_00030, R.mipmap.cardboard_00032, R.mipmap.cardboard_00034,
            R.mipmap.cardboard_00036, R.mipmap.cardboard_00038, R.mipmap.cardboard_00040,
            R.mipmap.cardboard_00042, R.mipmap.cardboard_00044, R.mipmap.cardboard_00046,
            R.mipmap.cardboard_00048, R.mipmap.cardboard_00050, R.mipmap.cardboard_00052,
            R.mipmap.cardboard_00054, R.mipmap.cardboard_00056, R.mipmap.cardboard_00058,
            R.mipmap.cardboard_00060, R.mipmap.cardboard_00062, R.mipmap.cardboard_00064,
            R.mipmap.cardboard_00066, R.mipmap.cardboard_00068, R.mipmap.cardboard_00070,
            R.mipmap.cardboard_00072, R.mipmap.cardboard_00074, R.mipmap.cardboard_00076,
            R.mipmap.cardboard_00078};

    @BindView(R2.id.iv_pic)
    FrameAnimImageView ivPic;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_unity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().initSersorEventListener();
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if(getTitleBar()!=null){
            getTitleBar().setTitleText("VR模式");
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
        ivPic.setImageRes(LOGO_ANIM_RES_ARR);
        ivPic.setSwitchDuration(40);
        ivPic.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ivPic!=null) {
            ivPic.stop();
        }
        getPresenter().destroytSersorEventListener();
    }

    @OnClick(R2.id.btn_enter)
    public void onClick() {
        getPresenter().enterUnity();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().startSensorListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPresenter().stopSensorListener();
    }

}

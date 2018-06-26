package com.whaley.biz.program.ui.unity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.widget.BaseViewHolder;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.arrange.BaseTopicFragment;
import com.whaley.biz.program.ui.arrange.PackageFragment;
import com.whaley.biz.program.ui.arrange.PackageView;
import com.whaley.biz.program.ui.unity.presenter.UnityPackagePresenter;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;

import butterknife.BindView;

/**
 * Created by dell on 2017/8/30.
 */

@Route(path = ProgramRouterPath.UNITY_PACKAGE)
public class UnityPackageFragment extends BaseTopicFragment<UnityPackagePresenter> implements PackageView {

    View viewBuy;
    UnityPackageFragment.BuyViewHolder vrViewHolder;

    @Override
    public void updata(RecyclerViewModel recyclerViewModel) {
        updatePayView();
        super.updata(recyclerViewModel);
    }

    public void updatePayView() {
        if (getPresenter().isShowPayBotton()) {
            if (viewBuy == null) {
                viewBuy = vsBuy.inflate();
            }
            pullBehavior.setShowPlayer(false);
            viewBuy.setVisibility(View.VISIBLE);
            vrViewHolder = new UnityPackageFragment.BuyViewHolder(viewBuy);
            vrViewHolder.tvBuy.setText(getPresenter().getPayString());
        }else {
            if (viewBuy != null) {
                viewBuy.setVisibility(View.GONE);
            }
        }
    }

    static class BuyViewHolder extends BaseViewHolder {
        @BindView(R2.id.buy_set)
        FrameLayout buySet;
        @BindView(R2.id.tv_buy)
        TextView tvBuy;

        public BuyViewHolder(View view) {
            super(view);
        }

    }
}


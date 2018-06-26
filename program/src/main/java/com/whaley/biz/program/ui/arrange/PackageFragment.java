package com.whaley.biz.program.ui.arrange;

import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.widget.BaseViewHolder;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.arrange.presenter.PackagePresenter;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;

import butterknife.BindView;

/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:
 */
@Route(path = ProgramRouterPath.PACKAGE)
public class PackageFragment extends BaseTopicFragment<PackagePresenter> implements PackageView {

    View viewBuy;
    BuyViewHolder vrViewHolder;

    @Override
    public void updata(RecyclerViewModel recyclerViewModel) {
        updatePayView();
        super.updata(recyclerViewModel);
    }

    @Override
    public void updatePayView() {
        if (getPresenter().isShowPayBotton()) {
            if (viewBuy == null) {
                viewBuy = vsBuy.inflate();
            }
            viewBuy.setVisibility(View.VISIBLE);
            vrViewHolder = new BuyViewHolder(viewBuy);
            vrViewHolder.tvBuy.setText(getPresenter().getPayString());
            vrViewHolder.tvBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().onPayClick();
                }
            });
        } else {
            if (viewBuy != null) {
                viewBuy.setVisibility(View.GONE);
            }
//            if (getPresenter().getViewData().isHaveTopic()) {
//                pullBehavior.setShowPlayer(false);
//            } else {
//                pullBehavior.setShowPlayer(true);
//            }
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

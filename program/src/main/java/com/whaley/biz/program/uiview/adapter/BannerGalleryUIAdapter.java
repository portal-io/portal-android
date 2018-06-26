package com.whaley.biz.program.uiview.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.OnClickableUIViewClickListener;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.VisibleSwitcher;
import com.whaley.biz.program.uiview.viewmodel.BannerGalleryUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.BannerItemUIViewModel;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.banner.BannerAdapter;
import com.whaley.core.widget.banner.BannerViewHolder;
import com.whaley.core.widget.banner.WhaleyBanner;
import com.whaley.core.widget.viewpager.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2017/8/23.
 */

public class BannerGalleryUIAdapter extends BaseUIAdapter<BannerGalleryUIAdapter.ViewHolder, BannerGalleryUIViewModel> implements ImageloaderUser,VisibleSwitcher {

    ImageRequest.RequestManager requestManager;


    boolean isVisible;

    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public BannerGalleryUIAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new BannerGalleryUIAdapter.ViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_live_recommend_banner_layout, parent, false));
    }


    @Override
    public void changeVisible(boolean isVisible) {
        getViewHolder().adapter.changeVisible(isVisible);
    }


    @Override
    public void onAttach() {
        super.onAttach();
        getViewHolder().adapter.attach();
    }

    @Override
    public void onDettatch() {
        super.onDettatch();
        getViewHolder().adapter.dettach();
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        getViewHolder().adapter.onRecycled();
        requestManager = null;
    }

    @Override
    public void onBindViewHolder(final BannerGalleryUIAdapter.ViewHolder UIViewHolder, final BannerGalleryUIViewModel uiViewModel, int position) {
        UIViewHolder.adapter.setRealContext(getRealContext());
        UIViewHolder.adapter.setRequestManager(requestManager);
        UIViewHolder.banner.setBannerListener(new BannerAdapter.BannerListener() {
            @Override
            public void onBannerSelected(final BannerViewHolder bannerViewHolder, final int position) {
                if (uiViewModel.getBannerItemUIViewModels() != null && uiViewModel.getBannerItemUIViewModels().size() > position) {
                    BannerItemUIViewModel bannerItemViewData = uiViewModel.getBannerItemUIViewModels().get(position);
                    UIViewHolder.tvTitle.setText(bannerItemViewData.getTitle());
                    UIViewHolder.tvDes.setText(bannerItemViewData.getSubTitle());
                    View.OnClickListener onClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getOnUIViewClickListener() == null) {
                                return;
                            }
                            getOnUIViewClickListener().onViewClick((ClickableUIViewHolder) bannerViewHolder);
                        }
                    };
                    UIViewHolder.tvTitle.setOnClickListener(onClickListener);
                    UIViewHolder.tvDes.setOnClickListener(onClickListener);
                }
            }

            @Override
            public void onBannerItemClick(BannerViewHolder bannerViewHolder, int i) {
                if (getOnUIViewClickListener() == null) {
                    return;
                }
                getOnUIViewClickListener().onViewClick((ClickableUIViewHolder) bannerViewHolder);
            }
        });
        if(UIViewHolder.lastBindedModel==null||UIViewHolder.lastBindedModel != uiViewModel) {
            UIViewHolder.banner.setData(uiViewModel.getBannerItemUIViewModels());
            UIViewHolder.lastBindedModel = uiViewModel;
        }else {
            UIViewHolder.adapter.loadImages();
        }
//        UIViewHolder.adapter.setData(uiViewModel.getBannerItemUIViewModels());
    }

    static class ViewHolder extends ClickableSimpleViewHolder {

        @BindView(R2.id.banner)
        public WhaleyBanner banner;
        @BindView(R2.id.tv_title)
        TextView tvTitle;
        @BindView(R2.id.tv_des)
        TextView tvDes;

        public LiveRecommendBannerAdapter adapter;

        public BannerGalleryUIViewModel lastBindedModel;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            banner.getViewPager().setClipChildren(false);
            banner.getViewPager().setClipToPadding(false);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) banner.getViewPager().getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.rightMargin = DisplayUtil.convertDIP2PX(10);
            layoutParams.leftMargin = DisplayUtil.convertDIP2PX(10);
            banner.getViewPager().requestLayout();
            banner.getViewPager().setPageMargin(DisplayUtil.convertDIP2PX(3));
            adapter = new LiveRecommendBannerAdapter();
            banner.setIndicatorEnable(false)
                    .setAdapter(adapter);
        }
    }

}



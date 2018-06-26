package com.whaley.biz.program.uiview.adapter;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.VisibleSwitcher;
import com.whaley.biz.program.uiview.viewmodel.BannerImgLoopViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.BannerImgViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.widget.ActivityBannerView;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.banner.BannerAdapter;
import com.whaley.core.widget.banner.BannerViewHolder;
import com.whaley.core.widget.banner.WhaleyBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date:2018/1/29
 * Introduction:
 */

public class ActivityBannerLmgLoopUIAdapter extends BaseUIAdapter<ActivityBannerLmgLoopUIAdapter.BannerImgLoopViewHolder, BannerImgLoopViewUIViewModel> implements ImageloaderUser, VisibleSwitcher {
    static final int DEFAULT_HEIGHT = DisplayUtil.convertDIP2PX(187);

    BannerImgLoopViewUIViewModel oldDatas;
    ImageRequest.RequestManager requestManager;
    boolean isVisible;

    @Override
    public BannerImgLoopViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new BannerImgLoopViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_activity_banner_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(BannerImgLoopViewHolder UIViewHolder, BannerImgLoopViewUIViewModel uiViewModel, int i) {
        UIViewHolder.bannerImageAdapter.setRealContext(getRealContext());
        UIViewHolder.bannerImageAdapter.setRequestManager(requestManager);
        UIViewHolder.bannerImageAdapter.setName(UIViewHolder.viewBanner.getTextView());
        int height = DEFAULT_HEIGHT;
        if (uiViewModel.getHeight() > 0) {
            height = uiViewModel.getHeight();
        }
        ViewGroup.LayoutParams layoutParams = UIViewHolder.viewBanner.getLayoutParams();
        if (layoutParams.height != height) {
            layoutParams.height = height;
            UIViewHolder.viewBanner.requestLayout();
        }
        if (oldDatas == null || oldDatas != uiViewModel) {
            int page_indicator_unfocused = uiViewModel.getIndicator_unfocused() > 0 ? uiViewModel.getIndicator_unfocused() : R.mipmap.page_indicator_unfocused;
            int page_indicator_focused = uiViewModel.getIndicator_focused() > 0 ? uiViewModel.getIndicator_focused() : R.mipmap.page_indicator_focused;
            int gravity;
            switch (uiViewModel.getAlign()) {
                case BannerImgLoopViewUIViewModel.ALIGN_PARENT_LEFT:
                    gravity = Gravity.LEFT | Gravity.BOTTOM;
                    break;
                case BannerImgLoopViewUIViewModel.CENTER_HORIZONTAL:
                    gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
                    break;
                default:
                    gravity = Gravity.RIGHT | Gravity.BOTTOM;
                    break;
            }
            UIViewHolder.viewBanner
                    .setIndicatorEnable(true)
                    .setNormalIndicator(page_indicator_unfocused)
                    .setSelectedIndicator(page_indicator_focused)
                    .setIndicatorGravity(gravity)
                    .setBannerListener(new BannerAdapter.BannerListener() {
                        @Override
                        public void onBannerSelected(BannerViewHolder bannerViewHolder, int i) {

                        }

                        @Override
                        public void onBannerItemClick(BannerViewHolder bannerViewHolder, int i) {
                            ActivityBannerLmgLoopUIAdapter.BannerImageViewHodler holder = (ActivityBannerLmgLoopUIAdapter.BannerImageViewHodler) bannerViewHolder;
                            if (getOnUIViewClickListener() != null)
                                getOnUIViewClickListener().onViewClick(holder);
                        }
                    })
                    .setAutoChange(true)
                    .setAutoChangeDuration(2000)
                    .setData(uiViewModel.getBannerImgViewModelList());
            oldDatas = uiViewModel;
        } else {
            UIViewHolder.bannerImageAdapter.loadImages();
        }
        changeVisible(true);
    }

    @Override
    public void changeVisible(boolean isVisible) {
        if (this.isVisible == isVisible)
            return;
        this.isVisible = isVisible;
        if (isVisible) {
            getViewHolder().viewBanner.startAutoChange();
        } else {
            getViewHolder().viewBanner.stopAutoChange();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        oldDatas = null;
    }

    @Override
    public void onDettatch() {
        super.onDettatch();
        changeVisible(false);
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        getViewHolder().bannerImageAdapter.onRecycle();
        requestManager = null;
    }

    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }


    public class BannerImageAdapter extends BannerAdapter<BannerImgViewUIViewModel, ActivityBannerLmgLoopUIAdapter.BannerImageViewHodler> {
        ImageRequest.RequestManager requestManager;

        List<ActivityBannerLmgLoopUIAdapter.BannerImageViewHodler> bindedViewholders = new ArrayList<>();

        Context realContext;
        TextView name;

        public BannerImageAdapter() {
            super(true);
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public void setRealContext(Context realContext) {
            this.realContext = realContext;
        }

        public Context getRealContext() {
            return realContext == null ? AppContextProvider.getInstance().getContext() : realContext;
        }

        public void setRequestManager(ImageRequest.RequestManager requestManager) {
            this.requestManager = requestManager;
        }

        @Override
        public ActivityBannerLmgLoopUIAdapter.BannerImageViewHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            MutableContextWrapper contextWrapper = new MutableContextWrapper(viewGroup.getContext());
            View view = LayoutInflater.from(contextWrapper).cloneInContext(contextWrapper).inflate(
                    R.layout.layout_banner_view, viewGroup, false);
            return new ActivityBannerLmgLoopUIAdapter.BannerImageViewHodler(view);
        }

        @Override
        public void onRecycleViewHolder(@NonNull ActivityBannerLmgLoopUIAdapter.BannerImageViewHodler holder) {
            super.onRecycleViewHolder(holder);
            bindedViewholders.remove(holder);
            holder.recycled();
        }

        @Override
        protected void onBindViewHolder(@NonNull ActivityBannerLmgLoopUIAdapter.BannerImageViewHodler bannerViewHolder, BannerImgViewUIViewModel bannerImgViewUIViewModel, int i) {
            MutableContextWrapper contextWrapper = (MutableContextWrapper) bannerViewHolder.getRootView().getContext();
            contextWrapper.setBaseContext(getRealContext());
            name.setText(bannerImgViewUIViewModel.getName());
            if (requestManager != null) {
                requestManager.load(bannerImgViewUIViewModel.getImageUrl()).big().fitCenter().into(bannerViewHolder.imageView);
            }
            bindedViewholders.add(bannerViewHolder);
        }

        public void loadImages() {
            for (ActivityBannerLmgLoopUIAdapter.BannerImageViewHodler viewHodler : bindedViewholders) {
                BannerImgViewUIViewModel bannerImgViewUIViewModel = viewHodler.getBindModel();
                requestManager.load(bannerImgViewUIViewModel.getImageUrl()).big().fitCenter().into(viewHodler.imageView);
            }
        }

        public void onRecycle() {
            setRealContext(null);
            setRequestManager(null);
            for (ActivityBannerLmgLoopUIAdapter.BannerImageViewHodler viewHodler : bindedViewholders) {
                viewHodler.recycled();
            }
        }
    }


    /**
     * BannerView的适配器
     */
    public static class BannerImageViewHodler extends BannerViewHolder implements ClickableUIViewHolder {
        private ImageView imageView;

        public BannerImageViewHodler(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.iv_banner);
        }

        @Override
        public ClickableUIViewModel getData() {
            return getBindModel();
        }

        @Override
        public void bindData(com.whaley.core.widget.uiview.UIViewModel uiViewModel) {
            bindModel(uiViewModel);
        }

        @Override
        public void setViewType(int i) {

        }

        @Override
        public int getViewType() {
            return getType();
        }

        @Override
        public void setInited() {

        }

        @Override
        public boolean isInited() {
            return true;
        }

        @Override
        public <R extends View> R getRootView() {
            return (R) getItemView();
        }

        public void recycled() {
            MutableContextWrapper contextWrapper = (MutableContextWrapper) getRootView().getContext();
            contextWrapper.setBaseContext(AppContextProvider.getInstance().getContext());
//            ImageLoader.with(AppContextProvider.getInstance().getContext())
//                    .load(R.mipmap.default_6)
//                    .into(imageView);
            ImageLoader.clearView(imageView);
            imageView.setImageResource(0);
        }
    }

    public class BannerImgLoopViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.view_banner)
        ActivityBannerView viewBanner;
        ActivityBannerLmgLoopUIAdapter.BannerImageAdapter bannerImageAdapter;

        public BannerImgLoopViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            bannerImageAdapter = new ActivityBannerLmgLoopUIAdapter.BannerImageAdapter();
            viewBanner.setAdapter(bannerImageAdapter);
        }
    }
}

package com.whaley.biz.program.uiview.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.request.Request;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.playersupport.component.bannerplayer.BannerTargetController;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.viewmodel.BannerItemUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.widget.LayoutListenerView;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.banner.BannerAdapter;
import com.whaley.core.widget.banner.BannerViewHolder;
import com.whaley.core.widget.uiview.UIViewModel;
import com.whaley.core.widget.viewholder.AbsViewHolder;
import com.whaley.core.widget.viewpager.RecyclerPagerAdapter;
import com.whaley.core.widget.viewpager.ViewPager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YangZhi on 2017/2/16 15:09.
 */

public class LiveRecommendBannerAdapter extends BannerAdapter<BannerItemUIViewModel, LiveRecommendBannerAdapter.BannerItemViewHolder> {

    private static final int BANNER_PLAYER_WIDTH = DisplayUtil.screenW - 2 * DisplayUtil.convertDIP2PX(10);

    private static final int BANNER_PLAYER_HEIGHT = DisplayUtil.convertDIP2PX(184);

    private List<BannerItemUIViewModel> bannerItemViewDatas;

    private ImageRequest.RequestManager requestManager;

    private boolean isVisible;

    private boolean isAttached;

    BannerItemViewHolder currentViewHolder;


    BannerListener bannerListener;

    List<BannerItemViewHolder> bindedViewHolders = new ArrayList<>();

    Context realContext;

    public LiveRecommendBannerAdapter() {
        super(true);
    }

    public void setRealContext(Context realContext) {
        this.realContext = realContext;
    }

    @Override
    public void setBannerListener(BannerListener bannerListener) {
        super.setBannerListener(bannerListener);
        this.bannerListener = bannerListener;
    }


    public void changeVisible(boolean isVisible) {
        this.isVisible = isVisible;
        if (currentViewHolder != null) {
            currentViewHolder.changeVisible(isVisible);
        }
    }

    public void attach() {
        isAttached = true;
        if (currentViewHolder != null) {
            currentViewHolder.attach();
        }
    }

    public void dettach() {
        isAttached = false;
        if (currentViewHolder != null) {
            currentViewHolder.dettach();
        }
    }

    public void onRecycled() {
        setRealContext(null);
        setBannerListener(null);
        setRequestManager(null);
        if (bindedViewHolders.size() > 0) {
            Iterator<BannerItemViewHolder> iterator = bindedViewHolders.iterator();
            while (iterator.hasNext()) {
                BannerItemViewHolder viewHolder = iterator.next();
                viewHolder.recycled();
            }
        }
    }

    public BannerItemUIViewModel getItemData(int position) {
        if (this.bannerItemViewDatas != null && this.bannerItemViewDatas.size() > position) {
            BannerItemUIViewModel data = this.bannerItemViewDatas.get(position);
            return data;
        }
        return null;
    }

    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    protected void setItemClick(BannerItemViewHolder holder) {
//        super.setItemClick(holder);
    }

    @Override
    protected void onBindViewHolder(@NonNull final BannerItemViewHolder viewHolder, BannerItemUIViewModel data, final int position) {
        Context context = viewHolder.getRootView().getContext();
        if (context instanceof MutableContextWrapper) {
            MutableContextWrapper contextWrapper = (MutableContextWrapper) context;
            contextWrapper.setBaseContext(realContext);
        }
        loadImage(viewHolder, data);
        viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bannerListener == null) {
                    return;
                }
                bannerListener.onBannerItemClick(viewHolder, position);
            }
        });

        if (data.getBannerPlayerViewModel() != null) {
            viewHolder.bannerTargetController.setPlayData(data.getBannerPlayerViewModel().getRecommendModel().getPlayData());
        } else {
            viewHolder.bannerTargetController.setPlayData(null);
        }

        if (isAttached) {
            viewHolder.attach();
        }
        bindedViewHolders.add(viewHolder);
    }

    private void loadImage(BannerItemViewHolder viewHolder, BannerItemUIViewModel data) {
        viewHolder.ivImage.setAlpha(1f);
        viewHolder.ivImage.setVisibility(View.VISIBLE);
        requestManager.load(data.getImage()).big().centerCrop().into(viewHolder.ivImage);
    }

    public void loadImages() {
        if (bindedViewHolders.size() > 0) {
            Iterator<BannerItemViewHolder> iterator = bindedViewHolders.iterator();
            while (iterator.hasNext()) {
                BannerItemViewHolder viewHolder = iterator.next();
                loadImage(viewHolder, (BannerItemUIViewModel) viewHolder.getBindModel());
            }
        }
        if (currentViewHolder != null) {
            BannerItemUIViewModel bannerItemUIViewModel = currentViewHolder.getBindModel();
            if (bannerItemUIViewModel != null && bannerItemUIViewModel.getBannerPlayerViewModel() != null) {
                currentViewHolder.bannerTargetController.setPlayData(bannerItemUIViewModel.getBannerPlayerViewModel().getRecommendModel().getPlayData());
            } else {
                currentViewHolder.bannerTargetController.setPlayData(null);
            }
        }
    }


    @Override
    public BannerItemViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
        return new BannerItemViewHolder(getLayoutInflaterFormParent(container).inflate(R.layout.item_live_recommend_banner_item_layout, container, false));
    }

    @Override
    protected boolean isRecycle() {
        return true;
    }

    @Override
    public void onRecycleViewHolder(@NonNull BannerItemViewHolder holder) {
        super.onRecycleViewHolder(holder);
        bindedViewHolders.remove(holder);
        holder.dettach();
        holder.recycled();
    }

    @Override
    protected void onHideViewHolder(BannerItemViewHolder viewHolder) {
        super.onHideViewHolder(viewHolder);
        viewHolder.bannerTargetController.changeVisible(false);
    }

    @Override
    protected void onShowViewHolder(BannerItemViewHolder viewHolder) {
        super.onShowViewHolder(viewHolder);
        currentViewHolder = viewHolder;
        viewHolder.bannerTargetController.changeVisible(isVisible);
    }

    @Override
    public void onAttached() {
        super.onAttached();
        for (int i = 0, j = getmAttached().size() - 1; i <= j; i++) {
            BannerItemViewHolder itemViewHolder = getViewHolder(i);
            if (itemViewHolder != null && itemViewHolder != getCurrentViewHolder()) {
                if (itemViewHolder.ivImage.getVisibility() != View.VISIBLE)
                    itemViewHolder.ivImage.setVisibility(View.VISIBLE);
                itemViewHolder.ivImage.setAlpha(1f);
            }
        }
    }

    @Override
    public void onDettached() {
        super.onDettached();
    }

    public BannerItemViewHolder getCurrentViewHolder() {
        return currentViewHolder;
    }

    protected LayoutInflater getLayoutInflaterFormParent(ViewGroup parent) {
        MutableContextWrapper contextWrapper = new MutableContextWrapper(parent.getContext());
        return LayoutInflater.from(contextWrapper).cloneInContext(contextWrapper);
    }


    public static class BannerItemViewHolder extends BannerViewHolder implements ClickableUIViewHolder {

        @BindView(R2.id.view_target)
        public View viewTarget;
        @BindView(R2.id.iv_image)
        public ImageView ivImage;

        public BannerTargetController bannerTargetController;

        public BannerItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            bannerTargetController = new BannerTargetController(viewTarget, BANNER_PLAYER_WIDTH, BANNER_PLAYER_HEIGHT, new BannerTargetController.CoverListener() {
                @Override
                public void showCover(boolean isAnim) {
                    if (isAnim) {
                        AdditiveAnimator.cancelAnimations(ivImage);
                        AdditiveAnimator.animate(ivImage).alpha(1f)
                                .addStartAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivImage.setVisibility(View.VISIBLE);
                                    }
                                })
                                .start();
                    } else {
                        ivImage.setAlpha(1f);
                        ivImage.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void hideCover() {
                    AdditiveAnimator.cancelAnimations(ivImage);
                    AdditiveAnimator.animate(ivImage).alpha(0f)
                            .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                                @Override
                                public void onAnimationEnd(boolean wasCancelled) {
                                    ivImage.setVisibility(View.GONE);
                                }
                            })
                            .start();
                }
            });
        }


        public void dettach() {
            bannerTargetController.dettach();
        }

        public void attach() {
            bannerTargetController.attach();
        }

        public void changeVisible(boolean isVisible) {
            bannerTargetController.changeVisible(isVisible);
        }

        public void recycled() {
            Context context = getRootView().getContext();
            if (context instanceof MutableContextWrapper) {
                MutableContextWrapper contextWrapper = (MutableContextWrapper) context;
                contextWrapper.setBaseContext(AppContextProvider.getInstance().getContext());
            }
            AdditiveAnimator.cancelAnimations(ivImage);
//            ImageLoader.with(AppContextProvider.getInstance().getContext())
//                    .load(R.mipmap.default_6)
//                    .into(ivImage);
            ImageLoader.clearView(ivImage);
            ivImage.setImageResource(R.drawable.bg_placeholder_color_shape);
        }


        @Override
        public void bindData(UIViewModel uiViewModel) {
            bindModel(uiViewModel);
        }

        @Override
        public ClickableUIViewModel getData() {
            return getBindModel();
        }

        @Override
        public int getViewType() {
            return getType();
        }

        @Override
        public void setViewType(int i) {
            setType(i);
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
    }


    public interface BannerItemClickListener {
        void onBannerItemClick(boolean forceStart, int bannerPosition);
    }

}


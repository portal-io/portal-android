package com.whaley.biz.program.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.playerui.PlayerView;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.playercontroller.IPlayerController;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.playersupport.event.NewPlayerPageEvent;
import com.whaley.biz.program.playersupport.widget.NormalPlayerView;
import com.whaley.biz.program.ui.detail.component.ProgramCollectionComponent;
import com.whaley.biz.program.ui.detail.component.ProgramDetailComponent;
import com.whaley.biz.program.ui.detail.component.ProgramDownloadComponent;
import com.whaley.biz.program.ui.detail.component.ProgramFollowComponent;
import com.whaley.biz.program.ui.detail.component.ProgramShareComponent;
import com.whaley.biz.program.ui.detail.presenter.BaseProgramDetailPresenter;
import com.whaley.biz.program.ui.detail.viewholder.MovieViewHolder;
import com.whaley.biz.program.ui.detail.viewholder.ProgramViewHolder;
import com.whaley.biz.program.ui.detail.viewholder.TVViewHolder;
import com.whaley.biz.program.ui.detail.viewholder.VRViewHolder;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.StrUtil;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YangZhi on 2017/8/23 14:06.
 */

public class BaseProgramDetailFragment<T extends BaseProgramDetailPresenter> extends BaseMVPFragment<T> implements ProgramDetailView {

    public static List<PlayData> PLAYDATAS;

    @BindView(R2.id.vs_vr)
    ViewStub vsVr;
    @BindView(R2.id.vs_movie)
    ViewStub vsMovie;
    @BindView(R2.id.vs_tv)
    ViewStub vsTv;
    @BindView(R2.id.playerview)
    NormalPlayerView playerView;
    @BindView(R2.id.emptyLayout_half)
    EmptyDisplayView emptyLayoutHalf;
    @BindView(R2.id.btn_pay)
    TextView btnPay;


    @OnClick(R2.id.btn_pay)
    void btnPayClick() {
        getPresenter().btnPayClick();
    }

    TVViewHolder tvViewHolder;

    MovieViewHolder movieViewHolder;

    VRViewHolder vrViewHolder;

    ProgramViewHolder currentViewHolder;

    ImageRequest.RequestManager requestManager;


    ProgramViewHolder.ButtonsClickListener buttonsClickListener = new ProgramViewHolder.ButtonsClickListener() {
        @Override
        public void onDownloadClick() {
            getPresenter().onDownloadClick();
        }

        @Override
        public void onCollectionClick() {
            getPresenter().collection();
        }

        @Override
        public void onShareClick() {
            getPresenter().onShareClick();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_program_detail;
    }


    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        requestManager = ImageLoader.with(this);
        setupPlayerView();
    }

    protected void setupPlayerView() {
        playerView.registHalfSwitchComponent();
        playerView.regist(new ProgramDetailComponent(this));
        playerView.regist(new ProgramDownloadComponent(this));
        playerView.regist(new ProgramCollectionComponent(this));
        playerView.regist(new ProgramFollowComponent(this));
        playerView.regist(new ProgramShareComponent(this));
        playerView.setViewPrepareListener(new PlayerView.ViewPrepareListener() {
            @Override
            public void onViewPrepared(IPlayerController playerController) {
                getPresenter().onPlayerViewPrepared(playerController);
                if (PLAYDATAS == null) {
                    finishView();
                    return;
                }
                playerController.setNewPlayData(PLAYDATAS, getPresenter().getPlayerPos());
            }
        });
    }

    @Override
    public void updatePlayerHeight(int height) {
        if(playerView==null)
            return;
        ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
        if (layoutParams.height == height) {
            return;
        }
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = height;
        playerView.requestLayout();
//        playerView.setLayoutParams(layoutParams);
    }

    @Override
    public void updateRealPlayerWidth(int width) {
        if(playerView==null)
            return;
        ViewGroup.LayoutParams layoutParams = playerView.getRealPlayerView().getLayoutParams();
        if (layoutParams.width == width) {
            return;
        }
        layoutParams.width = width;
        playerView.requestLayout();
    }

    @Override
    public void updateInfo(Object object) {
        currentViewHolder.bindData(object);
    }

    @Override
    public void changeToMoive() {
        if (movieViewHolder == null) {
            View view = vsMovie.inflate();
            movieViewHolder = new MovieViewHolder(view);
            movieViewHolder.setButtonsClickListener(buttonsClickListener);
        }
        movieViewHolder.show();
        currentViewHolder = movieViewHolder;
        if (tvViewHolder != null)
            tvViewHolder.hide();
        if (vrViewHolder != null)
            vrViewHolder.hide();
    }

    @Override
    public void changeToTV() {
        if (tvViewHolder == null) {
            View view = vsTv.inflate();
            tvViewHolder = new TVViewHolder(view);
            tvViewHolder.setButtonsClickListener(buttonsClickListener);
            tvViewHolder.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(IViewHolder viewHolder, int position) {
//                    updateSeries(position);
                    getPresenter().changeSeries(position);
                }
            });
        }
        tvViewHolder.show();
        currentViewHolder = tvViewHolder;
        if (movieViewHolder != null)
            movieViewHolder.hide();
        if (vrViewHolder != null)
            vrViewHolder.hide();
    }

    @Override
    public void changeToVR() {
        if (vrViewHolder == null) {
            View view = vsVr.inflate();
            vrViewHolder = new VRViewHolder(view);
            vrViewHolder.setButtonsClickListener(buttonsClickListener);
            vrViewHolder.setOnPosterClick(new VRViewHolder.OnPosterClickListener() {
                @Override
                public void onFollowClick(View view) {
                    getPresenter().onFollowClick();
                }

                @Override
                public void onPosterClick(View view) {
                    getPresenter().onPosterUserClick();
                }
            });
            vrViewHolder.setRequestManager(requestManager);
        }
        vrViewHolder.show();
        currentViewHolder = vrViewHolder;
        if (movieViewHolder != null)
            movieViewHolder.hide();
        if (tvViewHolder != null)
            tvViewHolder.hide();
    }

    @Override
    public void updatePayBtn(boolean isChargeable, boolean isShowPayBtn, String pice) {
        if (isShowPayBtn && !StrUtil.isEmpty(pice)) {
            btnPay.setText(pice);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) btnPay.getLayoutParams();
            layoutParams.height = DisplayUtil.convertDIP2PX(40);
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) btnPay.getLayoutParams();
            layoutParams.height = 0;
            if (vrViewHolder != null) {
                if (isChargeable) {
                    vrViewHolder.tvHaveVoucher.setVisibility(View.VISIBLE);
                } else {
                    vrViewHolder.tvHaveVoucher.setVisibility(View.GONE);
                }
            }
        }
        btnPay.requestLayout();
    }

    @Override
    public void updateCollection(boolean isCollection) {
        if (currentViewHolder != null && currentViewHolder.getLayoutCollect() != null) {
            currentViewHolder.getLayoutCollect().setSelected(isCollection);
            TextView textView = (TextView) currentViewHolder.getLayoutCollect().getChildAt(1);
            if (textView == null) {
                return;
            }
            if (isCollection) {
                textView.setTextColor(getResources().getColor(R.color.color1));
                textView.setText("已加入播单");
            } else {
                textView.setTextColor(getResources().getColor(R.color.color6));
                textView.setText("加入播单");
            }
        }
    }

    @Override
    public void updateFollow(boolean isUpdate, boolean isFollow) {
        if (vrViewHolder != null) {
            vrViewHolder.setFollowData(isUpdate, isFollow);
        }
    }

    @Override
    public void updateDownloaded(boolean isDownloaded) {
        if (vrViewHolder != null) {
            vrViewHolder.updateDownloaded(isDownloaded);
        }
    }


    @Override
    public EmptyDisplayView getEmptyDisplayView() {
        return emptyLayoutHalf;
    }

    @Override
    protected void unBindViews() {
        super.unBindViews();
        if (vrViewHolder != null) {
            vrViewHolder.destory();
            vrViewHolder = null;
        }
        if (tvViewHolder != null) {
            tvViewHolder.destory();
            tvViewHolder = null;
        }
        if (movieViewHolder != null) {
            movieViewHolder.destory();
            movieViewHolder = null;
        }
    }

    @Override
    public void finish() {
        if (playerView != null) {
            playerView.destory();
        }
        PLAYDATAS = null;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.finish();
    }

    @Subscribe
    public void onNewPlayerPageEvent(NewPlayerPageEvent newPlayerPageEvent) {
        finish();
    }
}

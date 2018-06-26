package com.whaley.biz.program.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.playerui.PlayerView;
import com.whaley.biz.playerui.playercontroller.IPlayerController;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.detail.component.drama.DramaCollectionComponent;
import com.whaley.biz.program.ui.detail.component.drama.DramaDetailComponent;
import com.whaley.biz.program.ui.detail.component.drama.DramaFollowComponent;
import com.whaley.biz.program.ui.detail.component.drama.DramaShareComponent;
import com.whaley.biz.program.ui.detail.presenter.DramaDetailPresenter;
import com.whaley.biz.program.ui.detail.viewholder.DramaViewHolder;
import com.whaley.biz.program.ui.detail.viewholder.VRViewHolder;

/**
 * Created by dell on 2017/11/13.
 */

@Route(path = ProgramRouterPath.PROGRAM_DRAMA)
public class DramaDetailFragment extends BaseProgramDetailFragment<DramaDetailPresenter> implements DramaDetailView{

    DramaViewHolder dramaViewHolder;

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        changeToDrama();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_program_drama_detail;
    }

    @Override
    protected void setupPlayerView() {
        playerView.registHalfSwitchComponent();
        playerView.regist(new DramaDetailComponent(this));
        playerView.regist(new DramaCollectionComponent(this));
        playerView.regist(new DramaFollowComponent(this));
        playerView.regist(new DramaShareComponent(this));
        playerView.setViewPrepareListener(new PlayerView.ViewPrepareListener() {
            @Override
            public void onViewPrepared(IPlayerController playerController) {
                getPresenter().onPlayerViewPrepared(playerController);
                if (PLAYDATAS == null) {
                    finishView();
                    return;
                }
                playerController.setNewPlayData(PLAYDATAS, 0);
            }
        });
    }

    private void changeToDrama() {
        if (dramaViewHolder == null) {
            View view = vsVr.inflate();
            dramaViewHolder = new DramaViewHolder(view);
            dramaViewHolder.setButtonsClickListener(buttonsClickListener);
            dramaViewHolder.setOnPosterClick(new VRViewHolder.OnPosterClickListener() {
                @Override
                public void onFollowClick(View view) {
                    getPresenter().onFollowClick();
                }

                @Override
                public void onPosterClick(View view) {
                    getPresenter().onPosterUserClick();
                }
            });
            dramaViewHolder.setRequestManager(requestManager);
        }
        dramaViewHolder.show();
        currentViewHolder = dramaViewHolder;
        if (movieViewHolder != null)
            movieViewHolder.hide();
        if (tvViewHolder != null)
            tvViewHolder.hide();
        if (vrViewHolder != null)
            vrViewHolder.hide();
    }

    @Override
    public void changeToVR() {
        //
    }

    @Override
    public void changeToTV() {
        //
    }

    @Override
    public void changeToMoive() {
        //
    }

    @Override
    public void updateDownloaded(boolean isDownloaded) {
        //
    }

    @Override
    public void updatePayBtn(boolean isChargeable, boolean isShowPayBtn, String pice) {
        //
    }

    @Override
    public void updateFollow(boolean isUpdate, boolean isFollow) {
        if (dramaViewHolder != null) {
            dramaViewHolder.setFollowData(isUpdate, isFollow);
        }
    }

    @Override
    protected void unBindViews() {
        super.unBindViews();
        if (dramaViewHolder != null) {
            dramaViewHolder.destory();
            dramaViewHolder = null;
        }
    }

}

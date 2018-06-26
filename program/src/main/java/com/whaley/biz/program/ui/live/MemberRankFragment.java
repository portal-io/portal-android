package com.whaley.biz.program.ui.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.whaley.biz.common.ui.RecyclerLoaderFragment;
import com.whaley.biz.program.ui.live.adapter.MemberRankAdapter;
import com.whaley.biz.program.ui.live.presenter.MemberRankPresenter;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.widget.viewholder.ListAdapter;

/**
 * Created by YangZhi on 2017/10/13 17:44.
 */

public class MemberRankFragment extends RecyclerLoaderFragment<MemberRankPresenter,Object>{


    @Override
    protected ListAdapter onCreateAdapter() {
        MemberRankAdapter adapter = new MemberRankAdapter();
        adapter.setRequestManager(ImageLoader.with(this));
        return adapter;
    }

    @Override
    protected boolean isShouldLoadMore() {
        return false;
    }

}

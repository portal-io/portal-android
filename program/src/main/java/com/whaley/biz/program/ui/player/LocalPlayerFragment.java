package com.whaley.biz.program.ui.player;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramRouterPath;

/**
 * Created by YangZhi on 2017/8/22 12:16.
 */

@Route(path = ProgramRouterPath.PLAYER_LOCAL)
public class LocalPlayerFragment extends PlayerFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_player_local;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

}

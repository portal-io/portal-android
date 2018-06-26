package com.whaley.biz.common.ui;

import android.content.Intent;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.router.Router;
import com.whaley.core.uiframe.presenter.PagePresenter;

/**
 * Created by YangZhi on 2017/7/25 17:18.
 */

public class BasePagePresenter<VIEW extends BasePageView> extends PagePresenter<VIEW> implements BIConstants {

    boolean isBrowsed;

    long startDurationTime;

    public BasePagePresenter(VIEW view) {
        super(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        startDurationTime = System.currentTimeMillis();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isNeedBrowseBuried()) {
            browseDuration();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CommonConstants.SHARE_REQUEST_CODE && !data.getBooleanExtra(CommonConstants.SHARE_PARAM_OUTSIDE, true))
            return;
        if (isNeedBrowseBuried()) {
            browse(false);
        }
    }

    public void finish() {
        if (getUIView() != null) {
            getUIView().finishView();
        }
    }

    public Starter getStater() {
        if (getUIView() != null && getUIView().getAttachActivity() != null)
            return (Starter) getUIView().getAttachActivity();
        return AppContextProvider.getInstance().getStarter();
    }

    public void regist() {
        EventController.regist(this);
    }

    public void unRegist() {
        EventController.unRegist(this);
    }

    //================bi埋点==================//

    /**
     * 浏览
     */
    public void browse(boolean isFromFetch) {
        if (isFromFetch) {
            if (isBrowsed) {
                return;
            } else {
                isBrowsed = true;
            }
        }
        LogInfoParam.Builder builder = getGeneralBuilder(BROWSE_VIEW);
        if (builder != null) {
            builder.setNextPageId(builder.getCurrentPageId());
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

    /**
     * 浏览时长
     */
    private void browseDuration() {
        LogInfoParam.Builder builder = getGeneralBuilder(BROWSE_DURATION);
        if (builder != null) {
            builder.putEventPropKeyValue(EVENT_PROP_KEY_ACTION_TYPE, ACTION_TYPE_END_BROWSE);
            builder.putEventPropKeyValue(EVENT_PROP_KEY_DURATION, String.valueOf(System.currentTimeMillis() - startDurationTime));
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

    protected LogInfoParam.Builder getGeneralBuilder(String eventId) {
        return null;
    }

    protected boolean isNeedBrowseBuried() {
        return false;
    }

}

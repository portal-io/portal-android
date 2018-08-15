package com.whaley.biz.launcher.presenter;

import android.os.Bundle;
import android.view.View;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.hybrid.TitleBarModel;
import com.whaley.biz.common.model.hybrid.WebviewGoPageModel;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.launcher.event.ModifyFestivalEvent;
import com.whaley.biz.launcher.festival.FestivalManager;
import com.whaley.biz.launcher.model.FestivalModel;
import com.whaley.biz.launcher.model.UpdateModel;
import com.whaley.biz.launcher.model.UserModel;
import com.whaley.biz.launcher.util.ComputeUtil;
import com.whaley.biz.launcher.util.SharedPreferencesUtil;
import com.whaley.biz.launcher.view.MainView;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: qxw
 * Date:2017/8/9
 * Introduction:
 */

public class MainPresenter extends BasePagePresenter<MainView> {

    String updateModel;

    public MainPresenter(MainView view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        regist();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegist();
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        if (getUIView() != null && getUIView().getAttachActivity() != null) {
            String updateModelString = SharedPreferencesUtil.getUpDateData();
            if (StrUtil.isEmpty(updateModelString)) {
                if (!showEventPoster()) {
                    showRecommendedContent(true);
                }
            } else {
                UpdateModel model = GsonUtil.getGson().fromJson(updateModelString, UpdateModel.class);
                if (model != null && model.getUpdateType() == 1) {
                    showUpdate(true, updateModelString);
                } else {
                    updateModel = updateModelString;
                    if (!showEventPoster()) {
                        showUpdate(false, updateModelString);
                    }
                }
            }
            getFestival(true);
        }
    }

    private void getFestival(boolean isShow){
        if(FestivalManager.getInstance().isFestivalAvailable()){
            if(getUIView()!=null && isShow){
                getUIView().showSpringFestival();
            }
        }else{
            if(getUIView()!=null){
                getUIView().hideSpringFestival();
            }
        }
    }

    public void onClickFestival(){
        FestivalModel festivalModel = FestivalManager.getInstance().getFestivalModel();
        if(festivalModel!=null){
            goWeb(festivalModel);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModifyFestival(ModifyFestivalEvent modifyFestivalEvent){
        getFestival(modifyFestivalEvent.isShow());
    }

    private void goWeb(FestivalModel festivalModel) {
        PageModel pageModel = new PageModel();
        Bundle bundle = new Bundle();
        pageModel.setRouterPath(ProgramRouterPath.WEB_INNER);
        String url = festivalModel.getUrl();
        TitleBarModel titleBarModel = TitleBarModel.createTitleBarModel(festivalModel.getDisplayName());
        ShareModel shareModel = null;
        if (!StrUtil.isEmpty(url) && url.indexOf("topBarTransparent=1") != -1) {
            titleBarModel.setType(4);
            shareModel=ShareModel.createBuilder().setUrl(url)
                    .setImgUrl("")
                    .setDes("")
                    .setType(ShareConstants.TYPE_ALL)
                    .setShareType(ShareTypeConstants.TYPE_SHARE_WEB)
                    .setTitle(festivalModel.getDisplayName()).build();
        }
        bundle.putParcelable(ProgramConstants.WEBVIEW_DATA, WebviewGoPageModel.createWebviewGoPageModel(url, titleBarModel, shareModel));
        pageModel.setBundle(bundle);
        GoPageUtil.goPage(getStater(), pageModel);
    }

    public void showNotice() {
        Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
            @Override
            public void onCall(UserModel userModel) {
                if (getUIView() == null) {
                    return;
                }
                int numMotice = SharedPreferencesUtil.getUnreadData(userModel.getAccount_id());
                if (numMotice > 0) {
                    if (numMotice > 99) {
                        getUIView().showNotice("99+");
                        return;
                    }
                    getUIView().showNotice(String.valueOf(numMotice));
                }
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
            }
        }).excute();
    }

    public void onClose(boolean isEventPoster, boolean isShow) {
        String keyString;
        if (isEventPoster) {
            if (!StrUtil.isEmpty(updateModel)) {
                showUpdate(false, updateModel);
            } else {
                showRecommendedContent(true);
            }
            keyString = CommonConstants.KEY_POSTER;
        } else {
            keyString = CommonConstants.KEY_CONTENT;
        }
        if (isShow)
            ComputeUtil.getOnClickBuilder(keyString, BIConstants.AD_CLOSE);
    }

    private void showUpdate(boolean isEnforce, String updateModelString) {
        Map<String, Object> map = new HashMap<>();
        if (!isEnforce) {
            map.put("clickListener", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRecommendedContent(true);
                }
            });
        }
        map.put("content", updateModelString);
        map.put("activity", getUIView().getAttachActivity());
        Router.getInstance().buildExecutor("/setting/service/updatedialog").putObjParam(map).notTransParam().excute();
        updateModel = null;
    }

    private boolean showEventPoster() {
        String posterPath = SharedPreferencesUtil.getSplashUrl(CommonConstants.KEY_POSTER);
        if (getUIView() != null && !StrUtil.isEmpty(posterPath) && ComputeUtil.isShowSplash(CommonConstants.KEY_POSTER)) {
            getUIView().showEventPoster(posterPath);
            return true;
        }
        return false;
    }

    public void showRecommendedContent(boolean isStart) {
        String contentPath = SharedPreferencesUtil.getSplashUrl(CommonConstants.KEY_CONTENT);
        if (getUIView() != null && !StrUtil.isEmpty(contentPath) && ComputeUtil.isShowSplash(CommonConstants.KEY_CONTENT)) {
            getUIView().showRecommendedContent(contentPath, isStart);
        }

    }

    public void onRecommendClick() {
    }

    public void onLiveClick() {
    }

    public void onPortalClick(){

    }

    public void onDiscoverClick() {
        Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
            @Override
            public void onCall(UserModel userModel) {
                if (SharedPreferencesUtil.getUnreadData(userModel.getAccount_id()) > 0) {
                    EventController.postEvent(new BaseEvent("follow_my_click"));
                    getUIView().hideNotice();
                }
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
            }
        }).excute();

    }

    public void onUserClick() {
    }


    public void processNotice(String notice) {
        Router.getInstance().buildExecutor("/jpush/service/noctice").putObjParam(notice).excute();
    }

    public void onAdvertising(String keyString) {
        Router.getInstance().buildExecutor("/program/service/gorecommendpage").notTransParam().putObjParam(SharedPreferencesUtil.getSplashParam(keyString)).excute();
        ComputeUtil.getOnClickBuilder(keyString, BIConstants.AD_CLICK);
    }


    public void onRecommendedContent() {
        onAdvertising(CommonConstants.KEY_CONTENT);
    }

    public void onEventPoster() {
        onAdvertising(CommonConstants.KEY_POSTER);
    }

    public void saveSplashTime(boolean isPoster) {
        SharedPreferencesUtil.setSplashTime(isPoster ? CommonConstants.KEY_POSTER : CommonConstants.KEY_CONTENT, System.currentTimeMillis());
    }
}

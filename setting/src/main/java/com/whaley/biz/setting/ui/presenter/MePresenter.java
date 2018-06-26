package com.whaley.biz.setting.ui.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.setting.constant.SettingConstants;
import com.whaley.biz.setting.MeType;
import com.whaley.biz.setting.R;
import com.whaley.biz.common.model.hybrid.TitleBarModel;
import com.whaley.biz.common.model.hybrid.WebviewGoPageModel;
import com.whaley.biz.setting.interactor.Me;
import com.whaley.biz.setting.interactor.MeCard;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.biz.setting.router.GoPageUtil;
import com.whaley.biz.setting.router.PageModel;
import com.whaley.biz.setting.router.ProgramRouterPath;
import com.whaley.biz.setting.router.SettingRouterPath;
import com.whaley.biz.setting.ui.repository.MeRepository;
import com.whaley.biz.setting.ui.view.MeView;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

import static com.whaley.biz.setting.MeType.GIFT;

/**
 * Created by dell on 2017/7/27.
 */

public class MePresenter extends BasePagePresenter<MeView> implements MeType {

    @Repository
    MeRepository repository;

    @UseCase
    Me meUseCase;

    @UseCase
    MeCard meCardUseCase;

    public MeRepository getMeRepository() {
        return repository;
    }

    public MePresenter(MeView view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        regist();
        getMeViewModels();
        checkLogin(true);
    }

    private void getMeViewModels() {
        meCardUseCase.execute(new DisposableObserver() {
            @Override
            public void onNext(@NonNull Object o) {
                getUIView().updateCardList();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        meUseCase.execute(new DisposableObserver() {
            @Override
            public void onNext(@NonNull Object o) {
                getUIView().updateList();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        unRegist();
    }

    public void onClickItem(int type) {
        switch (type) {
            case LOCAL:
                GoPageUtil.goPage(getStater(), PageModel.obtain(SettingRouterPath.LOCAL));
                break;
            case COLLECT:
                PageModel pageModel = PageModel.obtain(ProgramRouterPath.COLLECT);
                Bundle bundle = new Bundle();
                bundle.putString("key_login_tips", AppContextProvider.getInstance().getContext().getString(R.string.dialog_collection));
                pageModel.setBundle(bundle);
                GoPageUtil.goPage(getStater(), pageModel);
                break;
            case PAY:
                pageModel = PageModel.obtain(SettingRouterPath.REDEMPTION);
                bundle = new Bundle();
                bundle.putString("key_login_tips", AppContextProvider.getInstance().getContext().getString(R.string.dialog_pay));
                pageModel.setBundle(bundle);
                GoPageUtil.goPage(getStater(), pageModel);
                break;
            case MeType.GIFT:
                pageModel = PageModel.obtain(SettingRouterPath.GIFT);
                bundle = new Bundle();
                bundle.putString("key_login_tips", AppContextProvider.getInstance().getContext().getString(R.string.dialog_gift));
                pageModel.setBundle(bundle);
                GoPageUtil.goPage(getStater(), pageModel);
                break;
            case FEEDBACK:
                WebviewGoPageModel webviewGoPageModel = WebviewGoPageModel.createWebviewGoPageModel("https://sojump.com/jq/10997224.aspx",
                        TitleBarModel.createTitleBarModel("问题反馈"));
                Router.getInstance().buildExecutor("/hybrid/service/goPage").putObjParam(webviewGoPageModel).excute();
                break;
            case FORUM:
                Intent i = new Intent();
                i.setAction("android.intent.action.VIEW");
                Uri u = Uri.parse(AppContextProvider.getInstance().getContext().getString(R.string.user_forum));
                i.setData(u);
                getStater().startActivity(i);
                break;
            case HELP:
                webviewGoPageModel = WebviewGoPageModel.createWebviewGoPageModel("file:///android_asset/helper.html",
                        TitleBarModel.createTitleBarModel("帮助"));
                Router.getInstance().buildExecutor("/hybrid/service/goPage").putObjParam(webviewGoPageModel).excute();
                break;
            case ABOUT:
                GoPageUtil.goPage(getStater(), PageModel.obtain(SettingRouterPath.ABOUT));
                break;
            case SETTING:
                GoPageUtil.goPage(getStater(), PageModel.obtain(SettingRouterPath.SETTING));
                break;
            case CURRENCY:
                pageModel = PageModel.obtain(SettingRouterPath.CURRENCY);
                bundle = new Bundle();
                bundle.putString("key_login_tips", AppContextProvider.getInstance().getContext().getString(R.string.dialog_currency));
                pageModel.setBundle(bundle);
                GoPageUtil.goPage(getStater(), pageModel);
                break;
        }
    }

    private void checkLogin(final boolean isLogin) {
        Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
            @Override
            public void onCall(UserModel userModel) {
                hasLogged(userModel);
                if (isLogin)
                    obtainUserInfo();
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
                notLogin();
            }
        }).excute();
    }


    private void obtainUserInfo() {
        Router.getInstance().buildExecutor("/user/service/obtainuserinfo").callback(new Executor.Callback<UserModel>() {
            @Override
            public void onCall(UserModel userModel) {
                hasLogged(userModel);
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
                getUIView().showToast("登录已过期");
            }
        }).excute();
    }

    public void onClickLogin() {
        GoPageUtil.goPage(getStater(), PageModel.obtain(SettingRouterPath.LOGIN));
    }

    public void onClickRegister() {
        GoPageUtil.goPage(getStater(), PageModel.obtain(SettingRouterPath.REGISTER));
    }

    public void onClickNickname() {
        GoPageUtil.goPage(getStater(), PageModel.obtain(SettingRouterPath.USERINFO));
    }

    private void hasLogged(UserModel userModel) {
        getMeRepository().setLogin(true);
        getMeRepository().setUserModel(userModel);
        if (getUIView() != null) {
            getUIView().updateLoginStatus();
        }
    }

    private void notLogin() {
        getMeRepository().setUserModel(null);
        getMeRepository().setLogin(false);
        if (getUIView() != null) {
            getUIView().updateLoginStatus();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case SettingConstants.EVENT_SIGN_OUT:
                notLogin();
                break;
            case SettingConstants.EVENT_LOGIN_SUCCESS:
                checkLogin(true);
                break;
            case SettingConstants.EVENT_UPATE_NAME:
            case SettingConstants.EVENT_UPATE_AVATAR:
                checkLogin(false);
                break;
            case SettingConstants.EVENT_LOGIN_CANCEL:
                if(getUIView()!=null){
                    getUIView().hideStatusBar();
                }
                break;
        }
    }

}

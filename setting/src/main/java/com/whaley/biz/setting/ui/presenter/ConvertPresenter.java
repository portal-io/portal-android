package com.whaley.biz.setting.ui.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.common.widget.keyboardHelper.KeyBoardHelper;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.router.GoPageUtil;
import com.whaley.biz.setting.router.PageModel;
import com.whaley.biz.setting.router.SettingRouterPath;
import com.whaley.biz.setting.util.RedemptionErrorUtil;
import com.whaley.biz.setting.event.RedemptionCodeEvent;
import com.whaley.biz.setting.interactor.ConvertRedeem;
import com.whaley.biz.setting.model.RedemptionCodeModel;
import com.whaley.biz.setting.ui.repository.ConvertRepository;
import com.whaley.biz.setting.ui.view.ConvertView;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;
import com.whaley.core.utils.KeyboardUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/4.
 */

public class ConvertPresenter extends BasePagePresenter<ConvertView> implements BIConstants{

    @Repository
    ConvertRepository repository;

    @UseCase
    ConvertRedeem convertRedeem;

    private Disposable disposable;

    static final String STR_UNITY = "str_fromUnity";

    public ConvertPresenter(ConvertView view) {
        super(view);
    }

    public ConvertRepository getConvertRepository(){
        return repository;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
            getConvertRepository().setUnity(arguments.getBoolean(STR_UNITY, false));
        }
    }

    public void userDoRedeem() {
        String code = getConvertRepository().getCode();
        if (TextUtils.isEmpty(code)) {
            getUIView().showToast("兑换码不能为空");
            return;
        }
        getUIView().showLoading(null);
        ConvertRedeem.ConvertRedeemParam param = new ConvertRedeem.ConvertRedeemParam(code);
        if(disposable!=null){
            disposable.dispose();
        }
        disposable = convertRedeem.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<RedemptionCodeModel>(getUIView(), true){
                    @Override
                    public void onNext(@NonNull RedemptionCodeModel redemptionCodeModel) {
                        super.onNext(redemptionCodeModel);
                        getConvertRepository().setConvertResult(true);
                        if(!getConvertRepository().isUnity()) {
                            EventController.postEvent(new RedemptionCodeEvent(redemptionCodeModel));
                        }
                        onConvertSuccess(redemptionCodeModel);
                        if(getUIView()!=null){
                            KeyboardUtil.hideKeyBoard(getUIView().getEditText());
                            getUIView().removeLoading();
                            if(getConvertRepository().isUnity()) {
                                PageModel pageModel = PageModel.obtain(SettingRouterPath.REDEMPTION);
                                Bundle bundle = new Bundle();
                                bundle.putString("key_login_tips", AppContextProvider.getInstance().getContext().getString(R.string.dialog_pay));
                                bundle.putBoolean("str_fromUnity", true);
                                redemptionCodeModel.setUnity(true);
                                bundle.putSerializable("redemptionCodeModel", redemptionCodeModel);
                                pageModel.setBundle(bundle);
                                GoPageUtil.goPage(getStater(), pageModel);
                            }
                            getUIView().finishView();
                        }
                    }
                    @Override
                    public void onStatusError(int status, String message, String subCode, RedemptionCodeModel data) {
                        super.onStatusError(status, message, subCode, data);
                        getConvertRepository().setConvertResult(false);
                        showDialog(RedemptionErrorUtil.getErrorSting(subCode, message));
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        getConvertRepository().setConvertResult(false);
                        if (getUIView() != null) {
                            getUIView().removeLoading();
                        }
                    }
                    @Override
                    protected boolean isShowToast() {
                        return false;
                    }
                });
    }

    public void showDialog(String msg) {
        DialogUtil.showDialog(getUIView().getAttachActivity(), msg);
    }

    public void onRedemptionChanged(String s) {
        getConvertRepository().setCode(s);
        if (TextUtils.isEmpty(s)) {
            if(getUIView()!=null)
                getUIView().removeBtn();
        } else {
            if(getUIView()!=null)
                getUIView().showBtn();
        }
    }

    @Override
    public void onDestroy() {
        if (getConvertRepository().isUnity() && !getConvertRepository().isConvertResult()) {
            Router.getInstance().buildExecutor("/unity/service/exchange").excute();
        }
        super.onDestroy();
        if(disposable!=null){
            disposable.dispose();
            disposable = null;
        }
    }

    //==============================BI埋点====================================//
    /**
     * 兑换成功
     */
    private void onConvertSuccess(RedemptionCodeModel redemptionCodeModel) {
        if(redemptionCodeModel!=null) {
            LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                    .setEventId(CONVERT)
                    .setCurrentPageId(ROOT_CONVERT_DETAIL)
                    .putEventPropKeyValue(EVENT_PROP_KEY_EVENT_TYPE, redemptionCodeModel.getRelatedType())
                    .putEventPropKeyValue(EVENT_PROP_KEY_EVENT_ID, redemptionCodeModel.getRelatedCode())
                    .setNextPageId(ROOT_CONVERT_DETAIL);
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

}

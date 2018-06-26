package com.whaley.biz.program.ui.live.presenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.LiveConstants;
import com.whaley.biz.program.constants.PayConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.AddReserve;
import com.whaley.biz.program.interactor.CancelReserve;
import com.whaley.biz.program.interactor.CheckLogin;
import com.whaley.biz.program.interactor.GetReserveDetail;
import com.whaley.biz.program.model.CouponModel;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.biz.program.model.PayEventModel;
import com.whaley.biz.program.model.pay.GoodsModel;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.ui.event.LiveReserveChangeEvent;
import com.whaley.biz.program.ui.live.LiveReserveDetailView;
import com.whaley.biz.program.ui.live.repository.LiveReserveDetailRepository;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.biz.program.utils.PayUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareUtil;
import com.whaley.core.share.model.ShareParam;
import com.whaley.core.utils.GsonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.whaley.biz.program.model.pay.ThirdPayModel.DISPLAY_MODE_PORTRAIT;

/**
 * Created by dell on 2017/8/17.
 */

public class LiveReserveDetailPresenter extends BasePagePresenter<LiveReserveDetailView> implements BIConstants {


    @Repository
    LiveReserveDetailRepository repository;

    @UseCase
    GetReserveDetail getReserveDetail;

    @UseCase
    AddReserve addReserve;

    @UseCase
    CancelReserve cancelReserve;

    boolean isChange = false;

    private Disposable disposable1, disposable2, disposable3;

    public LiveReserveDetailPresenter(LiveReserveDetailView view) {
        super(view);
    }

    public LiveReserveDetailRepository getRepository() {
        return repository;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        regist();
        if (arguments != null) {
            getRepository().setCode(arguments.getString(ProgramConstants.KEY_PARAM_ID));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isChange){
            EventController.postEvent(new LiveReserveChangeEvent());
        }
        unRegist();
        if (disposable1 != null) {
            disposable1.dispose();
        }
        if (disposable2 != null) {
            disposable2.dispose();
        }
        if (disposable3 != null) {
            disposable3.dispose();
        }
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        getDetailInfo();
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    public void getDetailInfo() {
        if (disposable1 != null) {
            disposable1.dispose();
        }
        disposable1 = getReserveDetail.buildUseCaseObservable(getRepository().getCode())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<LiveDetailsModel>(getUIView(), true) {
                    @Override
                    public void onNext(@NonNull LiveDetailsModel liveDetailsModel) {
                        super.onNext(liveDetailsModel);
                        if (getUIView() != null) {
                            getUIView().updateData(getRepository().getReserveDetailViewModel());
                            getUIView().updateReserve(getRepository().isHasOrder());
                        }
                        browse(true);
                        if (getRepository().getLiveStatus() == ProgramConstants.LIVE_STATE_BEING) {
                            if (getUIView() != null) {
                                GoPageUtil.goPage(getStater(), FormatPageModel.getLivePageModel(liveDetailsModel.getPlayData()));
                                getUIView().finishView();
                            }
                            return;
                        }
                        if (getRepository().getLiveStatus() == ProgramConstants.LIVE_STATE_AFTER) {
                            if (getUIView() != null) {
                                getUIView().showToast("直播已结束");
                                getUIView().finishView();
                            }
                            return;
                        }
                        if (getRepository().isCharge()) {
                            checklogin(false);
                        }
                    }
                });
    }

    public void checklogin(final boolean isBuy) {
        CheckLogin checkLogin = new CheckLogin();
        checkLogin.buildUseCaseObservable(null).subscribe(new DisposableObserver<UserModel>() {
            @Override
            public void onNext(@NonNull UserModel userModel) {
                if (isBuy) {
                    if (userModel.isLoginUser()) {
                        buy();
                    } else {
                        DialogUtil.showDialog(getStater().getAttatchContext(),
                                AppContextProvider.getInstance().getContext().getString(R.string.dialog_buy_pay), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        TitleBarActivity.goPage(getStater(), 0, "/user/ui/login");
                                    }
                                });
                    }
                } else {
                    if (userModel.isLoginUser()) {
                        checkPay(false);
                    } else {
                        getRepository().setBuy(false);
                        if (getUIView() != null) {
                            getUIView().showPay(true);
                        }
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //
            }

            @Override
            public void onComplete() {
                //
            }
        });
    }

    private void checkPay(final boolean isBuy) {
        Router.getInstance().buildExecutor("/pay/service/checkpay").putObjParam(new GoodsModel(getRepository().getCode(), "live")).callback(new Executor.Callback<Boolean>() {
            @Override
            public void onCall(Boolean isPayed) {
                if (isPayed) {
                    getRepository().setBuy(true);
                    if (getUIView() != null) {
                        getUIView().showPay(false);
                    }
                    if (isBuy) {
                        addReserve();
                    }
                } else {
                    getRepository().setBuy(false);
                    if (getUIView() != null) {
                        getUIView().showPay(true);
                    }
                    if (isBuy) {
                        checklogin(true);
                    }
                }
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
                getRepository().setBuy(false);
                if (getUIView() != null) {
                    getUIView().showPay(true);
                }
                if (isBuy) {
                    checklogin(true);
                }
            }
        }).excute();
    }

    public boolean isVoucher() {
        return getRepository().isCharge() && getRepository().isBuy()
                && getRepository().getCouponPackModel() != null;
    }

    public void onReserveClick() {
        if (getRepository().isHasOrder()) {
            cancelReserve();
        } else {
            if (getRepository().isCharge()) {
                checkPay(true);
            } else {
                addReserve();
            }
        }
    }

    public void addReserve() {
        if (disposable2 != null) {
            disposable2.dispose();
        }
        disposable2 = addReserve.buildUseCaseObservable(getRepository().getCode())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<CMSResponse>() {
                    @Override
                    public void onFinalError(Throwable e) {
                    }

                    @Override
                    public void onNotLoggedInError() {
                        super.onNotLoggedInError();
                        DialogUtil.showDialog(getStater().getAttatchContext(),
                                AppContextProvider.getInstance().getContext().getString(R.string.dialog_reserve), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        TitleBarActivity.goPage(getStater(), 0, "/user/ui/login");
                                    }
                                });
                    }

                    @Override
                    public void onStatusError(int status, String message) {
                        if (getUIView() != null) {
                            getUIView().showToast(message);
                        }
                    }

                    @Override
                    public void onNoDataError() {
                    }

                    @Override
                    public void onNext(@NonNull CMSResponse response) {
                        if (getUIView() != null) {
                            getUIView().showToast(response.getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        isChange = true;
                        getRepository().setTvAmount(getRepository().getTvAmount() + 1);
                        getRepository().setHasOrder(true);
                        if (getUIView() != null) {
                            getUIView().updateReserve(true);
                        }
                        livePrevue();
                    }
                });
    }

    public void cancelReserve() {
        if (disposable3 != null) {
            disposable3.dispose();
        }
        disposable3 = cancelReserve.buildUseCaseObservable(getRepository().getCode())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<CMSResponse>() {
                    @Override
                    public void onFinalError(Throwable e) {
                    }

                    @Override
                    public void onNotLoggedInError() {
                        super.onNotLoggedInError();
                        DialogUtil.showDialog(getStater().getAttatchContext(),
                                AppContextProvider.getInstance().getContext().getString(R.string.dialog_reserve), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        TitleBarActivity.goPage(getStater(), 0, "/user/ui/login");
                                    }
                                });
                    }

                    @Override
                    public void onStatusError(int status, String message) {
                        if (getUIView() != null) {
                            getUIView().showToast(message);
                        }
                    }

                    @Override
                    public void onNoDataError() {
                    }

                    @Override
                    public void onNext(@NonNull CMSResponse response) {
                        if (getUIView() != null) {
                            getUIView().showToast(response.getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        isChange = true;
                        getRepository().setTvAmount(getRepository().getTvAmount() - 1);
                        getRepository().setHasOrder(false);
                        if (getUIView() != null) {
                            getUIView().updateReserve(false);
                        }
                    }
                });
    }

    public void onShare() {
        ShareParam.Builder builder = repository.getBuilder();
        if (builder != null) {
            builder.setContext(getUIView().getAttachActivity());
            ShareUtil.share(builder.build());
        }
    }

    private void buy() {
        if (getRepository().getCouponPackModel() == null || getRepository().getCouponPackModel().couponModelList == null ||
                getRepository().getCouponPackModel().couponModelList.isEmpty()) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.pay_no_voucher));
        } else {
            GoPageUtil.goPage(getStater(), FormatPageModel.getPageModelPay(getRepository().getCode(), getRepository().getCouponPackModel().couponModelList,
                    AppContextProvider.getInstance().getContext().getString(R.string.pay_live_content), false, "livePrevue", AppContextProvider.getInstance().getContext().getString(R.string.dialog_buy_pay)));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case PayConstants.EVENT_PAY:
                PayEventModel payEventModel = event.getObject(PayEventModel.class);
                if (payEventModel != null && payEventModel.isPayed()) {
                    List<CouponModel> couponModelList = getRepository().getCouponPackModel().couponModelList;
                    for (CouponModel couponModel : couponModelList) {
                        if (payEventModel.getGoodsNo().equals(couponModel.getCode()) && getUIView() != null) {
                            getRepository().setBuy(payEventModel.isPayed());
                            getUIView().showPay(!payEventModel.isPayed());
                            break;
                        }
                    }
                }
                break;
            case ProgramConstants.EVENT_LOGIN_SUCCESS:
                getDetailInfo();
                break;
        }
    }


    //================bi埋点==================//

    /**
     * 预约
     */
    private void livePrevue() {
        LogInfoParam.Builder builder = getGeneralBuilder(PREVUE_CLICK);
        if (builder != null) {
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

    @Override
    protected LogInfoParam.Builder getGeneralBuilder(String eventId) {
        LiveDetailsModel model = getRepository().getSeverModel();
        if (model != null) {
            LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                    .setEventId(eventId)
                    .setCurrentPageId(ROOT_LIVE_PREVUE)
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_CHARGEABLE, String.valueOf(model.getIsChargeable()))
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, model.getCode())
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, model.getDisplayName());

            return builder;
        }
        return null;
    }

    @Override
    protected boolean isNeedBrowseBuried() {
        return true;
    }

}

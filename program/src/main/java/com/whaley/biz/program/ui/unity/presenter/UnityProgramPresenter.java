package com.whaley.biz.program.ui.unity.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.PayConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.CheckLogin;
import com.whaley.biz.program.interactor.GetDetailInfo;
import com.whaley.biz.program.interactor.GetLiveDetail;
import com.whaley.biz.program.interactor.IsPayed;
import com.whaley.biz.program.model.CouponModel;
import com.whaley.biz.program.model.CouponPackModel;
import com.whaley.biz.program.model.PayEventModel;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.biz.program.model.pay.PayResultInfo;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.model.response.LiveDetailsResponse;
import com.whaley.biz.program.model.response.ProgramDetailResponse;
import com.whaley.biz.program.ui.unity.UnityProgramView;
import com.whaley.biz.program.ui.unity.repository.UnityProgramRepository;
import com.whaley.biz.program.utils.CouponPackUtil;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.biz.program.utils.PayUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/30.
 */

public class UnityProgramPresenter extends BasePagePresenter<UnityProgramView> implements ProgramConstants {

    static final String STR_PARAM_UNITY_PAY = "str_unity_pay";
    static final String EVENT_LOGIN_SUCCESS = "login_success";
    static final int LOGIN_REQUEST_CODE = 101;

    @Repository
    UnityProgramRepository repository;

    public UnityProgramPresenter(UnityProgramView view) {
        super(view);
    }

    public UnityProgramRepository getRepository() {
        return repository;
    }

    @UseCase
    GetDetailInfo getDetailInfo;

    @UseCase
    GetLiveDetail getLiveDetail;

    @UseCase
    IsPayed isPayed;

    private Disposable liveDisposable, programDisposable, isPayedDisposable;

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
            ProgramDetailModel programDetailModel = GsonUtil.getGson().fromJson(arguments.getString(STR_PARAM_UNITY_PAY), ProgramDetailModel.class);
            if (programDetailModel != null) {
                getRepository().setProgramDetailModel(programDetailModel);
                getRepository().setCode(programDetailModel.getCode());
                getRepository().setProgramType(programDetailModel.getProgramType());
                getRepository().setPlayNum();
                getRepository().setCouponModel();
            }
        }
        regist();
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        onUnityPay();
    }

    @Override
    public void onDestroy() {
        PayResultInfo payResultInfo = new PayResultInfo(getRepository().getCode(), getRepository().isPayed(), getRepository().isHasBeenPaid());
        Router.getInstance().buildExecutor("/unity/service/pay").putObjParam(GsonUtil.getGson().toJson(payResultInfo)).excute();
        super.onDestroy();
        unRegist();
        if (programDisposable != null) {
            programDisposable.dispose();
        }
        if (liveDisposable != null) {
            liveDisposable.dispose();
        }
        if (isPayedDisposable != null) {
            isPayedDisposable.dispose();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case PayConstants.EVENT_PAY:
                PayEventModel payEventModel = event.getObject(PayEventModel.class);
                if (payEventModel != null && payEventModel.isPayed()) {
                    String payGoodNo = payEventModel.getGoodsNo();
                    CouponPackModel couponPackModel = getRepository().getCouponPackModel();
                    if (couponPackModel != null && couponPackModel.couponModelList != null && couponPackModel.couponModelList.size() > 0) {
                        for (CouponModel couponModel : couponPackModel.couponModelList) {
                            if (payGoodNo.equals(couponModel.getCode())) {
                                getRepository().setPayed(payEventModel.isPayed());
                                break;
                            }
                        }
                    }else{
                        getRepository().setPayed(false);
                    }
                }else{
                    getRepository().setPayed(false);
                }
                if (getUIView() != null) {
                    getUIView().finishView();
                }
                break;
            case EVENT_LOGIN_SUCCESS:
                Router.getInstance().buildExecutor("/unity/service/login").excute();
                checkPay();
                break;
            case ProgramConstants.EVENT_NOT_LOGIN_PAY:
                if (getUIView() != null) {
                    getUIView().finishView();
                }
                break;
        }
    }

    public void getVideoDetailData() {
        if (programDisposable != null) {
            programDisposable.dispose();
        }
        GetDetailInfo.Param param = new GetDetailInfo.Param();
        param.setCode(getRepository().getCode());
        programDisposable = getDetailInfo.buildUseCaseObservable(param).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<ProgramDetailResponse>(getUIView(), true) {
                    @Override
                    public void onNext(@NonNull ProgramDetailResponse programDetailResponse) {
                        super.onNext(programDetailResponse);
                        if (programDetailResponse != null && programDetailResponse.getData() != null) {
                            getRepository().setCouponPackModel(CouponPackUtil.getCouponPackModel(programDetailResponse.getData().getCouponDto(),
                                    programDetailResponse.getData().getContentPackageQueryDtos(), false));
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        onUnityPay();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        onUnityPay();
                    }
                });
    }

    public void getLiveDetail() {
        if (liveDisposable != null) {
            liveDisposable.dispose();
        }
        GetLiveDetail.Param param = new GetLiveDetail.Param();
        param.setCode(getRepository().getCode());
        liveDisposable = getLiveDetail.buildUseCaseObservable(param).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<LiveDetailsResponse>(getUIView(), true) {
                    @Override
                    public void onNext(@NonNull LiveDetailsResponse liveDetailsResponse) {
                        super.onNext(liveDetailsResponse);
                        if (liveDetailsResponse != null && liveDetailsResponse.getData() != null) {
                            getRepository().setCouponPackModel(CouponPackUtil.getCouponPackModel(liveDetailsResponse.getData().getCouponDto(),
                                    liveDetailsResponse.getData().getContentPackageQueryDtos(), false));
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        onUnityPay();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        onUnityPay();
                    }
                });
    }

    private void checkPay() {
        if (isPayedDisposable != null) {
            isPayedDisposable.dispose();
        }
        IsPayed.Param param = new IsPayed.Param(getRepository().getCode(), getRepository().getProgramType());
        isPayedDisposable = isPayed.buildUseCaseObservable(param).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean isPayed) {
                getRepository().setPayed(isPayed);
                getRepository().setHasBeenPaid(isPayed);
                if (isPayed) {
                    if (getUIView() != null) {
                        getUIView().finishView();
                    }
                } else {
                    if (TYPE_LIVE.equals(getRepository().getProgramType())) {
                        getLiveDetail();
                    } else {
                        getVideoDetailData();
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getRepository().setPayed(false);
                getRepository().setHasBeenPaid(false);
                if (TYPE_LIVE.equals(getRepository().getProgramType())) {
                    getLiveDetail();
                } else {
                    getVideoDetailData();
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 如果unity回调购买页面
     */
    public void onUnityPay() {
        CheckLogin checkLogin = new CheckLogin();
        checkLogin.buildUseCaseObservable(null).subscribe(new DisposableObserver<UserModel>() {
            @Override
            public void onNext(@NonNull UserModel userModel) {
                if (userModel.isLoginUser()) {
                    buy();
                } else {
                    DialogUtil.showDialog(getStater().getAttatchContext(),
                            AppContextProvider.getInstance().getContext().getString(R.string.dialog_buy_pay), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TitleBarActivity.goPage(getStater(), LOGIN_REQUEST_CODE, "/user/ui/login");
                                }

                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (getUIView() != null) {
                                        getUIView().finishView();
                                    }
                                }
                            });
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

    private void buy() {
        if (getRepository().getCouponPackModel() == null) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.pay_no_voucher));
        } else {
            final String payIntro = AppContextProvider.getInstance().getContext().getString(R.string.pay_recorded_content) +
                    getDateFromMileSeconds(getRepository().getProgramDetailModel().getDisableTimeDate());
          GoPageUtil.goPage(getStater(), FormatPageModel.getPageModelPay(getRepository().getCode(), getRepository().getCouponPackModel().couponModelList,
                    payIntro, true, getRepository().getProgramType(), AppContextProvider.getInstance().getContext().getString(R.string.dialog_buy_pay)));
        }
    }

    private String getDateFromMileSeconds(long seconds) {
        if (seconds <= 0)
            return "";
        else {
            Date date = new Date();
            try {
                date.setTime(seconds);
            } catch (NumberFormatException nfe) {
                return "";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
            return sdf.format(date);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CheckLogin checkLogin = new CheckLogin();
        checkLogin.buildUseCaseObservable(null).subscribe(new DisposableObserver<UserModel>() {
            @Override
            public void onNext(@NonNull UserModel userModel) {
                if (!userModel.isLoginUser()) {
                    if (getUIView() != null) {
                        getUIView().finishView();
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

}

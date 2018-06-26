package com.whaley.biz.program.ui.unity.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.response.BaseResponse;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.PayConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.CheckLogin;
import com.whaley.biz.program.interactor.MergePackage;
import com.whaley.biz.program.interactor.mapper.PackageViewModelMapper;
import com.whaley.biz.program.model.CouponModel;
import com.whaley.biz.program.model.PackageListModel;
import com.whaley.biz.program.model.PayEventModel;
import com.whaley.biz.program.model.pay.PayResultInfo;
import com.whaley.biz.program.model.pay.ThirdPayModel;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.ui.arrange.TopicView;
import com.whaley.biz.program.ui.arrange.presenter.BaseTopicPresenter;
import com.whaley.biz.program.ui.unity.repository.UnityPackageRepository;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.uiview.viewmodel.TopicHeadViewModel;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/30.
 */

public class UnityPackagePresenter extends BaseTopicPresenter<TopicView> {

    static final String STR_JSON = "str_json";
    static final String EVENT_LOGIN_SUCCESS = "login_success";
    static final int LOGIN_REQUEST_CODE = 102;

    @Repository
    UnityPackageRepository packageRepository;

    @UseCase
    MergePackage mergePackage;


    @UseCase
    PackageViewModelMapper mapper;

    @UseCase
    CheckLogin checkLogin;

    Disposable initDisposable;

    public UnityPackagePresenter(TopicView view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        regist();
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        if (arguments != null) {
            String json = arguments.getString(STR_JSON);
            PackageListModel packageListModel = GsonUtil.getGson().fromJson(json, PackageListModel.class);
            packageRepository.setId(packageListModel.getPack().getCode());
            initData(packageListModel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PayResultInfo payResultInfo = new PayResultInfo(packageRepository.getId(), packageRepository.isPay(), packageRepository.isHasBeenPaid());
        Router.getInstance().buildExecutor("/unity/service/pay").putObjParam(GsonUtil.getGson().toJson(payResultInfo)).excute();
        if (initDisposable != null) {
            initDisposable.dispose();
        }
        unRegist();
    }

    private void initData(PackageListModel packageListModel) {
        if (initDisposable != null) {
            initDisposable.dispose();
        }
        initDisposable = Observable.just(packageListModel)
                .map(build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<RecyclerViewModel>(getUIView()) {
                    @Override
                    public void onNext(@NonNull RecyclerViewModel o) {
                        getBaseTopicRepository().setRecyclerViewModel(o);
                        getUIView().updata(o);
                        onUnityPay();
                    }
                });
    }

    @Override
    public TopicHeadViewModel getTopicHead() {
        return packageRepository.getTopicHead();
    }

    public Observable<? extends BaseResponse> onObservable() {
        return mergePackage.buildUseCaseObservable(packageRepository.getId());
    }

    public Function<? super Object, RecyclerViewModel> build() {
        return mapper.buildFunction();
    }


    public boolean isShowPayBotton() {
        if (packageRepository.isChargeable() && !packageRepository.isPay()) {
            return true;
        } else {
            return false;
        }
    }

    public String getPayString() {
        return packageRepository.getPayString();
    }

    public void onUnityPay() {
        CheckLogin checkLogin = new CheckLogin();
        checkLogin.buildUseCaseObservable(null).subscribe(new DisposableObserver<UserModel>() {
            @Override
            public void onNext(@NonNull UserModel userModel) {
                if (userModel.isLoginUser()) {
                    onPayClick();
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

    public void onPayClick() {
        GoPageUtil.goPage(getStater(), FormatPageModel.getPageModelPay(packageRepository.getId(), packageRepository.getCouponModels(),
                "", true,
                ProgramConstants.TYPE_CONTENT_PACKGE, AppContextProvider.getInstance().getContext().getString(R.string.dialog_buy_pay)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case PayConstants.EVENT_PAY:
                PayEventModel payEventModel = event.getObject(PayEventModel.class);
                if (payEventModel == null || !payEventModel.isPayed()) {
                    packageRepository.setPay(false);
                } else if ((packageRepository.getId().equals(payEventModel.getProgramCode()))
                        || (packageRepository.getCouponModels() != null
                        && packageRepository.getCouponModels().get(0) != null
                        && payEventModel.getGoodsNo().equals(packageRepository.getCouponModels().get(0).getCode()))
                        ) {
                    packageRepository.setPay(payEventModel.isPayed());
                }else{
                    packageRepository.setPay(false);
                }
                if (getUIView() != null) {
                    getUIView().finishView();
                }
                break;
            case EVENT_LOGIN_SUCCESS:
                Router.getInstance().buildExecutor("/unity/service/login").excute();
                getData();
                break;
            case ProgramConstants.EVENT_NOT_LOGIN_PAY:
                if (getUIView() != null) {
                    getUIView().finishView();
                }
                break;
        }
    }

    @Override
    public void getData() {
        dispose();
        disposable = onObservable()
                .doOnNext(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(@NonNull BaseResponse baseResponse) throws Exception {
                        if (packageRepository.isHasBeenPaid()) {
                            throw new HasBeenPaidException("已购买");
                        }
                    }
                })
                .map(new Function<BaseResponse, Object>() {
                    @Override
                    public Object apply(@NonNull BaseResponse baseResponse) throws Exception {
                        return baseResponse.getData();
                    }
                })
                .map(build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<RecyclerViewModel>(getUIView(), true, getBaseTopicRepository().isShowEmpty()) {
                    @Override
                    public void onNext(@NonNull RecyclerViewModel o) {
                        getBaseTopicRepository().setRecyclerViewModel(o);
                        getUIView().updata(o);
                        onUnityPay();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HasBeenPaidException) {
                            if (getUIView() != null) {
                                getUIView().finishView();
                            }
                        }else {
                            super.onError(e);
                        }
                    }
                });
    }


    @Override
    public Observable<RecyclerViewModel> onCreatedObservable() {
        return null;
    }

    public static class HasBeenPaidException extends Exception {

        public HasBeenPaidException(String msg) {
            super(msg);
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

    @Override
    protected String getPageId() {
        return null;
    }

}

package com.whaley.biz.setting.ui.presenter;

import android.os.Bundle;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.setting.event.ModifyAddressEvent;
import com.whaley.biz.setting.interactor.CheckLogin;
import com.whaley.biz.setting.interactor.SaveAddress;
import com.whaley.biz.setting.model.AddressModel;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.biz.setting.response.AddressResponse;
import com.whaley.biz.setting.ui.repository.AddressRepository;
import com.whaley.biz.setting.ui.view.AddressView;
import com.whaley.biz.setting.ui.viewmodel.AddressViewModel;
import com.whaley.biz.sign.SignType;
import com.whaley.biz.sign.SignUtil;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by dell on 2017/8/3.
 */

public class AddressPresenter extends BasePagePresenter<AddressView> implements CommonConstants {

    @Repository
    AddressRepository repository;

    @UseCase
    SaveAddress saveAddress;

    Disposable disposable1,disposable2;

    public static final String STR_ADDRESS = "str_address";

    public AddressPresenter(AddressView view) {
        super(view);
    }

    public AddressRepository getAddressRepository() {
        return repository;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
            AddressViewModel addressModel = (AddressViewModel) arguments.get(STR_ADDRESS);
            getAddressRepository().setAddressViewModel(addressModel);
        }
    }

    public void CheckLogin() {
        CheckLogin checkLogin = new CheckLogin();
        disposable1 = checkLogin.buildUseCaseObservable(null)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserModel>() {
            @Override
            public void onNext(@NonNull UserModel userModel) {
                saveAddress(userModel.getAccount_id());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void saveAddress(String account_id) {
        final AddressViewModel addressViewModel = getAddressRepository().getAddressViewModel();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        AddressModel addressModel = new AddressModel();
        addressModel.setWhaleyuid(account_id);
        addressModel.setTimestamp(timestamp);
        addressModel.setName(addressViewModel.getName());
        addressModel.setAddress(addressViewModel.getAddress());
        addressModel.setMobile(addressViewModel.getMobile());
        SignUtil.SignBuilder signBuilder = SignUtil.builder().signType(SignType.TYPE_WHALEYVR)
                .put("whaleyuid", account_id).put("name", addressViewModel.getName())
                .put("address", addressViewModel.getAddress())
                .put("mobile", addressViewModel.getMobile())
                .put("timestamp", timestamp)
                .put(KEY_APP_NAME, VALUE_APP_NAME)
                .put(KEY_APP_VERSION, VALUE_APP_VERSION_NAME)
                .put(KEY_APP_VERSION_CODE, VALUE_APP_VERSION_CODE)
                .put(KEY_SYSTEM_NAME, VALUE_SYSTEM_NAME)
                .put(KEY_SYSTEM_VERSION, VALUE_SYSTEM_VERSION);
        addressModel.setSign(signBuilder.getSign());
        if(disposable2!=null){
            disposable2.dispose();
        }
        disposable2 = saveAddress.buildUseCaseObservable(addressModel)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<AddressResponse>(getUIView(), true) {
                    @Override
                    public void onNext(@NonNull AddressResponse addressResponse) {
                        super.onNext(addressResponse);
                        if (getUIView() != null) {
                            getUIView().showToast("地址修改成功");
                        }
                        EventController.postEvent(new ModifyAddressEvent(addressViewModel));
                        if (getUIView() != null) {
                            getUIView().finishView();
                        }
                    }

                    @Override
                    public void onFinalError(Throwable e) {
                        super.onFinalError(e);
                        if (getUIView() != null) {
                            getUIView().showToast("地址修改失败：" + e.getMessage());
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable1 != null) {
            disposable1.dispose();
        }
        if (disposable2 != null) {
            disposable2.dispose();
        }
    }

}

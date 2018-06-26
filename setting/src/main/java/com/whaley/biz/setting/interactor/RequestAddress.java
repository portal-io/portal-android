package com.whaley.biz.setting.interactor;

import android.text.TextUtils;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.setting.model.AddressModel;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.biz.setting.ui.repository.GiftRepository;
import com.whaley.biz.setting.ui.viewmodel.AddressViewModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.biz.setting.api.GiftAPI;
import com.whaley.biz.setting.response.AddressResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/7/25.
 */

public class RequestAddress extends UseCase<AddressViewModel, RequestAddress.RequestAddressParam> {

    public RequestAddress(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<AddressViewModel> buildUseCaseObservable(final RequestAddressParam param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .map(new Function<UserModel, UserModel>() {
                    @Override
                    public UserModel apply(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            return userModel;
                        }
                        throw new NotLoggedInErrorException("未登录");
                    }
                }).flatMap(new Function<UserModel, ObservableSource<AddressResponse>>() {
            @Override
            public ObservableSource<AddressResponse> apply(@NonNull UserModel userModel) throws Exception {
                return getRepositoryManager().obtainRemoteService(GiftAPI.class)
                        .requestAddress(userModel.getAccount_id(), param.getTimestamp());
            }
        }) .map(new CommonFunction<AddressResponse,AddressModel>())
                .map(new Function<AddressModel, AddressViewModel>() {
                    @Override
                    public AddressViewModel apply(@NonNull AddressModel addressModel) throws Exception {
                        AddressViewModel addressViewModel = new AddressViewModel();
                        if(addressModel!=null) {
                            addressViewModel.setName(addressModel.getName());
                            addressViewModel.setMobile(addressModel.getMobile());
                            StringBuilder sb = new StringBuilder();
                            if(!TextUtils.isEmpty(addressModel.getProvince())){
                                sb.append(addressModel.getProvince());
                            }
                            if(!TextUtils.isEmpty(addressModel.getCity())){
                                sb.append(addressModel.getCity());
                            }
                            if(!TextUtils.isEmpty(addressModel.getCounty())){
                                sb.append(addressModel.getCounty());
                            }
                            sb.append(addressModel.getAddress());
                            addressViewModel.setAddress(sb.toString());
                        }
                        return addressViewModel;
                    }
                })
                .doOnNext(new Consumer<AddressViewModel>() {
                    @Override
                    public void accept(@NonNull AddressViewModel addressViewModel) throws Exception {
                        String text = "";
                        if(addressViewModel!=null){
                            text = addressViewModel.getAddress();
                        }
                        GiftRepository giftRepository = getRepositoryManager().obtainMemoryService(GiftRepository.class);
                        if(TextUtils.isEmpty(text)){
                            giftRepository.setAddress("");
                            giftRepository.setBtnText("添加");
                        }else{
                            giftRepository.setAddress("当前地址：" + text);
                            giftRepository.setBtnText("修改");
                        }
                        giftRepository.setAddressViewModel(addressViewModel);
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        GiftRepository giftRepository = getRepositoryManager().obtainMemoryService(GiftRepository.class);
                        giftRepository.setAddress("");
                        giftRepository.setBtnText("添加");
                        giftRepository.setAddressViewModel(null);
                    }
                });
    }

    public static class RequestAddressParam{

        private String timestamp;

        public RequestAddressParam(String timestamp){
            this.timestamp = timestamp;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

    }

}


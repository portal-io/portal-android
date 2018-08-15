package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.model.LoginModel;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.model.portal.SyncResponse;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.api.UserVrApi;
import com.whaley.biz.user.ui.repository.RegisterRepository;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date: 2017/7/17
 */

public class MobilePhoneLogin extends BaseUseCase<UserModel, MobilePhoneLogin.MobilePhoneLoginParam> {


    public MobilePhoneLogin(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<UserModel> buildUseCaseObservable(UseCaseParam<MobilePhoneLoginParam> param) {
        Observable<UserModel> observable =
                getRepositoryManager()
                        .obtainRemoteService(UserVrApi.class)
                        .login(param.getParam().mobile,
                                param.getParam().code,
                                UserConstants.NCODE,
                                UserConstants.FROM,
                                UserManager.getInstance().getDeviceId(),
                                param.getParam().thirdID,
                                param.getParam().unionid)
                        .map(new CommonFunction<WhaleyResponse<LoginModel>, LoginModel>())
                        .map(new Function<LoginModel, UserModel>() {
                            @Override
                            public UserModel apply(@NonNull LoginModel loginBean) throws Exception {
                                UserModel userModel = loginBean.convertToUserBean();
                                userModel.changeToAddInformation();
                                return userModel;
                            }
                        })
                        .observeOn(Schedulers.io())
                        .doOnNext(new Consumer<UserModel>() {
                            @Override
                            public void accept(final UserModel userModel) throws Exception {
                                SyncPortal syncPortal = new SyncPortal(getRepositoryManager());
                                syncPortal.buildUseCaseObservable(userModel.getAccount_id())
                                        .subscribe(new Consumer<SyncResponse>() {
                                            @Override
                                            public void accept(SyncResponse syncResponse) throws Exception {

                                                userModel.setPortalAccessToken(syncResponse.getAccessTokenModel().getAccess_token());
                                                userModel.setPortalAddress(syncResponse.getUserInfoModel().getPortalAddress());
                                            }
                                        });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Consumer<UserModel>() {
                    @Override
                    public void accept(@NonNull UserModel userBean) throws Exception {
                        if (!userBean.isAddInformation()) {
                            UserManager.getInstance().saveLoginUser(userBean);
                        }
                    }
                });
        return observable;
    }

    public void executePhoneLogin(DisposableObserver<UserModel> updateUIObserver) {
        RegisterRepository registerRepository = getRepositoryManager().obtainMemoryService(RegisterRepository.class);
        UserModel user = registerRepository.getUserModel();
        String thirdID = null;
        String unionid = null;
        if (user != null) {
            thirdID = user.getThird_id();
            unionid = user.getUnionid();
        }
        MobilePhoneLoginParam param = new MobilePhoneLoginParam(registerRepository.getPhone(), registerRepository.getSmsCode(), thirdID, unionid);
        execute(updateUIObserver, new UseCaseParam(param));
    }

    public static class MobilePhoneLoginParam {
        private String mobile;
        private String code;
        private String thirdID;
        private String unionid;

        MobilePhoneLoginParam(String mobile, String code, String thirdID, String unionid) {
            this.mobile = mobile;
            this.code = code;
            this.thirdID = thirdID;
            this.unionid = unionid;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }


        public String getThirdID() {
            return thirdID;
        }

        public void setThirdID(String thirdID) {
            this.thirdID = thirdID;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }
    }
}

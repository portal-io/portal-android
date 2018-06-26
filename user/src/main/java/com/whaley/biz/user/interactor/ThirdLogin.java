package com.whaley.biz.user.interactor;


import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.common.response.BaseResponse;
import com.whaley.biz.common.response.Response;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.model.AccessTokenModel;
import com.whaley.biz.user.model.LoginModel;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.api.UserVrApi;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.StrUtil;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Administrator on 2017/7/18.
 */

public class ThirdLogin extends BaseUseCase<UserModel, UserModel> {
    public ThirdLogin(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<UserModel> buildUseCaseObservable(final UseCaseParam<UserModel> param) {
        return getRepositoryManager()
                .obtainRemoteService(UserVrApi.class)
                .thirdLogin(param.getParam().getOpenid(),
                        param.getParam().getOrgin(),
                        UserConstants.FROM,
                        UserManager.getInstance().getDeviceId(),
                        param.getParam().getNickname(),
                        param.getParam().getUnionid(),
                        param.getParam().getAvatar(),
                        param.getParam().getArea(),
                        param.getParam().getGender())
                .map(new CommonFunction<WhaleyResponse<LoginModel>, UserModel>() {

                    @Override
                    protected UserModel getData(BaseResponse response) {
                        UserModel userModel = param.getParam();
                        WhaleyResponse<LoginModel> whaleyResponse = (WhaleyResponse<LoginModel>) response;
                        LoginModel loginModel = whaleyResponse.getData();
                        if (whaleyResponse.checkStatus()) {
                            if (!StrUtil.isEmpty(loginModel.getAccesstoken())) {
                                userModel.setAccessTokenModel(new AccessTokenModel(loginModel.getAccesstoken(), loginModel.getRefreshtoken(), loginModel.getExpiretime()));
                            }
                            if (StrUtil.isEmpty(loginModel.getUsername()) || loginModel.getUsername().startsWith("vr_")) {
                                userModel.setAddInformation(true);
                            } else {
                                userModel.setAddInformation(false);
                                userModel.setNickname(loginModel.getUsername());
                            }
                            userModel.setAvatar(loginModel.getAvatar());
                            userModel.setMobile(loginModel.getMobile());
                            userModel.setAccount_id(loginModel.getHeliosid());
                            return userModel;
                        }
                        if (response.getStatus() == 144) {
                            userModel.setThird_id(whaleyResponse.getData().getThird_id());
                        }
                        return userModel;
                    }
                });
    }

    public Observable<UserModel> buildUseCaseObservable(UserModel userModel) {
        return buildUseCaseObservable(new UseCaseParam(userModel));
    }
}

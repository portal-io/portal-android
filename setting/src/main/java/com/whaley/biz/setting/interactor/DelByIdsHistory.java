package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.setting.util.RequestUtils;
import com.whaley.biz.setting.api.UserHistoryApi;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.AppUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/8/24.
 */

public class DelByIdsHistory extends UseCase<CMSResponse, DelByIdsHistory.Param> {

    public DelByIdsHistory(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<CMSResponse> buildUseCaseObservable(final DelByIdsHistory.Param param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<CMSResponse>>() {
                    @Override
                    public ObservableSource<CMSResponse> apply(@NonNull UserModel userModel) throws Exception {
                        final String uid;
                        final String deviceId;
                        if(userModel.isLoginUser()){
                            uid = userModel.getAccount_id();
                            deviceId = userModel.getDeviceId();
                        }else{
                            uid = null;
                            deviceId = AppUtil.getDeviceId();
                        }
                        return getRepositoryManager().obtainRemoteService(UserHistoryApi.class)
                                .delByIds(param.getDataSource(), uid, deviceId, param.getDelIds(),
                                        RequestUtils.getHistorySign(param.getDataSource(), uid, deviceId, param.getDelIds()));
                    }
                }).map(new ResponseFunction<CMSResponse,CMSResponse>());
    }

    public static class Param{

        private String dataSource;
        private String delIds;

        public Param(String dataSource, String delIds){
            this.dataSource = dataSource;
            this.delIds = delIds;
        }

        public String getDataSource() {
            return dataSource;
        }

        public void setDataSource(String dataSource) {
            this.dataSource = dataSource;
        }

        public String getDelIds() {
            return delIds;
        }

        public void setDelIds(String delIds) {
            this.delIds = delIds;
        }
    }

}

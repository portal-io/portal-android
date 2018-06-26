package com.whaley.biz.pay.interactor;

import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.pay.PayApi;
import com.whaley.biz.pay.PayUtil;
import com.whaley.biz.pay.model.program.PayResultModel;
import com.whaley.biz.pay.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Author: qxw
 * Date:2017/7/20
 * Introduction:批量查询购买接口
 */

public class IsPayedList extends UseCase<List<PayResultModel>, IsPayedList.IsPayedListModel> {


    public IsPayedList(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<List<PayResultModel>> buildUseCaseObservable(final IsPayedListModel param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<CMSResponse<List<PayResultModel>>>>() {
                    @Override
                    public ObservableSource<CMSResponse<List<PayResultModel>>> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager()
                                .obtainRemoteService(PayApi.class)
                                .goodsPayedList(userModel.getAccount_id(), param.goodsNos, param.goodsTypes,
                                        PayUtil.getSign(userModel.getAccount_id(), param.goodsNos, param.goodsTypes));
                    }
                })
                .map(new CommonFunction<CMSResponse<List<PayResultModel>>, List<PayResultModel>>());
    }

    public static class IsPayedListModel {
        private String goodsNos;
        private String goodsTypes;

        public String getGoodsNos() {
            return goodsNos;
        }

        public void setGoodsNos(String goodsNos) {
            this.goodsNos = goodsNos;
        }

        public String getGoodsTypes() {
            return goodsTypes;
        }

        public void setGoodsTypes(String goodsTypes) {
            this.goodsTypes = goodsTypes;
        }
    }
}

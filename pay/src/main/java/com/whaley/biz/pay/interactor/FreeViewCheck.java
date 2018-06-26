package com.whaley.biz.pay.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.pay.PayApi;
import com.whaley.biz.pay.model.FreeViewModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/9/7 21:31.
 */

public class FreeViewCheck extends UseCase<CMSResponse<FreeViewModel>, FreeViewCheck.Param> {

    public FreeViewCheck(){
        super(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<CMSResponse<FreeViewModel>> buildUseCaseObservable(Param param) {
        return getRepositoryManager().obtainRemoteService(PayApi.class)
                .freeViewCheck(param.getCode(), param.getDeciceno())
                .map(new ResponseFunction<CMSResponse<FreeViewModel>, CMSResponse<FreeViewModel>>());
    }

    public static class Param {
        private String code;
        private String deciceno;

        public void setCode(String code) {
            this.code = code;
        }

        public void setDeciceno(String deciceno) {
            this.deciceno = deciceno;
        }

        public String getDeciceno() {
            return deciceno;
        }

        public String getCode() {
            return code;
        }
    }
}

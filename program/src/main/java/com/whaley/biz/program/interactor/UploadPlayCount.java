package com.whaley.biz.program.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.response.BaseResponse;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.program.api.StatisticApi;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.model.CountModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/9/19 21:12.
 */

public class UploadPlayCount extends UseCase<CMSResponse<CountModel>, UploadPlayCount.Param> {

    public UploadPlayCount() {
        super(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<CMSResponse<CountModel>> buildUseCaseObservable(Param param) {
        return getRepositoryManager().obtainRemoteService(StatisticApi.class)
                .updateCount(param.getItemid(), param.programType, param.videoType, "play")
                .map(new ResponseFunction<CMSResponse<CountModel>, CMSResponse<CountModel>>());
    }

    public static class Param {
        private String itemid;
        private String programType;
        private String videoType;

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getProgramType() {
            return programType;
        }

        public void setProgramType(String programType) {
            this.programType = programType;
        }

        public String getVideoType() {
            return videoType;
        }

        public void setVideoType(String videoType) {
            this.videoType = videoType;
        }
    }
}

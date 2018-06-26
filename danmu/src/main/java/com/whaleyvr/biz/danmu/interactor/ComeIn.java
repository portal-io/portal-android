package com.whaleyvr.biz.danmu.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RemoteRepository;
import com.whaley.core.repository.RepositoryManager;
import com.whaleyvr.biz.danmu.api.DanmuAPI;
import com.whaleyvr.biz.danmu.response.ComeInReponse;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/7/24.
 */

public class ComeIn extends BaseUseCase<ComeInReponse, ComeIn.ComeInParam> {

    public ComeIn(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<ComeInReponse> buildUseCaseObservable(UseCaseParam<ComeInParam> param) {
        return getRepositoryManager().obtainRemoteService(DanmuAPI.class)
                .comein(param.getParam().getSid(), param.getParam().getTitle(),
                        param.getParam().getTimestamp())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ResponseFunction<ComeInReponse, ComeInReponse>());
    }

    public static class ComeInParam{

        private String sid;
        private String title;
        private String timestamp;

        public ComeInParam(String sid, String title, String timestamp){
            this.sid = sid;
            this.title = title;
            this.timestamp = timestamp;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

}

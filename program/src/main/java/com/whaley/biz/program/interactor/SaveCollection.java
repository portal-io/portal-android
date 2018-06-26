package com.whaley.biz.program.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.program.api.CollectionAPI;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.response.StringResponse;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/9/1
 * Introduction:
 */

public class SaveCollection extends UseCase<String, SaveCollection.Param> {

    public SaveCollection() {
    }

    public SaveCollection(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<String> buildUseCaseObservable(final Param param) {
        CheckLogin checkLogin = new CheckLogin(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            return getRepositoryManager().obtainRemoteService(CollectionAPI.class)
                                    .saveCollection(userModel.getAccount_id(),
                                            userModel.getNickname(),
                                            param.code,
                                            param.programName,
                                            param.videoType,
                                            param.programType,
                                            param.status,
                                            param.duration,
                                            param.picUrl)
                                    .map(new CommonFunction<StringResponse, String>());
                        } else {
                            throw new NotLoggedInErrorException("未登录");
                        }
                    }
                });
    }

    public static class Param {
        String code;
        String programName;
        String videoType;
        String programType = ProgramConstants.TYPE_RECORDED;
        int status = 1;
        int duration;
        String picUrl;

        public Param(String code, String programName, String videoType, int duration, String picUrl) {
            this.code = code;
            this.programName = programName;
            this.videoType = videoType;
            this.duration = duration;
            this.picUrl = picUrl;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getProgramName() {
            return programName;
        }

        public void setProgramName(String programName) {
            this.programName = programName;
        }

        public String getVideoType() {
            return videoType;
        }

        public void setVideoType(String videoType) {
            this.videoType = videoType;
        }

        public String getProgramType() {
            return programType;
        }

        public void setProgramType(String programType) {
            this.programType = programType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }
    }
}

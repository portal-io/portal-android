package com.whaley.biz.user.interactor;


import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.api.UserVrApi;
import com.whaley.biz.user.interactor.function.RefreshTokenFunction;
import com.whaley.biz.user.model.ImageModel;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.R.attr.data;

/**
 * Author: qxw
 * Date:2017/7/19
 * Introduction:
 */

public class UpdateAvatar extends UseCase<ImageModel, String> {

    public UpdateAvatar() {
    }

    public UpdateAvatar(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<ImageModel> buildUseCaseObservable(final String s) {
        File avatarFile = new File(s);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), avatarFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("Filedata", avatarFile.getName(), photoRequestBody);
        MultipartBody.Part accessTokenPart = MultipartBody.Part.createFormData("accesstoken", UserManager.getInstance().getAccesstoken());
        return getRepositoryManager().obtainRemoteService(UserVrApi.class).changeNewAvatar(accessTokenPart, imagePart).map(new CommonFunction<WhaleyResponse<ImageModel>, ImageModel>())
                .doOnNext(new Consumer<ImageModel>() {
                    @Override
                    public void accept(@NonNull ImageModel imageModel) throws Exception {
                        UserModel userBean = UserManager.getInstance().getUser();
                        userBean.setAvatarTime(System.currentTimeMillis());
                        userBean.setAvatar(imageModel.getImg());
                        UserManager.getInstance().saveUser(userBean);
                    }
                })
                .onErrorResumeNext(new RefreshTokenFunction<Throwable, ImageModel>() {
                    @Override
                    public ObservableSource<ImageModel> buidModelObservable() {
                        return buildUseCaseObservable(s);
                    }
                });
    }
}

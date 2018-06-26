package com.whaley.biz.user.ui.presenter;

import android.Manifest;
import android.os.Bundle;

import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.event.UpdateNameEvent;
import com.whaley.biz.user.interactor.UpdateNickname;
import com.whaley.biz.user.ui.repository.EditNickNameRepository;
import com.whaley.biz.user.ui.view.EditNickNameView;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.utils.PermissionUtil;

import io.reactivex.annotations.NonNull;

/**
 * Author: qxw
 * Date:2017/8/3
 * Introduction:
 */

public class EditNickNamePresenter extends BaseUserPresenter<EditNickNameView> {

    @Repository
    EditNickNameRepository editNickNameRepository;

    @UseCase
    UpdateNickname updateNickName;

    public EditNickNamePresenter(EditNickNameView view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        if (UserManager.getInstance().isLogin()) {
            editNickNameRepository.setUserName(UserManager.getInstance().getUser().getNickname());
            getUIView().showNickName(editNickNameRepository.getUserName());
        } else {
            finish();
        }

    }

    public void onNickNameChanged(String nickName) {
        editNickNameRepository.setUserName(nickName);
    }

    public void onSaveClick() {
//        if (PermissionUtil.requestPermission(getUIView().getAttachActivity(), Manifest.permission.READ_PHONE_STATE, 1)) {
//            return;
//        }
        if (checkNickName(editNickNameRepository.getUserName())) {
            return;
        }
        updateNickname();
    }

    private void updateNickname() {
        updateNickName.execute(new UpdateUIObserver<String>(getUIView(), true) {
            @Override
            public void onNext(@NonNull String s) {
                super.onNext(s);
                EventController.postEvent(new UpdateNameEvent(UserConstants.EVENT_UPATE_NAME));
                getUIView().removeLoading();
                finish();
            }

            @Override
            public void onStatusError(int status, String message) {
                super.onStatusError(status, message);
                getUIView().removeLoading();
            }
        });
    }

}

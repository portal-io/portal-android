package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.user.R;
import com.whaley.biz.user.ui.repository.UserInfoRepository;
import com.whaley.biz.user.ui.view.UserInfoView;
import com.whaley.biz.user.ui.viewmodel.UserInfoViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.repository.IRepositoryManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Author: qxw
 * Date:2017/8/3
 * Introduction:
 */

//public class UserInfo extends BaseUseCase {
//
//
//    public UserInfo(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
//        super(repositoryManager, executeThread, postExecutionThread);
//    }
//
//    @Override
//    public Observable buildUseCaseObservable(Object o) {
//        return Observable.just(setUserInfoViewModel())
//                .doOnNext(new Consumer<List<UserInfoViewModel>>() {
//                    @Override
//                    public void accept(@NonNull List<UserInfoViewModel> userInfoViewModels) throws Exception {
//                        getRepositoryManager().obtainMemoryService(UserInfoRepository.class).setUserInfoViewModels(userInfoViewModels);
//                    }
//                });
//    }
//
//    private List<UserInfoViewModel> setUserInfoViewModel() {
//        List<UserInfoViewModel> settingViewModels = new ArrayList<>();
//
//    }
//
//
//    private void addUserInfoViewModel(List<UserInfoViewModel> settingViewModels, int stringID, String text, boolean isRightPic) {
//        UserInfoViewModel settingViewModel = newSettingViewModel(stringID);
//        if (isRightPic) {
//            settingViewModel.setRightPic(R.mipmap.setting_special_into);
//        }
//        if (text != null) {
//            settingViewModel.setText(text);
//        }
//        settingViewModels.add(settingViewModel);
//    }
//
//    private UserInfoViewModel newSettingViewModel(int stringID) {
//        UserInfoViewModel userInfo = new UserInfoViewModel();
//        userInfo.setName(AppContextProvider.getInstance().getContext().getString(stringID));
//        return userInfo;
//    }
//}

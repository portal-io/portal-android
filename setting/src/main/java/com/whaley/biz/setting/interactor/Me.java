package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.setting.MeType;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.ui.repository.MeRepository;
import com.whaley.biz.setting.ui.viewmodel.MeViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.repository.IRepositoryManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by dell on 2017/8/28.
 */

public class Me extends BaseUseCase implements MeType{

    public Me(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable buildUseCaseObservable(Object o) {
        return Observable.just(setMeViewModels())
                .doOnNext(new Consumer<List<MeViewModel>>() {
                    @Override
                    public void accept(@NonNull List<MeViewModel> meViewModels) throws Exception {
                        getRepositoryManager().obtainMemoryService(MeRepository.class).setMeViewModels(meViewModels);
                    }
                });
    }

    private List<MeViewModel> setMeViewModels() {
        List<MeViewModel> settingViewModels = new ArrayList<>();
        addLocalModel(settingViewModels);
        addCollectModel(settingViewModels);
//        addPayModel(settingViewModels);
//        addGiftModel(settingViewModels);
        addForumModel(settingViewModels);
        addHelpModel(settingViewModels);
//        addFeedbackModel(settingViewModels);
        addSettingModel(settingViewModels);
        addAboutModel(settingViewModels);
        return settingViewModels;
    }

    private void addLocalModel(List<MeViewModel> meViewModels){
        MeViewModel meViewModel = new MeViewModel();
//        meViewModel.setLeftPic(R.mipmap.icon_local);
        meViewModel.setRightPic(R.mipmap.setting_special_into);
        meViewModel.setName(AppContextProvider.getInstance().getContext().getString(R.string.me_local_manager));
        meViewModel.setText(AppContextProvider.getInstance().getContext().getString(R.string.me_vr_player));
        meViewModel.setType(LOCAL);
        meViewModels.add(meViewModel);
    }

    private void addCollectModel(List<MeViewModel> meViewModels){
        MeViewModel meViewModel = new MeViewModel();
//        meViewModel.setLeftPic(R.mipmap.icon_collection);
        meViewModel.setRightPic(R.mipmap.setting_special_into);
        meViewModel.setName(AppContextProvider.getInstance().getContext().getString(R.string.me_collect));
        meViewModel.setType(COLLECT);
        meViewModel.setDivederBelow(true);
        meViewModels.add(meViewModel);
    }

    private void addPayModel(List<MeViewModel> meViewModels){
        MeViewModel meViewModel = new MeViewModel();
//        meViewModel.setLeftPic(R.mipmap.icon_pay);
        meViewModel.setRightPic(R.mipmap.setting_special_into);
        meViewModel.setName(AppContextProvider.getInstance().getContext().getString(R.string.me_pay));
        meViewModel.setType(PAY);
        meViewModels.add(meViewModel);
    }

    private void addGiftModel(List<MeViewModel> meViewModels){
        MeViewModel meViewModel = new MeViewModel();
//        meViewModel.setLeftPic(R.mipmap.icon_gift);
        meViewModel.setRightPic(R.mipmap.setting_special_into);
        meViewModel.setName(AppContextProvider.getInstance().getContext().getString(R.string.me_gift));
        meViewModel.setType(GIFT);
        meViewModels.add(meViewModel);
    }

    private void addFeedbackModel(List<MeViewModel> meViewModels){
        MeViewModel meViewModel = new MeViewModel();
//        meViewModel.setLeftPic(R.mipmap.icon_feedback);
        meViewModel.setRightPic(R.mipmap.setting_special_into);
        meViewModel.setName(AppContextProvider.getInstance().getContext().getString(R.string.me_feedback));
        meViewModel.setType(FEEDBACK);
        meViewModels.add(meViewModel);
    }

    private void addForumModel(List<MeViewModel> meViewModels){
        MeViewModel meViewModel = new MeViewModel();
//        meViewModel.setLeftPic(R.mipmap.icon_forum);
        meViewModel.setRightPic(R.mipmap.setting_special_into);
        meViewModel.setName(AppContextProvider.getInstance().getContext().getString(R.string.me_forum));
        meViewModel.setType(FORUM);
        meViewModels.add(meViewModel);
    }

    private void addHelpModel(List<MeViewModel> meViewModels){
        MeViewModel meViewModel = new MeViewModel();
//        meViewModel.setLeftPic(R.mipmap.icon_help);
        meViewModel.setRightPic(R.mipmap.setting_special_into);
        meViewModel.setName(AppContextProvider.getInstance().getContext().getString(R.string.me_help));
        meViewModel.setType(HELP);
        meViewModel.setDivederBelow(true);
        meViewModels.add(meViewModel);
    }

    private void addSettingModel(List<MeViewModel> meViewModels){
        MeViewModel meViewModel = new MeViewModel();
        meViewModel.setRightPic(R.mipmap.setting_special_into);
        meViewModel.setName(AppContextProvider.getInstance().getContext().getString(R.string.me_setting));
        meViewModel.setType(SETTING);
        meViewModels.add(meViewModel);
    }

    private void addAboutModel(List<MeViewModel> meViewModels){
        MeViewModel meViewModel = new MeViewModel();
//        meViewModel.setLeftPic(R.mipmap.icon_about);
        meViewModel.setRightPic(R.mipmap.setting_special_into);
        meViewModel.setName(AppContextProvider.getInstance().getContext().getString(R.string.me_about));
        meViewModel.setText(AppContextProvider.getInstance().getContext().getString(R.string.me_join_group));
        meViewModel.setType(ABOUT);
        meViewModels.add(meViewModel);
    }

}

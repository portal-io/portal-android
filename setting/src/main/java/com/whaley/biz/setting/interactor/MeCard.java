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
 * Created by dell on 2017/10/12.
 */

public class MeCard extends BaseUseCase implements MeType {

    public MeCard(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable buildUseCaseObservable(Object o) {
        return Observable.just(setMeViewModels())
                .doOnNext(new Consumer<List<MeViewModel>>() {
                    @Override
                    public void accept(@NonNull List<MeViewModel> meViewModels) throws Exception {
                        getRepositoryManager().obtainMemoryService(MeRepository.class).setMeCardViewModels(meViewModels);
                    }
                });
    }

    private List<MeViewModel> setMeViewModels() {
        List<MeViewModel> settingViewModels = new ArrayList<>();
        addCurrencyModel(settingViewModels);
        addPayModel(settingViewModels);
        addGiftModel(settingViewModels);
        return settingViewModels;
    }

    private void addCurrencyModel(List<MeViewModel> meViewModels){
        MeViewModel meViewModel = new MeViewModel();
        meViewModel.setLeftPic(R.mipmap.me_currency);
        meViewModel.setName(AppContextProvider.getInstance().getContext().getString(R.string.me_currency));
        meViewModel.setType(CURRENCY);
        meViewModels.add(meViewModel);
    }

    private void addPayModel(List<MeViewModel> meViewModels){
        MeViewModel meViewModel = new MeViewModel();
        meViewModel.setLeftPic(R.mipmap.me_pay);
        meViewModel.setName(AppContextProvider.getInstance().getContext().getString(R.string.me_pay));
        meViewModel.setType(PAY);
        meViewModels.add(meViewModel);
    }

    private void addGiftModel(List<MeViewModel> meViewModels){
        MeViewModel meViewModel = new MeViewModel();
        meViewModel.setLeftPic(R.mipmap.me_gift);
        meViewModel.setName(AppContextProvider.getInstance().getContext().getString(R.string.me_gift));
        meViewModel.setType(GIFT);
        meViewModels.add(meViewModel);
    }

}


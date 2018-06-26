package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.setting.model.UserHistoryModel;
import com.whaley.biz.setting.ui.viewmodel.HistoryViewModel;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;

/**
 * Author: qxw
 * Date:2017/9/18
 * Introduction:
 */

public class UpdateHistory extends UseCase<List<HistoryViewModel>, UpdateHistory.Param> {

    public UpdateHistory() {
    }

    public UpdateHistory(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<List<HistoryViewModel>> buildUseCaseObservable(final UpdateHistory.Param param) {
        return Observable.create(new ObservableOnSubscribe<List<HistoryViewModel>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<HistoryViewModel>> e) throws Exception {
                List<HistoryViewModel> list = new ArrayList<HistoryViewModel>();
                list.addAll(param.historyViewModels);
                UserHistoryModel userHistoryModel = param.userHistoryModel;
                if (list != null && list.size() > 0) {
                    for (HistoryViewModel historyViewModel : list) {
                        if (historyViewModel.getCode().equals(userHistoryModel.getProgramCode())) {
                            list.remove(historyViewModel);
                            historyViewModel.setHeadType(0);
                            historyViewModel.setHeadDate("今天");
                            StringBuilder duration = new StringBuilder();
                            if ("moretv_tv".equals(userHistoryModel.getVideoType())) {
                                duration.append("第");
                                duration.append(SettingUtil.ToCH(userHistoryModel.getCurEpisode()));
                                duration.append("集  ");
                            }
                            duration.append("已看至");
                            duration.append(SettingUtil.getPercentage(userHistoryModel.getPlayTime(), userHistoryModel.getTotalPlayTime()));
                            historyViewModel.setDuration(duration.toString());
                            historyViewModel.getSeverModel().setPlayTime(userHistoryModel.getPlayTime());
                            list.add(0, historyViewModel);
                            break;
                        }
                    }
                }
                if (e.isDisposed())
                    return;
                e.onNext(list);
                e.onComplete();
            }
        });
    }

    public static class Param {
        private List<HistoryViewModel> historyViewModels;
        private UserHistoryModel userHistoryModel;

        public Param(List<HistoryViewModel> historyViewModels, UserHistoryModel userHistoryModel) {
            this.historyViewModels = historyViewModels;
            this.userHistoryModel = userHistoryModel;
        }
    }
}

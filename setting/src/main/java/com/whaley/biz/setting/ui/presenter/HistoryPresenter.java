package com.whaley.biz.setting.ui.presenter;

import android.os.Bundle;
import android.view.View;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.interactor.observer.RefreshObserver;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.interactor.UpdateHistory;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.api.UserHistoryApi;
import com.whaley.biz.setting.interactor.DelByIdsHistory;
import com.whaley.biz.setting.interactor.DelHistory;
import com.whaley.biz.setting.interactor.RequestHistory;
import com.whaley.biz.setting.model.UserHistoryModel;
import com.whaley.biz.setting.response.UserHistoryResponse;
import com.whaley.biz.setting.router.FormatPageModel;
import com.whaley.biz.setting.router.GoPageUtil;
import com.whaley.biz.setting.ui.repository.HistoryRepository;
import com.whaley.biz.setting.ui.view.HistoryView;
import com.whaley.biz.setting.ui.viewmodel.HistoryViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.utils.DateUtils;
import com.whaley.core.utils.GsonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/24.
 */

public class HistoryPresenter extends LoadPresenter<HistoryView> {

    String VIDEO_TYPE_MORETV_2D = "moretv_2d";
    String VIDEO_TYPE_MORETV_TV = "moretv_tv";
    String VIDEO_TYPE_DRAMA = "dynamic";
    @Repository
    HistoryRepository repository;

    @UseCase
    RequestHistory requestHistory;

    @UseCase
    DelHistory delHistory;

    @UseCase
    DelByIdsHistory delByIdsHistory;

    @UseCase
    UpdateHistory updateHistory;

    private Disposable disposable1, disposable2, disposable;

    public HistoryPresenter(HistoryView view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        onRefresh();
        regist();
    }

    @Override
    public void onRefresh() {
        int page = 0;
        int size = 50;
        String dataSource = UserHistoryApi.DATA_SOURCE;
        refresh(requestHistory.buildUseCaseObservable(new RequestHistory.Param(page, size, dataSource)), new RefreshObserver(getUIView(), isShowEmpty()) {
            @Override
            public void onComplete() {
                super.onComplete();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                super.onError(e);
            }
        });
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    protected Consumer getConvertConsumer() {
        return new Consumer<LoaderUseCase.LoaderData<UserHistoryResponse>>() {
            @Override
            public void accept(@NonNull LoaderUseCase.LoaderData<UserHistoryResponse> o) throws Exception {
                List<HistoryViewModel> list = new ArrayList<>();
                List<UserHistoryModel> listDatas = o.getLoadListData().getListData();
                for (UserHistoryModel userHistoryModel : listDatas) {
                    HistoryViewModel historyViewModel = new HistoryViewModel();
                    historyViewModel.convert(userHistoryModel);
                    historyViewModel.setId(userHistoryModel.getId());
                    historyViewModel.setPic(userHistoryModel.getProgramImgUrl());
                    String timeString;
                    String name = userHistoryModel.getProgramName();
                    String code = userHistoryModel.getProgramCode();
                    StringBuilder duration = new StringBuilder();
                    if (VIDEO_TYPE_MORETV_TV.equals(userHistoryModel.getVideoType())) {
                        name = userHistoryModel.getParentDisplayName();
                        duration.append("第");
                        duration.append(SettingUtil.ToCH(userHistoryModel.getCurEpisode()));
                        duration.append("集  ");
                    }

                    long timeLong = userHistoryModel.getReportTime();
                    int headType = DateUtils.timeDistance(timeLong);
                    if (headType >= 0) {
                        timeString = "今天";
                        headType = 0;
                    } else if (headType == -1) {
                        timeString = "昨天";
                        headType = 1;
                    } else if (headType == -2) {
                        timeString = "前天";
                        headType = 2;
                    } else if (headType < -2 && headType > -7) {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
                        String daySting = sdf.format(timeLong);
                        timeString = daySting;
                        headType = 3;
                    } else {
                        timeString = "一周前";
                        headType = 7;
                    }
                    if (VIDEO_TYPE_DRAMA.equals(userHistoryModel.getProgramType())) {
                        historyViewModel.setDrama(true);
                        duration.append("");
                    } else {
                        switch (userHistoryModel.getPlayStatus()) {
                            case 0:
                                duration.append("已看至1%");
                                break;
                            case 1:
                                duration.append("已看至");
                                duration.append(SettingUtil.getPercentage(userHistoryModel.getPlayTime(), userHistoryModel.getTotalPlayTime()));
                                break;
                            case 2:
                                duration.append("已看完");
                                break;
                        }
                    }
                    historyViewModel.setCode(code);
                    historyViewModel.setName(name);
                    historyViewModel.setDuration(duration.toString());
                    historyViewModel.setHeadDate(timeString);
                    historyViewModel.setHeadType(headType);
                    list.add(historyViewModel);
                }
                o.getLoadListData().setViewDatas(list);
            }
        };
    }


    public HistoryRepository getHistoryRepository() {
        return repository;
    }

    private void del() {
        StringBuilder ids = new StringBuilder();
        String dataSource = UserHistoryApi.DATA_SOURCE;
        for (String id : getHistoryRepository().getCheckList()) {
            if (ids.length() != 0) {
                ids.append(",");
            }
            ids.append(id);
        }
        if (disposable1 != null) {
            disposable1.dispose();
        }
        disposable1 = delByIdsHistory.buildUseCaseObservable(new DelByIdsHistory.Param(dataSource, ids.toString()))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<CMSResponse>(getUIView()) {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        onRefresh();
                    }
                });
    }

    private void delAll() {
        String dataSource = UserHistoryApi.DATA_SOURCE;
        if (disposable2 != null) {
            disposable2.dispose();
        }
        disposable2 = delHistory.buildUseCaseObservable(new DelHistory.Param(dataSource))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<CMSResponse>(getUIView()) {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        onRefresh();
                    }
                });
    }

    public void onCheck() {
        getHistoryRepository().setCheck(!getHistoryRepository().isCheck());
        if (getHistoryRepository().isCheck()) {
            showEdit();
        } else {
            cancelEdit();
        }
    }

    public void showEdit() {
        getUIView().showEdit();
    }

    public void cancelEdit() {
        getUIView().cancelEdit();
    }


    public void onItemClick(int position) {
        if (getLoaderRepository().getLoadListData().getViewDatas().size() > position) {
            HistoryViewModel userHistoryItemData = (HistoryViewModel) getLoaderRepository().getLoadListData().getViewDatas().get(position);
            if (getHistoryRepository().isCheck()) {
                if (userHistoryItemData.isSelect) {
                    getHistoryRepository().getCheckList().remove(userHistoryItemData.getId() + "");
                } else {
                    getHistoryRepository().getCheckList().add(userHistoryItemData.getId() + "");
                }
                userHistoryItemData.isSelect = !userHistoryItemData.isSelect;
                getUIView().update(position);
            } else {
                if (userHistoryItemData.isDrama()) {
                    GoPageUtil.goPage(getStater(), FormatPageModel.getPlayerPageModel(userHistoryItemData.getSeverModel().getPlayData(), false, true));
                } else {
                    GoPageUtil.goPage(getStater(), FormatPageModel.getPlayerPageModel(userHistoryItemData.getSeverModel().getPlayData(), false));
                }

            }
        }
    }

    public void onAllClick() {
        boolean isSelect = true;
        if (getLoaderRepository().getLoadListData().getViewDatas().size() == getHistoryRepository().getCheckList().size()) {
            isSelect = false;
        }
        onUpdate(isSelect);
    }

    public void onUpdate(boolean isSelect) {
        if (getLoaderRepository().getLoadListData() == null)
            return;
        getHistoryRepository().getCheckList().clear();
        List<HistoryViewModel> historyViewModels = getLoaderRepository().getLoadListData().getViewDatas();
        for (HistoryViewModel userHistoryItemData : historyViewModels) {
            userHistoryItemData.isSelect = isSelect;
            if (isSelect) {
                getHistoryRepository().getCheckList().add(userHistoryItemData.getId() + "");
            }
        }
        getUIView().updateAll();
    }

    public void onDeleteClick() {
        if (getLoaderRepository().getLoadListData().getViewDatas().size() == getHistoryRepository().getCheckList().size()) {
            delAll();
        } else {
            del();
        }
        getHistoryRepository().setCheck(false);
    }

    public void showDeleteDialog() {
        if (getHistoryRepository().getCheckList().size() > 0) {
            DialogUtil.showDialog(getUIView().getAttachActivity(), AppContextProvider.getInstance().getContext().getResources().getString(R.string.text_delete_dialog), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeleteClick();
                }
            });
        }
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        unRegist();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable1 != null) {
            disposable1.dispose();
        }
        if (disposable2 != null) {
            disposable2.dispose();
        }
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if ("history_update".equals(event.getEventType())) {
            UserHistoryModel userHistoryModel = GsonUtil.getGson().fromJson((String) event.getObject(), UserHistoryModel.class);
            UpdateHistory.Param param = new UpdateHistory.Param((List<HistoryViewModel>) getLoaderRepository().getLoadListData().getViewDatas(), userHistoryModel);
            disposable = updateHistory.buildUseCaseObservable(param)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new UpdateUIObserver<List<HistoryViewModel>>(null) {
                        @Override
                        public void onNext(@NonNull List<HistoryViewModel> historyViewModels) {
                            if (getUIView() != null) {
                                getLoaderRepository().getLoadListData().setViewDatas(historyViewModels);
                                getUIView().updateOnRefresh(getLoaderRepository().getLoadListData().getViewDatas());
                            }
                        }
                    });
        }
    }

    public int getCheckNum() {
        return getHistoryRepository().getCheckList().size();
    }

    public int getTotalNum() {
        return getLoaderRepository().getLoadListData().getViewDatas().size();
    }

}


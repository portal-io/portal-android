package com.whaley.biz.program.ui.user.presenter;

import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.View;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.DeleteCollection;
import com.whaley.biz.program.interactor.RequestCollect;
import com.whaley.biz.program.model.CollectModel;
import com.whaley.biz.program.model.response.CollectListResponse;
import com.whaley.biz.program.ui.user.CollectView;
import com.whaley.biz.program.ui.user.repository.CollectRepository;
import com.whaley.biz.program.ui.user.viewmodel.CollectViewModel;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/1.
 */

public class CollectPresenter extends LoadPresenter<CollectView> {

    @Repository
    CollectRepository repository;

    @UseCase
    RequestCollect requestCollect;

    @UseCase
    DeleteCollection deleteCollect;

    private Disposable disposable;

    private static final int SIZE = 200;

    private static final String VIDEO_TYPE_MORETV_TV = "moretv_tv";

    public CollectPresenter(CollectView view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        onRefresh();
    }

    @Override
    public void onLoadMore() {
        int page = getLoaderRepository().getLoadListData().getPage() + 1;
        loadMore(requestCollect.buildUseCaseObservable(new RequestCollect.RequestCollectionParam(page, SIZE)));
    }

    public void onRefresh() {
        refresh(requestCollect.buildUseCaseObservable(new RequestCollect.RequestCollectionParam(0, SIZE)));
    }

    @Override
    protected Consumer getConvertConsumer() {
        return new Consumer<LoaderUseCase.LoaderData<CollectListResponse>>() {
            @Override
            public void accept(@NonNull LoaderUseCase.LoaderData<CollectListResponse> o) throws Exception {
                List<CollectViewModel> list = new ArrayList<>();
                List<CollectModel> listDatas = o.getLoadListData().getListData();
                for (CollectModel collectModel : listDatas) {
                    CollectViewModel collectViewModel = new CollectViewModel();
                    collectViewModel.convert(collectModel);
                    collectViewModel.setCode(collectModel.getProgramCode());
                    collectViewModel.setName(collectModel.getProgramName());
                    collectViewModel.setPic(collectModel.getPicUrl());
                    collectViewModel.setProgramType(collectModel.getProgramType());
                    collectViewModel.setTv(VIDEO_TYPE_MORETV_TV.equals(collectModel.getVideoType()));
                    if (collectViewModel.isTv()) {
                        collectViewModel.setDuration("电视剧");
                    } else {
                        collectViewModel.setDuration(formatTime(collectModel.getDuration()));
                    }
                    list.add(collectViewModel);
                }
                o.getLoadListData().setViewDatas(list);
            }
        };
    }

    private CollectRepository getSettingRepository() {
        return repository;
    }

    public void onClickEdit() {
        getSettingRepository().setEdit(!getSettingRepository().isEdit());
        if (getSettingRepository().isEdit()) {
            if (getUIView() != null) {
                getUIView().onResetEdit();
            }
            unCheckAll();
        }
        if (getUIView() != null) {
            getUIView().onChangeEdit(getSettingRepository().isEdit());
        }
    }

    public void onItemClick(int position) {
        CollectViewModel collectViewModel = (CollectViewModel) getLoaderRepository().getLoadListData().getViewDatas().get(position);
        if (collectViewModel == null)
            return;
        if (getSettingRepository().isEdit()) {
            if (getSettingRepository().getSelectedList().contains(collectViewModel.getCode())) {
                getSettingRepository().getSelectedList().remove(collectViewModel.getCode());
                collectViewModel.setSelected(false);
            } else {
                getSettingRepository().getSelectedList().add(collectViewModel.getCode());
                collectViewModel.setSelected(true);
            }
            if (getUIView() != null) {
                getUIView().onChangeSelected(collectViewModel.isSelected(), position);
            }
        } else {
            CollectModel collectModel = collectViewModel.getSeverModel();
            if (ProgramConstants.TYPE_DYNAMIC.equals(collectModel.getProgramType())) {
                GoPageUtil.goPage(getStater(), FormatPageModel.getPayerPageModel(collectModel.getPlayData(), false, true));
            } else {
                GoPageUtil.goPage(getStater(), FormatPageModel.getPayerPageModel(collectModel.getPlayData(), false));
            }
        }
    }

    public void onClickDelete() {
        if (getSettingRepository().getSelectedList().isEmpty()) {
            if (getUIView() != null) {
                getUIView().showToast("未选中任何选项");
            }
        } else {
            showDeleteDialog();
        }
    }

    public void showDeleteDialog() {
        DialogUtil.showDialog(getUIView().getAttachActivity(), "确定要删除吗？", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCollect();
            }
        });
    }

    private void deleteCollect() {
        StringBuilder programCodes = new StringBuilder();
        for (String code : getSettingRepository().getSelectedList()) {
            programCodes.append(code);
            programCodes.append(",");
        }
        programCodes.deleteCharAt(programCodes.lastIndexOf(","));
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = deleteCollect.buildUseCaseObservable(programCodes.toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull String s) {
                        Iterator<CollectViewModel> iterator = getLoaderRepository().getLoadListData().getViewDatas().iterator();
                        while (iterator.hasNext()) {
                            CollectViewModel collectViewModel = iterator.next();
                            if (getSettingRepository().getSelectedList().contains(collectViewModel.getCode())) {
                                iterator.remove();
                            }
                        }
                        if (getUIView() != null) {
                            getUIView().onRemove();
                        }
                        onClickEdit();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void onAllClick() {
        if (getSettingRepository().getSelectedList().size() == getLoaderRepository().getLoadListData().getViewDatas().size()) {
            unCheckAll();
        } else {
            checkAll();
        }
    }

    public void checkAll() {
        getSettingRepository().getSelectedList().clear();
        for (CollectViewModel collectViewModel : (List<CollectViewModel>) getLoaderRepository().getLoadListData().getViewDatas()) {
            collectViewModel.setSelected(true);
            getSettingRepository().getSelectedList().add(collectViewModel.getCode());
        }
        if (getUIView() != null) {
            getUIView().onChangeCheck();
        }
    }

    public void unCheckAll() {
        for (CollectViewModel collectViewModel : (List<CollectViewModel>) getLoaderRepository().getLoadListData().getViewDatas()) {
            collectViewModel.setSelected(false);
        }
        getSettingRepository().getSelectedList().clear();
        if (getUIView() != null) {
            getUIView().onChangeCheck();
        }
    }

    private String formatTime(int seconds) {
        int minuter = seconds / 60;
        int hour = minuter / 60;
        int second = seconds % 60;
        minuter %= 60;
        return String.format("%02d:%02d:%02d", hour, minuter, second);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public List<String> getSelectedList() {
        return getSettingRepository().getSelectedList();
    }

    public List<CollectViewModel> getListViewModel() {
        return getLoaderRepository().getLoadListData().getViewDatas();
    }

    public boolean isEdit() {
        return getSettingRepository().isEdit();
    }

    public int getCheckNum() {
        return getSelectedList().size();
    }

    public int getTotalNum() {
        return getLoaderRepository().getLoadListData().getViewDatas().size();
    }

}

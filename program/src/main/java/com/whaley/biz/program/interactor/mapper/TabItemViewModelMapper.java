package com.whaley.biz.program.interactor.mapper;

import com.whaley.biz.common.interactor.Mapper;
import com.whaley.biz.common.ui.viewmodel.TabItemViewModel;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by YangZhi on 2017/8/14 16:03.
 */

public class TabItemViewModelMapper extends Mapper<TabItemViewModel, Object> {

    public TabItemViewModelMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }


    protected TabItemViewModel convert(Object obj) {
        if (obj instanceof RecommendModel) {
            RecommendModel recommendModel = (RecommendModel) obj;
            TabItemViewModel itemViewModel = new TabItemViewModel(recommendModel.getName(), recommendModel);
            return itemViewModel;
        }
        return null;
    }
}

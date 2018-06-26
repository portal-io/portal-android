package com.whaley.biz.program.interactor.mapper;

import com.whaley.biz.common.interactor.Mapper;
import com.whaley.biz.common.ui.viewmodel.TabItemViewModel;
import com.whaley.biz.program.model.CpDetailModel;
import com.whaley.biz.program.model.CpModel;
import com.whaley.biz.program.ui.follow.presenter.PublisherDetailPresenter;
import com.whaley.biz.program.ui.uimodel.PublisherDetailViewModel;
import com.whaley.core.repository.IRepositoryManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/8/16
 * Introduction:
 */

public class PublisheretailMapper extends Mapper<PublisherDetailViewModel, CpDetailModel> {

    public PublisheretailMapper() {
    }

    public PublisheretailMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    protected PublisherDetailViewModel convert(CpDetailModel model) {
        PublisherDetailViewModel viewModel = new PublisherDetailViewModel();
        CpModel cp = model.getCp();
        viewModel.setBgPic(cp.getBackgroundPic());
        viewModel.setHeadPic(cp.getHeadPic());
        viewModel.setName(cp.getName());
        viewModel.setInfo(cp.getInfo());
        viewModel.setFollowed(model.getFollow() == 1);
        viewModel.setFans(cp.getFansCount());
        List<TabItemViewModel> titles = new ArrayList<>();
        titles.add(new TabItemViewModel(PublisherDetailPresenter.TAB_TITLE_MOST_RECENT, null));
        titles.add(new TabItemViewModel(PublisherDetailPresenter.TAB_TITLE_MOST_POPULAR, null));
        viewModel.setTitles(titles);
        viewModel.setCount(2);
        return viewModel;
    }
}

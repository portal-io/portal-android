package com.whaley.biz.program.ui.search.presenter;

import android.os.Bundle;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.GetSearch;
import com.whaley.biz.program.model.SearchModel;
import com.whaley.biz.program.model.response.SearchResponse;
import com.whaley.biz.program.ui.search.SearchResultView;
import com.whaley.biz.program.ui.search.repository.SearchResultRepository;
import com.whaley.biz.program.ui.uimodel.SearchResultViewModel;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by dell on 2017/8/25.
 */

public class SearchResultPresenter extends LoadPresenter<SearchResultView> implements ProgramConstants {

    public static final String STR_PARAM_TEXT = "str_text";

    @Repository
    SearchResultRepository searchResultRepository;

    @UseCase
    GetSearch getSearch;

    public SearchResultPresenter(SearchResultView view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
            getRepository().setText(arguments.getString(STR_PARAM_TEXT));
        }
    }

    public SearchResultRepository getRepository(){
        return searchResultRepository;
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        onRefresh();
    }

    @Override
    public void onLoadMore() {

    }

    public void onRefresh() {
        refresh(getSearch.buildUseCaseObservable(getRepository().getText()));
    }

    @Override
    protected Consumer getConvertConsumer() {
        return new Consumer<LoaderUseCase.LoaderData<SearchResponse>>() {
            @Override
            public void accept(@NonNull LoaderUseCase.LoaderData<SearchResponse> o) throws Exception {
                List<SearchResultViewModel> list = new ArrayList<>();
                List<SearchModel.ProgramBean> listDatas = o.getLoadListData().getListData();
                for (SearchModel.ProgramBean searchModel : listDatas) {
                    SearchResultViewModel searchResultViewModel = new SearchResultViewModel();
                    searchResultViewModel.convert(searchModel);
                    searchResultViewModel.setDrama(ProgramConstants.TYPE_DYNAMIC.equals(searchModel.getProgram_type()));
                    searchResultViewModel.setPic(searchModel.getBig_pic());
                    searchResultViewModel.setName(searchModel.getDisplay_name());
                    searchResultViewModel.setSubTitle(searchModel.getSubtitle());
                    list.add(searchResultViewModel);
                }
                o.getLoadListData().setViewDatas(list);
            }
        };
    }

    public void onClick(int position) {
        SearchResultViewModel searchResultViewModel = (SearchResultViewModel)getLoaderRepository().getLoadListData().getViewDatas().get(position);
        SearchModel.ProgramBean programBean = searchResultViewModel.getSeverModel();
        GoPageUtil.goPage(getStater(), FormatPageModel.getSearchPageModel(programBean, searchResultViewModel.isDrama()));
    }

}

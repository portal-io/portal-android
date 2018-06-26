package com.whaley.biz.common.ui.presenter;

import android.os.Bundle;
import android.support.v4.util.LruCache;

import com.whaley.biz.common.interactor.LoadMore;
import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.interactor.observer.LoadMoreObserver;
import com.whaley.biz.common.interactor.Refresh;
import com.whaley.biz.common.interactor.observer.RefreshObserver;
import com.whaley.biz.common.repository.LoaderRepository;
import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.biz.common.ui.viewmodel.LoadListData;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by YangZhi on 2017/7/27 17:39.
 */

public abstract class LoadPresenter<VIEW extends LoaderView> extends BasePagePresenter<VIEW>{

    static final LruCache<String,MemoryRepository> MEMORY_CACHE = new LruCache<>(15);

    @Repository
    private LoaderRepository loaderRepository;

    @UseCase
    private LoadMore loadMore;

    @UseCase
    private Refresh refresh;

    private static final Consumer NONE_CONSUMER =new Consumer() {
        @Override
        public void accept(@NonNull Object o) throws Exception {

        }
    };

    public LoadPresenter(VIEW view) {
        super(view);
    }

    public abstract void onLoadMore();

    public abstract void onRefresh();

    public void loadMore(Observable observable){
        loadMore(observable,new LoadMoreObserver(getUIView()));
    }

    public void loadMore(Observable observable,LoadMoreObserver loadMoreObserver){
        loadMore.execute(buildLoadMoreObservable(observable),loadMoreObserver);
    }

    public void refresh(Observable observable,RefreshObserver refreshObserver){
        refresh.execute(buildRefreshObservable(observable),refreshObserver);
    }

    public void refresh(Observable observable){
        refresh(observable,new RefreshObserver(getUIView(), isShowEmpty()));
    }

    protected Observable<LoaderUseCase.LoaderData> buildRefreshObservable(Observable observable){
        return observable
                .map(refresh.buildFunction())
                .doOnNext(getConvertConsumer());
    }

    protected Observable<LoaderUseCase.LoaderData> buildLoadMoreObservable(Observable observable){
        return observable
                .map(loadMore.buildFunction())
                .doOnNext(getConvertConsumer());
    }


    protected Consumer<LoaderUseCase.LoaderData> getConvertConsumer(){
        return NONE_CONSUMER;
    }

    public Refresh getRefresh() {
        return refresh;
    }

    public LoadMore getLoadMore() {
        return loadMore;
    }

    public LoaderRepository getLoaderRepository() {
        return loaderRepository;
    }

    public void setLoaderRepository(LoaderRepository loaderRepository) {
        this.loaderRepository = loaderRepository;
        IRepositoryManager repositoryManager = RepositoryManager.create(loaderRepository);
        this.refresh.setRepositoryManager(repositoryManager);
        this.loadMore.setRepositoryManager(repositoryManager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
    }

    protected void saveToMemoryCache(String key,MemoryRepository repository,Bundle outState){
        String saveKey = getClass().getName()+"_"+key;
        MEMORY_CACHE.put(saveKey,repository);
        outState.putString(saveKey,key);
    }

    protected <R extends MemoryRepository> R getRepositoryFromMemoryCache(String key,Bundle saveInstanceState){
        String saveKey = getClass().getName()+"_"+key;
        return (R) MEMORY_CACHE.get(saveKey);
    }

    public boolean isShowEmpty(){
        return loaderRepository.getLoadListData() == null || loaderRepository.getLoadListData().getViewDatas() == null
                || loaderRepository.getLoadListData().getViewDatas().isEmpty();
    }

}

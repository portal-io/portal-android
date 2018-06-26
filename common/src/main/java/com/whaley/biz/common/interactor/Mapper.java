package com.whaley.biz.common.interactor;

import com.whaley.biz.common.ui.viewmodel.IViewModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by YangZhi on 2017/8/14 18:56.
 */

public abstract class Mapper<VIEWMODEL extends IViewModel,T> extends UseCase<VIEWMODEL,T>{

    public Mapper(){

    }

    public Mapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    protected abstract VIEWMODEL convert(T obj);

    public Function<T,VIEWMODEL> buildFunction(){
        return new Function<T, VIEWMODEL>() {
            @Override
            public VIEWMODEL apply(@NonNull T t) throws Exception {
                return convert(t);
            }
        };
    }

    @Override
    public Observable<VIEWMODEL> buildUseCaseObservable(T t) {
       return Observable.just(t)
                .map(buildFunction());
    }

    public Single<List<VIEWMODEL>> buildListUseCaseObservable(List<T> tList){
        return Observable.fromIterable(tList)
                .map(buildFunction())
                .toList();
    }
}

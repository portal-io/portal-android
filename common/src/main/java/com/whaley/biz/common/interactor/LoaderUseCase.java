package com.whaley.biz.common.interactor;

import com.whaley.biz.common.exception.ResponseErrorException;
import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.ui.viewmodel.LoadListData;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by YangZhi on 2017/7/31 11:11.
 */

public abstract class LoaderUseCase<RESPONSE extends BaseListResponse> extends UseCase<LoaderUseCase.LoaderData<RESPONSE>,Observable<RESPONSE>>{

    public LoaderUseCase(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    public Function<RESPONSE, LoaderUseCase.LoaderData<RESPONSE>> buildFunction(){
        return new Function<RESPONSE, LoaderUseCase.LoaderData<RESPONSE>>() {
            @Override
            public LoaderUseCase.LoaderData<RESPONSE> apply(@NonNull RESPONSE response) throws Exception {
                LoadService service = getRepositoryManager().obtainMemoryService(LoadService.class);
                List dataList = response.getList();
                handleListData(service,dataList);
                service.getLoadListData().setHasMore(checkHasMore(response));
                LoaderUseCase.LoaderData<RESPONSE> loaderData = new LoaderData<>();
                loaderData.setResponse(response);
                loaderData.setLoadListData(service.getLoadListData());
                if((service.getLoadListData().getViewDatas()==null|| service.getLoadListData().getViewDatas().isEmpty()))
                    throw new ResponseErrorException();
                return loaderData;
            }
        };
    }

    @Override
    public final Observable<LoaderUseCase.LoaderData<RESPONSE>> buildUseCaseObservable(Observable<RESPONSE> responseObservable) {
        return responseObservable.map(buildFunction());
    }

    protected void handleListData(LoadService service,List dataList) throws Exception{
    }

    protected boolean checkHasMore(BaseListResponse response){
        return !response.isLast();
    }

    public interface LoadService{
        void saveLoadListData(LoadListData data);

        LoadListData getLoadListData();
    }

    public static final class LoaderData<RESPONSE extends BaseListResponse>{
        private LoadListData loadListData;
        private RESPONSE response;

        public RESPONSE getResponse() {
            return response;
        }

        public LoadListData getLoadListData() {
            return loadListData;
        }

        public void setLoadListData(LoadListData loadListData) {
            this.loadListData = loadListData;
        }

        public void setResponse(RESPONSE response) {
            this.response = response;
        }
    }
}

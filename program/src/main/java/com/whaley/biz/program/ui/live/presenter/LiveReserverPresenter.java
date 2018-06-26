package com.whaley.biz.program.ui.live.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.AddReserve;
import com.whaley.biz.program.interactor.CancelReserve;
import com.whaley.biz.program.interactor.GetReservation;
import com.whaley.biz.program.interactor.mapper.ReserveViewModelMapper;
import com.whaley.biz.program.interactor.mapper.UIViewModelMapper;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.biz.program.ui.live.LiveReserveDetailFragment;
import com.whaley.biz.program.ui.live.repository.LiveReserveRepository;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.uiview.adapter.CardVideoUIAdapter;
import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.biz.program.uiview.presenter.LoadUIViewPresenter;
import com.whaley.biz.program.uiview.repository.RecyclerUIViewRepository;
import com.whaley.biz.program.uiview.viewmodel.CardVideoUIViewModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/14.
 */

public class LiveReserverPresenter extends LoadUIViewPresenter<LoadUIViewPresenter.RecyclerUIVIEW> implements BIConstants{


    @Repository
    LiveReserveRepository liveReserveRepository;

    @UseCase
    GetReservation getReservation;

    @UseCase
    ReserveViewModelMapper mapper;

    @UseCase
    AddReserve addReserve;

    @UseCase
    CancelReserve cancelReserve;

    private Disposable disposable1, disposable2;

    public LiveReserverPresenter(RecyclerUIVIEW view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        regist();
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        unRegist();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (ProgramConstants.EVENT_LOGIN_SUCCESS.equals(event.getEventType()) || ProgramConstants.EVENT_SIGN_OUT.equals(event.getEventType())
                || ProgramConstants.EVENT_RESERVE_CHANGE.equals(event.getEventType())) {
            onRefresh();
        }
    }

    @Override
    protected RecyclerUIViewRepository getRecyclerRepository() {
        return liveReserveRepository;
    }

    @Override
    public UIViewModelMapper getMapper() {
        return mapper;
    }

    @Override
    public GetReservation getUserCase() {
        return getReservation;
    }


    public void addReserve(final ClickableUIViewHolder viewHolder) {
        if (viewHolder.getData() != null && viewHolder.getData().getType() == ViewTypeConstants.TYPE_RESERVE_CARD) {
            final CardVideoUIViewModel cardVideoUIViewModel = (CardVideoUIViewModel) viewHolder.getData();
            final LiveDetailsModel liveDetailsModel = cardVideoUIViewModel.getSeverModel();
            if (liveDetailsModel.getIsChargeable() == 1) {
                LiveReserveDetailFragment.goPage(getStater(), liveDetailsModel.getCode());
                return;
            }
            if (disposable1 != null) {
                disposable1.dispose();
            }
            disposable1 = addReserve.buildUseCaseObservable(liveDetailsModel.getCode())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new ErrorHandleObserver<CMSResponse>() {
                        @Override
                        public void onFinalError(Throwable e) {
                        }

                        @Override
                        public void onNotLoggedInError() {
                            super.onNotLoggedInError();
                            DialogUtil.showDialog(getStater().getAttatchContext(),
                                    AppContextProvider.getInstance().getContext().getString(R.string.dialog_reserve), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            TitleBarActivity.goPage(getStater(), 0, "/user/ui/login");
                                        }
                                    });
                        }

                        @Override
                        public void onStatusError(int status, String message) {
                            if (getUIView() != null) {
                                getUIView().showToast(message);
                            }
                        }

                        @Override
                        public void onNoDataError() {
                        }

                        @Override
                        public void onNext(@NonNull CMSResponse response) {
                            if (getUIView() != null) {
                                getUIView().showToast(response.getMsg());
                            }
                        }

                        @Override
                        public void onComplete() {
                            if (cardVideoUIViewModel != null) {
                                cardVideoUIViewModel.setReserve(true);
                            }
                            TextView tvAdd = ((CardVideoUIAdapter.ViewHolder) viewHolder).tvAdd;
                            tvAdd.setSelected(true);
                            tvAdd.setText("已预约");
                            livePrevue(liveDetailsModel);
                        }
                    });
        }
    }

    public void cancelReserve(final ClickableUIViewHolder viewHolder) {
        if (viewHolder.getData() != null && viewHolder.getData().getType() == ViewTypeConstants.TYPE_RESERVE_CARD) {
            final CardVideoUIViewModel cardVideoUIViewModel = (CardVideoUIViewModel) viewHolder.getData();
            LiveDetailsModel liveDetailsModel = cardVideoUIViewModel.getSeverModel();
            if (disposable2 != null) {
                disposable2.dispose();
            }
            disposable2 = cancelReserve.buildUseCaseObservable(liveDetailsModel.getCode())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new ErrorHandleObserver<CMSResponse>() {
                        @Override
                        public void onFinalError(Throwable e) {
                        }

                        @Override
                        public void onNotLoggedInError() {
                            super.onNotLoggedInError();
                            DialogUtil.showDialog(getStater().getAttatchContext(),
                                    AppContextProvider.getInstance().getContext().getString(R.string.dialog_reserve), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            TitleBarActivity.goPage(getStater(), 0, "/user/ui/login");
                                        }
                                    });
                        }

                        @Override
                        public void onStatusError(int status, String message) {
                            if (getUIView() != null) {
                                getUIView().showToast(message);
                            }
                        }

                        @Override
                        public void onNoDataError() {
                        }

                        @Override
                        public void onNext(@NonNull CMSResponse response) {
                            if (getUIView() != null) {
                                getUIView().showToast(response.getMsg());
                            }
                        }

                        @Override
                        public void onComplete() {
                            if (cardVideoUIViewModel != null) {
                                cardVideoUIViewModel.setReserve(false);
                            }
                            TextView tvAdd = ((CardVideoUIAdapter.ViewHolder) viewHolder).tvAdd;
                            tvAdd.setSelected(false);
                            tvAdd.setText("立即预约");
                        }
                    });
        }
    }

    public void onViewClick(ClickableUIViewHolder viewHolder) {
        PageModel pageModel = viewHolder.getData().getPageModel();
        pageModel.setRequestCode(1);
        GoPageUtil.goPage(getStater(), pageModel);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onRefresh();
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
    }

    //================bi埋点==================//

    /**
     * 预约
     */
    private void livePrevue(LiveDetailsModel liveDetailsModel) {
        if(liveDetailsModel==null)
            return;
        LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                .setEventId(PREVUE_CLICK)
                .setCurrentPageId(ROOT_LIVE_PREVUE)
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, liveDetailsModel.getCode())
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, liveDetailsModel.getDisplayName())
                .setNextPageId(ROOT_LIVE_PREVUE);
        if (builder != null) {
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

}

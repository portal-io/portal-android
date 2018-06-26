package com.whaley.biz.program.interactor;

import android.text.TextUtils;

import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.interactor.CommonCMSFunction;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.program.api.LiveAPI;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.model.response.LiveDetailsResponse;
import com.whaley.biz.program.ui.live.repository.LiveReserveDetailRepository;
import com.whaley.biz.program.ui.uimodel.ReserveDetailViewModel;
import com.whaley.biz.program.utils.CouponPackUtil;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.share.model.ShareParam;
import com.whaley.core.utils.DateUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/8/17.
 */

public class GetReserveDetail extends UseCase<LiveDetailsModel, String> implements ProgramConstants {

    public GetReserveDetail(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<LiveDetailsModel> buildUseCaseObservable(final String code) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<LiveDetailsResponse>>() {
                    @Override
                    public ObservableSource<LiveDetailsResponse> apply(@NonNull UserModel userModel) throws Exception {
                        String uid = null;
                        if (userModel.isLoginUser()) {
                            uid = userModel.getAccount_id();
                        }
                        return getRepositoryManager().obtainRemoteService(LiveAPI.class).requestLiveDetail(code, uid);
                    }
                }).map(new CommonCMSFunction<LiveDetailsResponse, LiveDetailsModel>())
                .doOnNext(new Consumer<LiveDetailsModel>() {
                    @Override
                    public void accept(@NonNull LiveDetailsModel liveDetailsModel) throws Exception {
                        final LiveReserveDetailRepository repository = getRepositoryManager().obtainMemoryService(LiveReserveDetailRepository.class);
                        if (liveDetailsModel != null) {
                            repository.convert(liveDetailsModel);
                            repository.setLiveStatus(liveDetailsModel.getLiveStatus());
                            repository.setHasOrder(liveDetailsModel.isLiveOrdered());
                            repository.setBuy(liveDetailsModel.isLiveOrdered());
                            repository.setCouponPackModel(CouponPackUtil.getCouponPackModel(liveDetailsModel.getCouponDto(),
                                    liveDetailsModel.getContentPackageQueryDtos(), false));
                            repository.setTvAmount(liveDetailsModel.getLiveOrderCount());
                            repository.setCharge(liveDetailsModel.getIsChargeable() == 1);
                            if (repository.getCouponPackModel() != null) {
                                repository.setPrice(repository.getCouponPackModel().price);
                            }
                            ReserveDetailViewModel reserveDetailViewModel = new ReserveDetailViewModel();
                            reserveDetailViewModel.setTvDate("距离" + DateUtils.foramteToYYYYMMDDHHMM(liveDetailsModel.getBeginTime()) + " 开播还有 ");
                            if (LIVE_STATE_AFTER == liveDetailsModel.getLiveStatus()) {
                                reserveDetailViewModel.setTvRemain("已结束");
                                reserveDetailViewModel.setTvUnit("");
                                reserveDetailViewModel.setTvDate("");
                            } else {
                                int timeLeftSeconds = liveDetailsModel.getTimeLeftSeconds();
                                String remain;
                                String unit;
                                int days = timeLeftSeconds / (60 * 60 * 24);
                                int hours = (timeLeftSeconds % (60 * 60 * 24)) / (60 * 60);
                                int minutes = (timeLeftSeconds % (60 * 60)) / (60);
                                if (days > 0) {
                                    remain = days + " ";
                                    unit = "天";
                                } else if (hours > 0) {
                                    remain = hours + " ";
                                    unit = "小时";
                                } else if (minutes > 0) {
                                    remain = minutes + " ";
                                    unit = "分钟";
                                } else {
                                    remain = "即将开始";
                                    unit = "";
                                    reserveDetailViewModel.setTvDate("");
                                }
                                reserveDetailViewModel.setTvRemain(remain);
                                reserveDetailViewModel.setTvUnit(unit);
                            }
                            reserveDetailViewModel.setPic(liveDetailsModel.getPoster());
                            reserveDetailViewModel.setName(liveDetailsModel.getDisplayName());
                            if (!TextUtils.isEmpty(liveDetailsModel.getDescription())) {
                                reserveDetailViewModel.setIntro("简介:  " + liveDetailsModel.getDescription());
                            }
                            reserveDetailViewModel.setAddress(liveDetailsModel.getAddress());
                            reserveDetailViewModel.setGuests(liveDetailsModel.getGuests());
                            repository.setReserveDetailViewModel(reserveDetailViewModel);
                            ShareModel shareModel = ShareModel.createBuilder()
                                    .setTitle(liveDetailsModel.getDisplayName())
                                    .setDes(liveDetailsModel.getDescription())
                                    .setCode(liveDetailsModel.getCode())
                                    .setVideo(true)
                                    .setImgUrl(liveDetailsModel.getPoster())
                                    .setType(ShareConstants.TYPE_ALL)
                                    .setShareType(ShareTypeConstants.TYPE_SHARE_LIVE)
                                    .build();
                            Router.getInstance().buildExecutor("/share/service/sharemodel").putObjParam(shareModel).notTransParam()
                                    .callback(new Executor.Callback<ShareParam.Builder>() {
                                        @Override
                                        public void onCall(ShareParam.Builder builder) {
                                            repository.setBuilder(builder);
                                        }

                                        @Override
                                        public void onFail(Executor.ExecutionError executionError) {

                                        }
                                    }).notTransCallbackData().excute();
                        }
                    }
                });
    }

}

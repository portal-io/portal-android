package com.whaley.biz.program.interactor.mapper;

import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.model.CouponModel;
import com.whaley.biz.program.model.PackageItemModel;
import com.whaley.biz.program.model.PackageListModel;
import com.whaley.biz.program.model.PackageModel;
import com.whaley.biz.program.model.pay.PayResultModel;
import com.whaley.biz.program.ui.arrange.repository.PackageService;
import com.whaley.biz.program.uiview.viewmodel.CardVideoUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.uiview.viewmodel.ShareBottomUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.TopicHeadViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.utils.CouponPackUtil;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.StringUtil;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.share.model.ShareParam;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:
 */

public class PackageViewModelMapper extends UIViewModelMapper<Object> {

    public PackageViewModelMapper() {
    }

    public PackageViewModelMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    protected RecyclerViewModel convert(Object model) {
        initDefaultViewModel();
        if (model == null) {
            getRecyclerViewModel().setClickableUiViewModels(null);
            return getRecyclerViewModel();
        }
        if (model instanceof List) {
            List list = (List) model;
            if (list == null && list.size() == 0) {
                return getRecyclerViewModel();
            }
            for (int i = 0; i < list.size(); i++) {
                Object o = list.get(i);
                if (o instanceof PayResultModel) {
                    PayResultModel payResultModel = (PayResultModel) o;
                    int j = i + 1;
                    if (j > 0 && j < getRecyclerViewModel().getClickableUiViewModels().size()) {
                        ClickableUIViewModel clickableUIViewModel = getRecyclerViewModel().getClickableUiViewModels().get(i + 1);
                        if (clickableUIViewModel instanceof CardVideoUIViewModel) {
                            CardVideoUIViewModel cardVideoUIViewModel = (CardVideoUIViewModel) clickableUIViewModel;
                            cardVideoUIViewModel.setPay(payResultModel.isResult());
                        }
                    }
                }
            }
            return getRecyclerViewModel();
        }
        List<ClickableUIViewModel> clickableUiViewModelList = new ArrayList<>();
        if (model instanceof PackageListModel) {
            TopicHeadViewModel topicHeadData = new TopicHeadViewModel();
            PackageListModel data = (PackageListModel) model;
            PackageModel packageModel = data.getPack();
            final PackageService service = getRepositoryManager().obtainMemoryService(PackageService.class);
            boolean isChargeable = packageModel.getIsChargeable() == 1;
            if (packageModel != null) {
                if (isChargeable && packageModel.getCouponDto() != null && !service.isPay()) {
                    List<CouponModel> couponModels = new ArrayList<>();
                    service.setPay(service.isPay() || "0".equals(packageModel.getCouponDto().getDiscountPrice()));
                    packageModel.getCouponDto().viewName = CouponPackUtil.getContent(packageModel.getType(), true);
                    packageModel.getCouponDto().isTopic = true;
                    couponModels.add(packageModel.getCouponDto());
                    service.setCouponModels(couponModels);
                    String payString;
                    if (packageModel.getType() == 1) {
                        payString = String.format("购买节目包观看券 ¥%1$s", StringUtil.fromFenToYuan(packageModel.getCouponDto().getDiscountPrice()));
                    } else {
                        payString = String.format("购买合集观看券 ¥%1$s", StringUtil.fromFenToYuan(packageModel.getCouponDto().getPrice()));
                    }
                    service.setPayString(payString);
                }
                if (packageModel.getType() == 1) {
                    service.setPackage(true);
                }
                topicHeadData.setBigImageUrl(packageModel.getPic());
                topicHeadData.setIntroduction(packageModel.getDescription());
                topicHeadData.setName(packageModel.getDisplayName());
                topicHeadData.setCode(packageModel.getCode());
                topicHeadData.setPay(service.isPay());
                topicHeadData.setIsChargeable(packageModel.getIsChargeable());
                service.setChargeable(isChargeable);

            }
            if (data.getItems() != null && data.getItems().getContent() != null && data.getItems().getContent().size() > 0) {
                List<PackageItemModel> packageItemModelList = data.getItems().getContent();
                topicHeadData.setNumVideo(packageItemModelList.size() + "个视频");
                clickableUiViewModelList.add(topicHeadData);
                StringBuilder goodsNos = new StringBuilder();
                StringBuilder goodsTypes = new StringBuilder();
                List<PlayData> list = new ArrayList<>();
                for (PackageItemModel packageItemModel : packageItemModelList) {
                    if (!packageItemModel.isLive()) {
                        list.add(packageItemModel.getPlayData());
                    }
                }
                int numVideo = 0;
                for (PackageItemModel packageItemModel : packageItemModelList) {
                    CardVideoUIViewModel cardVideoUIViewModel = new CardVideoUIViewModel();
                    cardVideoUIViewModel.convert(packageItemModel);
                    clickableUiViewModelList.add(cardVideoUIViewModel);
                    goodsNos.append(packageItemModel.getContentCode()).append(",");
                    goodsTypes.append(packageItemModel.getContentType()).append(",");
                    if (!packageItemModel.isLive()) {
                        List<PlayData> oneList = new ArrayList<>();
                        oneList.add(packageItemModel.getPlayData());
                        cardVideoUIViewModel.setPageModel(FormatPageModel.getPayerListPagModel(oneList, 0));
                        numVideo++;
                    } else {
                        cardVideoUIViewModel.setPageModel(FormatPageModel.getPageModel(packageItemModel));
                    }
                }
                goodsNos.delete(goodsNos.length() - 1, goodsNos.length());
                goodsTypes.delete(goodsTypes.length() - 1, goodsTypes.length());
                service.setGoodsNos(goodsNos.toString());
                service.setGoodsTypes(goodsTypes.toString());
                service.setPayList(isChargeable && !service.isPay() && !packageModel.getCouponDto().getPrice().equals(packageModel.getCouponDto().getDiscountPrice()));
                service.setTopicHead(topicHeadData);
                if (numVideo > 0) {
                    service.setPlayerButton(true);
                    service.setPageModel(FormatPageModel.getPayerListPagModel(list, 0, true, false));
                }
                ShareBottomUIViewModel shareBottomUIViewModel = new ShareBottomUIViewModel();
                shareBottomUIViewModel.setPay(isChargeable && !service.isPay());
                shareBottomUIViewModel.setContentType(packageModel != null && packageModel.getType() == 1 ? ShareBottomUIViewModel.PROGRAM_PACKAGE_TYPE : ShareBottomUIViewModel.PROGRAM_SET_TYPE);
                clickableUiViewModelList.add(shareBottomUIViewModel);
                getRecyclerViewModel().setClickableUiViewModels(clickableUiViewModelList);
                ShareModel shareModel = ShareModel.createBuilder()
                        .setCode(topicHeadData.getCode())
                        .setDes(topicHeadData.getIntroduction())
                        .setTitle(topicHeadData.getName())
                        .setImgUrl(topicHeadData.getBigImageUrl())
                        .setShareType(packageModel.getType() == 1 ? ShareTypeConstants.TYPE_SHARE_PACK
                                : ShareTypeConstants.TYPE_SHARE_SET)
                        .setType(ShareConstants.TYPE_ALL)
                        .build();
                Router.getInstance().buildExecutor("/share/service/sharemodel").putObjParam(shareModel).notTransParam()
                        .callback(new Executor.Callback<ShareParam.Builder>() {
                            @Override
                            public void onCall(ShareParam.Builder builder) {
                                service.setBuilder(builder);
                            }

                            @Override
                            public void onFail(Executor.ExecutionError executionError) {

                            }
                        }).notTransCallbackData().excute();
            }
        }
        return getRecyclerViewModel();

    }
}

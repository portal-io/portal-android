package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.model.ArrangeModel;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.uiview.adapter.RecyclerViewUIAdapter;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.core.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/16.
 */

public class LiveTopicUIViewModel extends BaseUIViewModel implements NestedParentUIViewModel{

    static final int OUT_RECT_PX = DisplayUtil.convertDIP2PX(4);

    private String title;
    private String subTitle;
    private String image;
    private boolean isShowItems;
    private RecyclerViewModel recyclerViewModel;

    static final String STR_LINE = " | ";
    static final String STR_PLAY_COUNT = "人播放";

    @Override
    public boolean isCanClick() {
        return true;
    }

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_LIVE_TOPIC;
    }

    public static int getOutRectPx() {
        return OUT_RECT_PX;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isShowItems() {
        return isShowItems;
    }

    public void setShowItems(boolean showItems) {
        isShowItems = showItems;
    }

    public RecyclerViewModel getRecyclerViewModel() {
        return recyclerViewModel;
    }

    public void setRecyclerViewModel(RecyclerViewModel recyclerViewModel) {
        this.recyclerViewModel = recyclerViewModel;
    }

    public void convert(RecommendModel recommendModel) {
        super.convert(recommendModel);
        if (recommendModel != null) {
            setTitle(recommendModel.getName());
            setSubTitle("共" + recommendModel.getDetailCount() + "部视频");
            setImage(recommendModel.getNewPicUrl());
            setPageModel(FormatPageModel.getPageModel(recommendModel));
            recyclerViewModel = new RecyclerViewModel();
            recyclerViewModel.setHorizontal(true);
            List<ClickableUIViewModel> clickableUiViewModels = new ArrayList<>();
            List<ArrangeModel> arrangeModels = recommendModel.getArrangeElements();
            if (arrangeModels != null && arrangeModels.size() > 0) {
                int i = 0;
                for (ArrangeModel model : arrangeModels) {
                    LiveTopicItemUIViewModel itemViewData = new LiveTopicItemUIViewModel();

                    itemViewData.convert(model);
                    if (i > 0)
                        itemViewData.setOutLeft(OUT_RECT_PX);
                    clickableUiViewModels.add(itemViewData);
                    i++;
                }
                LiveTopicMoreUIViewModel itemViewData = new LiveTopicMoreUIViewModel();
                itemViewData.convert(recommendModel);
                itemViewData.setOutLeft(OUT_RECT_PX);
                clickableUiViewModels.add(itemViewData);
                setShowItems(true);
            } else {
                setShowItems(false);
            }
            recyclerViewModel.setClickableUiViewModels(clickableUiViewModels);
        }
    }

    @Override
    public RecyclerViewModel getChildRecyclerViewModel() {
        return recyclerViewModel;
    }
}

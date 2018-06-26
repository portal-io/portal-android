package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.model.CpInfoModel;
import com.whaley.biz.program.model.CpProgramModel;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.uiview.model.ClickModel;
import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.biz.program.utils.FormatPageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: qxw
 * Date: 2017/3/20
 */

public class PromulagtorUIViewModel extends BaseUIViewModel implements NestedParentUIViewModel{

    private String imgUrl;
    private boolean isFollow;
    private RecyclerViewModel recyclerViewModel;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public RecyclerViewModel getRecyclerViewModel() {
        return recyclerViewModel;
    }

    public void setRecyclerViewModel(RecyclerViewModel recyclerViewModel) {
        this.recyclerViewModel = recyclerViewModel;
    }

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_PROMULGATOR;
    }

    public void convert(RecommendModel recommendModel) {
        super.convert(recommendModel);
        PageModel pageModel = FormatPageModel.getPageModel(recommendModel);
        setPageModel(pageModel);
        ClickModel clickModel = new ClickModel();
        clickModel.setCode(recommendModel.getCpInfo().getCode());
        setClickModel(clickModel);
        if (recommendModel != null) {
            setFollow(recommendModel.getCpFollow() == 0 ? false : true);
            recyclerViewModel = new RecyclerViewModel();
            recyclerViewModel.setHorizontal(true);
            List<ClickableUIViewModel> clickableUiViewModels = new ArrayList<>();
            if (recommendModel.getCpInfo() != null) {
                CpInfoModel cpInfoModel = recommendModel.getCpInfo();
                setImgUrl(cpInfoModel.getHeadPic());
                PromulagtorDescriptionUIViewModel promulagtorDescriptionUIViewModel = new PromulagtorDescriptionUIViewModel();
                promulagtorDescriptionUIViewModel.convert(cpInfoModel);
                clickableUiViewModels.add(promulagtorDescriptionUIViewModel);
            }
            if (recommendModel.getCpProgramDtos() != null && recommendModel.getCpProgramDtos().size() > 0) {
                List<PlayData> playModels = new ArrayList<>();
                for (CpProgramModel cpProgramModel : recommendModel.getCpProgramDtos()) {
                    PromulagtorContentUIViewModel programUIViewModel = new PromulagtorContentUIViewModel();
                    programUIViewModel.convert(cpProgramModel);
                    clickableUiViewModels.add(programUIViewModel);
                }
            }
            recyclerViewModel.setClickableUiViewModels(clickableUiViewModels);
        }
    }

    @Override
    public boolean isCanClick() {
        return true;
    }

    @Override
    public RecyclerViewModel getChildRecyclerViewModel() {
        return recyclerViewModel;
    }
}

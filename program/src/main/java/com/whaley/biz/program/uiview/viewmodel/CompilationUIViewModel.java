package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;

/**
 * Created by dell on 2017/8/15.
 */

public class CompilationUIViewModel extends BaseUIViewModel {

    private String imgUrl;
    private String name;
    private String subtitle;

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_COMPILATION;
    }

    @Override
    public boolean isCanClick() {
        return true;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void convert(RecommendModel recommend) {
        super.convert(recommend);
        setPageModel(FormatPageModel.getPageModel(recommend));
    }
}

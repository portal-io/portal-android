package com.whaley.biz.program.ui.detail.component.drama;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.program.ui.detail.DramaDetailView;

/**
 * Created by dell on 2017/11/14.
 */

public class DramaDetailComponent extends BaseComponent {

    DramaDetailView dramaDetailView;

    public DramaDetailComponent(DramaDetailView dramaDetailView) {
        this.dramaDetailView = dramaDetailView;
    }

    @Override
    protected BaseController onCreateController() {
        return new DramaDetailController(dramaDetailView);
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }

    @Override
    public void destroy() {
        super.destroy();
        dramaDetailView = null;
    }
}

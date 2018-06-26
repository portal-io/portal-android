package com.whaley.biz.program.ui.detail.component.drama;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.program.ui.detail.ProgramDetailView;

/**
 * Created by dell on 2017/11/14.
 */

public class DramaShareComponent extends BaseComponent {

    ProgramDetailView programDetailView;

    public DramaShareComponent(ProgramDetailView programDetailView) {
        this.programDetailView = programDetailView;
    }

    @Override
    protected BaseController onCreateController() {
        return new DramaShareController(programDetailView);
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }

    @Override
    public void destroy() {
        super.destroy();
        programDetailView = null;
    }
}

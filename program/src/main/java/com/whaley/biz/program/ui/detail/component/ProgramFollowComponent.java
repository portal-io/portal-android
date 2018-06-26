package com.whaley.biz.program.ui.detail.component;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.program.ui.detail.ProgramDetailView;

public class ProgramFollowComponent extends BaseComponent {

    ProgramDetailView programDetailView;

    public ProgramFollowComponent(ProgramDetailView programDetailView) {
        this.programDetailView = programDetailView;
    }

    @Override
    protected BaseController onCreateController() {
        return new ProgramFollowController(programDetailView);
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
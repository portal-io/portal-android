package com.whaley.biz.program.ui.uimodel;

import java.util.List;

/**
 * Created by YangZhi on 2017/10/12 12:35.
 */

public class HeadContributionViewModel {
   private List<ContributionViewModel> contributionViewModels;

    public void setContributionViewModels(List<ContributionViewModel> contributionViewModels) {
        this.contributionViewModels = contributionViewModels;
    }

    public List<ContributionViewModel> getContributionViewModels() {
        return contributionViewModels;
    }
}

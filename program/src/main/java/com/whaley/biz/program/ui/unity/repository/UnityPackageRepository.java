package com.whaley.biz.program.ui.unity.repository;

import com.whaley.biz.program.ui.arrange.repository.PackageRepository;

/**
 * Created by dell on 2017/8/30.
 */

public class UnityPackageRepository extends PackageRepository  {


    private boolean isHasBeenPaid;



    public boolean isHasBeenPaid() {
        return isHasBeenPaid;
    }

    public void setHasBeenPaid(boolean hasBeenPaid) {
        isHasBeenPaid = hasBeenPaid;
    }
}

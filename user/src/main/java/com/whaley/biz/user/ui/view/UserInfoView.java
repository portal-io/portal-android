package com.whaley.biz.user.ui.view;

import com.whaley.biz.common.ui.view.BasePageView;

/**
 * Author: qxw
 * Date:2017/8/3
 * Introduction:
 */

public interface UserInfoView extends BasePageView {

    void showAvatar(String imageUrl);

    void showNickname(String nickName);

    void showPhone(String phone);

    void setBingQQ(boolean selected);

    void setBingWeixin(boolean selected);

    void setBingWeibo(boolean selected);

}

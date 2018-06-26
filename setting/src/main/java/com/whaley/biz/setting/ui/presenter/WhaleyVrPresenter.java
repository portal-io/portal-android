package com.whaley.biz.setting.ui.presenter;

import android.content.Intent;
import android.net.Uri;

import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.common.model.hybrid.TitleBarModel;
import com.whaley.biz.common.model.hybrid.WebviewGoPageModel;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.ui.view.WhaleyVrView;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.bi.model.BIConstants;
import com.whaley.core.router.Router;
import com.whaleyvr.core.network.http.HttpManager;


/**
 * Created by dell on 2017/8/9.
 */

public class WhaleyVrPresenter extends BasePagePresenter<WhaleyVrView> {

    public WhaleyVrPresenter(WhaleyVrView view) {
        super(view);
    }

    public void onAgreementClick() {
        WebviewGoPageModel webviewGoPageModel = WebviewGoPageModel.createWebviewGoPageModel("file:///android_asset/privacy.html",
                TitleBarModel.createTitleBarModel("用户协议"));
        Router.getInstance().buildExecutor("/hybrid/service/goPage").putObjParam(webviewGoPageModel).excute();
    }

    public void onEnterQQClick() {
        joinQQGroup("yfz-t2ko_eBo1PPI5glDxyaxFECT-WBH");
    }

    /****************
     *
     * 发起添加群流程。群号：微鲸VR官方粉丝群(467306966) 的 key 为：  yfz-t2ko_eBo1PPI5glDxyaxFECT-WBH
     * 调用 joinQQGroup(Jwzoibbs24Li3csK6ydk0WtF3P2e5YFq) 即可发起手Q客户端申请加群 微鲸VR官方粉丝群(170321770)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            getStater().startActivity(intent);
            return true;
        } catch (Exception e) {
            if (getUIView() != null) {
                getUIView().showToast("未安装手机QQ或安装的版本不支持");
            }
            return false;
        }
    }

    public void onTrade() {
        String url = HttpManager.getInstance().isTest() ? "http://minisite.test.snailvr.com/app-inner-aboutus-h5/" : "http://minisite-c.snailvr.com/app-inner-aboutus-h5/";
        String shareUrl = HttpManager.getInstance().isTest() ? "http://minisite.test.snailvr.com/app-about-h5/" : "http://minisite-c.snailvr.com/app-about-h5/";
        ShareModel shareModel = ShareModel.createBuilder().setUrl(shareUrl).setShareType(ShareTypeConstants.TYPE_SHARE_ABOUT).setType(6)
                .setResId(R.mipmap.trade_logo).setFrom(BIConstants.ROOT_ABOUT).setDes(AppContextProvider.getInstance().getContext().getString(R.string.trade_share_intro))
                .setTitle(AppContextProvider.getInstance().getContext().getString(R.string.trade_share_title)).build();
        WebviewGoPageModel webviewGoPageModel = WebviewGoPageModel.createWebviewGoPageModel(url,
                TitleBarModel.createTitleBarModel("商务合作"), shareModel);
        Router.getInstance().buildExecutor("/hybrid/service/goPage").putObjParam(webviewGoPageModel).excute();
    }

}

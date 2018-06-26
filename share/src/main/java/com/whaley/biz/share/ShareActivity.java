package com.whaley.biz.share;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.share.ShareListener;
import com.whaley.core.share.ShareUtil;
import com.whaley.core.share.model.ShareParam;

import java.util.Map;

/**
 * Created by dell on 2016/8/5.
 */
@Route(path = "/share/ui/share")
public class ShareActivity extends CommonActivity implements BIConstants, ShareTypeConstants {

    private final static int ANIMATION_DURATION = 200;

    Intent resultIntent;
    RelativeLayout rlShare;

    private ShareParam shareParam;
    private int type;
    private Map<Integer, ShareParam> shareParamsMap;

    private boolean isOutside = false;

    protected int getLayoutID() {
        return R.layout.activity_share;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        shareParam = (ShareParam) getIntent().getSerializableExtra("data");
        if (shareParam.isDifferentParams()) {
            shareParamsMap = ShareUtil.getShareParams(shareParam.getCallbackId());
            if (shareParamsMap == null)
                finish();
        }
        rlShare = (RelativeLayout) findViewById(R.id.rl_share);
        rlShare.setOnClickListener(null);
        findViewById(R.id.qq_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(ShareConstants.TYPE_QQ);
            }
        });
        findViewById(R.id.qq_zone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(ShareConstants.TYPE_QZONE);
            }
        });
        findViewById(R.id.sina_weibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(ShareConstants.TYPE_SINA);
            }
        });
        findViewById(R.id.weixin_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(ShareConstants.TYPE_WEIXIN);
            }
        });
        findViewById(R.id.weixin_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvent();
                share(ShareConstants.TYPE_WEIXIN_CIRCLE);
            }
        });
        findViewById(R.id.share_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy(shareParam.getFrom());
            }
        });
        findViewById(R.id.back_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRecommendIcon();
            }
        });
        findViewById(R.id.backgroud_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRecommendIcon();
            }
        });
        measure();
    }

    @Override
    public void onBackPressed() {
        hideRecommendIcon();
    }

    private void postEvent() {
        EventController.postEvent(new BaseEvent("/share/service/share", null));
    }

    private void copy(String from) {
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(shareParam.getUrl());
        showToast(getString(R.string.copy_link));
        onFinishResult(7);
        hideRecommendIcon();
    }

    private void share(int type) {
        this.type = type;
        postEvent();
        isOutside = true;
        ShareParam shareParam = getRealShareParam(type);
        ShareUtil.share(shareParam);
        onClickShare(shareParam);
    }

    private ShareParam getRealShareParam(int type) {
        ShareParam shareParam;
        if (shareParamsMap == null) {
            shareParam = this.shareParam;
            shareParam.setContext(this);
            shareParam.setType(type);
        } else {
            shareParam = shareParamsMap.get(type);
            shareParam.setContext(this);
        }
        shareParam.setListener(shareListener);
        return shareParam;
    }

    private void onFinishResult(int type) {
        Intent intent = new Intent();
        intent.putExtra("ShareOutside", isOutside);
        intent.putExtra("type", type);
        intent.putExtra("CallbackId", shareParam.getCallbackId());
        setResult(RESULT_OK, intent);
        this.resultIntent = intent;
        finish();
    }

    private ShareListener shareListener = new ShareListener() {
        @Override
        public void onResult(int type) {
            showToast("分享成功");
            onFinishResult(type);
        }

        @Override
        public void onError(int type, Throwable var2) {
            showToast("分享失败");
            finish();
        }

        @Override
        public void onCancel(int type) {
            showToast("分享取消");
        }
    };

    int mesureHeight;

    private void measure() {
        ViewTreeObserver vto = rlShare.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlShare.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                recommend_layout_y = rlShare.getY();
                mesureHeight = rlShare.getMeasuredHeight();
                showRecommendIcon();
            }
        });
    }

    private void showRecommendIcon() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(rlShare, "translationY", mesureHeight, 0);
        AnimatorSet set = new AnimatorSet();
        set.play(anim);
        set.setDuration(ANIMATION_DURATION);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    private void hideRecommendIcon() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(rlShare, "translationY", 0, mesureHeight);
        AnimatorSet set = new AnimatorSet();
        set.play(anim);
        set.setDuration(ANIMATION_DURATION);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
//                backIcon.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                if (null != recommendLayout) {
//                    recommendLayout.setVisibility(View.GONE);
//                }
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    @Override
    public void finish() {
        if (resultIntent == null) {
            Intent intent = new Intent();
            intent.putExtra("ShareOutside", isOutside);
            intent.putExtra("type", type);
            intent.putExtra("CallbackId", shareParam.getCallbackId());
            setResult(RESULT_CANCELED, intent);
        }
        if (shareParamsMap != null) {
            ShareUtil.removeShareParams(shareParam.getCallbackId());
        }
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareUtil.onActivityResult(requestCode, resultCode, data);
    }

    //==============================BI埋点====================================//

    /**
     * 点击分享渠道
     */
    private void onClickShare(ShareParam shareParam) {
        if (shareParam != null) {
            String currentPageId = getCurrentPageId(shareParam);
            if (TextUtils.isEmpty(currentPageId))
                return;
            LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                    .setEventId(SHARE)
                    .setCurrentPageId(currentPageId)
                    .setNextPageId(currentPageId);
            if (TYPE_SHARE_WEB == shareParam.getShareType()) {
                builder.putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_ID, shareParam.getUrl())
                        .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_NAME, shareParam.getShareName());
            } else if (TYPE_SHARE_SET == shareParam.getShareType() || TYPE_SHARE_PACK == shareParam.getShareType() || TYPE_SHARE_TOPIC == shareParam.getShareType()) {
                builder.putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_ID, shareParam.getShareCode())
                        .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_NAME, shareParam.getShareName());
            } else {
                builder.putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, shareParam.getShareCode())
                        .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, shareParam.getShareName());
            }
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

    private String getCurrentPageId(ShareParam shareParam) {
        String currentPageId = null;
        switch (shareParam.getShareType()) {
            case TYPE_SHARE_LIVE:
                currentPageId = ROOT_LIVE_PREVUE;
                break;
            case TYPE_SHARE_LIVE_PLAYER:
                currentPageId = ROOT_LIVE_DETAILS;
                break;
            case TYPE_SHARE_VIDEO:
            case TYPE_SHARE_MOVIE:
            case TYPE_SHARE_MT:
            case TYPE_SHARE_MM:
                currentPageId = ROOT_VIDEO_DETAILS;
                break;
            case TYPE_SHARE_TOPIC:
                currentPageId = ROOT_TOPIC;
                break;
            case TYPE_SHARE_SET:
                currentPageId = ROOT_PROGRAM_SET;
                break;
            case TYPE_SHARE_PACK:
                currentPageId = ROOT_PROGRAM_PACKAGE;
                break;
            case TYPE_SHARE_DRAMA:
                currentPageId = ROOT_DRAMA;
                break;
            case TYPE_SHARE_WEB:
                currentPageId = ROOT_H5_INNER;
                break;
        }
        return currentPageId;
    }

}

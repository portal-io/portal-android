package com.whaley.biz.program.uiview;

import android.view.ViewGroup;

import com.whaley.biz.program.uiview.adapter.ActivityBannerLmgLoopUIAdapter;
import com.whaley.biz.program.uiview.adapter.ActivityCardVideoUIAdapter;
import com.whaley.biz.program.uiview.adapter.ActivityHeadUIAdapter;
import com.whaley.biz.program.uiview.adapter.ActivityShareBottomUIAdapter;
import com.whaley.biz.program.uiview.adapter.AreaHeadUIAdapter;
import com.whaley.biz.program.uiview.adapter.BannerGalleryUIAdapter;
import com.whaley.biz.program.uiview.adapter.BannerImgLoopUIAdapter;
import com.whaley.biz.program.uiview.adapter.BannerPlayerSingerUIAdapter;
import com.whaley.biz.program.uiview.adapter.ButtonTagUIAdapter;
import com.whaley.biz.program.uiview.adapter.ButtonUIAdapter;
import com.whaley.biz.program.uiview.adapter.CardTopicUIAdapter;
import com.whaley.biz.program.uiview.adapter.ChannelIconUIAdapter;
import com.whaley.biz.program.uiview.adapter.CompilationUIAdapter;
import com.whaley.biz.program.uiview.adapter.LiveUIAdapter;
import com.whaley.biz.program.uiview.adapter.LiveProgramUIAdapter;
import com.whaley.biz.program.uiview.adapter.GraphicIconHeadUIAdapter;
import com.whaley.biz.program.uiview.adapter.GraphicLeftAndRightUIAdapter;
import com.whaley.biz.program.uiview.adapter.HorziontalFullImgUIAdapter;
import com.whaley.biz.program.uiview.adapter.IconRoundUIAdapter;
import com.whaley.biz.program.uiview.adapter.LineUIAdapter;
import com.whaley.biz.program.uiview.adapter.LiveHeaderUIAdapter;
import com.whaley.biz.program.uiview.adapter.LiveTopicItemUIAdapter;
import com.whaley.biz.program.uiview.adapter.LiveTopicMoreUIAdapter;
import com.whaley.biz.program.uiview.adapter.LiveTopicUIAdapter;
import com.whaley.biz.program.uiview.adapter.ProgramUIAdapter;
import com.whaley.biz.program.uiview.adapter.PromulagtorContentUIAdapter;
import com.whaley.biz.program.uiview.adapter.PromulagtorDescriptionUIAdapter;
import com.whaley.biz.program.uiview.adapter.PromulagtorUIAdapter;
import com.whaley.biz.program.uiview.adapter.RecyclerViewUIAdapter;
import com.whaley.biz.program.uiview.adapter.CardVideoUIAdapter;
import com.whaley.biz.program.uiview.adapter.ShareBottomUIAdapter;
import com.whaley.biz.program.uiview.adapter.TopicHeadUIAdapter;
import com.whaley.biz.program.uiview.adapter.UIAdapter;

/**
 * Created by YangZhi on 2017/3/14 14:30.
 */

public class UIAdapterCreator implements ViewTypeConstants {

    public static UIAdapter createUIAdapterByType(ViewGroup parent, int type) {
        UIAdapter uiAdapter = null;
        switch (type) {
            case TYPE_TAB_INDICATOR:
                break;
            case TYPE_BANNER_IMG_LOOP:
                uiAdapter = new BannerImgLoopUIAdapter();
                break;
            case TYPE_PROGRAM:
                uiAdapter = new ProgramUIAdapter();
                break;
            case TYPE_RECYCLE_VIEW:
                uiAdapter = new RecyclerViewUIAdapter();
                break;
            case TYPE_AREA_HEAD:
                uiAdapter = new AreaHeadUIAdapter();
                break;
            case TYPE_CHANNEL_ICON:
                uiAdapter = new ChannelIconUIAdapter();
                break;
            case TYPE_IMG_HORZIONTAL_FULL:
                uiAdapter = new HorziontalFullImgUIAdapter();
                break;
            case TYPE_VIEW_LINE:
                uiAdapter = new LineUIAdapter();
                break;
            case TYPE_BUTTON:
                uiAdapter = new ButtonUIAdapter();
                break;
            case TYPE_BUTTON_TAG:
                uiAdapter = new ButtonTagUIAdapter();
                break;
            case TYPE_BANNER_PLAYER_SINGER:
                uiAdapter = new BannerPlayerSingerUIAdapter();
                break;
            case TYPE_PROMULGATOR:
                uiAdapter = new PromulagtorUIAdapter();
                break;
            case TYPE_PROMULGATOR_DESCRIPTION:
                uiAdapter = new PromulagtorDescriptionUIAdapter();
                break;
            case TYPE_PROMULGATOR_CONTENT:
                uiAdapter = new PromulagtorContentUIAdapter();
                break;
            case TYPE_CHANNEL_ICON_ROUND:
                uiAdapter = new IconRoundUIAdapter();
                break;
            case TYPE_GRAPHIC_LEFT_AND_RIGHT:
                uiAdapter = new GraphicLeftAndRightUIAdapter();
                break;
            case TYPE_GRAPHIC_ICON_HEAD:
                uiAdapter = new GraphicIconHeadUIAdapter();
                break;
            case TYPE_FLEX_BUTTON_TAGS:
//                uiAdapter = new FlexButtonTagsUIAdapter();
                break;
            case TYPE_RESERVE_CARD:
                uiAdapter = new CardVideoUIAdapter();
                break;
            case TYPE_COMPILATION:
                uiAdapter = new CompilationUIAdapter();
                break;
            case TYPE_LIVE_HEADER:
                uiAdapter = new LiveHeaderUIAdapter();
                break;
            case TYPE_RECOMMEND_LIVE:
                uiAdapter = new LiveUIAdapter();
                break;
            case TYPE_RECOMMEND_LIVE_PROGRAM:
                uiAdapter = new LiveProgramUIAdapter();
                break;
            case TYPE_LIVE_TOPIC:
                uiAdapter = new LiveTopicUIAdapter();
                break;
            case TYPE_LIVE_TOPIC_ITEM:
                uiAdapter = new LiveTopicItemUIAdapter();
                break;
            case TYPE_LIVE_TOPIC_MORE:
                uiAdapter = new LiveTopicMoreUIAdapter();
                break;
            case TYPE_TOPIC_HEAD:
                uiAdapter = new TopicHeadUIAdapter();
                break;
            case TYPE_TOPIC_CARD:
                uiAdapter = new CardTopicUIAdapter();
                break;
            case TYPE_SHARE_BOTTOM:
                uiAdapter = new ShareBottomUIAdapter();
                break;
            case TYPE_BANNER_GALLERY:
                uiAdapter = new BannerGalleryUIAdapter();
                break;
            case TYPE_ACTIVITY_VIDEO:
                uiAdapter = new ActivityCardVideoUIAdapter();
                break;
            case TYPE_ACTIVITY_HEAD:
                uiAdapter = new ActivityHeadUIAdapter();
                break;
            case TYPE_ACTIVITY_SHARE_BOTTOM:
                uiAdapter = new ActivityShareBottomUIAdapter();
                break;
            case TYPE_ACTIVITY_BANNER_IMG_LOOP:
                uiAdapter = new ActivityBannerLmgLoopUIAdapter();
        }
        return uiAdapter;
    }
}

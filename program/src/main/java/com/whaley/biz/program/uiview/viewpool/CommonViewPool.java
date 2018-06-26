package com.whaley.biz.program.uiview.viewpool;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.core.debug.Debug;
import com.whaley.core.debug.logger.Log;

/**
 * Created by YangZhi on 2017/9/22 20:54.
 */

public class CommonViewPool extends RecyclerView.RecycledViewPool implements ViewTypeConstants{

    public static final String TAG = "CommonViewPool";

    public static final boolean isDebug = Debug.isDebug();

    private final SparseArrayCompat<Integer> maxTypes = new SparseArrayCompat();

    private static class Holder{
        private static final CommonViewPool INSTANCE = new CommonViewPool();
    }

    public static CommonViewPool getInstance(){
        return Holder.INSTANCE;
    }

    private CommonViewPool(){
        setMaxRecycledViews(TYPE_RECYCLE_VIEW,5);
        setMaxRecycledViews(TYPE_BANNER_IMG_LOOP,5);
        setMaxRecycledViews(TYPE_BANNER_PLAYER_SINGER,5);
        setMaxRecycledViews(TYPE_PROGRAM,30);
        setMaxRecycledViews(TYPE_AREA_HEAD,30);
        setMaxRecycledViews(TYPE_CHANNEL_ICON,30);
        setMaxRecycledViews(TYPE_IMG_HORZIONTAL_FULL,20);
        setMaxRecycledViews(TYPE_BUTTON,20);
        setMaxRecycledViews(TYPE_BUTTON_TAG,20);
        setMaxRecycledViews(TYPE_PROMULGATOR,20);
        setMaxRecycledViews(TYPE_PROMULGATOR_DESCRIPTION,20);
        setMaxRecycledViews(TYPE_PROMULGATOR_CONTENT,20);
        setMaxRecycledViews(TYPE_BANNER_IMG,15);
        setMaxRecycledViews(TYPE_VIEW_LINE,20);
        setMaxRecycledViews(TYPE_GRAPHIC_LEFT_AND_RIGHT,20);
        setMaxRecycledViews(TYPE_CHANNEL_ICON_ROUND,20);
        setMaxRecycledViews(TYPE_GRAPHIC_ICON_HEAD,20);
        setMaxRecycledViews(TYPE_FLEX_BUTTON_TAGS,12);
        setMaxRecycledViews(TYPE_RESERVE_CARD,20);
        setMaxRecycledViews(TYPE_COMPILATION,20);
        setMaxRecycledViews(TYPE_LIVE_HEADER,10);
        setMaxRecycledViews(TYPE_BANNER_GALLERY,1);
        setMaxRecycledViews(TYPE_LIVE_TOPIC,10);
        setMaxRecycledViews(TYPE_LIVE_TOPIC_ITEM,20);
        setMaxRecycledViews(TYPE_LIVE_TOPIC_MORE,5);
        setMaxRecycledViews(TYPE_RECOMMEND_LIVE,10);
        setMaxRecycledViews(TYPE_RECOMMEND_LIVE_PROGRAM,10);
    }


    @Override
    public RecyclerView.ViewHolder getRecycledView(int viewType) {
        if(isDebug) {
            Log.d(TAG, "取出ViewHolder type = " + viewType);
        }
        return super.getRecycledView(viewType);
    }

    @Override
    public void putRecycledView(RecyclerView.ViewHolder scrap) {
        super.putRecycledView(scrap);
        if(isDebug) {
            Log.d(TAG, "添加ViewHolder type = " + scrap.getItemViewType());
        }
    }


    @Override
    public void setMaxRecycledViews(int viewType, int max) {
        super.setMaxRecycledViews(viewType, max);
        maxTypes.put(viewType,max);
    }

    public int getMaxRecycledViewsByType(int viewType){
        return maxTypes.get(viewType,1);
    }
}

package com.whaleyvr.biz.unity.vrone;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.snailvr.vrone.R;
import com.snailvr.vrone.RenderActivityForManager;
import com.whaley.core.utils.GsonUtil;
import com.whaleyvr.biz.unity.util.HermesConstants;
import com.whaleyvr.biz.unity.model.MediaInfo;

/**
 * Created by dell on 2017/12/5.
 */

public class SplitPlayerActivity extends RenderActivityForManager {

    private SplitLayout splitLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams
//                (FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//        View view = LayoutInflater.from(this).inflate(R.layout.test_cover, null, true);
//        addContentView(view, params1);
        String eventName = null;
        MediaInfo mediaInfo = null;
        if(getIntent() != null) {
            String[] data = getIntent().getStringArrayExtra("message");
            if(data.length> 2){
                eventName = data[0];
                String jsonData = data[2];
                if(!TextUtils.isEmpty(jsonData))
                    mediaInfo = GsonUtil.getGson().fromJson(jsonData, MediaInfo.class);
            }
        }
        if(HermesConstants.EVENT_PLAY.equals(eventName) && mediaInfo != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                    (FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            if(filterMovieSource(mediaInfo)){
                splitLayout = new LiveSplitLayout(this);
            }else {
                splitLayout = new SplitLayout(this);
            }
            splitLayout.setData(mediaInfo);
            addContentView(splitLayout, params);
        }
    }

    private boolean filterMovieSource(MediaInfo mediaInfo){
        if("LIVE".equals(mediaInfo.getMovieSource())){
            return true;
        }
        return false;
    }

    public static void startScene(Context context, String eventName, String dataType, String jsonData) {
        String[] dataArr = new String[]{eventName, dataType, jsonData};
        Intent intent = new Intent(context, SplitPlayerActivity.class);
        intent.putExtra("test", "111111");
        intent.putExtra("message", dataArr);
        intent.addFlags(65536);
        context.startActivity(intent);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        try {
            Configuration config = new Configuration();
            config.setToDefaults();
            res.updateConfiguration(config, res.getDisplayMetrics());
        } catch (Exception e) {
        }
        return res;
    }

}

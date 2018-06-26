package com.whaleyvr.biz.hybrid;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.core.router.Router;
import com.whaley.core.utils.StrUtil;
import com.whaleyvr.biz.hybrid.router.GoPageUtil;
import com.whaleyvr.biz.hybrid.router.PageModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangz on 2017/10/8.
 */

public class SchemeFilterActivity extends CommonActivity{
    static final String PATH_RECOMMEND = "/recommend";

    static final String PATH_ROUTER = "router";

    @Override
    protected void initView(Bundle savedInstanceState) {
//        super.initView(savedInstanceState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if(uri != null){
            String path = uri.getPath();
            if(!StrUtil.isEmpty(path)) {
//                path = path.replace(File.separator,"");
                switch (path) {
                    case PATH_RECOMMEND:
                        String data = uri.getQueryParameter("data");
                        Map<String,Object> map = new HashMap<>();
                        map.put("param_starter",this);
                        map.put("param_data",data);
                        Router.getInstance().buildExecutor("/program/service/routerecommend")
                                .putParams(map)
                                .notTransCallbackData()
                                .notTransParam()
                                .excute();
                        break;
                    default:
                        PageModel pageModel = PageModel.obtain(path);
                        GoPageUtil.goPage(this, pageModel);
                }
            }
        }
        finish();
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }
}
